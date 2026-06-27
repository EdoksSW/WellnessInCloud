package gui;

import controller.Controller;
import model.commerce.Categoria;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GestioneCategorie {

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private JPanel listaPanel;

    public GestioneCategorie(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        frame = new JFrame("Wellness In Cloud - Gestione Categorie");
        frame.setSize(500, 380);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titolo = new JLabel("Elenco Categorie", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titolo, BorderLayout.NORTH);

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JScrollPane(listaPanel), BorderLayout.CENTER);

        JPanel sud = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAggiungi = new JButton("Aggiungi Categoria");
        btnAggiungi.addActionListener(e -> aggiungiCategoria());
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
        ArrayList<Categoria> categorie = controller.getListaCategorie();
        if (categorie.isEmpty()) {
            listaPanel.add(new JLabel("Nessuna categoria presente."));
        } else {
            for (Categoria c : categorie) {
                JPanel riga = new JPanel(new BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                riga.add(new JLabel(c.getNome()), BorderLayout.CENTER);

                JPanel azioni = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton btnModifica = new JButton("Modifica");
                btnModifica.addActionListener(e -> modificaCategoria(c));
                JButton btnRimuovi = new JButton("Rimuovi");
                btnRimuovi.addActionListener(e -> rimuoviCategoria(c));
                azioni.add(btnModifica);
                azioni.add(btnRimuovi);
                riga.add(azioni, BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void aggiungiCategoria() {
        String nome = chiediTesto("Nome categoria", null);
        if (nome == null) return;
        boolean esito = controller.aggiungiCategoria(nome);
        JOptionPane.showMessageDialog(frame, esito ? "Categoria aggiunta." : "Aggiunta non riuscita.");
        if (esito) aggiornaLista();
    }

    private void modificaCategoria(Categoria c) {
        String nome = chiediTesto("Nome categoria", c.getNome());
        if (nome == null) return;
        boolean esito = controller.modificaCategoria(c.getId(), nome);
        JOptionPane.showMessageDialog(frame, esito ? "Categoria modificata." : "Modifica non riuscita.");
        if (esito) aggiornaLista();
    }

    private void rimuoviCategoria(Categoria c) {
        int risposta = JOptionPane.showConfirmDialog(frame, "Rimuovere la categoria \"" + c.getNome() + "\"?",
                "Conferma", JOptionPane.YES_NO_OPTION);
        if (risposta != JOptionPane.YES_OPTION) return;
        boolean esito = controller.rimuoviCategoria(c.getId());
        JOptionPane.showMessageDialog(frame, esito ? "Categoria rimossa." : "Rimozione non riuscita.");
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
}
