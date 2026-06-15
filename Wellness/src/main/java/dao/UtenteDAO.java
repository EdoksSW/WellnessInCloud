package dao;

import model.enums.RuoloStaff;
import model.utenti.Utente;

import java.time.LocalDate;

public interface UtenteDAO {

    Utente loginDB(String email, String password);

    boolean registraClienteDB(String cf, String nome, String cognome, String email, String telefono,
                              LocalDate dataNascita, String password, String indirizzo, int numCivico, String cap);

    boolean registraStaffDB(String cf, String nome, String cognome, String email, String telefono,
                            LocalDate dataNascita, String password, String qualifica, String iban, RuoloStaff ruolo);
}
