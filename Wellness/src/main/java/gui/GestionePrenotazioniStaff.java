package gui;

import controller.Controller;
import model.logistica.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionePrenotazioniStaff {
    private JPanel mainPanel;
    private JTable tablePrenotazioni;
    private JButton btnCancella;
    private JButton btnIndietro;
    private JButton btnAggiorna; // Il nuovo bottone
    private DefaultTableModel modelloTabella;
    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;

    public GestionePrenotazioniStaff(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        String[] colonne = {"ID", "Cliente (CF)", "Data", "Ora", "Corso/Lezione", "Stato"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablePrenotazioni.setModel(modelloTabella);

        caricaTabella();

        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        btnAggiorna.addActionListener(e -> caricaTabella());

        btnCancella.addActionListener(e -> {
            int rigaSelezionata = tablePrenotazioni.getSelectedRow();
            if (rigaSelezionata == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona prima una prenotazione dalla tabella per cancellarla!", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idPrenotazione = (int) modelloTabella.getValueAt(rigaSelezionata, 0);
            int conferma = JOptionPane.showConfirmDialog(mainPanel, "Sei sicura di voler annullare questa prenotazione?", "Conferma Annullamento", JOptionPane.YES_NO_OPTION);

            if (conferma == JOptionPane.YES_OPTION) {
                boolean successo = controller.annullaPrenotazioneCliente(idPrenotazione);
                if (successo) {
                    JOptionPane.showMessageDialog(mainPanel, "Prenotazione cancellata con successo!");
                    caricaTabella();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Errore durante la cancellazione nel database.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame = new JFrame("Gestione Globale Prenotazioni");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(tablePrenotazioni), BorderLayout.CENTER);

        JPanel panelBottoni = new JPanel();
        panelBottoni.add(btnAggiorna); // Aggiunto al pannello
        panelBottoni.add(btnCancella);
        panelBottoni.add(btnIndietro);
        mainPanel.add(panelBottoni, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void caricaTabella() {
        modelloTabella.setRowCount(0);
        List<Prenotazione> prenotazioni = controller.ottieniTuttePrenotazioniStaff();
        for (Prenotazione p : prenotazioni) {
            modelloTabella.addRow(new Object[]{
                    p.getIdPrenotazione(), p.getCliente().getCodiceFiscale(), p.getDataPren(), p.getOraPren(), p.getLezione().getNome(), p.getStato().getLabel()
            });
        }
    }
}