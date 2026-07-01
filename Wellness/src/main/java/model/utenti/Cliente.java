package model.utenti;
import model.enums.StatoAccount;
import java.time.LocalDate;

public class Cliente extends Utente {
    private StatoAccount statoAcc;

    public Cliente(String codiceFiscale, String nome, String cognome, String email, String telefono, String password,
                   LocalDate dataNascita, int eta, String via, int civico, String cap, String cartaFedelta,
                   StatoAccount statoAcc) {
        super(codiceFiscale, nome, cognome, email, telefono, password, dataNascita, eta, via, civico, cap, cartaFedelta);
        this.statoAcc = statoAcc;
    }

    public StatoAccount getStatoAcc() {
        return statoAcc;
    }

    public void setStatoAcc(StatoAccount statoAcc) {
        this.statoAcc = statoAcc;
    }
}
