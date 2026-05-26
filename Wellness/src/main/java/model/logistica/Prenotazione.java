package model.logistica;

import model.enums.StatoPrenotazione;
import model.utenti.Cliente;

import java.time.LocalDateTime;

public class Prenotazione {
    private LocalDateTime dataOra;
    private StatoPrenotazione stato;
    private Cliente cliente;
    private Lezione lezione;
}
