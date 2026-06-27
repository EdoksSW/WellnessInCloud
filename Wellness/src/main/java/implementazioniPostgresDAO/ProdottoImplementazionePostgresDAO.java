package implementazioniPostgresDAO;

import dao.ProdottoDAO;
import database.ConnessioneDatabase;
import model.commerce.Categoria;
import model.commerce.Prodotto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
        String query = "SELECT p.id_prodotto, p.nome, p.prezzo, p.giacenza, p.id_categoria, c.cat_prodotto AS categoria_nome " +
                "FROM prodotto p LEFT JOIN categoria c ON p.id_categoria = c.id_categoria ORDER BY p.nome;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria categoria = null;
                int idCategoria = rs.getInt("id_categoria");
                if (!rs.wasNull()) {
                    categoria = new Categoria(idCategoria, rs.getString("categoria_nome"));
                }
                daRestituire.add(new Prodotto(
                        rs.getInt("id_prodotto"),
                        rs.getString("nome"),
                        rs.getBigDecimal("prezzo"),
                        rs.getInt("giacenza"),
                        categoria
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura dei prodotti: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiProdotto(String nome, BigDecimal prezzo, int giacenza, Integer idCategoria) {
        String query = "INSERT INTO prodotto (nome, prezzo, giacenza, id_categoria) VALUES (?, ?, ?, ?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setBigDecimal(2, prezzo);
            ps.setInt(3, giacenza);
            ps.setObject(4, idCategoria, Types.INTEGER);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta del prodotto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modificaProdotto(int idProdotto, String nome, BigDecimal prezzo, int giacenza, Integer idCategoria) {
        String query = "UPDATE prodotto SET nome = ?, prezzo = ?, giacenza = ?, id_categoria = ? WHERE id_prodotto = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setBigDecimal(2, prezzo);
            ps.setInt(3, giacenza);
            ps.setObject(4, idCategoria, Types.INTEGER);
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
