package implementazioniPostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;
import model.commerce.Carrello;
import model.logistica.Prenotazione;
import model.utenti.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteImplementazionePostgresDAO implements ClienteDAO
{
    private Connection connection;

    ClienteImplementazionePostgresDAO()
    {
        try
        {
            this.connection=ConnessioneDatabase.getInstance().connection;
            System.out.println("Inizializzazione connessione ad DB evvenuta con successo!!");
        }catch (SQLException e)
        {
            System.out.println("Errore inizializzazione connessione al DataBase!!");
            e.printStackTrace();
        }
    }

    @Override
    public Cliente trovaPerCodiceFiscale(String codiceFiscale)
    {
        String query="SELECT * FROM cliente WHERE codicefiscale = ? ;";

        try(PreparedStatement preparedStatement=this.connection.prepareStatement(query))
        {
            preparedStatement.setString(1,codiceFiscale);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {

            }
        }catch(SQLException e)
        {

        }
    }

    @Override
    public Carrello ottieniCarrello(String codiceFiscaleCliente)
    {
        return null;
    }

    @Override
    public boolean agiornaCarrello(String codiceFiscaleCliente, Carrello carrello)
    {
        return false;
    }

    @Override
    public boolean acquistaAbbonamento(String codiceFiscaleCliente, int idAbbonamento, LocalDate dataInizio, double prezzoPagato)
    {
        return false;
    }

    @Override
    public boolean prenotaLezione(String codiceFiscaleCliente, int idLezione, LocalDate dataPrenotazione)
    {
        return false;
    }

    @Override
    public boolean annullaPrenotazione(String codiceFiscaleCliente, int idPrenotazione)
    {
        return false;
    }

    @Override
    public List<Prenotazione> ottieniPrenotazioneCliente(String codiceFiscaleCliente)
    {
        return List.of();
    }
}
