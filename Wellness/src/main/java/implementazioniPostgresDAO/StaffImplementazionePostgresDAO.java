package implementazioniPostgresDAO;

import dao.StaffDAO;
import database.ConnessioneDatabase;
import model.enums.RuoloStaff;
import model.enums.StatoAccount;
import model.utenti.Cliente;
import model.utenti.Staff;

import java.sql.Connection;
import java.sql.Date;
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

    private Staff costruisciStaff(ResultSet rs) throws SQLException {
        LocalDate dataNascita = rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null;
        return new Staff(
                rs.getString("codice_fiscale"),
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("password"),
                dataNascita,
                rs.getInt("eta"),
                rs.getString("via"),
                rs.getInt("civico"),
                rs.getString("cap"),
                rs.getString("carta_fedelta"),
                rs.getString("qualifica"),
                rs.getString("iban"),
                RuoloStaff.valueOf(rs.getString("ruolo"))
        );
    }

    @Override
    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> daRestituire = new ArrayList<>();
        String query = "SELECT u.codice_fiscale, u.nome, u.cognome, u.email, u.telefono, u.password, u.datanascita, u.eta, u.via, u.civico, u.cap, u.carta_fedelta, s.qualifica, s.iban, s.ruolo " +
                "FROM staff s JOIN utente u ON s.codice_fiscale = u.codice_fiscale ORDER BY u.cognome, u.nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daRestituire.add(costruisciStaff(rs));
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
        String query = "SELECT u.codice_fiscale, u.nome, u.cognome, u.email, u.telefono, u.password, u.datanascita, u.eta, u.via, u.civico, u.cap, u.carta_fedelta, s.qualifica, s.iban, s.ruolo " +
                "FROM staff s JOIN utente u ON s.codice_fiscale = u.codice_fiscale WHERE s.ruolo = 'ISTRUTTORE' ORDER BY u.cognome, u.nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daRestituire.add(costruisciStaff(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura degli istruttori: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public ArrayList<Cliente> getAllClientiDaStaff() {
        ArrayList<Cliente> daRestituire = new ArrayList<>();
        String query = "SELECT u.codice_fiscale, u.nome, u.cognome, u.email, u.telefono, u.password, u.datanascita, u.eta, u.via, u.civico, u.cap, u.carta_fedelta, c.stato_account " +
                "FROM cliente c JOIN utente u ON c.codice_fiscale = u.codice_fiscale ORDER BY u.cognome, u.nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate dataNascita = rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null;
                daRestituire.add(new Cliente(
                        rs.getString("codice_fiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("password"),
                        dataNascita,
                        rs.getInt("eta"),
                        rs.getString("via"),
                        rs.getInt("civico"),
                        rs.getString("cap"),
                        rs.getString("carta_fedelta"),
                        StatoAccount.valueOf(rs.getString("stato_account").trim().toUpperCase())
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura dei clienti: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiClienteDaStaff(Cliente cliente) {
        String queryUtente = "INSERT INTO utente (codice_fiscale, nome, cognome, email, telefono, password, datanascita, eta, via, civico, cap, carta_fedelta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String queryCliente = "INSERT INTO cliente (codice_fiscale, stato_account) VALUES (?, ?);";
        try {
            this.connection.setAutoCommit(false);
            try (PreparedStatement psUtente = this.connection.prepareStatement(queryUtente)) {
                psUtente.setString(1, cliente.getCodiceFiscale());
                psUtente.setString(2, cliente.getNome());
                psUtente.setString(3, cliente.getCognome());
                psUtente.setString(4, cliente.getEmail());
                psUtente.setString(5, cliente.getTelefono());
                psUtente.setString(6, cliente.getPassword());
                if (cliente.getDataNascita() != null) {
                    psUtente.setDate(7, Date.valueOf(cliente.getDataNascita()));
                } else {
                    psUtente.setNull(7, java.sql.Types.DATE);
                }
                psUtente.setInt(8, cliente.getEta());
                psUtente.setString(9, cliente.getVia());
                psUtente.setInt(10, cliente.getCivico());
                psUtente.setString(11, cliente.getCap());
                psUtente.setString(12, cliente.getCartaFedelta());
                psUtente.executeUpdate();
            }
            try (PreparedStatement psCliente = this.connection.prepareStatement(queryCliente)) {
                psCliente.setString(1, cliente.getCodiceFiscale());
                psCliente.setString(2, cliente.getStatoAcc() != null ? cliente.getStatoAcc().name() : StatoAccount.ATTIVO.name());
                psCliente.executeUpdate();
            }
            this.connection.commit();
            this.connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
                this.connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Errore durante l'aggiunta del cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificaClienteDaStaff(Cliente cliente) {
        String query = "UPDATE utente SET nome = ?, cognome = ?, email = ?, telefono = ? WHERE codice_fiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCodiceFiscale());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica del cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviClienteDaStaff(String codiceFiscale) {
        String query = "DELETE FROM utente WHERE codice_fiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, codiceFiscale);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione del cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public LocalDate getCertificatoDaStaff(String codiceFiscale) {
        String query = "SELECT data_scadenza FROM certificato WHERE cf_cliente = ? ORDER BY data_scadenza DESC NULLS LAST LIMIT 1;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, codiceFiscale);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getDate("data_scadenza") != null) {
                    return rs.getDate("data_scadenza").toLocalDate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura del certificato: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean aggiornaCertificatoDaStaff(String codiceFiscale, LocalDate nuovaScadenza) {
        String queryUpdate = "UPDATE certificato SET data_scadenza = ? WHERE cert_id = " +
                "(SELECT cert_id FROM certificato WHERE cf_cliente = ? ORDER BY data_emissione DESC NULLS LAST, cert_id DESC LIMIT 1);";
        String queryInsert = "INSERT INTO certificato (tipo, data_emissione, data_scadenza, cf_cliente) VALUES ('Medico', CURRENT_DATE, ?, ?);";
        try (PreparedStatement psUpdate = this.connection.prepareStatement(queryUpdate)) {
            psUpdate.setDate(1, Date.valueOf(nuovaScadenza));
            psUpdate.setString(2, codiceFiscale);
            if (psUpdate.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del certificato: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        try (PreparedStatement psInsert = this.connection.prepareStatement(queryInsert)) {
            psInsert.setDate(1, Date.valueOf(nuovaScadenza));
            psInsert.setString(2, codiceFiscale);
            return psInsert.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del certificato: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
