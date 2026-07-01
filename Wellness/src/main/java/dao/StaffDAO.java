package dao;

import model.utenti.Cliente;
import model.utenti.Staff;

import java.time.LocalDate;
import java.util.ArrayList;

public interface StaffDAO {
    ArrayList<Staff> getAllStaff();

    ArrayList<Staff> getIstruttori();

    ArrayList<Cliente> getAllClientiDaStaff();

    boolean aggiungiClienteDaStaff(Cliente cliente);

    boolean modificaClienteDaStaff(Cliente cliente);

    boolean rimuoviClienteDaStaff(String codiceFiscale);

    LocalDate getCertificatoDaStaff(String codiceFiscale);

    boolean aggiornaCertificatoDaStaff(String codiceFiscale, LocalDate nuovaScadenza);
}
