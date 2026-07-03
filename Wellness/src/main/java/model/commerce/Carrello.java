package model.commerce;

import model.utenti.Cliente;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Carrello {
    private int id_carrello;
    private Cliente cliente;
    private Map<Prodotto, Integer> prodotti;
    private BigDecimal totale;


    public Carrello(Cliente cliente, int id_carrello, BigDecimal totale) {
        this.cliente = cliente;
        this.id_carrello = id_carrello;
        this.totale = totale;
        this.prodotti=new HashMap<>(); //inizializzo una mappa vuota
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public void aggiungiProdotto(Prodotto prodotto, int quantita)
    {
        this.prodotti.put(prodotto, quantita);
    }

    public int getId_carrello() {
        return id_carrello;
    }

    public void setId_carrello(int id_carrello) {
        this.id_carrello = id_carrello;
    }

    public Map<Prodotto, Integer> getMapCarrellol() {
        return prodotti;
    }

    public void setProdotti(Map<Prodotto, Integer> prodotti) {
        this.prodotti = prodotti;
    }


}

