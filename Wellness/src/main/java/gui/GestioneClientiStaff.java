package gui;

import controller.Controller;
import model.utenti.Cliente;
import model.enums.StatoAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class GestioneClientiStaff {

    private JPanel mainPanel;
    private JTable tabellaClienti;
    private JButton btnAggiungi;
    private JButton btnModifica;
    private JButton btnRimuovi;
    private JButton btnIndietro;

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private DefaultTableModel modelloTabella;

    public GestioneClientiStaff(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        String[] colonne = {"Codice Fiscale", "Nome", "Cognome", "Email", "Telefono"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabellaClienti.setModel(modelloTabella);

        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        btnRimuovi.addActionListener(e -> {
            int riga = tabellaClienti.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona un cliente dalla lista.");
                return;
            }
            String cf = (String) modelloTabella.getValueAt(riga, 0);
            int scelta = JOptionPane.showConfirmDialog(mainPanel, "Eliminare definitivamente il cliente " + cf + "?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (scelta == JOptionPane.YES_OPTION) {
                if (controller.rimuoviClienteTramiteStaff(cf)) {
                    JOptionPane.showMessageDialog(mainPanel, "Cliente rimosso dal database.");
                    aggiornaTabella();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Errore durante l'eliminazione.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAggiungi.addActionListener(e -> {
            String cf = JOptionPane.showInputDialog(mainPanel, "Codice Fiscale:");
            if (cf == null || cf.trim().isEmpty()) return;
            String nome = JOptionPane.showInputDialog(mainPanel, "Nome:");
            if (nome == null || nome.trim().isEmpty()) return;
            String cognome = JOptionPane.showInputDialog(mainPanel, "Cognome:");
            if (cognome == null || cognome.trim().isEmpty()) return;
            String email = JOptionPane.showInputDialog(mainPanel, "Email:");
            String telefono = JOptionPane.showInputDialog(mainPanel, "Telefono:");

            Cliente nuovo = new Cliente(cf, nome, cognome, email, telefono, "1234", null, 0, "", 0, "", null, StatoAccount.ATTIVO);
            if (controller.aggiungiClienteTramiteStaff(nuovo)) {
                JOptionPane.showMessageDialog(mainPanel, "Cliente registrato con successo.");
                aggiornaTabella();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Errore inserimento. Controlla se il CF esiste gia'.");
            }
        });

        btnModifica.addActionListener(e -> {
            int riga = tabellaClienti.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona un cliente da modificare.");
                return;
            }
            String cf = (String) modelloTabella.getValueAt(riga, 0);
            String vecchioNome = (String) modelloTabella.getValueAt(riga, 1);
            String vecchioCognome = (String) modelloTabella.getValueAt(riga, 2);

            String nuovoNome = JOptionPane.showInputDialog(mainPanel, "Modifica Nome:", vecchioNome);
            if (nuovoNome == null) return;
            String nuovoCognome = JOptionPane.showInputDialog(mainPanel, "Modifica Cognome:", vecchioCognome);
            if (nuovoCognome == null) return;
            String nuovaEmail = JOptionPane.showInputDialog(mainPanel, "Modifica Email:", modelloTabella.getValueAt(riga, 3));
            String nuovoTel = JOptionPane.showInputDialog(mainPanel, "Modifica Telefono:", modelloTabella.getValueAt(riga, 4));

            Cliente modificato = new Cliente(cf, nuovoNome, nuovoCognome, nuovaEmail, nuovoTel, "", null, 0, "", 0, "", null, StatoAccount.ATTIVO);
            if (controller.modificaClienteTramiteStaff(modificato)) {
                JOptionPane.showMessageDialog(mainPanel, "Dati aggiornati.");
                aggiornaTabella();
            }
        });

        frame = new JFrame("Wellness In Cloud - Gestione Clienti");
        frame.setContentPane(mainPanel);
        frame.setSize(750, 450);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aggiornaTabella();
        frame.setVisible(true);
    }

    private void aggiornaTabella() {
        modelloTabella.setRowCount(0);
        ArrayList<Cliente> lista = controller.getListaClientiDaStaff();
        for (Cliente c : lista) {
            modelloTabella.addRow(new Object[]{c.getCodiceFiscale(), c.getNome(), c.getCognome(), c.getEmail(), c.getTelefono()});
        }
    }
}
