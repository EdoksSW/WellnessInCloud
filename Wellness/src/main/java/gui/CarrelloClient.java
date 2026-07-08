package gui;

import controller.Controller;
import model.commerce.Carrello;
import model.commerce.Prodotto;
import model.utenti.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CarrelloClient {
    private JPanel carrelloPanel;
    private JButton txtHome;
    private JPanel txtComandi;
    private JScrollPane scrollCarrello;
    private JTable tabellaCarrello;
    private JPanel lblCmAcquisti;
    private JButton txtAcquista;
    private JTextField lblTotale;
    private DefaultTableModel modelloCarrello;

    private Controller controller;
    private Cliente clienteLoggato;
    private JFrame frameChiamante;
    JFrame frame;

    public CarrelloClient(Controller controller, Cliente cliente, JFrame frameChiamante)
    {
        this.controller=controller;
        this.clienteLoggato=cliente;
        this.frameChiamante=frameChiamante;

        String[] colonne={"ID","Prodotto","Prezzo Unitario", "Quantità","Subtotale"};

        modelloCarrello=new DefaultTableModel(colonne, 0)
        {
            public boolean isCellEditable(int row, int column) {return false;}
        };

        tabellaCarrello.setModel(modelloCarrello);
        tabellaCarrello.setRowHeight(25);

        tabellaCarrello.getColumnModel().getColumn(0).setMinWidth(0);
        tabellaCarrello.getColumnModel().getColumn(0).setMaxWidth(0);
        tabellaCarrello.getColumnModel().getColumn(0).setWidth(0);

        caricaCarrello();

        JButton txtRimuovi=new JButton("Rimuovi");
        txtComandi.add(txtRimuovi);

        txtRimuovi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata=tabellaCarrello.getSelectedRow();
                if(rigaSelezionata == -1)
                {
                    JOptionPane.showMessageDialog(frame,"Seleziona un prodotto dalla tabella per rimuoverlo.","Selezione Mancante", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idProdotto=(int) tabellaCarrello.getModel().getValueAt(rigaSelezionata,0);
                String nomeProdotto=tabellaCarrello.getModel().getValueAt(rigaSelezionata,1).toString();
                int quantitaInCarrello=(int) tabellaCarrello.getModel().getValueAt(rigaSelezionata,3);

                String inputQuantita=JOptionPane.showInputDialog(frame,"Quantità da rimuovere per '"+nomeProdotto+"' (max "+quantitaInCarrello+"):","Rimuovi dal Carrello", JOptionPane.QUESTION_MESSAGE);
                if(inputQuantita == null || inputQuantita.trim().isEmpty())
                {
                    return;
                }

                try
                {
                    int quantita=Integer.parseInt(inputQuantita.trim());
                    if(quantita <= 0)
                    {
                        JOptionPane.showMessageDialog(frame,"Quantità non valida!");
                        return;
                    }
                    if(quantita > quantitaInCarrello)
                    {
                        JOptionPane.showMessageDialog(frame,"Non puoi rimuovere più di "+quantitaInCarrello+" unità.");
                        return;
                    }

                    if(controller.rimuoviProdottoCarrello(clienteLoggato, idProdotto, quantita))
                    {
                        JOptionPane.showMessageDialog(frame,"Prodotto rimosso correttamente!");
                        caricaCarrello();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(frame,"Si è verificato un errore durante la rimozione.","Errore di Sistema", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(frame,"Formato non valido! Inserire un valore numerico intero.","Errore Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        txtHome.addActionListener(new ActionListener() {
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
        txtAcquista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel defaultTableModel1 = (DefaultTableModel) tabellaCarrello.getModel();

                if(defaultTableModel1.getRowCount() == 0)
                {
                    JOptionPane.showMessageDialog(frame, "Il tuo carrello è vuoto! Aggiungi dei prodotti prima di acquistare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int risposta = JOptionPane.showConfirmDialog(frame, "Vuoi procedere con l'acquisto?", "Conferma l'ordine", JOptionPane.YES_NO_OPTION);
                if(risposta == JOptionPane.YES_OPTION)
                {
                    if(controller.finalizzaAcquisto(clienteLoggato))
                    {
                        JOptionPane.showMessageDialog(frame, "Acquisto effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        new ShopClient(controller, clienteLoggato, null);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Errore durante l'acquisto. Controlla la disponibilità dei prodotti.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        frame=new JFrame("Wellness - Carrello");
        frame.setContentPane(carrelloPanel);
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void caricaCarrello()
    {
        modelloCarrello.setRowCount(0);
        Carrello carrello=controller.ottieniCarrelloCliente(clienteLoggato);
        BigDecimal totaleAccumulato = BigDecimal.ZERO;

        if(carrello != null && carrello.getMapCarrellol() != null && !carrello.getMapCarrellol().isEmpty())
        {
            for (Map.Entry<Prodotto, Integer> entry : carrello.getMapCarrellol().entrySet())
            {
                Prodotto prodotto=entry.getKey();
                int quantita=entry.getValue();

                BigDecimal prezzoUnitario = (prodotto.getPrezzo() != null) ? prodotto.getPrezzo() : BigDecimal.ZERO;
                BigDecimal quantitaBD = new BigDecimal(quantita);
                BigDecimal subtotaleBD = prezzoUnitario.multiply(quantitaBD);

                totaleAccumulato = totaleAccumulato.add(subtotaleBD);

                Object[] riga=new Object[5];
                riga[0]=prodotto.getId_prodotto();
                riga[1]=prodotto.getNome();
                riga[2]="euro "+prezzoUnitario.setScale(2, RoundingMode.HALF_UP);
                riga[3]=quantita;
                riga[4]="euro "+subtotaleBD.setScale(2, RoundingMode.HALF_UP);
                modelloCarrello.addRow(riga);
            }
        }

        lblTotale.setText("Totale Carrello: euro "+ totaleAccumulato.setScale(2, RoundingMode.HALF_UP));
    }
}