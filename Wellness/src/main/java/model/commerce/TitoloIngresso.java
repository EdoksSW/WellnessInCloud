package model.commerce;

import java.math.BigDecimal;

public class TitoloIngresso {
    private int id_titoloIngresso;
    private BigDecimal prezzo;
    private String tipo;

    public TitoloIngresso(int id_titoloIngresso, BigDecimal prezzo, String tipo) {
        this.id_titoloIngresso = id_titoloIngresso;
        this.prezzo = prezzo;
        this.tipo = tipo;
    }

    public int getId_titoloIngresso() {
        return id_titoloIngresso;
    }

    public void setId_titoloIngresso(int id_titoloIngresso) {
        this.id_titoloIngresso = id_titoloIngresso;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
