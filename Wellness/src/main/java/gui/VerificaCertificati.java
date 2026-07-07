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
    private JButton btnModificaScadenza;
    private JButton btnIndietro;
    private JButton btnAggiungi;

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private DefaultTableModel modelloTabella;

    public VerificaCertificati(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        String[] colonne = {"Codice Fiscale", "Nome", "Cognome", "Stato / Scadenza", "Percorso File PDF"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabellaCertificati.setModel(modelloTabella);

        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        // Logica di aggiunta slegata dalla selezione della tabella
        btnAggiungi.addActionListener(e -> {
            ArrayList<Cliente> listaClienti = controller.getListaClientiDaStaff();

            if (listaClienti == null || listaClienti.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Non ci sono clienti registrati nel sistema.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JComboBox<String> comboClienti = new JComboBox<>();
            // MODIFICA QUI: Formattazione anti-omonimia con Cognome, Nome, Email e CF
            for (Cliente c : listaClienti) {
                String infoCliente = c.getCognome().toUpperCase() + " " + c.getNome() +
                        " (" + c.getEmail() + ") - CF: " + c.getCodiceFiscale();
                comboClienti.addItem(infoCliente);
            }

            int sceltaCliente = JOptionPane.showConfirmDialog(
                    mainPanel,
                    comboClienti,
                    "Seleziona il Cliente per il nuovo certificato",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (sceltaCliente == JOptionPane.OK_OPTION) {
                int index = comboClienti.getSelectedIndex();
                Cliente clienteScelto = listaClienti.get(index);

                String cf = clienteScelto.getCodiceFiscale();
                String nome = clienteScelto.getNome();
                String cognome = clienteScelto.getCognome();

                String inputData = JOptionPane.showInputDialog(mainPanel, "Inserisci la data di scadenza del certificato (AAAA-MM-GG):");
                if (inputData != null && !inputData.trim().isEmpty()) {
                    try {
                        LocalDate nuovaScadenza = LocalDate.parse(inputData.trim());
                        String nuovoPath = ValidazioneInput.generaPathCertificato(nome, cognome, nuovaScadenza);

                        if (controller.inserisciCertificatoStaff(cf, nuovaScadenza, nuovoPath)) {
                            JOptionPane.showMessageDialog(mainPanel, "Nuovo certificato aggiunto con successo per " + nome + " " + cognome + ".");
                            aggiornaTabella();
                        } else {
                            JOptionPane.showMessageDialog(mainPanel, "Errore durante l'inserimento del certificato.", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Formato data non valido! Usa AAAA-MM-GG.", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // La modifica rimane agganciata alla riga per correggere un errore specifico
        btnModificaScadenza.addActionListener(e -> {
            int riga = tabellaCertificati.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona il certificato specifico da modificare.");
                return;
            }

            String stato = (String) modelloTabella.getValueAt(riga, 3);
            if (stato.equals("Nessun certificato registrato")) {
                JOptionPane.showMessageDialog(mainPanel, "Nessun certificato presente su questa riga. Usa 'Aggiungi'.", "Operazione non consentita", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String cf = (String) modelloTabella.getValueAt(riga, 0);
            String nome = (String) modelloTabella.getValueAt(riga, 1);
            String cognome = (String) modelloTabella.getValueAt(riga, 2);
            String vecchioPath = (String) modelloTabella.getValueAt(riga, 4);

            String inputData = JOptionPane.showInputDialog(mainPanel, "Modifica la data di scadenza di QUESTO certificato (AAAA-MM-GG):");
            if (inputData != null && !inputData.trim().isEmpty()) {
                try {
                    LocalDate nuovaScadenza = LocalDate.parse(inputData.trim());
                    String nuovoPath = ValidazioneInput.generaPathCertificato(nome, cognome, nuovaScadenza);

                    if (controller.aggiornaScadenzaCertificatoStaff(cf, nuovaScadenza, nuovoPath, vecchioPath)) {
                        JOptionPane.showMessageDialog(mainPanel, "Certificato specifico modificato con successo.");
                        aggiornaTabella();
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "Errore durante la modifica.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Formato data non valido! Usa AAAA-MM-GG.", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame = new JFrame("Wellness In Cloud - Controllo Certificati");
        frame.setContentPane(mainPanel);
        frame.setSize(750, 450);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aggiornaTabella();
        frame.setVisible(true);
    }

    private void aggiornaTabella() {
        modelloTabella.setRowCount(0);
        ArrayList<String[]> dati = controller.ottieniCertificatiDaStaff();
        for (String[] riga : dati) {
            modelloTabella.addRow(riga);
        }
    }
}