package model.commerce;

import model.enums.StatoOrdine;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Ordine {
    int id_ordine;
    LocalDate data_ordine;
    private BigDecimal totale;
    private StatoOrdine stato;
    private Pagamento pagamento;

    public Ordine(int id_ordine, LocalDate data_ordine, BigDecimal totale, StatoOrdine stato, Pagamento pagamento) {
        this.id_ordine = id_ordine;
        this.data_ordine = data_ordine;
        this.totale = totale;
        this.stato = stato;
        this.pagamento = pagamento;
    }

    public int getId_ordine() {
        return id_ordine;
    }

    public void setId_ordine(int id_ordine) {
        this.id_ordine = id_ordine;
    }

    public LocalDate getData_ordine() {
        return data_ordine;
    }

    public void setData_ordine(LocalDate data_ordine) {
        this.data_ordine = data_ordine;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
