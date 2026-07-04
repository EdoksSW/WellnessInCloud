package dao;

import model.commerce.Categoria;

import java.util.ArrayList;

public interface CategoriaDAO {
    ArrayList<Categoria> getAllCategorie();

    boolean aggiungiCategoria(String nome);

    boolean modificaCategoria(int idCategoria, String nome);

    boolean rimuoviCategoria(int idCategoria);


}
