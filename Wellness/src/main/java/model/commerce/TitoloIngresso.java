package model.commerce;

import model.enums.TipoTitolo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class TitoloIngresso {
    private int id_titoloIngresso;
    private BigDecimal prezzo;
    private TipoTitolo tipo;

    public TitoloIngresso(int id_titoloIngresso, BigDecimal prezzo, TipoTitolo tipo) {
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

    public TipoTitolo getTipo() {
        return tipo;
    }

    public void setTipo(TipoTitolo tipo) {
        this.tipo = tipo;
    }
}
