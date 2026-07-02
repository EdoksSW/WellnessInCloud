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

        // MODIFICA: Aggiunto INNER JOIN con la tabella utente per recuperare i dati anagrafici
        String query = "SELECT u.codice_fiscale, u.nome, u.cognome, u.email, u.telefono, u.password, u.datanascita, u.eta, s.qualifica, s.iban, s.ruolo " +
                "FROM utente u " +
                "INNER JOIN staff s ON u.codice_fiscale = s.codice_fiscale " +
                "ORDER BY u.cognome, u.nome;";

        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate dataNascita = rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null;
                int eta = rs.getInt("eta");

                Staff s = new Staff(
                        rs.getString("codice_fiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("password"),
                        dataNascita,
                        eta,
                        "", 0, "", "", // Tappi
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

        // MODIFICA: Aggiunto INNER JOIN anche qui
        String query = "SELECT u.codice_fiscale, u.nome, u.cognome, u.email, u.telefono, u.password, u.datanascita, u.eta, s.qualifica, s.iban, s.ruolo " +
                "FROM utente u " +
                "INNER JOIN staff s ON u.codice_fiscale = s.codice_fiscale " +
                "WHERE s.ruolo = 'ISTRUTTORE' " +
                "ORDER BY u.cognome, u.nome;";

        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate dataNascita = rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null;
                int eta = rs.getInt("eta");

                Staff s = new Staff(
                        rs.getString("codice_fiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("password"),
                        dataNascita,
                        eta,
                        "", 0, "", "", // Tappi
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
    public boolean aggiungiStaff(Staff staff) {
        String queryUtente = "INSERT INTO utente (codice_fiscale, nome, cognome, email, telefono, password, datanascita, eta, via, civico, cap, carta_fedelta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String queryStaff = "INSERT INTO staff (codice_fiscale, ruolo, qualifica, iban) VALUES (?, ?, ?, ?);";
        try {
            this.connection.setAutoCommit(false);
            try (PreparedStatement psUtente = this.connection.prepareStatement(queryUtente)) {
                psUtente.setString(1, staff.getCodiceFiscale());
                psUtente.setString(2, staff.getNome());
                psUtente.setString(3, staff.getCognome());
                psUtente.setString(4, staff.getEmail());
                psUtente.setString(5, staff.getTelefono());
                psUtente.setString(6, staff.getPassword());
                if (staff.getDataNascita() != null) {
                    psUtente.setDate(7, java.sql.Date.valueOf(staff.getDataNascita()));
                } else {
                    psUtente.setNull(7, java.sql.Types.DATE);
                }
                psUtente.setInt(8, staff.getEta());
                psUtente.setString(9, staff.getVia());
                psUtente.setInt(10, staff.getCivico());
                psUtente.setString(11, staff.getCap());
                psUtente.setString(12, staff.getCartaFedelta());
                psUtente.executeUpdate();
            }
            try (PreparedStatement psStaff = this.connection.prepareStatement(queryStaff)) {
                psStaff.setString(1, staff.getCodiceFiscale());
                psStaff.setString(2, staff.getRuolo() != null ? staff.getRuolo().name() : null);
                psStaff.setString(3, staff.getQualifica());
                psStaff.setString(4, staff.getIban());
                psStaff.executeUpdate();
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
            System.err.println("Errore durante l'aggiunta dello staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificaStaff(Staff staff) {
        String queryUtente = "UPDATE utente SET nome = ?, cognome = ?, email = ?, telefono = ? WHERE codice_fiscale = ?;";
        String queryStaff = "UPDATE staff SET qualifica = ?, iban = ?, ruolo = ? WHERE codice_fiscale = ?;";
        try {
            this.connection.setAutoCommit(false);
            try (PreparedStatement psUtente = this.connection.prepareStatement(queryUtente)) {
                psUtente.setString(1, staff.getNome());
                psUtente.setString(2, staff.getCognome());
                psUtente.setString(3, staff.getEmail());
                psUtente.setString(4, staff.getTelefono());
                psUtente.setString(5, staff.getCodiceFiscale());
                psUtente.executeUpdate();
            }
            try (PreparedStatement psStaff = this.connection.prepareStatement(queryStaff)) {
                psStaff.setString(1, staff.getQualifica());
                psStaff.setString(2, staff.getIban());
                psStaff.setString(3, staff.getRuolo() != null ? staff.getRuolo().name() : null);
                psStaff.setString(4, staff.getCodiceFiscale());
                psStaff.executeUpdate();
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
            System.err.println("Errore durante la modifica dello staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rimuoviStaff(String codiceFiscale) {
        String query = "DELETE FROM utente WHERE codice_fiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, codiceFiscale);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione dello staff: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<model.utenti.Cliente> getAllClientiDaStaff() {
        ArrayList<model.utenti.Cliente> daRestituire = new ArrayList<>();

        String query = "SELECT u.codice_fiscale, u.nome, u.cognome, u.email, u.telefono, c.stato_account " +
                "FROM utente u " +
                "INNER JOIN cliente c ON u.codice_fiscale = c.codice_fiscale " +
                "ORDER BY u.cognome, u.nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String stato_acc = rs.getString("stato_account");
                model.enums.StatoAccount statoAccount = model.enums.StatoAccount.valueOf(stato_acc.trim().toUpperCase());

                model.utenti.Cliente c = new model.utenti.Cliente(
                        rs.getString("codice_fiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        "", null, 0, "", 0, "", "", // Tappi per i parametri vuoti
                        statoAccount
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
        // MODIFICA: Logica a due tabelle con transazione per evitare salvataggi a metà
        String insertUtente = "INSERT INTO utente (codice_fiscale, nome, cognome, email, telefono, password, datanascita, eta, via, civico, cap, carta_fedelta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String insertCliente = "INSERT INTO cliente (codice_fiscale, stato_account) VALUES (?, ?);";

        try {
            this.connection.setAutoCommit(false);

            try (PreparedStatement psUtente = this.connection.prepareStatement(insertUtente)) {
                psUtente.setString(1, cliente.getCodiceFiscale());
                psUtente.setString(2, cliente.getNome());
                psUtente.setString(3, cliente.getCognome());
                psUtente.setString(4, cliente.getEmail());
                psUtente.setString(5, cliente.getTelefono());
                psUtente.setString(6, cliente.getPassword() != null ? cliente.getPassword() : "1234");

                if (cliente.getDataNascita() != null) {
                    psUtente.setDate(7, java.sql.Date.valueOf(cliente.getDataNascita()));
                } else {
                    psUtente.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
                }

                psUtente.setInt(8, cliente.getEta());
                psUtente.setString(9, "");
                psUtente.setInt(10, 0);
                psUtente.setString(11, "");
                psUtente.setString(12, "");
                psUtente.executeUpdate();
            }

            try (PreparedStatement psCliente = this.connection.prepareStatement(insertCliente)) {
                psCliente.setString(1, cliente.getCodiceFiscale());
                psCliente.setString(2, cliente.getStatoAcc().name());
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
            System.err.println("Errore SQL in aggiungiCliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modificaClienteDaStaff(model.utenti.Cliente cliente) {
        // MODIFICA: Aggiorniamo la tabella utente, non più cliente
        String query = "UPDATE utente SET nome = ?, cognome = ?, email = ?, telefono = ?, datanascita = ? WHERE codice_fiscale = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());

            if (cliente.getDataNascita() != null) {
                ps.setDate(5, java.sql.Date.valueOf(cliente.getDataNascita()));
            } else {
                ps.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
            }

            ps.setString(6, cliente.getCodiceFiscale());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore in modificaClienteDaStaff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rimuoviClienteDaStaff(String codiceFiscale) {
        // MODIFICA: Transazione per rimuovere prima il figlio (cliente) e poi il padre (utente)
        String queryCliente = "DELETE FROM cliente WHERE codice_fiscale = ?;";
        String queryUtente = "DELETE FROM utente WHERE codice_fiscale = ?;";

        try {
            this.connection.setAutoCommit(false);

            try (PreparedStatement ps1 = this.connection.prepareStatement(queryCliente)) {
                ps1.setString(1, codiceFiscale);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = this.connection.prepareStatement(queryUtente)) {
                ps2.setString(1, codiceFiscale);
                ps2.executeUpdate();
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
            System.err.println("Errore in rimuoviClienteDaStaff: " + e.getMessage());
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
                    psInsert.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
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