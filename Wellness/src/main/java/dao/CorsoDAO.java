package dao;

import model.logistica.Corso;

import java.util.ArrayList;

public interface CorsoDAO {
    ArrayList<Corso> getAllCorsi();

    boolean aggiungiCorso(String nome, String descrizione, String cfStaff);

    boolean modificaCorso(int idCorso, String nome, String descrizione, String cfStaff);

    boolean rimuoviCorso(int idCorso);
}
