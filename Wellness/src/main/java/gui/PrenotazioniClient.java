package gui;

import controller.Controller;
import model.logistica.Prenotazione;
import model.utenti.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PrenotazioniClient {
    private JPanel prenotazioniPanel;
    private JPanel sezioniPanel;
    private JPanel comandiPanel;
    private JButton bntHome;
    private JButton bntPrenota;
    private JTable tabellaPrenotazioni;
    private JButton bntAnnullaPrenotazione;
    private JScrollPane scrollPrenotazioni;

    private Controller controller;
    private Cliente clienteLoggato;
    private JFrame frameChiamante;
    private JFrame frame;

    public PrenotazioniClient(Controller controller, Cliente cliente, JFrame frameChiamante)
    {
        this.controller=controller;
        this.clienteLoggato=cliente;
        this.frameChiamante=frameChiamante;

        String[] colonne={"Id_Prenotazione", "Lezione", "Data Lezione", "Ora", "Stato"};
        DefaultTableModel defaultTableModel=new DefaultTableModel(colonne, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        List<Prenotazione> listaPrenotazioni=controller.ottieniPrenotazioniAttiveCliente(clienteLoggato);

        if(listaPrenotazioni!=null)
        {
            for (Prenotazione p : listaPrenotazioni) {
                Object[] riga = new Object[5];
                riga[0] = p.getIdPrenotazione();
                riga[1] = (p.getLezione() != null) ? p.getLezione().getNome() : "N/A";
                riga[2] = p.getDataPren(); // Data della lezione
                riga[3] = p.getOraPren();  // Ora della lezione
                riga[4] = (p.getStato() != null) ? p.getStato().getLabel() : "CONFERMATA";

                defaultTableModel.addRow(riga);
            }
        }

        tabellaPrenotazioni.setModel(defaultTableModel);
        tabellaPrenotazioni.setRowHeight(25);


        bntHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });


        bntAnnullaPrenotazione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = tabellaPrenotazioni.getSelectedRow();

                //Verifico se la riga sia effettivamente stata selezionata
                if(rigaSelezionata == -1)
                {
                    JOptionPane.showMessageDialog(frame,"Seleziona una prenotazione della tabella per annullarla.","Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idPrenotazione = (int) tabellaPrenotazioni.getValueAt(rigaSelezionata,0);
                String statoAttuale=tabellaPrenotazioni.getValueAt(rigaSelezionata,4).toString();

                //Controllo se la lezione è già stata "Annullata"
                if(statoAttuale.equalsIgnoreCase("Annullata"))
                {
                    JOptionPane.showMessageDialog(frame,"Questa prenotazione risulta già annullata.", "Operazione Rifiutata", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int scelta=JOptionPane.showConfirmDialog(frame,"Sei sicuro di voler annullare la prenotazione "+idPrenotazione+"?", "Conferma Annullamento", JOptionPane.YES_NO_OPTION);

                if(scelta == JOptionPane.YES_NO_OPTION)
                {
                    if(controller.annullaPrenotazioneCliente(idPrenotazione))
                    {
                        JOptionPane.showMessageDialog(frame,"Prenotazione annullata con successo!");

                        //Rimozione riga
                        ((DefaultTableModel) tabellaPrenotazioni.getModel()).removeRow(rigaSelezionata);
                    }else
                    {
                        JOptionPane.showMessageDialog(frame,"Si è verificato un errore durante l'annullamento della prenotazione sul DataBase", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        bntPrenota.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new NuovaPrenotazioneClient(controller, clienteLoggato, frame);
            }
        });

        frame=new JFrame("Wellness - Le Mie Prenotazioni");
        frame.setContentPane(prenotazioniPanel);
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        bntHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });
    }
}
