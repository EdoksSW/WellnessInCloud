package implementazioniPostgresDAO;

import dao.StaffDAO;
import database.ConnessioneDatabase;
import model.enums.RuoloStaff;
import model.utenti.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class StaffImplementazionePostgresDAO implements StaffDAO {

    private Connection connection;

    public StaffImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> daRestituire = new ArrayList<>();
        String query = "SELECT codicefiscale, nome, cognome, email, password, telefono, datanascita, eta, qualifica, iban, ruolo FROM staff ORDER BY cognome, nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate dataNascita = rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null;
                int eta = rs.getInt("eta");
                Staff s = new Staff(
                        rs.getString("codicefiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("password"),
                        dataNascita,
                        eta,
                        rs.getString("qualifica"),
                        rs.getString("iban"),
                        RuoloStaff.valueOf(rs.getString("ruolo"))
                );
                daRestituire.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura dello staff: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public ArrayList<Staff> getIstruttori() {
        ArrayList<Staff> daRestituire = new ArrayList<>();
        String query = "SELECT codicefiscale, nome, cognome, email, password, telefono, datanascita, eta, qualifica, iban, ruolo FROM staff WHERE ruolo = 'ISTRUTTORE' ORDER BY cognome, nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate dataNascita = rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null;
                int eta = rs.getInt("eta");
                Staff s = new Staff(
                        rs.getString("codicefiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("password"),
                        dataNascita,
                        eta,
                        rs.getString("qualifica"),
                        rs.getString("iban"),
                        RuoloStaff.valueOf(rs.getString("ruolo"))
                );
                daRestituire.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura degli istruttori: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public ArrayList<model.utenti.Cliente> getAllClientiDaStaff() {
        ArrayList<model.utenti.Cliente> daRestituire = new ArrayList<>();
        String query = "SELECT codicefiscale, nome, cognome, email, telefono, stato_account FROM cliente ORDER BY cognome, nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String stato_acc = rs.getString("stato_account");
                model.enums.StatoAccount statoAccount = model.enums.StatoAccount.valueOf(stato_acc.trim().toUpperCase());

                model.utenti.Cliente c = new model.utenti.Cliente(
                        rs.getString("codicefiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        "", null, 0, "", 0, "", statoAccount
                );
                daRestituire.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Errore lettura clienti da StaffDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiClienteDaStaff(model.utenti.Cliente cliente) {
        // Ho aggiunto "datanascita" nella query e un punto interrogativo in più
        String query = "INSERT INTO cliente (codicefiscale, nome, cognome, email, telefono, password, datanascita, eta, stato_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, cliente.getCodiceFiscale());
            ps.setString(2, cliente.getNome());
            ps.setString(3, cliente.getCognome());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getPassword() != null ? cliente.getPassword() : "1234");

            // Inseriamo la data di nascita tradotta per Postgres
            ps.setDate(7, java.sql.Date.valueOf(cliente.getDataNascita()));

            ps.setInt(8, cliente.getEta());
            ps.setString(9, cliente.getStatoAcc().name());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificaClienteDaStaff(model.utenti.Cliente cliente) {
        // Aggiunto "datanascita = ?" come quinto parametro da aggiornare
        String query = "UPDATE cliente SET nome = ?, cognome = ?, email = ?, telefono = ?, datanascita = ? WHERE codicefiscale = ?;";

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());

            // Passiamo la data aggiornata (5)
            ps.setDate(5, java.sql.Date.valueOf(cliente.getDataNascita()));

            // Il Codice Fiscale ora scala in posizione 6
            ps.setString(6, cliente.getCodiceFiscale());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rimuoviClienteDaStaff(String codiceFiscale) {
        String query = "DELETE FROM cliente WHERE codicefiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, codiceFiscale);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public LocalDate getCertificatoDaStaff(String codiceFiscale) {
        String query = "SELECT data_scadenza FROM certificato WHERE cf_cliente = ?;";

        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, codiceFiscale);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Leggiamo la colonna "data_scadenza"
                    java.sql.Date dataSQL = rs.getDate("data_scadenza");
                    if (dataSQL != null) return dataSQL.toLocalDate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean aggiornaCertificatoDaStaff(String codiceFiscale, LocalDate nuovaScadenza) {

        String updateQuery = "UPDATE certificato SET data_scadenza = ? WHERE cf_cliente = ?;";

        try (PreparedStatement psUpdate = this.connection.prepareStatement(updateQuery)) {
            psUpdate.setDate(1, java.sql.Date.valueOf(nuovaScadenza));
            psUpdate.setString(2, codiceFiscale);

            int righeModificate = psUpdate.executeUpdate();

            if (righeModificate > 0) {
                return true;
            } else {
                String insertQuery = "INSERT INTO certificato (data_emissione, data_scadenza, cf_cliente) VALUES (?, ?, ?);";

                try (PreparedStatement psInsert = this.connection.prepareStatement(insertQuery)) {
                    psInsert.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Data di oggi come emissione
                    psInsert.setDate(2, java.sql.Date.valueOf(nuovaScadenza));
                    psInsert.setString(3, codiceFiscale);

                    return psInsert.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore in aggiornaCertificatoDaStaff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
