package model.utenti;

import java.time.LocalDate;

public class Admin extends Utente
{
    public Admin(String codiceFiscale, String nome, String cognome, String email, String telefono, String password,
                 LocalDate dataNascita, int eta, String via, int civico, String cap, String cartaFedelta) {
        super(codiceFiscale, nome, cognome, email, telefono, password, dataNascita, eta, via, civico, cap, cartaFedelta);
    }
}
