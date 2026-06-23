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
                "WITH utenti AS (" +
                "  SELECT email, password, codicefiscale, nome, cognome, telefono, datanascita, eta, via, civico, cap, stato_account, NULL AS iban, NULL AS ruolo, NULL AS qualifica, 'Cliente' AS ruol FROM cliente " +
                "  UNION ALL " +
                "  SELECT email, password, codicefiscale, nome, cognome, telefono, datanascita, eta, via, civico, cap, NULL AS stato_account, iban, ruolo, qualifica, 'Staff' AS ruol FROM staff " +
                "  UNION ALL " +
                "  SELECT email, password, codicefiscale, nome, cognome, NULL AS telefono, datanascita, NULL AS eta, NULL AS via, NULL AS civico, NULL AS cap, NULL AS stato_account, NULL AS iban, NULL AS ruolo, NULL AS qualifica, 'Admin' AS ruol FROM admin " +
                ") " +
                "SELECT * FROM utenti WHERE email = ? AND password = ?;";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String codiceFiscale = resultSet.getString("codicefiscale");
                    String nome = resultSet.getString("nome");
                    String cognome = resultSet.getString("cognome");
                    String telefono = resultSet.getString("telefono");
                    String ruol = resultSet.getString("ruol");

                    LocalDate dataNascita = null;
                    if (resultSet.getDate("datanascita") != null) {
                        dataNascita = resultSet.getDate("datanascita").toLocalDate();
                    }

                    int eta = resultSet.getInt("eta");

                    switch (ruol) {
                        case "Admin":
                            return new Admin(codiceFiscale, nome, cognome, email, telefono, password, dataNascita);
                        case "Staff":
                            return new Staff(codiceFiscale, nome, cognome, email, telefono, password, dataNascita, eta,
                                    resultSet.getString("qualifica"), resultSet.getString("iban"),
                                    RuoloStaff.valueOf(resultSet.getString("ruolo")));
                        default:
                            String via = resultSet.getString("via");
                            String cap = resultSet.getString("cap");
                            String stato = resultSet.getString("stato_account");
                            int numCivico = resultSet.getInt("civico");
                            return new Cliente(codiceFiscale, nome, cognome, email, telefono, password, dataNascita, eta,
                                    via, numCivico, cap, StatoAccount.valueOf(stato.toUpperCase()));
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

    @Override
    public boolean registraClienteDB(String cf, String nome, String cognome, String email, String telefono,
                                     LocalDate dataNascita, int eta, String password, String indirizzo, int numCivico, String cap) {
        return false;
    }

    @Override
    public boolean registraStaffDB(String cf, String nome, String cognome, String email, String telefono,
                                   LocalDate dataNascita, int eta, String password, String qualifica, String iban, RuoloStaff ruolo) {
        return false;
    }
}
