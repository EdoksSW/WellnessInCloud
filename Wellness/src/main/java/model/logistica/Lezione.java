package model.logistica;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class Lezione {
    private int id_lezione;
    private String nome;
    private String descrizione;
    private String giorno;
    private LocalTime ora_inizio;
    private LocalTime oraFine;
    private String sala;

    private int id_corso;

    public Lezione(int id_lezione, String nome) {
        this.id_lezione = id_lezione;
        this.nome = nome;
    }

    public Lezione(int id_lezione, String nome, String descrizione, String giorno, LocalTime ora_inizio, LocalTime oraFine, String sala, int id_corso) {
        this.id_lezione = id_lezione;
        this.nome = nome;
        this.descrizione = descrizione;
        this.giorno = giorno;
        this.ora_inizio = ora_inizio;
        this.oraFine = oraFine;
        this.sala = sala;
        this.id_corso = id_corso;
    }

    public int getId_lezione() {
        return id_lezione;
    }

    public void setId_lezione(int id_lezione) {
        this.id_lezione = id_lezione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public LocalTime getOra_inizio() {
        return ora_inizio;
    }

    public void setOra_inizio(LocalTime ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public int getId_corso() {
        return id_corso;
    }

    public void setId_corso(int id_corso) {
        this.id_corso = id_corso;
    }
}
