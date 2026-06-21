package gui;

import controller.Controller;
import model.logistica.Turno;
import model.utenti.Staff;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GestioneTurni {

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private Staff staff;
    private JPanel listaPanel;

    public GestioneTurni(Controller controller, JFrame frameChiamante, Staff staff) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;
        this.staff = staff;

        frame = new JFrame("Turni - " + staff.getNome() + " " + staff.getCognome());
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titolo = new JLabel("Turni di " + staff.getNome() + " " + staff.getCognome(), SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titolo, BorderLayout.NORTH);

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JScrollPane(listaPanel), BorderLayout.CENTER);

        JPanel sud = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAggiungi = new JButton("Aggiungi Turno");
        btnAggiungi.addActionListener(e -> aggiungiTurno());
        JButton btnIndietro = new JButton("Indietro");
        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });
        sud.add(btnAggiungi);
        sud.add(btnIndietro);
        mainPanel.add(sud, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        aggiornaLista();
        frame.setVisible(true);
    }

    private void aggiornaLista() {
        listaPanel.removeAll();
        ArrayList<Turno> turni = controller.getTurniDiStaff(staff.getCodiceFiscale());
        if (turni.isEmpty()) {
            listaPanel.add(new JLabel("Nessun turno assegnato."));
        } else {
            for (Turno t : turni) {
                JPanel riga = new JPanel(new BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String info = "Data: " + t.getData() + "   |   " + t.getOraInizio() + " - " + t.getOraFine();
                riga.add(new JLabel(info), BorderLayout.CENTER);

                JPanel azioni = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton btnModifica = new JButton("Modifica");
                btnModifica.addActionListener(e -> modificaTurno(t));
                JButton btnRimuovi = new JButton("Rimuovi");
                btnRimuovi.addActionListener(e -> rimuoviTurno(t));
                azioni.add(btnModifica);
                azioni.add(btnRimuovi);
                riga.add(azioni, BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void aggiungiTurno() {
        LocalDate data = chiediData(null);
        if (data == null) return;
        LocalTime oraInizio = chiediOra("Ora inizio (HH:MM)", null);
        if (oraInizio == null) return;
        LocalTime oraFine = chiediOra("Ora fine (HH:MM)", null);
        if (oraFine == null) return;
        boolean esito = controller.aggiungiTurno(staff.getCodiceFiscale(), data, oraInizio, oraFine);
        JOptionPane.showMessageDialog(frame, esito ? "Turno aggiunto." : "Aggiunta non riuscita.");
        if (esito) aggiornaLista();
    }

    private void modificaTurno(Turno t) {
        LocalDate data = chiediData(t.getData());
        if (data == null) return;
        LocalTime oraInizio = chiediOra("Ora inizio (HH:MM)", t.getOraInizio());
        if (oraInizio == null) return;
        LocalTime oraFine = chiediOra("Ora fine (HH:MM)", t.getOraFine());
        if (oraFine == null) return;
        boolean esito = controller.modificaTurno(t.getId(), data, oraInizio, oraFine);
        JOptionPane.showMessageDialog(frame, esito ? "Turno modificato." : "Modifica non riuscita.");
        if (esito) aggiornaLista();
    }

    private void rimuoviTurno(Turno t) {
        int risposta = JOptionPane.showConfirmDialog(frame, "Rimuovere il turno del " + t.getData() + "?",
                "Conferma", JOptionPane.YES_NO_OPTION);
        if (risposta != JOptionPane.YES_OPTION) return;
        boolean esito = controller.rimuoviTurno(t.getId());
        JOptionPane.showMessageDialog(frame, esito ? "Turno rimosso." : "Rimozione non riuscita.");
        if (esito) aggiornaLista();
    }

    private LocalDate chiediData(LocalDate predefinita) {
        String s = JOptionPane.showInputDialog(frame, "Data (AAAA-MM-GG)", predefinita == null ? "" : predefinita.toString());
        if (s == null) return null;
        try {
            return LocalDate.parse(s.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Data non valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private LocalTime chiediOra(String etichetta, LocalTime predefinita) {
        String s = JOptionPane.showInputDialog(frame, etichetta, predefinita == null ? "" : predefinita.toString());
        if (s == null) return null;
        try {
            return LocalTime.parse(s.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Ora non valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
