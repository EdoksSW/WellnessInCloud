package gui;

import controller.Controller;
import model.enums.StatoPrenotazione;
import model.logistica.Lezione;
import model.utenti.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NuovaPrenotazioneClient {
    private JPanel nuovaPrenotazionePanel;
    private JTable tabellaLezioni;
    private JScrollPane scrollCorsi;
    private JButton bntAnnulla;
    private JButton bntAggiungi;

    private Controller controller;
    private Cliente clienteLoggato;
    private JFrame frameChiamante;
    JFrame frame;

    public NuovaPrenotazioneClient(Controller controller, Cliente cliente, JFrame frameChiamante)
    {
        this.controller=controller;
        this.clienteLoggato=cliente;
        this.frameChiamante=frameChiamante;

        String[] colonne={"ID Lezione", "Nome Corso / Lezione", "Giorno", "Ora Inizio", "Ora Fine", "Aula / Sala"};
        DefaultTableModel defaultTableModel=new DefaultTableModel(colonne,0)
        {
            @Override
            public  boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        List<Lezione> lezioniLista=controller.ottieniListaLezioni();

        if(lezioniLista != null)
        {
            for (Lezione lez : lezioniLista) {
                Object[] riga = new Object[6];
                riga[0] = lez.getId_lezione();
                riga[1] = lez.getNome();
                riga[2] = lez.getGiorno();
                riga[3] = lez.getOra_inizio();
                riga[4] = lez.getOraFine();
                riga[5] = "Sala " + lez.getId_sala();

                defaultTableModel.addRow(riga);
            }
        }

        tabellaLezioni.setModel(defaultTableModel);
        tabellaLezioni.setRowHeight(25);


        bntAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });


        bntAggiungi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata=tabellaLezioni.getSelectedRow();

                if(rigaSelezionata == -1)
                {
                    JOptionPane.showMessageDialog(frame,"Seleziona una riga prima di poter prenotare!","Attenzione",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idLezione=(int) tabellaLezioni.getValueAt(rigaSelezionata,0);
                String nomeLezione=tabellaLezioni.getValueAt(rigaSelezionata,1).toString();

                int scelta=JOptionPane.showConfirmDialog(frame,"Vuoi confermare la prenotazione per la lezione di: "+nomeLezione+"?","Conferma Prenotazione", JOptionPane.YES_NO_OPTION);

                if(scelta == JOptionPane.YES_OPTION)
                {
                    Lezione lezioneScleta=new Lezione(idLezione,nomeLezione);

                    boolean successo=controller.prenotaLezioneCliente(clienteLoggato,lezioneScleta, StatoPrenotazione.CONFERMATA);

                    if(successo)
                    {
                        JOptionPane.showMessageDialog(frame,"Prenotazione registrata con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);

                        if(frameChiamante != null)
                        {
                            frameChiamante.dispose();
                        }

                        new PrenotazioniClient(controller, clienteLoggato, null);
                        frame.dispose();
                    }else
                    {
                        JOptionPane.showMessageDialog(frame,"Errore durante l'inserimento della prenotazione. Verifica di non essere già iscritto a questa lezione.","Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame=new JFrame("Wellness - Lista Corsi e Lezioni");
        frame.setContentPane(nuovaPrenotazionePanel);
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
