package dao;

import model.utenti.Staff;

import java.util.ArrayList;

public interface StaffDAO {
    ArrayList<Staff> getAllStaff();

    ArrayList<Staff> getIstruttori();

    ArrayList<model.utenti.Cliente> getAllClientiDaStaff();
    boolean aggiungiClienteDaStaff(model.utenti.Cliente cliente);
    boolean modificaClienteDaStaff(model.utenti.Cliente cliente);
    boolean rimuoviClienteDaStaff(String codiceFiscale);

    java.time.LocalDate getCertificatoDaStaff(String codiceFiscale);
    boolean aggiornaCertificatoDaStaff(String codiceFiscale, java.time.LocalDate nuovaScadenza);

}
