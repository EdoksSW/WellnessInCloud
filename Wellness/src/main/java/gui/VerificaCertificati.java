package gui;

import controller.Controller;
import model.utenti.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class VerificaCertificati {

    private JPanel mainPanel;
    private JTable tabellaCertificati;
    private JButton btnAggiornaVista;
    private JButton btnModificaScadenza;
    private JButton btnIndietro;

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private DefaultTableModel modelloTabella;

    public VerificaCertificati(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        String[] colonne = {"Codice Fiscale", "Nome", "Cognome", "Stato Certificato"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabellaCertificati.setModel(modelloTabella);

        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        btnAggiornaVista.addActionListener(e -> aggiornaTabella());

        // Modifica / Rinnovo della data di scadenza del certificato medico
        btnModificaScadenza.addActionListener(e -> {
            int riga = tabellaCertificati.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona un cliente per aggiornare il certificato.");
                return;
            }
            String cf = (String) modelloTabella.getValueAt(riga, 0);
            String inputData = JOptionPane.showInputDialog(mainPanel, "Inserisci la nuova data di scadenza (AAAA-MM-GG):");

            if (inputData != null && !inputData.trim().isEmpty()) {
                try {
                    LocalDate nuovaScadenza = LocalDate.parse(inputData.trim());
                    if (controller.aggiornaScadenzaCertificatoStaff(cf, nuovaScadenza)) {
                        JOptionPane.showMessageDialog(mainPanel, "Certificato medico aggiornato con successo.");
                        aggiornaTabella();
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "Impossibile aggiornare la data.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Formato data non valido! Utilizza lo standard AAAA-MM-GG.", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame = new JFrame("Wellness In Cloud - Controllo Certificati");
        frame.setContentPane(mainPanel);
        frame.setSize(650, 450);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aggiornaTabella();
        frame.setVisible(true);
    }

    private void aggiornaTabella() {
        modelloTabella.setRowCount(0);
        ArrayList<Cliente> lista = controller.getListaClientiDaStaff();
        for (Cliente c : lista) {
            String statoCertificato = controller.ottieniStatoCertificatoStaff(c.getCodiceFiscale());
            modelloTabella.addRow(new Object[]{c.getCodiceFiscale(), c.getNome(), c.getCognome(), statoCertificato});
        }
    }
}