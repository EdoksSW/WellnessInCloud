package implementazioniPostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.enums.RuoloStaff;
import model.enums.StatoAccount;
import model.utenti.Admin;
import model.utenti.Cliente;
import model.utenti.Staff;
import model.utenti.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    //public Cliente(String codiceFiscale, String nome, String cognome, String email, String telefono, String password, LocalDate dataNascita, String indirizzo, int numCivico, String cap, StatoAccount statoAcc)
    //public Staff(String codiceFiscale, String nome, String cognome, String email, String telefono, String password, LocalDate dataNascita, String qualifica, String iban, RuoloStaff ruolo)
    //public Admin(String codiceFiscale, String nome, String cognome, String email, String telefono, String password, LocalDate dataNascita)
    
    @Override
    public Utente loginDB(String email, String password) {
        //Definisco la query con i dei segnaposto (?) per la sicurezza
        //Tutte le SELECT della UNION devono avere le stesse identiche colonne nello stesso ordine
        //Uso dei NULL per i campi che alcune tabelle non hanno
        String query= "WITH Utente AS (" +
                "SELECT email, password, codicefiscale, nome, cognome, telefono, datanascita, eta, via, civico, cap, stato_account, NULL AS qualifica, NULL AS iban, NULL AS ruolo, 'Cliente' AS ruol FROM Cliente " +
                "  UNION ALL " +
                "  SELECT email, password, codicefiscale, nome, cognome, telefono, datanascita, eta, via, civico, cap, NULL, qualifica, iban, ruolo, 'Staff' AS ruol FROM Staff " +
                "  UNION ALL " +
                "  SELECT email, password, codicefiscale, nome, cognome, NULL, datanascita, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Admin' AS ruol FROM Admin" +
                ") " +
                "SELECT ruolo " +
                "FROM Utente " +
                "WHERE email=? AND password = ?;";

        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query)) {
            //Associo i parametri di input ai segnaposto della query
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            //Esecuzione della query sulla tabella del DB
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next())
                {
                    String nome = resultSet.getString("nome");
                    String cognome = resultSet.getString("cognome");
                    String codicefiscale = resultSet.getString("codice_fiscale");
                    String telefono = resultSet.getString("telefono");

                    LocalDate datanascita = null;
                    if (resultSet.getDate("data_nascita") != null) {
                        datanascita = resultSet.getDate("data_nascita").toLocalDate();
                    }

                    String ruolo = resultSet.getString("ruolo");
                    int eta = resultSet.getInt("eta");
                    String via = resultSet.getString("via");
                    int civico = resultSet.getInt("civico");
                    String cap = resultSet.getString("cap");
                    String stato_acc=resultSet.getString("stato_account");
                    String ruol = resultSet.getString("ruol");
                    if(ruol.equals("Admin")) return new Admin(codicefiscale, nome, cognome, email, "?", password, datanascita);
                    else if(ruol.equals("Cliente"))
                    {
                        return new Cliente(codicefiscale, nome, cognome, email, telefono, password, datanascita, via, civico, cap, StatoAccount.valueOf(stato_acc.toUpperCase()));
                    }
                    else
                    {
                        return new Staff(codicefiscale, nome, cognome, email, telefono, password,datanascita, resultSet.getString("qualifica"),cap,RuoloStaff.valueOf(ruolo));
                    }
                }
            }
        }catch (SQLException e)
        {
            System.err.println("Errore critico durante il login nel DB: "+ e.getMessage());
            e.printStackTrace(); //mostra la riga esatta dell'errore SQL in console
            return null;
        }

        //Se il resultSet è vuoto (credenziali errate), restituisce null
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
