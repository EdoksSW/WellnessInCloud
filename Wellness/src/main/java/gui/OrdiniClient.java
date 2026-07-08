package gui;

import controller.Controller;
import model.commerce.Ordine;
import model.utenti.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrdiniClient {
    private JPanel ordiniClient;
    private JTable txtTabellaOrdini;
    private JButton txtHome;
    private JScrollPane scrollOrdini;
    private JButton bntAnnullaOrdine;

    private Controller controller;
    private Cliente clienteLoggato;
    private JFrame frameChiamante;
    JFrame frame;

    public OrdiniClient(Controller controller, Cliente cliente, JFrame frameChiamante)
    {
        this.controller= controller;
        this.clienteLoggato=cliente;
        this.frameChiamante=frameChiamante;

        String[] colonne={"ID Ordine","Data","Totale Speso", "StatoSpedizione"};
        DefaultTableModel defaultTableModel=new DefaultTableModel(colonne, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        List<Ordine> listaOrdini = controller.ottieniStoricoOrdiniCliente(clienteLoggato);

        if(listaOrdini != null)
        {
            for(Ordine ordine: listaOrdini)
            {
                Object[] riga=new Object[4];
                riga[0] = ordine.getId_ordine();
                riga[1] = ordine.getData_ordine();
                riga[2] = "euro " + ordine.getTotale();
                riga[3] = (ordine.getStato() != null) ? ordine.getStato() : "";

                defaultTableModel.addRow(riga);
            }
        }

        txtTabellaOrdini.setModel(defaultTableModel);

        txtTabellaOrdini.setRowHeight(25);
        //Bottone Home
        txtHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        bntAnnullaOrdine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = txtTabellaOrdini.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(frame, "Seleziona un ordine dalla tabella per annullarlo.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // [Risoluzione Indici]: ID ordine è colonna 0, lo STATO reale è colonna 3
                int idOrdine = (int) txtTabellaOrdini.getValueAt(rigaSelezionata, 0);
                Object statoCell = txtTabellaOrdini.getValueAt(rigaSelezionata, 3);
                String stato = (statoCell != null) ? statoCell.toString() : "";

                // Controllo logico-stringa: verifica dello stato di lavorazione (gestisce sia con che senza underscore)
                if (!stato.equalsIgnoreCase("IN ELABORAZIONE") && !stato.equalsIgnoreCase("IN_ELABORAZIONE")) {
                    JOptionPane.showMessageDialog(frame, "Non puoi annullare un ordine che è già in stato: " + stato, "Operazione Negata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int scelta = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler annullare l'ordine #" + idOrdine + "? I prodotti verranno rimessi in magazzino.", "Conferma Annullamento", JOptionPane.YES_NO_OPTION);

                if (scelta == JOptionPane.YES_OPTION) {
                    // Chiamata al controller per l'eliminazione atomica e ripristino giacenze
                    if (controller.annullaOrdineCliente(idOrdine)) {
                        JOptionPane.showMessageDialog(frame, "Ordine annullato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

                        // Rinfreschiamo graficamente il modello rimuovendo la riga
                        ((DefaultTableModel) txtTabellaOrdini.getModel()).removeRow(rigaSelezionata);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Errore durante l'annullamento dell'ordine.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 5. Inizializzazione e Visualizzazione del Frame Visivo
        frame = new JFrame("Wellness - I Miei Ordini");
        frame.setContentPane(ordiniClient);
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
