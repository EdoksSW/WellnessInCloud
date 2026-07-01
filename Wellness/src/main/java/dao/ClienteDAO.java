package dao;

import model.commerce.Carrello;
import model.commerce.Iscrizione;
import model.logistica.Prenotazione;
import model.utenti.Cliente;

import java.time.LocalDate;
import java.util.List;

public interface ClienteDAO
{
    //Recupero i dati di un cliente tramite chiave primaria
    Cliente trovaPerCodiceFiscale(String codiceFiscale);

    //Recupero l'entità Carrello associata al cliente per visualizzare i prodotti salvati.
    Carrello ottieniCarrello(Cliente cliente);

    //Aggiorno il carrello nel DB
    boolean agiornaCarrello(String codiceFiscaleCliente, Carrello carrello);

    //RELAZIONE: CLIENTE - ABBONAMENTO / ISCRIZIONE
    //Associo un Cliente ad un Abbonamento
    boolean effettuaIscrizione(Iscrizione iscrizione);

    //Registra l'ingresso di un cliente a una determinata lezione/sala
    boolean prenotaLezione(Prenotazione prenotazione);

    //Rimuove una prenotazione eggettuata in precedenza
    boolean annullaPrenotazione(int idPrenotazione);

    //Elenca di tutte le prenotazioni del Cliente
    List<Prenotazione> ottieniPrenotazioniCliente(Cliente cliente);

    List<model.commerce.Ordine> ottieniStoricoOrdini(Cliente cliente);
}
