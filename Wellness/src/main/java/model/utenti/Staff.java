package model.utenti;

import model.enums.RuoloStaff;

import java.time.LocalDate;

public class Staff extends Utente {
    private String qualifica,iban;
    private int eta;
    private RuoloStaff ruolo;

    public Staff(String codiceFiscale, String nome, String cognome, String email, String telefono, String password, LocalDate dataNascita, int eta, String qualifica, String iban, RuoloStaff ruolo) {
        super(codiceFiscale, nome, cognome, email, telefono, password, dataNascita);
        this.eta = eta;
        this.qualifica = qualifica;
        this.iban = iban;
        this.ruolo = ruolo;
    }

    public int getEta() {
        return eta;
    }

    public String getQualifica() {
        return qualifica;
    }

    public String getIban() {
        return iban;
    }

    public RuoloStaff getRuolo() {
        return ruolo;
    }
}
