package dao;

import model.utenti.Utente;

public interface UtenteDAO {

    Utente loginDB(String email, String password);
}
