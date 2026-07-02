package gui;

import controller.Controller;
import model.logistica.Corso;
import model.logistica.Lezione;

import javax.swing.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class GestioneLezioni {

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
    private Corso corso;

    public GestioneLezioni(Controller controller, JFrame frameChiamante, Corso corso) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;
        this.corso = corso;

        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        lblTitolo.setText("Lezioni del corso: " + corso.getNome());

        btnAggiungi.addActionListener(e -> aggiungiLezione());
        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        frame = new JFrame("Wellness In Cloud - Gestione Lezioni");
        frame.setContentPane(mainPanel);
        frame.setSize(700, 430);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aggiornaLista();
        frame.setVisible(true);
    }

    private void aggiornaLista() {
        listaPanel.removeAll();
        ArrayList<Lezione> lezioni = controller.getLezioniCorso(corso.getId());
        if (lezioni.isEmpty()) {
            listaPanel.add(new JLabel("Nessuna lezione presente."));
        } else {
            for (Lezione l : lezioni) {
                JPanel riga = new JPanel(new java.awt.BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String info = l.getNome()
                        + "   |   " + (l.getGiorno() == null ? "" : l.getGiorno())
                        + "   |   " + l.getOra_inizio() + " - " + l.getOraFine()
                        + "   |   Sala: " + l.getId_sala();
                riga.add(new JLabel(info), java.awt.BorderLayout.CENTER);

                JPanel azioni = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
                JButton btnModifica = new JButton("Modifica");
                btnModifica.addActionListener(e -> modificaLezione(l));
                JButton btnRimuovi = new JButton("Rimuovi");
                btnRimuovi.addActionListener(e -> rimuoviLezione(l));
                azioni.add(btnModifica);
                azioni.add(btnRimuovi);
                riga.add(azioni, java.awt.BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void aggiungiLezione() {
        ArrayList<Integer> sale = controller.getListaSale();
        if (sale.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessuna sala disponibile.");
            return;
        }
        String nome = chiediTesto("Nome lezione", null);
        if (nome == null) return;
        String descrizione = JOptionPane.showInputDialog(frame, "Descrizione", "");
        if (descrizione == null) return;
        String giorno = JOptionPane.showInputDialog(frame, "Giorno (es. Lunedi)", "");
        if (giorno == null) return;
        LocalTime oraInizio = chiediOra("Ora inizio (HH:MM)", null);
        if (oraInizio == null) return;
        LocalTime oraFine = chiediOra("Ora fine (HH:MM)", null);
        if (oraFine == null) return;
        Integer idSala = chiediSala(sale, null);
        if (idSala == null) return;

        boolean esito = controller.aggiungiLezione(corso.getId(), nome, descrizione, giorno, oraInizio, oraFine, idSala);
        JOptionPane.showMessageDialog(frame, esito ? "Lezione aggiunta." : "Aggiunta non riuscita.");
        if (esito) aggiornaLista();
    }

    private void modificaLezione(Lezione l) {
        ArrayList<Integer> sale = controller.getListaSale();
        if (sale.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessuna sala disponibile.");
            return;
        }
        String nome = chiediTesto("Nome lezione", l.getNome());
        if (nome == null) return;
        String descrizione = JOptionPane.showInputDialog(frame, "Descrizione", l.getDescrizione() == null ? "" : l.getDescrizione());
        if (descrizione == null) return;
        String giorno = JOptionPane.showInputDialog(frame, "Giorno (es. Lunedi)", l.getGiorno() == null ? "" : l.getGiorno());
        if (giorno == null) return;
        LocalTime oraInizio = chiediOra("Ora inizio (HH:MM)", l.getOra_inizio());
        if (oraInizio == null) return;
        LocalTime oraFine = chiediOra("Ora fine (HH:MM)", l.getOraFine());
        if (oraFine == null) return;
        Integer idSala = chiediSala(sale, l.getId_sala());
        if (idSala == null) return;

        boolean esito = controller.modificaLezione(l.getId_lezione(), nome, descrizione, giorno, oraInizio, oraFine, idSala);
        JOptionPane.showMessageDialog(frame, esito ? "Lezione modificata." : "Modifica non riuscita.");
        if (esito) aggiornaLista();
    }

    private void rimuoviLezione(Lezione l) {
        int risposta = JOptionPane.showConfirmDialog(frame, "Rimuovere la lezione \"" + l.getNome() + "\"?",
                "Conferma", JOptionPane.YES_NO_OPTION);
        if (risposta != JOptionPane.YES_OPTION) return;
        boolean esito = controller.rimuoviLezione(l.getId_lezione());
        JOptionPane.showMessageDialog(frame, esito ? "Lezione rimossa." : "Rimozione non riuscita.");
        if (esito) aggiornaLista();
    }

    private Integer chiediSala(ArrayList<Integer> sale, Integer predefinita) {
        Object scelta = JOptionPane.showInputDialog(frame, "Sala", "Selezione sala",
                JOptionPane.QUESTION_MESSAGE, null, sale.toArray(), predefinita != null ? predefinita : sale.get(0));
        return (Integer) scelta;
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
