package model.commerce;

import model.utenti.Cliente;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Carrello {
    private int id;
    private Cliente cliente;
    private ArrayList<Prodotto>prodotto;
    private BigDecimal totale;
}
