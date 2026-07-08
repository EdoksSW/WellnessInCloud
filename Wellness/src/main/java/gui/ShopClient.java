package gui;

import controller.Controller;
import model.commerce.Prodotto;
import model.utenti.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShopClient {
    private JPanel shopPanel;
    private JScrollPane srollProdotti;
    private JTable tabellaProdotti;
    private JButton bntAggiungi;
    private JButton bntVaiAlCarrello;
    private JButton bntHome;

    private Controller controller;
    private Cliente clienteLoggato;
    private JFrame frameChiamante;
    JFrame frame;

    public ShopClient(Controller controller, Cliente cliente, JFrame frameChiamante) {
        this.controller = controller;
        this.clienteLoggato = cliente;
        this.frameChiamante = frameChiamante;

        String[] colonne = {"ID", "Prodotto", "Categoria", "Prezzo", "Disponibilità"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Prodotto> catalogo = controller.ottieniCatalogoProdotto();

        if (catalogo != null) {
            for (Prodotto p : catalogo) {
                Object[] riga = new Object[5];
                riga[0] = p.getId_prodotto();
                riga[1] = p.getNome();
                riga[2] = (p.getCategoria() != null) ? p.getCategoria().getNome() : "N/A";
                riga[3] = "euro " + p.getPrezzo();
                riga[4] = p.getGiacenza();

                defaultTableModel.addRow(riga);
            }
        }

        tabellaProdotti.setModel(defaultTableModel);
        tabellaProdotti.setRowHeight(25);


        tabellaProdotti.getColumnModel().getColumn(0).setMinWidth(0);
        tabellaProdotti.getColumnModel().getColumn(0).setMaxWidth(0);
        tabellaProdotti.getColumnModel().getColumn(0).setWidth(0);


        bntAggiungi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = tabellaProdotti.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Per favore, seleziona un prodotto dalla tabella prima di continuare.", "Selezione Mancante", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idProdotto = (int) tabellaProdotti.getModel().getValueAt(rigaSelezionata, 0);
                String nomeProdotto = (String) tabellaProdotti.getModel().getValueAt(rigaSelezionata, 1);
                int disponibilitaMagazzino = (int) tabellaProdotti.getModel().getValueAt(rigaSelezionata, 4);

                String inputQuantita = JOptionPane.showInputDialog(frame, "Inserisci la quantità desiderata per '" + nomeProdotto + "':", "Aggiungi al Carrello", JOptionPane.QUESTION_MESSAGE);

                if (inputQuantita != null && !inputQuantita.trim().isEmpty()) {
                    try {
                        int quantitaInizializzata = Integer.parseInt(inputQuantita);

                        if (quantitaInizializzata <= 0) {
                            JOptionPane.showMessageDialog(frame, "Quantità non valida!");
                            return;
                        }
                        if (quantitaInizializzata > disponibilitaMagazzino) {
                            JOptionPane.showMessageDialog(frame, "Quantità insufficiente nel magazzino!\nDisponibilità massima attuale: " + disponibilitaMagazzino);
                            return;
                        }

                        boolean operazioneCompletata = controller.aggiungiProdottoCarrello(clienteLoggato, idProdotto, quantitaInizializzata);

                        if (operazioneCompletata) {
                            JOptionPane.showMessageDialog(frame, "Prodotto inserito correttamente!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Si è verificato un errore durante l'inserimento", "Errore di Sistema", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Formato non valido! Inserire un valore numerico intero.", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        bntVaiAlCarrello.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new CarrelloClient(controller, clienteLoggato, frame);
            }
        });


        bntHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameChiamante != null) {
                    frameChiamante.setVisible(true);
                } else {
                    new HomeClient(controller, clienteLoggato, null);
                }
                frame.dispose();
            }
        });

        // Inizializzazione e visualizzazione del JFrame della finestra
        frame = new JFrame("Wellness System - Catalogo Prodotti");
        frame.setContentPane(shopPanel);
        frame.setSize(750, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}