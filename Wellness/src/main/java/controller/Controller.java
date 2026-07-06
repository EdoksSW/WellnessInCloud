package controller;
import dao.UtenteDAO;
import dao.StaffDAO;
import dao.TurnoDAO;
import dao.CorsoDAO;
import dao.ProdottoDAO;
import dao.CategoriaDAO;
import dao.ClienteDAO;
import dao.TitoloIngressoDAO;
import dao.LezioneDAO;
import implementazioniPostgresDAO.*;
import model.utenti.*;
import model.enums.*;
import model.commerce.*;
import model.logistica.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Controller
{
    private List<Utente> utenti;

    private List<Prodotto> prodotti;
    private List<Corso> corsi;
    private List<Prenotazione> prenotazioni;
    private List<Iscrizione> iscrizioni;
    private List<Ordine> ordini;


    public Controller() {
        utenti = new ArrayList<>();
        prodotti = new ArrayList<>();
        corsi = new ArrayList<>();
        prenotazioni = new ArrayList<>();
        iscrizioni = new ArrayList<>();
        ordini = new ArrayList<>();
    }

    public Utente login(String email, String password) {
        UtenteDAO utenteDAO = new UtenteImplementazionePostgresDAO();
        return utenteDAO.loginDB(email, password);
    }

    public ArrayList<Staff> getListaStaff() {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.getAllStaff();
    }

    public ArrayList<Turno> getTurniDiStaff(String cfStaff) {
        TurnoDAO turnoDAO = new TurnoImplementazionePostgresDAO();
        return turnoDAO.getTurniByStaff(cfStaff);
    }

    public boolean aggiungiTurno(String cfStaff, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        TurnoDAO turnoDAO = new TurnoImplementazionePostgresDAO();
        return turnoDAO.aggiungiTurno(cfStaff, data, oraInizio, oraFine);
    }

    public boolean modificaTurno(int idTurno, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        TurnoDAO turnoDAO = new TurnoImplementazionePostgresDAO();
        return turnoDAO.modificaTurno(idTurno, data, oraInizio, oraFine);
    }

    public boolean rimuoviTurno(int idTurno) {
        TurnoDAO turnoDAO = new TurnoImplementazionePostgresDAO();
        return turnoDAO.rimuoviTurno(idTurno);
    }

    public ArrayList<Staff> getListaIstruttori() {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.getIstruttori();
    }

    public boolean aggiungiStaff(Staff staff) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.aggiungiStaff(staff);
    }

    public boolean modificaStaff(Staff staff) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.modificaStaff(staff);
    }

    public boolean rimuoviStaff(String codiceFiscale) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.rimuoviStaff(codiceFiscale);
    }

    public ArrayList<Corso> getListaCorsi() {
        CorsoDAO corsoDAO = new CorsoImplementazionePostgresDAO();
        return corsoDAO.getAllCorsi();
    }

    public boolean aggiungiCorso(String nome, String descrizione, String cfStaff) {
        CorsoDAO corsoDAO = new CorsoImplementazionePostgresDAO();
        return corsoDAO.aggiungiCorso(nome, descrizione, cfStaff);
    }

    public boolean modificaCorso(int idCorso, String nome, String descrizione, String cfStaff) {
        CorsoDAO corsoDAO = new CorsoImplementazionePostgresDAO();
        return corsoDAO.modificaCorso(idCorso, nome, descrizione, cfStaff);
    }

    public boolean rimuoviCorso(int idCorso) {
        CorsoDAO corsoDAO = new CorsoImplementazionePostgresDAO();
        return corsoDAO.rimuoviCorso(idCorso);
    }

    public ArrayList<Lezione> getLezioniCorso(int idCorso) {
        LezioneDAO lezioneDAO = new LezioneImplementazionePostgresDAO();
        return lezioneDAO.getLezioniByCorso(idCorso);
    }

    public boolean aggiungiLezione(int idCorso, String nome, String descrizione, String giorno, LocalTime oraInizio, LocalTime oraFine, int idSala) {
        LezioneDAO lezioneDAO = new LezioneImplementazionePostgresDAO();
        return lezioneDAO.aggiungiLezione(idCorso, nome, descrizione, giorno, oraInizio, oraFine, idSala);
    }

    public boolean modificaLezione(int idLezione, String nome, String descrizione, String giorno, LocalTime oraInizio, LocalTime oraFine, int idSala) {
        LezioneDAO lezioneDAO = new LezioneImplementazionePostgresDAO();
        return lezioneDAO.modificaLezione(idLezione, nome, descrizione, giorno, oraInizio, oraFine, idSala);
    }

    public boolean rimuoviLezione(int idLezione) {
        LezioneDAO lezioneDAO = new LezioneImplementazionePostgresDAO();
        return lezioneDAO.rimuoviLezione(idLezione);
    }

    public ArrayList<Integer> getListaSale() {
        LezioneDAO lezioneDAO = new LezioneImplementazionePostgresDAO();
        return lezioneDAO.getSale();
    }

    public ArrayList<Prodotto> getListaProdotti() {
        ProdottoDAO prodottoDAO = new ProdottoImplementazionePostgresDAO();
        return prodottoDAO.getAllProdotti();
    }

    public boolean aggiungiProdotto(String nome, BigDecimal prezzo, int giacenza, Integer idCategoria) {
        ProdottoDAO prodottoDAO = new ProdottoImplementazionePostgresDAO();
        return prodottoDAO.aggiungiProdotto(nome, prezzo, giacenza, idCategoria);
    }

    public boolean modificaProdotto(int idProdotto, String nome, BigDecimal prezzo, int giacenza, Integer idCategoria) {
        ProdottoDAO prodottoDAO = new ProdottoImplementazionePostgresDAO();
        return prodottoDAO.modificaProdotto(idProdotto, nome, prezzo, giacenza, idCategoria);
    }

    public boolean rimuoviProdotto(int idProdotto) {
        ProdottoDAO prodottoDAO = new ProdottoImplementazionePostgresDAO();
        return prodottoDAO.rimuoviProdotto(idProdotto);
    }

    public ArrayList<Categoria> getListaCategorie() {
        CategoriaDAO categoriaDAO = new CategoriaImplementazionePostgresDAO();
        return categoriaDAO.getAllCategorie();
    }

    public boolean aggiungiCategoria(String nome) {
        CategoriaDAO categoriaDAO = new CategoriaImplementazionePostgresDAO();
        return categoriaDAO.aggiungiCategoria(nome);
    }

    public boolean modificaCategoria(int idCategoria, String nome) {
        CategoriaDAO categoriaDAO = new CategoriaImplementazionePostgresDAO();
        return categoriaDAO.modificaCategoria(idCategoria, nome);
    }

    public boolean rimuoviCategoria(int idCategoria) {
        CategoriaDAO categoriaDAO = new CategoriaImplementazionePostgresDAO();
        return categoriaDAO.rimuoviCategoria(idCategoria);
    }

    public ArrayList<TitoloIngresso> getListaTitoli() {
        TitoloIngressoDAO titoloDAO = new TitoloIngressoImplementazionePostgresDAO();
        return titoloDAO.getAllTitoli();
    }

    public boolean aggiungiTitolo(String tipo, BigDecimal prezzo) {
        TitoloIngressoDAO titoloDAO = new TitoloIngressoImplementazionePostgresDAO();
        return titoloDAO.aggiungiTitolo(tipo, prezzo);
    }

    public boolean modificaTitolo(int idTitolo, String tipo, BigDecimal prezzo) {
        TitoloIngressoDAO titoloDAO = new TitoloIngressoImplementazionePostgresDAO();
        return titoloDAO.modificaTitolo(idTitolo, tipo, prezzo);
    }

    public boolean rimuoviTitolo(int idTitolo) {
        TitoloIngressoDAO titoloDAO = new TitoloIngressoImplementazionePostgresDAO();
        return titoloDAO.rimuoviTitolo(idTitolo);
    }

    public String messaggioAccessoNegato(Utente u) {
        if (u instanceof Cliente) {
            Cliente cliente = (Cliente) u;
            if (cliente.getStatoAcc() == StatoAccount.BLOCCATO) {
                return "Il tuo account è bloccato. Contatta la segreteria.";
            }
            if (cliente.getStatoAcc() == StatoAccount.IN_REVISIONE) {
                return "Il tuo account è attualmente in revisione. Contatta la segreteria.";
            }
        }
        return null;
    }

    public List<Utente> getUtenti() {
        return utenti;
    }

    //Controller Cliente--->
    public boolean effettuaIscrizioneCliente(Cliente cliente, TitoloIngresso titoloIngresso, int mesiDurata)
    {
        LocalDate dataInizio=LocalDate.now();
        LocalDate dataFine=dataInizio.plusMonths(mesiDurata);
        Iscrizione iscrizione=new Iscrizione(0, dataInizio, dataFine, cliente, titoloIngresso);

        ClienteDAO clienteDAO=new ClienteImplementazionePostgresDAO();
        return clienteDAO.effettuaIscrizione(iscrizione);
    }

    public boolean prenotaLezioneCliente(Cliente cliente, Lezione lezione, StatoPrenotazione statoPrenotazione)
    {
        LocalDate dataPren=LocalDate.now();
        LocalTime oraPren=LocalTime.now();

        Prenotazione prenotazione=new Prenotazione(0, dataPren, oraPren, statoPrenotazione, cliente, lezione);
        ClienteDAO clienteDAO=new ClienteImplementazionePostgresDAO();
        return clienteDAO.prenotaLezione(prenotazione);
    }

    public boolean annullaPrenotazioneCliente(int id_prenotazione)
    {
        ClienteDAO clienteDAO=new ClienteImplementazionePostgresDAO();
        return clienteDAO.annullaPrenotazione(id_prenotazione);
    }

    public List<Prenotazione> ottieniPrenotazioniAttiveCliente(Cliente cliente)
    {
        ClienteDAO clienteDAO=new ClienteImplementazionePostgresDAO();
        return clienteDAO.ottieniPrenotazioniCliente(cliente);
    }

    public Carrello ottieniCarrelloCliente(Cliente cliente)
    {
        ClienteDAO clienteDAO= new ClienteImplementazionePostgresDAO();
        return clienteDAO.ottieniCarrello(cliente);
    }

    public boolean annullaOrdineCliente(int idOrdine) {
        ClienteDAO clienteDAO = new ClienteImplementazionePostgresDAO();
        return clienteDAO.annullaOrdineInElaborazione(idOrdine);
    }

    public boolean finalizzaAcquisto(Cliente cliente)
    {
        ClienteDAO clienteDAO=new ClienteImplementazionePostgresDAO();
        return clienteDAO.completaAcquisto(cliente);
    }

    public List<Ordine> ottieniStoricoOrdiniCliente(Cliente cliente) {
        ClienteDAO clienteDAO = new ClienteImplementazionePostgresDAO();
        return clienteDAO.ottieniStoricoOrdini(cliente);
    }

    public List<Prodotto> ottieniCatalogoProdotto()
    {
        ClienteDAO clienteDAO=new ClienteImplementazionePostgresDAO();
        return clienteDAO.ottieniCatalogoProdotti();
    }

    public ArrayList<Cliente> getListaClientiDaStaff() {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.getAllClientiDaStaff();
    }

    public boolean aggiungiProdottoCarrello(Cliente cliente, int idProdotto, int quatita)
    {
        ClienteDAO clienteDAO=new ClienteImplementazionePostgresDAO();
        String cfCliente=cliente.getCodiceFiscale();
        return clienteDAO.aggiungiSingoloProdotto(cfCliente, idProdotto,quatita);
    }

    public boolean aggiungiClienteTramiteStaff(Cliente nuovoCliente) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.aggiungiClienteDaStaff(nuovoCliente);
    }

    public boolean modificaClienteTramiteStaff(Cliente clienteModificato) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.modificaClienteDaStaff(clienteModificato);
    }

    public boolean rimuoviClienteTramiteStaff(String codiceFiscale) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.rimuoviClienteDaStaff(codiceFiscale);
    }

    public String ottieniStatoCertificatoStaff(String codiceFiscale) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        LocalDate scadenza = staffDAO.getCertificatoDaStaff(codiceFiscale);

        if (scadenza == null) return "Nessun certificato registrato";
        if (scadenza.isBefore(LocalDate.now())) return "SCADUTO (Scadenza: " + scadenza + ")";
        return "Valido fino al: " + scadenza;
    }

    public boolean aggiornaScadenzaCertificatoStaff(String cf, LocalDate nuovaScadenza) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.aggiornaCertificatoDaStaff(cf, nuovaScadenza);
    }

    public String[] getDettagliAbbonamentoCliente(String cf) {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.getDettagliAbbonamento(cf);
    }

    public java.util.List<Prenotazione> ottieniTuttePrenotazioniStaff() {
        StaffDAO staffDAO = new StaffImplementazionePostgresDAO();
        return staffDAO.ottieniTuttePrenotazioni();
    }

    public List<Lezione> ottieniListaLezioni() {
        LezioneDAO lezioneDAO = new LezioneImplementazionePostgresDAO();
        return lezioneDAO.getTutteLezioni();
    }

}



