package dao;

import model.commerce.TitoloIngresso;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface TitoloIngressoDAO {
    ArrayList<TitoloIngresso> getAllTitoli();

    boolean aggiungiTitolo(String tipo, BigDecimal prezzo);

    boolean modificaTitolo(int idTitolo, String tipo, BigDecimal prezzo);

    boolean rimuoviTitolo(int idTitolo);
}
