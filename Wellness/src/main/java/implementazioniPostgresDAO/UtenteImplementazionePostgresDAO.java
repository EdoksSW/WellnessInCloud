package implementazioniPostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.enums.RuoloStaff;
import model.utenti.Utente;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class UtenteImplementazionePostgresDAO implements UtenteDAO {

    private Connection connection;

    public UtenteImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utente loginDB(String email, String password) {
        return null;
    }

    @Override
    public boolean registraClienteDB(String cf, String nome, String cognome, String email, String telefono,
                                     LocalDate dataNascita, String password, String indirizzo, int numCivico, String cap) {
        return false;
    }

    @Override
    public boolean registraStaffDB(String cf, String nome, String cognome, String email, String telefono,
                                   LocalDate dataNascita, String password, String qualifica, String iban, RuoloStaff ruolo) {
        return false;
    }
}
