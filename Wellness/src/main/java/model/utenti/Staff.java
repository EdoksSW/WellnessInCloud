package model.utenti;

import model.enums.RuoloStaff;

import java.time.LocalDate;

public class Staff extends Utente {
    private String qualifica,iban;
    private RuoloStaff ruolo;

    public Staff(String codiceFiscale, String nome, String cognome, String email, String telefono, String password, LocalDate dataNascita, String qualifica, String iban, RuoloStaff ruolo) {
        super(codiceFiscale, nome, cognome, email, telefono, password, dataNascita);
        this.qualifica = qualifica;
        this.iban = iban;
        this.ruolo = ruolo;
    }
}
