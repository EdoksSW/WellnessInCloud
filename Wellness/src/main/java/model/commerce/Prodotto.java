package model.commerce;

import java.math.BigDecimal;

public class Prodotto {
    private int id;
    private String nome;
    private BigDecimal prezzo;
    private int giacenza;
    private Categoria categoria;

    public Prodotto(int id, String nome, BigDecimal prezzo, int giacenza, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.giacenza = giacenza;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public int getId_prodotto() {
        return id;
    }

    public void setId_prodotto(int id_prodotto) {
        this.id = id_prodotto;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
