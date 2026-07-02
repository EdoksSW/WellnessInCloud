package dao;

import model.logistica.Lezione;

import java.time.LocalTime;
import java.util.ArrayList;

public interface LezioneDAO {
    ArrayList<Lezione> getLezioniByCorso(int idCorso);

    boolean aggiungiLezione(int idCorso, String nome, String descrizione, String giorno, LocalTime oraInizio, LocalTime oraFine, int idSala);

    boolean modificaLezione(int idLezione, String nome, String descrizione, String giorno, LocalTime oraInizio, LocalTime oraFine, int idSala);

    boolean rimuoviLezione(int idLezione);

    ArrayList<Integer> getSale();
}
