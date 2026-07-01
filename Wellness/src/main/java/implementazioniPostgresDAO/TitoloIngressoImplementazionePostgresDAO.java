package implementazioniPostgresDAO;

import dao.TitoloIngressoDAO;
import database.ConnessioneDatabase;
import model.commerce.TitoloIngresso;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TitoloIngressoImplementazionePostgresDAO implements TitoloIngressoDAO {

    private Connection connection;

    public TitoloIngressoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<TitoloIngresso> getAllTitoli() {
        ArrayList<TitoloIngresso> daRestituire = new ArrayList<>();
        String query = "SELECT id_titolo, tipo, prezzo FROM titolo_ingresso ORDER BY tipo;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daRestituire.add(new TitoloIngresso(
                        rs.getInt("id_titolo"),
                        rs.getBigDecimal("prezzo"),
                        rs.getString("tipo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura dei titoli: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiTitolo(String tipo, BigDecimal prezzo) {
        String query = "INSERT INTO titolo_ingresso (tipo, prezzo) VALUES (?, ?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, tipo);
            ps.setBigDecimal(2, prezzo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta del titolo: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modificaTitolo(int idTitolo, String tipo, BigDecimal prezzo) {
        String query = "UPDATE titolo_ingresso SET tipo = ?, prezzo = ? WHERE id_titolo = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, tipo);
            ps.setBigDecimal(2, prezzo);
            ps.setInt(3, idTitolo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica del titolo: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviTitolo(int idTitolo) {
        String query = "DELETE FROM titolo_ingresso WHERE id_titolo = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, idTitolo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione del titolo: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
