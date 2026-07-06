package dao;

import model.commerce.*;
import model.enums.StatoPrenotazione;
import model.logistica.Lezione;
import model.logistica.Prenotazione;
import model.utenti.Cliente;

import java.util.List;

public interface ClienteDAO
{
    //Recupero i dati di un cliente tramite chiave primaria
    Cliente trovaPerCodiceFiscale(String codiceFiscale);

    //Recupero l'entità Carrello associata al cliente per visualizzare i prodotti salvati.
    Carrello ottieniCarrello(Cliente cliente);

    //RELAZIONE: CLIENTE - ABBONAMENTO / ISCRIZIONE
    //Associo un Cliente ad un Abbonamento
    boolean effettuaIscrizione(Iscrizione iscrizione);

    //Registra l'ingresso di un cliente a una determinata lezione/sala
    boolean prenotaLezione(Prenotazione prenotazione);

    //Rimuove una prenotazione eggettuata in precedenza
    boolean annullaPrenotazione(int idPrenotazione);

    //Elenca di tutte le prenotazioni del Cliente
    List<Prenotazione> ottieniPrenotazioniCliente(Cliente cliente);

    List<Ordine> ottieniStoricoOrdini(Cliente cliente);

    List<OrdineDettaglio> ottieniDettagliOrdine(int idOrdine);

    boolean completaAcquisto(Cliente cliente);

    List<Prodotto> ottieniCatalogoProdotti();

    public boolean aggiungiSingoloProdotto(String cfCliente, int idProdotto, int quantita);

    public boolean annullaOrdineInElaborazione(int idOrdine);
}
