package implementazioniPostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;
import model.commerce.*;
import model.enums.StatoAccount;
import model.enums.StatoPrenotazione;
import model.logistica.Lezione;
import model.logistica.Prenotazione;
import model.utenti.Cliente;
import model.commerce.Ordine;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteImplementazionePostgresDAO implements ClienteDAO
{
    private Connection connection;

    public ClienteImplementazionePostgresDAO()
    {
        try
        {
            this.connection=ConnessioneDatabase.getInstance().connection;
            System.out.println("Inizializzazione connessione ad DB evvenuta con successo!!");
        }catch (SQLException e)
        {
            System.out.println("Errore inizializzazione connessione al DataBase!!");
            e.printStackTrace();
        }
    }

    @Override
    public Cliente trovaPerCodiceFiscale(String codiceFiscale)
    {
        String query="SELECT u.nome, u.cognome, u.email, u.telefono, u.password, u.datanascita, u.eta, u.via, u.civico, u.cap, u.carta_fedelta, c.stato_account " +
                "FROM cliente c JOIN utente u ON c.codice_fiscale = u.codice_fiscale WHERE c.codice_fiscale = ? ;";

        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query))
        {
            preparedStatement.setString(1,codiceFiscale);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                 String email=resultSet.getString("email");
                 String password=resultSet.getString("password");
                 String nome=resultSet.getString("nome");
                 String cognome=resultSet.getString("cognome");
                 String telefono=resultSet.getString("telefono");
                 String via=resultSet.getString("via");
                 int numCivico=resultSet.getInt("civico");
                 String cap=resultSet.getString("cap");
                 int eta=resultSet.getInt("eta");
                 String cartaFedelta=resultSet.getString("carta_fedelta");

                 String stato_acc=resultSet.getString("stato_account");
                 StatoAccount statoAccount=StatoAccount.valueOf(stato_acc.trim().toUpperCase());

                 LocalDate dataNascita=null;
                 if(resultSet.getDate("datanascita")!=null)
                 {
                     dataNascita=resultSet.getDate("datanascita").toLocalDate();
                 }
                 return new Cliente(codiceFiscale,nome, cognome, email, telefono, password, dataNascita, eta, via, numCivico, cap, cartaFedelta, statoAccount);
            }
        }catch(SQLException e) {
            System.out.println("Errore SQL in trovaPerCodiceFiscale: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Carrello ottieniCarrello(Cliente clienteSelezionato)
    {
        String query = "SELECT " +
                "prodotto.id_prodotto, " +
                "prodotto.nome, " +
                "prodotto.id_categoria, " +
                "categoria.nome AS categoria_nome, " +
                "dettaglio_carrello.quantita, " +
                "prodotto.giacenza," +
                "prodotto.prezzo, " +
                "carrello.totale, " +
                "carrello.id_carrello " +
                "FROM cliente " +
                "JOIN carrello ON carrello.cf_cliente = cliente.codice_fiscale " +
                "JOIN dettaglio_carrello ON carrello.id_carrello = dettaglio_carrello.id_carrello " +
                "JOIN prodotto ON dettaglio_carrello.id_prodotto = prodotto.id_prodotto " +
                "LEFT JOIN categoria ON prodotto.id_categoria = categoria.id_categoria " +
                "WHERE cliente.codice_fiscale = ?";

        Carrello carrello=null;

        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query))
        {
            preparedStatement.setString(1,clienteSelezionato.getCodiceFiscale());

            try(ResultSet resultSet= preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    if(carrello==null)
                    {
                        carrello=new Carrello(clienteSelezionato, resultSet.getInt("id_carrello"), resultSet.getBigDecimal("totale"));
                    }
                        int id_prodotto=resultSet.getInt("id_prodotto");
                        String nome=resultSet.getString("nome");
                        BigDecimal prezzo= resultSet.getBigDecimal("prezzo");
                        int giacenza=resultSet.getInt("giacenza");

                        Categoria categoria=null;
                        int idCategoria=resultSet.getInt("id_categoria");
                        if(!resultSet.wasNull())
                        {
                            categoria=new Categoria(idCategoria, resultSet.getString("categoria_nome"));
                        }

                        Prodotto prodotto=new Prodotto(id_prodotto, nome, prezzo, giacenza, categoria);
                        carrello.aggiungiProdotto(prodotto, resultSet.getInt("quantita"));

                }
            }
        }catch(SQLException e)
        {
            System.out.println("Errore ottenimento del carrello-> "+e.getMessage());
            e.printStackTrace();
            return null;
        }
        return carrello;
    }

    @Override
    public boolean agiornaCarrello(String codiceFiscaleCliente, Carrello carrello)
    {
        // Aggiorna il totale nella tabella del carrello
        String queryCarrello="UPDATE carrello SET totale=? WHERE id_carrello=?;";

        // Cancella i vecchi dettagli
        String queryCancellaDettagli="DELETE FROM dettaglio_carrello WHERE id_carrello=?;";

        // Inserisce i nuovi dettagli aggionrati
        String queryInserisciDettaglio="INSERT INTO dettaglio_carrello (id_carrello, id_prodotto, quantita) VALUES(?,?,?);";

        try
        {
            //Disabilito il commit automantico per una transazione sicura
            this.connection.setAutoCommit(false);

            //Aggiornamento totale del carrello
            try(PreparedStatement psCarrello=this.connection.prepareStatement(queryCarrello))
            {
                psCarrello.setBigDecimal(1,carrello.getTotale());
                psCarrello.setInt(2, carrello.getId_carrello());
                psCarrello.executeUpdate();
            }

            //Cancelliamo i vecchi dettagli di questo carrello
            try(PreparedStatement psCancella=this.connection.prepareStatement(queryCancellaDettagli))
            {
                psCancella.setInt(1,carrello.getId_carrello());
                psCancella.executeUpdate();
            }

            // Reinseriamo i prdototti correnti con le loro nuove quantità
            try(PreparedStatement psInserisci=this.connection.prepareStatement(queryInserisciDettaglio))
            {
                //Ciclo la mappa dei prodotti (o la lista insomma) per prendere ogni elemento
                for(Prodotto prodotto: carrello.getMapCarrellol().keySet())
                {
                    int quantita=carrello.getMapCarrellol().get(prodotto);
                    psInserisci.setInt(1, carrello.getId_carrello());
                    psInserisci.setInt(2, prodotto.getId_prodotto());
                    psInserisci.setInt(3, quantita);

                    //Acculimuliamo il comando dentro al Batch così da eseguire tutto insieme dopo
                    psInserisci.addBatch();
                }
                //Esegue tutti gli inserimenti insieme
                psInserisci.executeBatch();
            }

            //Se tutto è andato a buon fin, salviamo definitivamente sul DB
            this.connection.commit(); //Rende definitive tutte le modifiche
            this.connection.setAutoCommit(true); //Da adesso in poi tutto verrà modificato in runtime
            return true;
        }catch(SQLException e)
        {
            //In caso di errore, annulliamo tutto per non lasciare il carrello a metà
            try
            {
                this.connection.rollback();
                this.connection.setAutoCommit(true);
            }catch(SQLException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("Errore durante l'aggiornamento del carrello-> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean effettuaIscrizione(Iscrizione iscrizione)
    {
        String query="INSERT INTO iscrizione (data_inizio, data_fine, cf_cliente, id_titolo) VALUES(?,?,?,?);";

        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query))
        {
            preparedStatement.setDate(1, java.sql.Date.valueOf(iscrizione.getDataInizio()));
            preparedStatement.setDate(2, java.sql.Date.valueOf(iscrizione.getDataFine()));
            preparedStatement.setString(3, iscrizione.getCliente().getCodiceFiscale());
            preparedStatement.setInt(4, iscrizione.getTitoloIngresso().getId_titoloIngresso());

            int righeInserite=preparedStatement.executeUpdate();
            return righeInserite>0;
        }catch (SQLException e)
        {
            System.out.println("Errore nel DAO della funzione effettuaIScrizione-> "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean prenotaLezione(Prenotazione prenotazione)
    {
        String query="INSERT INTO prenotazione (data_pren, ora_pren, stato_prenotazione, cf_cliente, id_lezione) VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query))
        {
            preparedStatement.setDate(1, java.sql.Date.valueOf(prenotazione.getDataPren()));
            preparedStatement.setTime(2, java.sql.Time.valueOf(prenotazione.getOraPren()));
            preparedStatement.setString(3, prenotazione.getStato().getLabel());

            //per chiavi esterne
            preparedStatement.setString(4, prenotazione.getCliente().getCodiceFiscale());
            preparedStatement.setInt(5, prenotazione.getLezione().getId_lezione());

            int righe=preparedStatement.executeUpdate();
            return righe>0;
        }catch(SQLException e)
        {
            System.out.println("Errore in prenotazione-> "+ e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean annullaPrenotazione(int id_prenotazione)
    {
        String query = "UPDATE prenotazione SET stato_prenotazione= ? WHERE id_prenotazione=?;";
        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query))
        {
            preparedStatement.setString(1, StatoPrenotazione.ANNULLATA.getLabel());
            preparedStatement.setInt(2, id_prenotazione);

            int righe=preparedStatement.executeUpdate();
            return righe>0;
        }catch (SQLException e)
        {
            System.out.println("Errore in annullaPrenotazione-> "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Prenotazione> ottieniPrenotazioniCliente(Cliente cliente)
    {
        List<Prenotazione> listaPrenotazioni=new  ArrayList<>();
        String query = "SELECT " +
                "p.id_prenotazione, " +
                "p.data_pren, " +
                "p.ora_pren, " +
                "p.stato_prenotazione, " +
                "l.id_lezione, " +
                "l.nome " +
                "FROM prenotazione p " +
                "JOIN lezione l ON p.id_lezione = l.id_lezione " +
                "WHERE p.cf_cliente = ? AND p.stato_prenotazione != 'annullata' " +
                "ORDER BY p.data_pren DESC, p.ora_pren DESC;";
        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query))
        {
            preparedStatement.setString(1, cliente.getCodiceFiscale());

            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    int idLezione = resultSet.getInt("id_lezione");
                    String nomeLezione = resultSet.getString("nome");
                    Lezione lezione = new Lezione(idLezione, nomeLezione);

                    int idPrenotazione = resultSet.getInt("id_prenotazione");
                    LocalDate dataPren = resultSet.getDate("data_pren").toLocalDate();
                    LocalTime oraPren = resultSet.getTime("ora_pren").toLocalTime();

                    String stato_prenotazione=resultSet.getString("stato_prenotazione");
                    StatoPrenotazione statoPrenotazione=StatoPrenotazione.fromLabel(stato_prenotazione);

                    Prenotazione prenotazione=new Prenotazione(dataPren, oraPren, statoPrenotazione, cliente, lezione);
                    listaPrenotazioni.add(prenotazione);
                }
            }
        }catch (SQLException e)
        {
            System.out.println("Errore in ottieniPrenotazioniCliente-> "+e.getMessage());
            e.printStackTrace();
        }
        return  listaPrenotazioni;
    }

    @Override
    public List<Ordine> ottieniStoricoOrdini(Cliente cliente) {
        List<Ordine> storicoOrdini = new ArrayList<>();

        String query = "SELECT " +
                "ordine.id_ordine, " +
                "ordine.data_ordine, " +
                "ordine.stato AS stato_ordine, " +
                "ordine.totale AS totale_ordine, " +
                "pagamento.id_pagamento, " +
                "pagamento.importo AS importo_pagamento, " +
                "pagamento.metodo AS metodo_pagamento " +
                "FROM ordine " +
                "LEFT JOIN pagamento ON pagamento.id_ordine = ordine.id_ordine " +
                "WHERE ordine.cf_cliente = ? " +
                "ORDER BY ordine.data_ordine DESC;";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, cliente.getCodiceFiscale());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Ordine ordine = new Ordine();
                    ordine.setId_ordine(resultSet.getInt("id_ordine"));
                    ordine.setData_ordine(resultSet.getDate("data_ordine").toLocalDate());
                    ordine.setStato(model.enums.StatoOrdine.fromLabel(resultSet.getString("stato_ordine")));
                    ordine.setTotale(resultSet.getBigDecimal("totale_ordine"));

                    int idPagamento = resultSet.getInt("id_pagamento");
                    if (!resultSet.wasNull()) {
                        Pagamento pagamento = new Pagamento();
                        pagamento.setId_pagamento(idPagamento);
                        pagamento.setImporto(resultSet.getBigDecimal("importo_pagamento"));
                        pagamento.setMetodo(resultSet.getString("metodo_pagamento"));
                        ordine.setPagamento(pagamento);
                    }

                    ordine.setDettagli(ottieniDettagliOrdine(ordine.getId_ordine()));
                    storicoOrdini.add(ordine);
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore nel recupero dello storico ordini-> " + e.getMessage());
            e.printStackTrace();
        }

        return storicoOrdini;
    }

    @Override
    public List<OrdineDettaglio> ottieniDettagliOrdine(int idOrdine) {
        List<OrdineDettaglio> listaDettagli = new ArrayList<>();

        String query = "SELECT " +
                "ordine_dettaglio.quantita, " +
                "prodotto.id_prodotto, " +
                "prodotto.nome AS nome_prodotto, " +
                "prodotto.prezzo, " +
                "prodotto.giacenza, " +
                "prodotto.id_categoria, " +
                "categoria.nome AS nome_categoria " +
                "FROM ordine_dettaglio " +
                "JOIN prodotto ON ordine_dettaglio.id_prodotto = prodotto.id_prodotto " +
                "LEFT JOIN categoria ON prodotto.id_categoria = categoria.id_categoria " +
                "WHERE ordine_dettaglio.id_ordine = ?;";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idOrdine);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Categoria categoria = null;
                    int idCategoria = resultSet.getInt("id_categoria");
                    if (!resultSet.wasNull()) {
                        categoria = new Categoria(idCategoria, resultSet.getString("nome_categoria"));
                    }

                    int idProdotto = resultSet.getInt("id_prodotto");
                    String nomeProdotto = resultSet.getString("nome_prodotto");
                    BigDecimal prezzoProdotto = resultSet.getBigDecimal("prezzo");
                    int giacenza = resultSet.getInt("giacenza");

                    Prodotto prodotto = new Prodotto(idProdotto, nomeProdotto, prezzoProdotto, giacenza, categoria);

                    int quantita = resultSet.getInt("quantita");
                    listaDettagli.add(new OrdineDettaglio(prodotto, quantita));
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore nel recupero dei dettagli dell'ordine-> " + e.getMessage());
            e.printStackTrace();
        }

        return listaDettagli;
    }

    public boolean completaAcquisto(Cliente cliente) {
        // Definizione delle query SQL basate sulle tabelle del nostro schema logico[cite: 1]
        String queryInfoCarrello = "SELECT id_carrello, totale FROM carrello WHERE cf_cliente = ?";
        String queryInserisciOrdine = "INSERT INTO ordine (data_ordine, totale, stato, cf_cliente) VALUES (CURRENT_DATE, ?, 'in\nelaborazione', ?) RETURNING id_ordine";
        String querySpostaDettagli = "INSERT INTO ordine_dettaglio (id_ordine, id_prodotto, quantita) " +
                "SELECT ?, id_prodotto, quantita FROM dettaglio_carrello WHERE id_carrello = ?";
        String querySvuotaCarrello = "DELETE FROM dettaglio_carrello WHERE id_carrello = ?";

        try {
            // Disattiviamo l'autocommit per raggruppare tutte le operazioni in una transazione sicura
            this.connection.setAutoCommit(false);

            // Inizializzazione delle variabili per memorizzare i dati del carrello da recuperare[cite: 1]
            int idCarrello = -1;
            double totaleCarrello = 0.0;

            // Esecuzione della prima query per cercare l'ID e il totale del carrello del cliente[cite: 1]
            PreparedStatement psInfo = this.connection.prepareStatement(queryInfoCarrello);
            psInfo.setString(1, cliente.getCodiceFiscale());
            ResultSet rsInfo = psInfo.executeQuery();
            if (rsInfo.next()) {
                idCarrello = rsInfo.getInt("id_carrello");
                totaleCarrello = rsInfo.getDouble("totale");
            }

            // Se il carrello non viene trovato nel database, interrompiamo l'operazione annullando tutto
            if (idCarrello == -1) {
                this.connection.rollback();
                return false;
            }

            // Inserimento della testata del nuovo ordine con il totale della spesa[cite: 1]
            PreparedStatement psOrdine = this.connection.prepareStatement(queryInserisciOrdine);
            psOrdine.setDouble(1, totaleCarrello);
            psOrdine.setString(2, cliente.getCodiceFiscale());
            ResultSet rsOrdine = psOrdine.executeQuery();

            // Recupero dell'ID dell'ordine appena generato automaticamente dal database
            int idNuovoOrdine = -1;
            if (rsOrdine.next()) {
                idNuovoOrdine = rsOrdine.getInt("id_ordine");
            }

            // Spostamento in blocco di tutti i prodotti dal vecchio carrello ai dettagli del nuovo ordine[cite: 1]
            PreparedStatement psDettaglio = this.connection.prepareStatement(querySpostaDettagli);
            psDettaglio.setInt(1, idNuovoOrdine);
            psDettaglio.setInt(2, idCarrello);
            psDettaglio.executeUpdate();

            // Svuotamento del carrello del cliente tramite la cancellazione dei suoi dettagli[cite: 1]
            PreparedStatement psSvuota = this.connection.prepareStatement(querySvuotaCarrello);
            psSvuota.setInt(1, idCarrello);
            psSvuota.executeUpdate();

            // Conferma definitiva e salvataggio di tutte le modifiche sul database
            this.connection.commit();
            return true;

        } catch (SQLException e) {
            // In caso di errore SQL annulliamo ogni modifica effettuata per ripristinare i dati iniziali
            try {
                if (this.connection != null) {
                    this.connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
}
