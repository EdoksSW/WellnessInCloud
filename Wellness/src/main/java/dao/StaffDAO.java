package dao;

import model.logistica.Prenotazione;
import model.utenti.Cliente;
import model.utenti.Staff;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface StaffDAO {
    ArrayList<Staff> getAllStaff();

    ArrayList<Staff> getIstruttori();

    boolean aggiungiStaff(Staff staff);

    boolean modificaStaff(Staff staff);

    boolean rimuoviStaff(String codiceFiscale);

    ArrayList<Cliente> getAllClientiDaStaff();

    boolean aggiornaCertificatoDaStaff(String codiceFiscale, LocalDate nuovaScadenza, String nuovoPath);

    boolean aggiungiClienteDaStaff(Cliente cliente);

    boolean modificaClienteDaStaff(Cliente cliente);

    boolean rimuoviClienteDaStaff(String codiceFiscale);

    String[] getDettagliAbbonamento(String codiceFiscale);

    String getPathCertificatoDaStaff(String codiceFiscale);

    List<Prenotazione> ottieniTuttePrenotazioni();

    boolean inserisciCertificatoDaStaff(String codiceFiscale, LocalDate nuovaScadenza, String nuovoPath);

    ArrayList<String[]> getListaCertificatiCompleta();

    boolean modificaCertificatoSpecifico(String codiceFiscale, LocalDate nuovaScadenza, String nuovoPath, String vecchioPath);

    boolean modificaStatoPrenotazione(int idPrenotazione, String nuovoStato);

}
