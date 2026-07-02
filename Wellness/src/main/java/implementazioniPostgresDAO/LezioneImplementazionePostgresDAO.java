package implementazioniPostgresDAO;

import dao.LezioneDAO;
import database.ConnessioneDatabase;
import model.logistica.Lezione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

public class LezioneImplementazionePostgresDAO implements LezioneDAO {

    private Connection connection;

    public LezioneImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Lezione> getLezioniByCorso(int idCorso) {
        ArrayList<Lezione> daRestituire = new ArrayList<>();
        String query = "SELECT id_lezione, nome, descrizione, giorno, ora_inizio, ora_fine, id_corso, id_sala " +
                "FROM lezione WHERE id_corso = ? ORDER BY id_lezione;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, idCorso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalTime inizio = rs.getTime("ora_inizio") != null ? rs.getTime("ora_inizio").toLocalTime() : null;
                    LocalTime fine = rs.getTime("ora_fine") != null ? rs.getTime("ora_fine").toLocalTime() : null;
                    daRestituire.add(new Lezione(
                            rs.getInt("id_lezione"),
                            rs.getString("nome"),
                            rs.getString("descrizione"),
                            rs.getString("giorno"),
                            inizio,
                            fine,
                            rs.getInt("id_sala"),
                            rs.getInt("id_corso")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura delle lezioni: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiLezione(int idCorso, String nome, String descrizione, String giorno, LocalTime oraInizio, LocalTime oraFine, int idSala) {
        String query = "INSERT INTO lezione (nome, descrizione, giorno, ora_inizio, ora_fine, id_corso, id_sala) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setString(3, giorno);
            ps.setTime(4, oraInizio != null ? Time.valueOf(oraInizio) : null);
            ps.setTime(5, oraFine != null ? Time.valueOf(oraFine) : null);
            ps.setInt(6, idCorso);
            ps.setInt(7, idSala);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta della lezione: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modificaLezione(int idLezione, String nome, String descrizione, String giorno, LocalTime oraInizio, LocalTime oraFine, int idSala) {
        String query = "UPDATE lezione SET nome = ?, descrizione = ?, giorno = ?, ora_inizio = ?, ora_fine = ?, id_sala = ? WHERE id_lezione = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setString(3, giorno);
            ps.setTime(4, oraInizio != null ? Time.valueOf(oraInizio) : null);
            ps.setTime(5, oraFine != null ? Time.valueOf(oraFine) : null);
            ps.setInt(6, idSala);
            ps.setInt(7, idLezione);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica della lezione: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviLezione(int idLezione) {
        String query = "DELETE FROM lezione WHERE id_lezione = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, idLezione);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione della lezione: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<Integer> getSale() {
        ArrayList<Integer> daRestituire = new ArrayList<>();
        String query = "SELECT id_sala FROM sala ORDER BY id_sala;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daRestituire.add(rs.getInt("id_sala"));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura delle sale: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }
}
