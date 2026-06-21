package dao;

import model.logistica.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public interface TurnoDAO {
    ArrayList<Turno> getTurniByStaff(String cfStaff);

    boolean aggiungiTurno(String cfStaff, LocalDate data, LocalTime oraInizio, LocalTime oraFine);

    boolean modificaTurno(int idTurno, LocalDate data, LocalTime oraInizio, LocalTime oraFine);

    boolean rimuoviTurno(int idTurno);
}
