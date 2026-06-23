package model.commerce;

import java.math.BigDecimal;

public class Prodotto {
    int id_prodotto;
    private String nome;
    private BigDecimal prezzo;
    private int giacenza;
    String categoria;

    public Prodotto(int id_prodotto, String nome, BigDecimal prezzo, int giacenza, String categoria) {
        this.id_prodotto = id_prodotto;
        this.nome = nome;
        this.prezzo = prezzo;
        this.giacenza = giacenza;
        this.categoria = categoria;
    }

    public int getId_prodotto() {
        return id_prodotto;
    }

    public void setId_prodotto(int id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public int getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(int giacenza) {
        this.giacenza = giacenza;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
