package dao;

import model.commerce.Prodotto;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface ProdottoDAO {
    ArrayList<Prodotto> getAllProdotti();

    boolean aggiungiProdotto(String nome, BigDecimal prezzo, int giacenza, String categoria);

    boolean modificaProdotto(int idProdotto, String nome, BigDecimal prezzo, int giacenza, String categoria);

    boolean rimuoviProdotto(int idProdotto);
}
