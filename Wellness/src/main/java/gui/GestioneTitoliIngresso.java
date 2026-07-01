package gui;

import controller.Controller;
import model.commerce.TitoloIngresso;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GestioneTitoliIngresso {

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

    public GestioneTitoliIngresso(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        btnAggiungi.addActionListener(e -> aggiungiTitolo());
        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        frame = new JFrame("Wellness In Cloud - Gestione Titoli d'Ingresso");
        frame.setContentPane(mainPanel);
        frame.setSize(560, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aggiornaLista();
        frame.setVisible(true);
    }

    private void aggiornaLista() {
        listaPanel.removeAll();
        ArrayList<TitoloIngresso> titoli = controller.getListaTitoli();
        if (titoli.isEmpty()) {
            listaPanel.add(new JLabel("Nessun titolo presente."));
        } else {
            for (TitoloIngresso t : titoli) {
                JPanel riga = new JPanel(new java.awt.BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String info = t.getTipo() + "   |   Prezzo: " + t.getPrezzo() + " EUR";
                riga.add(new JLabel(info), java.awt.BorderLayout.CENTER);

                JPanel azioni = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
                JButton btnModifica = new JButton("Modifica");
                btnModifica.addActionListener(e -> modificaTitolo(t));
                JButton btnRimuovi = new JButton("Rimuovi");
                btnRimuovi.addActionListener(e -> rimuoviTitolo(t));
                azioni.add(btnModifica);
                azioni.add(btnRimuovi);
                riga.add(azioni, java.awt.BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void aggiungiTitolo() {
        String tipo = chiediTesto("Tipo (es. Mensile, Annuale)", null);
        if (tipo == null) return;
        BigDecimal prezzo = chiediPrezzo(null);
        if (prezzo == null) return;
        boolean esito = controller.aggiungiTitolo(tipo, prezzo);
        JOptionPane.showMessageDialog(frame, esito ? "Titolo aggiunto." : "Aggiunta non riuscita.");
        if (esito) aggiornaLista();
    }

    private void modificaTitolo(TitoloIngresso t) {
        String tipo = chiediTesto("Tipo (es. Mensile, Annuale)", t.getTipo());
        if (tipo == null) return;
        BigDecimal prezzo = chiediPrezzo(t.getPrezzo());
        if (prezzo == null) return;
        boolean esito = controller.modificaTitolo(t.getId_titoloIngresso(), tipo, prezzo);
        JOptionPane.showMessageDialog(frame, esito ? "Titolo modificato." : "Modifica non riuscita.");
        if (esito) aggiornaLista();
    }

    private void rimuoviTitolo(TitoloIngresso t) {
        int risposta = JOptionPane.showConfirmDialog(frame, "Rimuovere il titolo \"" + t.getTipo() + "\"?",
                "Conferma", JOptionPane.YES_NO_OPTION);
        if (risposta != JOptionPane.YES_OPTION) return;
        boolean esito = controller.rimuoviTitolo(t.getId_titoloIngresso());
        JOptionPane.showMessageDialog(frame, esito ? "Titolo rimosso." : "Rimozione non riuscita.");
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

    private BigDecimal chiediPrezzo(BigDecimal predefinito) {
        String s = JOptionPane.showInputDialog(frame, "Prezzo (es. 45.00)", predefinito == null ? "" : predefinito.toString());
        if (s == null) return null;
        try {
            BigDecimal valore = new BigDecimal(s.trim().replace(",", "."));
            if (valore.signum() < 0) {
                JOptionPane.showMessageDialog(frame, "Il prezzo non puo' essere negativo.", "Errore", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return valore;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Prezzo non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
