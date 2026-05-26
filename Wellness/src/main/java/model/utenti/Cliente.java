package model.utenti;
import model.enums.StatoAccount;
import java.time.LocalDate;

public class Cliente extends Utente {
    private String indirizzo, cap;
    private int numCivico;
    private StatoAccount statoAcc;

    public Cliente(String codiceFiscale, String nome, String cognome, String email, String telefono, String password, LocalDate dataNascita, String indirizzo, int numCivico, String cap, StatoAccount statoAcc) {
        super(codiceFiscale, nome, cognome, email, telefono, password, dataNascita);
        this.indirizzo = indirizzo;
        this.numCivico = numCivico;
        this.cap = cap;
        this.statoAcc = statoAcc;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public StatoAccount getStatoAcc() {
        return statoAcc;
    }

    public String getCap() {
        return cap;
    }

    public int getNumeroCivico() {
        return numCivico;
    }
}
