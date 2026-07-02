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

    @Override
    public Utente loginDB(String email, String password) {
        String query =
                "SELECT u.codice_fiscale, u.nome, u.cognome, u.telefono, u.email, u.password, u.datanascita, u.eta, u.via, u.civico, u.cap, u.carta_fedelta, " +
                "c.stato_account, s.ruolo, s.qualifica, s.iban, " +
                "(a.codice_fiscale IS NOT NULL) AS is_admin, " +
                "(c.codice_fiscale IS NOT NULL) AS is_cliente, " +
                "(s.codice_fiscale IS NOT NULL) AS is_staff " +
                "FROM utente u " +
                "LEFT JOIN admin a ON a.codice_fiscale = u.codice_fiscale " +
                "LEFT JOIN cliente c ON c.codice_fiscale = u.codice_fiscale " +
                "LEFT JOIN staff s ON s.codice_fiscale = u.codice_fiscale " +
                "WHERE u.email = ? AND u.password = ?;";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String codiceFiscale = resultSet.getString("codice_fiscale");
                    String nome = resultSet.getString("nome");
                    String cognome = resultSet.getString("cognome");
                    String telefono = resultSet.getString("telefono");
                    int eta = resultSet.getInt("eta");
                    String via = resultSet.getString("via");
                    int civico = resultSet.getInt("civico");
                    String cap = resultSet.getString("cap");
                    String cartaFedelta = resultSet.getString("carta_fedelta");

                    LocalDate dataNascita = null;
                    if (resultSet.getDate("datanascita") != null) {
                        dataNascita = resultSet.getDate("datanascita").toLocalDate();
                    }

                    if (resultSet.getBoolean("is_admin")) {
                        return new Admin(codiceFiscale, nome, cognome, email, telefono, password, dataNascita, eta, via, civico, cap, cartaFedelta);
                    } else if (resultSet.getBoolean("is_staff")) {
                        return new Staff(codiceFiscale, nome, cognome, email, telefono, password, dataNascita, eta, via, civico, cap, cartaFedelta,
                                resultSet.getString("qualifica"), resultSet.getString("iban"),
                                RuoloStaff.valueOf(resultSet.getString("ruolo")));
                    } else if (resultSet.getBoolean("is_cliente")) {
                        return new Cliente(codiceFiscale, nome, cognome, email, telefono, password, dataNascita, eta, via, civico, cap, cartaFedelta,
                                StatoAccount.valueOf(resultSet.getString("stato_account").toUpperCase()));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore critico durante il login nel DB: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
