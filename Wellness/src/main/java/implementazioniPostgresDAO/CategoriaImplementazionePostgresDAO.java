package implementazioniPostgresDAO;

import dao.CategoriaDAO;
import database.ConnessioneDatabase;
import model.commerce.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaImplementazionePostgresDAO implements CategoriaDAO {

    private Connection connection;

    public CategoriaImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Categoria> getAllCategorie() {
        ArrayList<Categoria> daRestituire = new ArrayList<>();
        String query = "SELECT id_categoria, cat_prodotto FROM categoria ORDER BY cat_prodotto;";
        try (PreparedStatement ps = this.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daRestituire.add(new Categoria(rs.getInt("id_categoria"), rs.getString("cat_prodotto")));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la lettura delle categorie: " + e.getMessage());
            e.printStackTrace();
        }
        return daRestituire;
    }

    @Override
    public boolean aggiungiCategoria(String nome) {
        String query = "INSERT INTO categoria (cat_prodotto) VALUES (?);";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta della categoria: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modificaCategoria(int idCategoria, String nome) {
        String query = "UPDATE categoria SET cat_prodotto = ? WHERE id_categoria = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setInt(2, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica della categoria: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviCategoria(int idCategoria) {
        String query = "DELETE FROM categoria WHERE id_categoria = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(query)) {
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione della categoria: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
