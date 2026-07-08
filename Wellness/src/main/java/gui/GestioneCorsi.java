package gui;

import controller.Controller;
import model.logistica.Corso;
import model.utenti.Staff;

import javax.swing.*;
import java.util.ArrayList;

public class GestioneCorsi {

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

    public GestioneCorsi(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        btnAggiungi.addActionListener(e -> aggiungiCorso());
        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        frame = new JFrame("Wellness In Cloud - Gestione Corsi");
        frame.setContentPane(mainPanel);
        frame.setSize(700, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aggiornaLista();
        frame.setVisible(true);
    }

    private void aggiornaLista() {
        listaPanel.removeAll();
        ArrayList<Corso> corsi = controller.getListaCorsi();
        if (corsi.isEmpty()) {
            listaPanel.add(new JLabel("Nessun corso presente."));
        } else {
            for (Corso c : corsi) {
                JPanel riga = new JPanel(new java.awt.BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String descr = c.getDescrizione() == null ? "" : c.getDescrizione();
                String istruttore = (c.getIstruttore() != null) ? (c.getIstruttore().getNome() + " " + c.getIstruttore().getCognome()) : "N/A";
                String info = c.getNome()
                        + "   |   " + descr
                        + "   |   Istruttore: " + istruttore;
                riga.add(new JLabel(info), java.awt.BorderLayout.CENTER);

                JPanel azioni = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
                JButton btnLezioni = new JButton("Gestisci Lezioni");
                btnLezioni.addActionListener(e -> {
                    new GestioneLezioni(controller, frame, c);
                    frame.setVisible(false);
                });
                JButton btnModifica = new JButton("Modifica");
                btnModifica.addActionListener(e -> modificaCorso(c));
                JButton btnRimuovi = new JButton("Rimuovi");
                btnRimuovi.addActionListener(e -> rimuoviCorso(c));
                azioni.add(btnLezioni);
                azioni.add(btnModifica);
                azioni.add(btnRimuovi);
                riga.add(azioni, java.awt.BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void aggiungiCorso() {
        ArrayList<Staff> istruttori = controller.getListaIstruttori();
        if (istruttori.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessun istruttore disponibile. Aggiungi prima uno staff con ruolo ISTRUTTORE.");
            return;
        }
        String nome = chiediTesto("Nome del corso", null);
        if (nome == null) return;
        String descrizione = chiediTesto("Descrizione", null);
        if (descrizione == null) return;
        Staff istruttore = chiediIstruttore(istruttori, null);
        if (istruttore == null) return;
        boolean esito = controller.aggiungiCorso(nome, descrizione, istruttore.getCodiceFiscale());
        JOptionPane.showMessageDialog(frame, esito ? "Corso aggiunto." : "Aggiunta non riuscita.");
        if (esito) aggiornaLista();
    }

    private void modificaCorso(Corso c) {
        ArrayList<Staff> istruttori = controller.getListaIstruttori();
        if (istruttori.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessun istruttore disponibile. Aggiungi prima uno staff con ruolo ISTRUTTORE.");
            return;
        }
        String nome = chiediTesto("Nome del corso", c.getNome());
        if (nome == null) return;
        String descrizione = chiediTesto("Descrizione", c.getDescrizione());
        if (descrizione == null) return;
        Staff istruttore = chiediIstruttore(istruttori, c.getIstruttore().getCodiceFiscale());
        if (istruttore == null) return;
        boolean esito = controller.modificaCorso(c.getId(), nome, descrizione, istruttore.getCodiceFiscale());
        JOptionPane.showMessageDialog(frame, esito ? "Corso modificato." : "Modifica non riuscita.");
        if (esito) aggiornaLista();
    }

    private void rimuoviCorso(Corso c) {
        int risposta = JOptionPane.showConfirmDialog(frame, "Rimuovere il corso \"" + c.getNome() + "\"?",
                "Conferma", JOptionPane.YES_NO_OPTION);
        if (risposta != JOptionPane.YES_OPTION) return;
        boolean esito = controller.rimuoviCorso(c.getId());
        JOptionPane.showMessageDialog(frame, esito ? "Corso rimosso." : "Rimozione non riuscita.");
        if (esito) aggiornaLista();
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

    private Staff chiediIstruttore(ArrayList<Staff> istruttori, String cfPreselezionato) {
        String[] etichette = new String[istruttori.size()];
        String selezioneIniziale = etichette.length > 0 ? etichette[0] : null;
        for (int i = 0; i < istruttori.size(); i++) {
            Staff s = istruttori.get(i);
            etichette[i] = s.getNome() + " " + s.getCognome() + " (" + s.getCodiceFiscale() + ")";
            if (cfPreselezionato != null && cfPreselezionato.equals(s.getCodiceFiscale())) {
                selezioneIniziale = etichette[i];
            }
        }
        Object scelta = JOptionPane.showInputDialog(frame, "Istruttore", "Selezione istruttore",
                JOptionPane.QUESTION_MESSAGE, null, etichette, selezioneIniziale);
        if (scelta == null) return null;
        for (int i = 0; i < etichette.length; i++) {
            if (etichette[i].equals(scelta)) return istruttori.get(i);
        }
        return null;
    }
}
