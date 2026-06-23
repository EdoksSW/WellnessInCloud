package model.logistica;

import model.utenti.Staff;

public class Corso {
    private String nome, descrizione;
    private int id;
    private Staff istruttore;

    public Corso(int id, String nome, String descrizione, Staff istruttore) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.istruttore = istruttore;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Staff getIstruttore() {
        return istruttore;
    }
}
