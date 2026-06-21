package implementazioniPostgresDAO;

import dao.TurnoDAO;
import database.ConnessioneDatabase;
import model.logistica.Turno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TurnoImplementazionePostgresDAO implements TurnoDAO {

    private Connection connection;

    public TurnoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Turno> getTurniByStaff(String cfStaff) {
        ArrayList<Turno> daRestituire = new ArrayList<>();
        String query = "SELECT t.id_turno, t.data, t.ora_inizio, t.ora_fine " +
                "FROM turno t JOIN staff_assegnato_turno sat ON t.id_turno = sat.id_turno " +
                "WHERE sat.cf_staff = ? ORDER BY t.data, t.ora_inizio;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, cfStaff);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate data = rs.getDate("data") != null ? rs.getDate("data").toLocalDate() : null;
                    LocalTime inizio = rs.getTime("ora_inizio") != null ? rs.getTime("ora_inizio").toLocalTime() : null;
                    LocalTime fine = rs.getTime("ora_fine") != null ? rs.getTime("ora_fine").toLocalTime() : null;
                    daRestituire.add(new Turno(rs.getInt("id_turno"), data, inizio, fine));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura dei turni: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiTurno(String cfStaff, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        String insertTurno = "INSERT INTO turno (data, ora_inizio, ora_fine) VALUES (?, ?, ?);";
        String insertAssegnazione = "INSERT INTO staff_assegnato_turno (cf_staff, id_turno) VALUES (?, ?);";
        try (PreparedStatement psTurno = this.connection.prepareStatement(insertTurno, Statement.RETURN_GENERATED_KEYS)) {
            psTurno.setDate(1, java.sql.Date.valueOf(data));
            psTurno.setTime(2, java.sql.Time.valueOf(oraInizio));
            psTurno.setTime(3, java.sql.Time.valueOf(oraFine));
            psTurno.executeUpdate();
            try (ResultSet chiavi = psTurno.getGeneratedKeys()) {
                if (chiavi.next()) {
                    int idTurno = chiavi.getInt(1);
                    try (PreparedStatement psAss = this.connection.prepareStatement(insertAssegnazione)) {
                        psAss.setString(1, cfStaff);
                        psAss.setInt(2, idTurno);
                        psAss.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta del turno: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modificaTurno(int idTurno, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        String query = "UPDATE turno SET data = ?, ora_inizio = ?, ora_fine = ? WHERE id_turno = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setDate(1, java.sql.Date.valueOf(data));
            ps.setTime(2, java.sql.Time.valueOf(oraInizio));
            ps.setTime(3, java.sql.Time.valueOf(oraFine));
            ps.setInt(4, idTurno);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica del turno: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviTurno(int idTurno) {
        String deleteAssegnazione = "DELETE FROM staff_assegnato_turno WHERE id_turno = ?;";
        String deleteTurno = "DELETE FROM turno WHERE id_turno = ?;";
        try (PreparedStatement psAss = this.connection.prepareStatement(deleteAssegnazione)) {
            psAss.setInt(1, idTurno);
            psAss.executeUpdate();
            try (PreparedStatement psTurno = this.connection.prepareStatement(deleteTurno)) {
                psTurno.setInt(1, idTurno);
                return psTurno.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione del turno: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
