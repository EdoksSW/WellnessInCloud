package model.commerce;

import model.utenti.Cliente;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Carrello {
    private int id_carrello;
    private Cliente cliente;
    private ArrayList<Prodotto>prodotto;
    private BigDecimal totale;

    public Carrello(Cliente cliente, int id_carrello, BigDecimal totale) {
        this.cliente = cliente;
        this.id_carrello = id_carrello;
        this.totale = totale;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getId_carrello() {
        return id_carrello;
    }

    public void setId_carrello(int id_carrello) {
        this.id_carrello = id_carrello;
    }
}

