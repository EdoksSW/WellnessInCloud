package gui;

import controller.Controller;
import model.commerce.Categoria;
import model.commerce.Prodotto;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GestionePrezzario {

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;
    private JPanel listaPanel;

    public GestionePrezzario(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        frame = new JFrame("Wellness In Cloud - Gestione Prezzario");
        frame.setSize(700, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titolo = new JLabel("Elenco Prodotti", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titolo, BorderLayout.NORTH);

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JScrollPane(listaPanel), BorderLayout.CENTER);

        JPanel sud = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAggiungi = new JButton("Aggiungi Prodotto");
        btnAggiungi.addActionListener(e -> aggiungiProdotto());
        JButton btnCategorie = new JButton("Gestisci Categorie");
        btnCategorie.addActionListener(e -> {
            new GestioneCategorie(controller, frame);
            frame.setVisible(false);
        });
        JButton btnIndietro = new JButton("Indietro");
        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });
        sud.add(btnAggiungi);
        sud.add(btnCategorie);
        sud.add(btnIndietro);
        mainPanel.add(sud, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        aggiornaLista();
        frame.setVisible(true);
    }

    private void aggiornaLista() {
        listaPanel.removeAll();
        ArrayList<Prodotto> prodotti = controller.getListaProdotti();
        if (prodotti.isEmpty()) {
            listaPanel.add(new JLabel("Nessun prodotto presente."));
        } else {
            for (Prodotto p : prodotti) {
                JPanel riga = new JPanel(new BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String categoria = p.getCategoria() == null ? "Nessuna" : p.getCategoria().getNome();
                String info = p.getNome()
                        + "   |   Prezzo: " + p.getPrezzo() + " EUR"
                        + "   |   Giacenza: " + p.getGiacenza()
                        + "   |   Categoria: " + categoria;
                riga.add(new JLabel(info), BorderLayout.CENTER);

                JPanel azioni = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton btnModifica = new JButton("Modifica");
                btnModifica.addActionListener(e -> modificaProdotto(p));
                JButton btnRimuovi = new JButton("Rimuovi");
                btnRimuovi.addActionListener(e -> rimuoviProdotto(p));
                azioni.add(btnModifica);
                azioni.add(btnRimuovi);
                riga.add(azioni, BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void aggiungiProdotto() {
        ArrayList<Categoria> categorie = controller.getListaCategorie();
        if (categorie.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessuna categoria disponibile. Creane una con 'Gestisci Categorie'.");
            return;
        }
        String nome = chiediTesto("Nome del prodotto", null);
        if (nome == null) return;
        BigDecimal prezzo = chiediPrezzo(null);
        if (prezzo == null) return;
        Integer giacenza = chiediGiacenza(null);
        if (giacenza == null) return;
        Categoria categoria = chiediCategoria(categorie, null);
        if (categoria == null) return;
        boolean esito = controller.aggiungiProdotto(nome, prezzo, giacenza, categoria.getId());
        JOptionPane.showMessageDialog(frame, esito ? "Prodotto aggiunto." : "Aggiunta non riuscita.");
        if (esito) aggiornaLista();
    }

    private void modificaProdotto(Prodotto p) {
        ArrayList<Categoria> categorie = controller.getListaCategorie();
        if (categorie.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessuna categoria disponibile. Creane una con 'Gestisci Categorie'.");
            return;
        }
        String nome = chiediTesto("Nome del prodotto", p.getNome());
        if (nome == null) return;
        BigDecimal prezzo = chiediPrezzo(p.getPrezzo());
        if (prezzo == null) return;
        Integer giacenza = chiediGiacenza(p.getGiacenza());
        if (giacenza == null) return;
        Categoria categoria = chiediCategoria(categorie, p.getCategoria());
        if (categoria == null) return;
        boolean esito = controller.modificaProdotto(p.getId(), nome, prezzo, giacenza, categoria.getId());
        JOptionPane.showMessageDialog(frame, esito ? "Prodotto modificato." : "Modifica non riuscita.");
        if (esito) aggiornaLista();
    }

    private void rimuoviProdotto(Prodotto p) {
        int risposta = JOptionPane.showConfirmDialog(frame, "Rimuovere il prodotto \"" + p.getNome() + "\"?",
                "Conferma", JOptionPane.YES_NO_OPTION);
        if (risposta != JOptionPane.YES_OPTION) return;
        boolean esito = controller.rimuoviProdotto(p.getId());
        JOptionPane.showMessageDialog(frame, esito ? "Prodotto rimosso." : "Rimozione non riuscita.");
        if (esito) aggiornaLista();
    }

    private Categoria chiediCategoria(ArrayList<Categoria> categorie, Categoria predefinita) {
        String[] etichette = new String[categorie.size()];
        String selezioneIniziale = etichette.length > 0 ? etichette[0] : null;
        for (int i = 0; i < categorie.size(); i++) {
            etichette[i] = categorie.get(i).getNome();
            if (predefinita != null && predefinita.getId() == categorie.get(i).getId()) {
                selezioneIniziale = etichette[i];
            }
        }
        Object scelta = JOptionPane.showInputDialog(frame, "Categoria", "Selezione categoria",
                JOptionPane.QUESTION_MESSAGE, null, etichette, selezioneIniziale);
        if (scelta == null) return null;
        for (int i = 0; i < etichette.length; i++) {
            if (etichette[i].equals(scelta)) return categorie.get(i);
        }
        return null;
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
        String s = JOptionPane.showInputDialog(frame, "Prezzo (es. 19.90)", predefinito == null ? "" : predefinito.toString());
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

    private Integer chiediGiacenza(Integer predefinito) {
        String s = JOptionPane.showInputDialog(frame, "Giacenza", predefinito == null ? "" : predefinito.toString());
        if (s == null) return null;
        try {
            int valore = Integer.parseInt(s.trim());
            if (valore < 0) {
                JOptionPane.showMessageDialog(frame, "La giacenza non puo' essere negativa.", "Errore", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return valore;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Giacenza non valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
