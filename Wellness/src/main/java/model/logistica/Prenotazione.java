package model.logistica;

import model.enums.StatoPrenotazione;
import model.utenti.Cliente;

import java.time.LocalDate;
import java.time.LocalTime;

public class Prenotazione {
    private int idPrenotazione; // AGGIUNTO ID
    private LocalDate dataPren;
    private LocalTime oraPren;
    private StatoPrenotazione stato;
    private Cliente cliente;
    private Lezione lezione;

    // Costruttore AGGIORNATO con ID
    public Prenotazione(int idPrenotazione, LocalDate dataPren, LocalTime oraPren, StatoPrenotazione stato, Cliente cliente, Lezione lezione) {
        this.idPrenotazione = idPrenotazione;
        this.dataPren = dataPren;
        this.oraPren = oraPren;
        this.stato = stato;
        this.cliente = cliente;
        this.lezione = lezione;
    }

    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public LocalDate getDataPren() { return dataPren; }
    public void setDataPren(LocalDate dataPren) { this.dataPren = dataPren; }
    public LocalTime getOraPren() { return oraPren; }
    public void setOraPren(LocalTime oraPren) { this.oraPren = oraPren; }
    public StatoPrenotazione getStato() { return stato; }
    public void setStato(StatoPrenotazione stato) { this.stato = stato; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Lezione getLezione() { return lezione; }
    public void setLezione(Lezione lezione) { this.lezione = lezione; }
}