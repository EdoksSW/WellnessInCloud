package implementazioniPostgresDAO;

import dao.ProdottoDAO;
import database.ConnessioneDatabase;
import model.commerce.Prodotto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdottoImplementazionePostgresDAO implements ProdottoDAO {

    private Connection connection;

    public ProdottoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Prodotto> getAllProdotti() {
        ArrayList<Prodotto> daRestituire = new ArrayList<>();
        String query = "SELECT id_prodotto, nome, prezzo, giacenza, categoria FROM prodotto ORDER BY nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daRestituire.add(new Prodotto(
                        rs.getInt("id_prodotto"),
                        rs.getString("nome"),
                        rs.getBigDecimal("prezzo"),
                        rs.getInt("giacenza"),
                        rs.getString("categoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura dei prodotti: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiProdotto(String nome, BigDecimal prezzo, int giacenza, String categoria) {
        String query = "INSERT INTO prodotto (nome, prezzo, giacenza, categoria) VALUES (?, ?, ?, ?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setBigDecimal(2, prezzo);
            ps.setInt(3, giacenza);
            ps.setString(4, categoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta del prodotto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modificaProdotto(int idProdotto, String nome, BigDecimal prezzo, int giacenza, String categoria) {
        String query = "UPDATE prodotto SET nome = ?, prezzo = ?, giacenza = ?, categoria = ? WHERE id_prodotto = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setBigDecimal(2, prezzo);
            ps.setInt(3, giacenza);
            ps.setString(4, categoria);
            ps.setInt(5, idProdotto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica del prodotto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviProdotto(int idProdotto) {
        String query = "DELETE FROM prodotto WHERE id_prodotto = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, idProdotto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione del prodotto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
