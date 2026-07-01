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
        String query = "INSERT INTO cliente (codicefiscale, nome, cognome, email, telefono, password, eta, stato_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, cliente.getCodiceFiscale());
            ps.setString(2, cliente.getNome());
            ps.setString(3, cliente.getCognome());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getPassword() != null ? cliente.getPassword() : "1234");
            ps.setInt(7, cliente.getEta());
            ps.setString(8, cliente.getStatoAcc().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificaClienteDaStaff(model.utenti.Cliente cliente) {
        String query = "UPDATE cliente SET nome = ?, cognome = ?, email = ?, telefono = ? WHERE codicefiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCodiceFiscale());
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
        String query = "SELECT scadenza_certificato FROM cliente WHERE codicefiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, codiceFiscale);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date dataSQL = rs.getDate("scadenza_certificato");
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
        String query = "UPDATE cliente SET scadenza_certificato = ? WHERE codicefiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setDate(1, java.sql.Date.valueOf(nuovaScadenza));
            ps.setString(2, codiceFiscale);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
