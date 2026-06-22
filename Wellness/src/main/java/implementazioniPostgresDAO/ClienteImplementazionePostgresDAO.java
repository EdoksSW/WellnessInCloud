package implementazioniPostgresDAO;

import dao.ClienteDAO;
import model.commerce.Carrello;
import model.logistica.Prenotazione;
import model.utenti.Cliente;

import java.time.LocalDate;
import java.util.List;

public class ClienteImplementazionePostgresDAO implements ClienteDAO
{

    @Override
    public Cliente trovaPerCodiceFiscale(String codiceFiscale) {
        return null;
    }

    @Override
    public Carrello ottieniCarrello(String codiceFiscaleCliente) {
        return null;
    }

    @Override
    public boolean agiornaCarrello(String codiceFiscaleCliente, Carrello carrello) {
        return false;
    }

    @Override
    public boolean acquistaAbbonamento(String codiceFiscaleCliente, int idAbbonamento, LocalDate dataInizio, double prezzoPagato) {
        return false;
    }

    @Override
    public boolean prenotaLezione(String codiceFiscaleCliente, int idLezione, LocalDate dataPrenotazione) {
        return false;
    }

    @Override
    public boolean annullaPrenotazione(String codiceFiscaleCliente, int idPrenotazione) {
        return false;
    }

    @Override
    public List<Prenotazione> ottieniPrenotazioneCliente(String codiceFiscaleCliente) {
        return List.of();
    }
}
