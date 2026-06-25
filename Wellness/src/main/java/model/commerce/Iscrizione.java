package model.commerce;

import model.utenti.Cliente;

import java.time.LocalDate;

public class Iscrizione {
    private int id_iscrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;

    private Cliente cliente;
    private TitoloIngresso titoloIngresso;

    public Iscrizione(int id_iscrizione, LocalDate dataInizio, LocalDate dataFine, Cliente cliente, TitoloIngresso titoloIngresso) {
        this.id_iscrizione = id_iscrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.cliente = cliente;
        this.titoloIngresso = titoloIngresso;
    }

    public int getId_iscrizione() {
        return id_iscrizione;
    }

    public void setId_iscrizione(int id_iscrizione) {
        this.id_iscrizione = id_iscrizione;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TitoloIngresso getTitoloIngresso() {
        return titoloIngresso;
    }

    public void setTitoloIngresso(TitoloIngresso titoloIngresso) {
        this.titoloIngresso = titoloIngresso;
    }
}
