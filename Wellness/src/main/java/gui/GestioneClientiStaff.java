package gui;

import controller.Controller;
import model.utenti.Cliente;
import model.enums.StatoAccount;
import model.commerce.TitoloIngresso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GestioneClientiStaff {

    private JPanel mainPanel;
    private JTable tabellaClienti;
    private JButton btnAggiungi;
    private JButton btnModifica;
    private JButton btnRimuovi;
    private JButton btnIndietro;
    private JPanel panelBottoni;
    private JScrollPane scrollPane;

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private DefaultTableModel modelloTabella;
    private ValidazioneInput validationInput;

    public GestioneClientiStaff(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        String[] colonne = {"Codice Fiscale", "Nome", "Cognome", "Email", "Telefono", "Abbonamento", "Mesi"};
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
            if (cf == null) return;
            if (!validationInput.isCodiceFiscaleValido(cf)) {
                JOptionPane.showMessageDialog(mainPanel, "Codice Fiscale non valido! Deve essere di 16 caratteri alfanumerici standard.", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cf = cf.trim().toUpperCase();

            String nome = JOptionPane.showInputDialog(mainPanel, "Nome:");
            if (nome == null) return;
            if (!validationInput.isNomeCognomeValido(nome)) {
                JOptionPane.showMessageDialog(mainPanel, "Nome non valido! Sono ammesse solo lettere (senza spazi, min 2 caratteri).", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String cognome = JOptionPane.showInputDialog(mainPanel, "Cognome:");
            if (cognome == null) return;
            if (!validationInput.isNomeCognomeValido(cognome)) {
                JOptionPane.showMessageDialog(mainPanel, "Cognome non valido! Sono ammesse solo lettere (senza spazi, min 2 caratteri).", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String email = JOptionPane.showInputDialog(mainPanel, "Email:");
            if (email == null) return;
            if (!validationInput.isEmailValida(email)) {
                JOptionPane.showMessageDialog(mainPanel, "Indirizzo Email non valido! Deve contenere la '@' e un dominio reale (es. nome@mail.com).", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String telefono = JOptionPane.showInputDialog(mainPanel, "Telefono:");
            if (telefono == null) return;
            if (!validationInput.isTelefonoValido(telefono)) {
                JOptionPane.showMessageDialog(mainPanel, "Numero di telefono non valido! Inserire solo cifre numeriche (tra 9 e 11 cifre).", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cliente nuovoCliente = new Cliente(cf, nome, cognome, email, telefono, "1234", null, 0, "", 0, "", "", StatoAccount.ATTIVO);

            if (controller.aggiungiClienteTramiteStaff(nuovoCliente)) {
                JOptionPane.showMessageDialog(mainPanel, "Cliente registrato! Procediamo con l'abbonamento.");
                gestisciAbbonamento(nuovoCliente);
                aggiornaTabella();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Errore inserimento. Controlla se il CF esiste già.");
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
            String vecchiaEmail = (String) modelloTabella.getValueAt(riga, 3);
            String vecchioTel = (String) modelloTabella.getValueAt(riga, 4);

            String nuovoNome = JOptionPane.showInputDialog(mainPanel, "Modifica Nome:", vecchioNome);
            if (nuovoNome == null) return;
            if (!validationInput.isNomeCognomeValido(nuovoNome)) {
                JOptionPane.showMessageDialog(mainPanel, "Nome modificato non valido! Solo lettere consentite.", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nuovoCognome = JOptionPane.showInputDialog(mainPanel, "Modifica Cognome:", vecchioCognome);
            if (nuovoCognome == null) return;
            if (!validationInput.isNomeCognomeValido(nuovoCognome)) {
                JOptionPane.showMessageDialog(mainPanel, "Cognome modificato non valido! Solo lettere consentite.", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nuovaEmail = JOptionPane.showInputDialog(mainPanel, "Modifica Email:", vecchiaEmail);
            if (nuovaEmail == null) return;
            if (!validationInput.isEmailValida(nuovaEmail)) {
                JOptionPane.showMessageDialog(mainPanel, "Email modificata non valida! Controllare il formato.", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nuovoTel = JOptionPane.showInputDialog(mainPanel, "Modifica Telefono:", vecchioTel);
            if (nuovoTel == null) return;
            if (!validationInput.isTelefonoValido(nuovoTel)) {
                JOptionPane.showMessageDialog(mainPanel, "Telefono modificato non valido! Solo cifre numeriche ammesse.", "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cliente modificato = new Cliente(cf, nuovoNome, nuovoCognome, nuovaEmail, nuovoTel, "", null, 0, "", 0, "", "", StatoAccount.ATTIVO);

            if (controller.modificaClienteTramiteStaff(modificato)) {
                int risposta = JOptionPane.showConfirmDialog(mainPanel, "Dati aggiornati. Vuoi assegnare o rinnovare l'abbonamento?", "Rinnovo", JOptionPane.YES_NO_OPTION);
                if (risposta == JOptionPane.YES_OPTION) {
                    gestisciAbbonamento(modificato);
                }
                aggiornaTabella();
            }
        });

        frame = new JFrame("Wellness In Cloud - Gestione Clienti");

        // Se preferisci togliere la forzatura del layout, elimina le 3 righe seguenti
        mainPanel.setLayout(new BorderLayout());
        if(scrollPane != null) mainPanel.add(scrollPane, BorderLayout.CENTER);
        if(panelBottoni != null) mainPanel.add(panelBottoni, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aggiornaTabella();
        frame.setVisible(true);
    }

    private void aggiornaTabella() {
        modelloTabella.setRowCount(0);
        ArrayList<Cliente> lista = controller.getListaClientiDaStaff();
        for (Cliente c : lista) {
            String[] dettagli = controller.getDettagliAbbonamentoCliente(c.getCodiceFiscale());
            modelloTabella.addRow(new Object[]{
                    c.getCodiceFiscale(), c.getNome(), c.getCognome(), c.getEmail(), c.getTelefono(), dettagli[0], dettagli[1]
            });
        }
    }

    private void gestisciAbbonamento(Cliente cliente) {
        ArrayList<TitoloIngresso> titoli = controller.getListaTitoli();

        if (titoli == null || titoli.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Nessun abbonamento disponibile nel database. Aggiungili prima da 'Gestione Titoli'.");
            return;
        }

        String[] opzioni = new String[titoli.size()];
        for (int i = 0; i < titoli.size(); i++) {
            opzioni[i] = titoli.get(i).getTipo() + " - €" + titoli.get(i).getPrezzo();
        }

        String scelta = (String) JOptionPane.showInputDialog(
                mainPanel,
                "Seleziona l'abbonamento per " + cliente.getNome() + ":",
                "Assegna Abbonamento",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opzioni,
                opzioni[0]
        );

        if (scelta != null) {
            TitoloIngresso titoloSelezionato = null;
            for (TitoloIngresso t : titoli) {
                if (scelta.startsWith(t.getTipo())) {
                    titoloSelezionato = t;
                    break;
                }
            }

            if (titoloSelezionato != null) {
                int mesi = 1;
                String tipo = titoloSelezionato.getTipo().toLowerCase();

                if (tipo.contains("annuale") || tipo.contains("anno")) {
                    mesi = 12;
                } else if (tipo.contains("semestrale")) {
                    mesi = 6;
                } else if (tipo.contains("trimestrale")) {
                    mesi = 3;
                } else if (tipo.contains("giornaliero") || tipo.contains("giorno")) {
                    mesi = 0;
                }

                if (controller.effettuaIscrizioneCliente(cliente, titoloSelezionato, mesi)) {
                    JOptionPane.showMessageDialog(mainPanel, "Abbonamento registrato con successo (" + mesi + " mesi).");
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Errore durante la registrazione dell'abbonamento.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}