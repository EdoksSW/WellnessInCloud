package implementazioniPostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;
import model.commerce.Carrello;
import model.commerce.Prodotto;
import model.enums.StatoAccount;
import model.logistica.Prenotazione;
import model.utenti.Cliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteImplementazionePostgresDAO implements ClienteDAO
{
    private Connection connection;

    ClienteImplementazionePostgresDAO()
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
        String query="SELECT * FROM cliente WHERE codicefiscale = ? ;";

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

                 String stato_acc=resultSet.getString("stato_account");
                 StatoAccount statoAccount=StatoAccount.valueOf(stato_acc.trim().toUpperCase());

                 LocalDate dataNascita=null;
                 if(resultSet.getDate("datanascita")!=null)
                 {
                     dataNascita=resultSet.getDate("datanascita").toLocalDate();
                 }
                 return new Cliente(codiceFiscale,nome, cognome, email, telefono, password, dataNascita, eta, via, numCivico, cap, statoAccount);
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
                "prodotto.categoria, " +
                "dettaglio_carrello.quantita, " + // Quantità reale nel carrello
                "prodotto.giacenza," +
                "prodotto.prezzo, " +            // Rimanenza in magazzino
                "carrello.totale, " +
                "carrello.id_carrello " +
                "FROM cliente " +
                "JOIN carrello ON cliente.id_carrello = carrello.id_carrello " +
                "JOIN dettaglio_carrello ON carrello.id_carrello = dettaglio_carrello.id_carrello " +
                "JOIN prodotto ON dettaglio_carrello.id_prodotto = prodotto.id_prodotto " +
                "WHERE cliente.codicefiscale = ?";

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
                        String categoria=resultSet.getString("categoria");
                        BigDecimal prezzo= resultSet.getBigDecimal("prezzo");
                        int giacenza=resultSet.getInt("giacenza");
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
                for(Prodotto prodotto: carrello.getProdotti().keySet())
                {
                    int quantita=carrello.getProdotti().get(prodotto);
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
    public boolean acquistaAbbonamento(String codiceFiscaleCliente, int idAbbonamento, LocalDate dataInizio, double prezzoPagato)
    {
        return false;
    }

    @Override
    public boolean prenotaLezione(String codiceFiscaleCliente, int idLezione, LocalDate dataPrenotazione)
    {
        return false;
    }

    @Override
    public boolean annullaPrenotazione(String codiceFiscaleCliente, int idPrenotazione)
    {
        return false;
    }

    @Override
    public List<Prenotazione> ottieniPrenotazioneCliente(String codiceFiscaleCliente)
    {
        return List.of();
    }
}
