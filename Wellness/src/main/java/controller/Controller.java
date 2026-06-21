package controller;
import dao.UtenteDAO;
import implementazioniPostgresDAO.UtenteImplementazionePostgresDAO;
import model.utenti.*;
import model.enums.*;
import model.commerce.*;
import model.logistica.*;
import java.util.ArrayList;
import java.time.LocalDate;
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

    public boolean registraCliente(Utente esecutore, String cf, String nome, String cognome, String email, String telefono,
                                   LocalDate dataNascita, String password, String indirizzo, int numCivico, String cap) {
        if (esecutore instanceof Staff || esecutore instanceof Admin) {
            for (Utente u : utenti) {
                if (u.getEmail().equalsIgnoreCase(email) || u.getCodiceFiscale().equalsIgnoreCase(cf)) return false;
            }
            utenti.add(new Cliente(cf, nome, cognome, email, telefono, password, dataNascita, indirizzo, numCivico, cap, StatoAccount.ATTIVO));
            return true;
        }
        return false;
    }

    public boolean registraStaff(Utente esecutore, String cf, String nome, String cognome, String email, String telefono,
                                 LocalDate dataNascita, String password, String qualifica, String iban, RuoloStaff ruolo) {
        if (esecutore instanceof Admin) {
            for (Utente u : utenti) {
                if (u.getEmail().equalsIgnoreCase(email) || u.getCodiceFiscale().equalsIgnoreCase(cf)) return false;
            }
            utenti.add(new Staff(cf, nome, cognome, email, telefono, password, dataNascita, qualifica, iban, ruolo));
            return true;
        }
        return false;

}
    public List<Utente> getUtenti() {
        return utenti;
    }}
