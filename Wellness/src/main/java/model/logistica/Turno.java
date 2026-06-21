package model.logistica;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private int id;
    private LocalDate data;
    private LocalTime oraInizio, oraFine;

    public Turno(int id, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        this.id = id;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

    public int getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }
}
