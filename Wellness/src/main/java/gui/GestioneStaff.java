package gui;

import controller.Controller;
import model.enums.RuoloStaff;
import model.utenti.Staff;

import javax.swing.*;
import java.util.ArrayList;

public class GestioneStaff {

    private JPanel mainPanel;
    private JLabel lblTitolo;
    private JScrollPane scrollPane;
    private JPanel listaPanel;
    private JPanel panelBottoni;
    private JButton btnAggiungi;
    private JButton btnIndietro;

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private ValidazioneInput validazioneInput;

    public GestioneStaff(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        btnAggiungi.addActionListener(e -> aggiungiStaff());
        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        frame = new JFrame("Wellness In Cloud - Gestisci Staff");
        frame.setContentPane(mainPanel);
        frame.setSize(750, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aggiornaLista();
        frame.setVisible(true);
    }

    private void aggiornaLista() {
        listaPanel.removeAll();
        ArrayList<Staff> listaStaff = controller.getListaStaff();
        if (listaStaff.isEmpty()) {
            listaPanel.add(new JLabel("Nessuno staff presente."));
        } else {
            for (Staff s : listaStaff) {
                JPanel riga = new JPanel(new java.awt.BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String info = s.getNome() + " " + s.getCognome()
                        + "   |   " + s.getEmail()
                        + "   |   Ruolo: " + s.getRuolo()
                        + "   |   Qualifica: " + s.getQualifica();
                riga.add(new JLabel(info), java.awt.BorderLayout.CENTER);

                JPanel azioni = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
                JButton btnModifica = new JButton("Modifica");
                btnModifica.addActionListener(e -> modificaStaff(s));
                JButton btnElimina = new JButton("Elimina");
                btnElimina.addActionListener(e -> eliminaStaff(s));
                JButton btnTurni = new JButton("Gestisci Turni");
                btnTurni.addActionListener(e -> {
                    new GestioneTurni(controller, frame, s);
                    frame.setVisible(false);
                });
                azioni.add(btnModifica);
                azioni.add(btnElimina);
                azioni.add(btnTurni);
                riga.add(azioni, java.awt.BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void aggiungiStaff() {
        /*String cf = chiediTesto("Codice Fiscale", null);
        if (cf == null) return;
        String nome = chiediTesto("Nome", null);
        if (nome == null) return;
        String cognome = chiediTesto("Cognome", null);
        if (cognome == null) return;
        String email = chiediTesto("Email", null);
        if (email == null) return;
        String telefono = JOptionPane.showInputDialog(frame, "Telefono", "");
        if (telefono == null) return;
        String qualifica = JOptionPane.showInputDialog(frame, "Qualifica", "");
        if (qualifica == null) return;
        String iban = JOptionPane.showInputDialog(frame, "IBAN", "");
        if (iban == null) return;
        RuoloStaff ruolo = chiediRuolo(null);
        if (ruolo == null) return;

        Staff nuovo = new Staff(cf, nome, cognome, email, telefono, "staff123", null, 0, null, 0, null, null, qualifica, iban, ruolo);
        boolean esito = controller.aggiungiStaff(nuovo);
        JOptionPane.showMessageDialog(frame, esito ? "Staff aggiunto." : "Aggiunta non riuscita. Controlla che CF/email non esistano gia'.");
        if (esito) aggiornaLista();*/
        String cf = chiediTesto("Codice Fiscale", null);
        if (cf == null) return;
        if (!validazioneInput.isCodiceFiscaleValido(cf)) {
            JOptionPane.showMessageDialog(frame, "Codice Fiscale non valido! Deve essere di 16 caratteri alfanumerici.", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cf = cf.toUpperCase();

        String nome = chiediTesto("Nome", null);
        if (nome == null) return;
        if (!validazioneInput.isNomeCognomeValido(nome)) {
            JOptionPane.showMessageDialog(frame, "Nome non valido! Sono ammesse solo lettere (senza spazi, min 2 caratteri).", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cognome = chiediTesto("Cognome", null);
        if (cognome == null) return;
        if (!validazioneInput.isNomeCognomeValido(cognome)) {
            JOptionPane.showMessageDialog(frame, "Cognome non valido! Sono ammesse solo lettere (senza spazi, min 2 caratteri).", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String email = chiediTesto("Email", null);
        if (email == null) return;
        if (!validazioneInput.isEmailValida(email)) {
            JOptionPane.showMessageDialog(frame, "Indirizzo Email non valido! Controllare la presenza di '@' e del dominio.", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String telefono = JOptionPane.showInputDialog(frame, "Telefono", "");
        if (telefono == null) return;
        if (telefono.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo Telefono non puo' essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validazioneInput.isTelefonoValido(telefono)) {
            JOptionPane.showMessageDialog(frame, "Numero di telefono non valido! Inserire solo cifre numeriche (9-11 cifre).", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String qualifica = JOptionPane.showInputDialog(frame, "Qualifica", "");
        if (qualifica == null) return;
        if (qualifica.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo Qualifica non puo' essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String iban = JOptionPane.showInputDialog(frame, "IBAN", "");
        if (iban == null) return;
        if (iban.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo IBAN non puo' essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RuoloStaff ruolo = chiediRuolo(null);
        if (ruolo == null) return;

        Staff nuovo = new Staff(cf, nome, cognome, email, telefono.trim(), "staff123", null, 0, null, 0, null, null, qualifica.trim(), iban.trim(), ruolo);
        boolean esito = controller.aggiungiStaff(nuovo);
        JOptionPane.showMessageDialog(frame, esito ? "Staff aggiunto." : "Aggiunta non riuscita. Controlla che CF/email non esistano gia'.");
        if (esito) aggiornaLista();
    }

    private void modificaStaff(Staff s) {
        /*String nome = chiediTesto("Nome", s.getNome());
        if (nome == null) return;
        String cognome = chiediTesto("Cognome", s.getCognome());
        if (cognome == null) return;
        String email = chiediTesto("Email", s.getEmail());
        if (email == null) return;
        String telefono = JOptionPane.showInputDialog(frame, "Telefono", s.getTelefono() == null ? "" : s.getTelefono());
        if (telefono == null) return;
        String qualifica = JOptionPane.showInputDialog(frame, "Qualifica", s.getQualifica() == null ? "" : s.getQualifica());
        if (qualifica == null) return;
        String iban = JOptionPane.showInputDialog(frame, "IBAN", s.getIban() == null ? "" : s.getIban());
        if (iban == null) return;
        RuoloStaff ruolo = chiediRuolo(s.getRuolo());
        if (ruolo == null) return;

        Staff modificato = new Staff(s.getCodiceFiscale(), nome, cognome, email, telefono, s.getPassword(), s.getDataNascita(),
                s.getEta(), s.getVia(), s.getCivico(), s.getCap(), s.getCartaFedelta(), qualifica, iban, ruolo);
        boolean esito = controller.modificaStaff(modificato);
        JOptionPane.showMessageDialog(frame, esito ? "Staff modificato." : "Modifica non riuscita.");
        if (esito) aggiornaLista();*/
        String nome = chiediTesto("Nome", s.getNome());
        if (nome == null) return;
        if (!validazioneInput.isNomeCognomeValido(nome)) {
            JOptionPane.showMessageDialog(frame, "Nome non valido! Sono ammesse solo lettere (senza spazi, min 2 caratteri).", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cognome = chiediTesto("Cognome", s.getCognome());
        if (cognome == null) return;
        if (!validazioneInput.isNomeCognomeValido(cognome)) {
            JOptionPane.showMessageDialog(frame, "Cognome non valido! Sono ammesse solo lettere (senza spazi, min 2 caratteri).", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String email = chiediTesto("Email", s.getEmail());
        if (email == null) return;
        if (!validazioneInput.isEmailValida(email)) {
            JOptionPane.showMessageDialog(frame, "Indirizzo Email non valido! Controllare il formato.", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String telefono = JOptionPane.showInputDialog(frame, "Telefono", s.getTelefono() == null ? "" : s.getTelefono());
        if (telefono == null) return;
        if (telefono.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo Telefono non puo' essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validazioneInput.isTelefonoValido(telefono)) {
            JOptionPane.showMessageDialog(frame, "Numero di telefono non valido! Inserire solo cifre numeriche (9-11 cifre).", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String qualifica = JOptionPane.showInputDialog(frame, "Qualifica", s.getQualifica() == null ? "" : s.getQualifica());
        if (qualifica == null) return;
        if (qualifica.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo Qualifica non puo' essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String iban = JOptionPane.showInputDialog(frame, "IBAN", s.getIban() == null ? "" : s.getIban());
        if (iban == null) return;
        if (iban.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo IBAN non puo' essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RuoloStaff ruolo = chiediRuolo(s.getRuolo());
        if (ruolo == null) return;

        Staff modificato = new Staff(s.getCodiceFiscale(), nome, cognome, email, telefono.trim(), s.getPassword(), s.getDataNascita(),
                s.getEta(), s.getVia(), s.getCivico(), s.getCap(), s.getCartaFedelta(), qualifica.trim(), iban.trim(), ruolo);
        boolean esito = controller.modificaStaff(modificato);
        JOptionPane.showMessageDialog(frame, esito ? "Staff modified." : "Modifica non riuscita.");
        if (esito) aggiornaLista();
    }

    private void eliminaStaff(Staff s) {
        int risposta = JOptionPane.showConfirmDialog(frame, "Eliminare lo staff " + s.getNome() + " " + s.getCognome() + "?",
                "Conferma", JOptionPane.YES_NO_OPTION);
        if (risposta != JOptionPane.YES_OPTION) return;
        boolean esito = controller.rimuoviStaff(s.getCodiceFiscale());
        JOptionPane.showMessageDialog(frame, esito ? "Staff eliminato."
                : "Eliminazione non riuscita: lo staff potrebbe essere istruttore di un corso.");
        if (esito) aggiornaLista();
    }

    private RuoloStaff chiediRuolo(RuoloStaff predefinito) {
        RuoloStaff[] valori = RuoloStaff.values();
        Object scelta = JOptionPane.showInputDialog(frame, "Ruolo", "Selezione ruolo",
                JOptionPane.QUESTION_MESSAGE, null, valori, predefinito != null ? predefinito : valori[0]);
        return (RuoloStaff) scelta;
    }

    private String chiediTesto(String etichetta, String predefinito) {
        String s = JOptionPane.showInputDialog(frame, etichetta, predefinito == null ? "" : predefinito);
        if (s == null) return null;
        if (s.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo non puo' essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return s.trim();
    }
}
