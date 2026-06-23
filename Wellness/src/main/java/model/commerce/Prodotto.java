package model.commerce;

import java.math.BigDecimal;

public class Prodotto {
    private int id;
    private String nome;
    private BigDecimal prezzo;
    private int giacenza;
    private String categoria;

    public Prodotto(int id, String nome, BigDecimal prezzo, int giacenza, String categoria) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.giacenza = giacenza;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public int getGiacenza() {
        return giacenza;
    }

    public String getCategoria() {
        return categoria;
    }
}
