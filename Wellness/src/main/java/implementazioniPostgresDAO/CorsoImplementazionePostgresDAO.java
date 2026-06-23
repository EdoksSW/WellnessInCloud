package implementazioniPostgresDAO;

import dao.CorsoDAO;
import database.ConnessioneDatabase;
import model.enums.RuoloStaff;
import model.logistica.Corso;
import model.utenti.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CorsoImplementazionePostgresDAO implements CorsoDAO {

    private Connection connection;

    public CorsoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Corso> getAllCorsi() {
        ArrayList<Corso> daRestituire = new ArrayList<>();
        String query = "SELECT c.id_corso, c.nome, c.descrizione, c.cf_staff, s.nome AS staff_nome, s.cognome AS staff_cognome, s.ruolo AS staff_ruolo " +
                "FROM corso c JOIN staff s ON c.cf_staff = s.codicefiscale ORDER BY c.nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Staff istruttore = new Staff(
                        rs.getString("cf_staff"),
                        rs.getString("staff_nome"),
                        rs.getString("staff_cognome"),
                        null, null, null, null, null, null,
                        RuoloStaff.valueOf(rs.getString("staff_ruolo"))
                );
                daRestituire.add(new Corso(
                        rs.getInt("id_corso"),
                        rs.getString("nome"),
                        rs.getString("descrizione"),
                        istruttore
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura dei corsi: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiCorso(String nome, String descrizione, String cfStaff) {
        String query = "INSERT INTO corso (nome, descrizione, cf_staff) VALUES (?, ?, ?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setString(3, cfStaff);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta del corso: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modificaCorso(int idCorso, String nome, String descrizione, String cfStaff) {
        String query = "UPDATE corso SET nome = ?, descrizione = ?, cf_staff = ? WHERE id_corso = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setString(3, cfStaff);
            ps.setInt(4, idCorso);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica del corso: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviCorso(int idCorso) {
        String query = "DELETE FROM corso WHERE id_corso = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, idCorso);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione del corso: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
