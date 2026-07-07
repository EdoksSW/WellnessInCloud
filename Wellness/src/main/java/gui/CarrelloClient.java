package gui;

import controller.Controller;
import model.commerce.Carrello;
import model.commerce.Prodotto;
import model.utenti.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private Controller controller;
    private Cliente clienteLoggato;
    private JFrame frameChiamante;
    JFrame frame;

    public CarrelloClient(Controller controller, Cliente cliente, JFrame frameChiamante)
    {
        this.controller=controller;
        this.clienteLoggato=cliente;
        this.frameChiamante=frameChiamante;

        String[] colonne={"Prodotto","Prezzo Unitario", "Quantità","Subtotale"};

        DefaultTableModel defaultTableModel=new DefaultTableModel(colonne, 0)
        {
            public boolean isCellEditable(int row, int column) {return false;}
        };

        //recupero dell'oggetto Carrello dal DB tramite il controller
        Carrello carrello= controller. ottieniCarrelloCliente(clienteLoggato);

        //Variabile di supporto per accumulare il costro totale se il carrello è vuoto
        double totaleSpeso=0.0;

        if(carrello != null && carrello.getMapCarrellol() != null && !carrello.getMapCarrellol().isEmpty())
        {
            for (Map.Entry<Prodotto, Integer> entry : carrello.getMapCarrellol().entrySet())
            {
                Prodotto prodotto=entry.getKey();
                int quantita = entry.getValue();

                Object[] riga= new Object[4];
                riga[0]=prodotto.getNome();
                riga[1]="euro "+prodotto.getPrezzo();
                riga[2]=quantita;

                //Calcolo del subtotale della riga (Presso del singolo prodotto * quantità Acquistata)
                double subtototale=prodotto.getPrezzo().doubleValue()*quantita;
                riga[3]="euro"+subtototale;

                //Inserimento della riga all'interno del modello della tabella
                defaultTableModel.addRow(riga);
            }

            totaleSpeso=carrello.getTotale().doubleValue();
        }

        // collegamento del modello dati alla Jtable
        tabellaCarrello.setModel(defaultTableModel);
        tabellaCarrello.setRowHeight(25);

        //Scrittura totale affianco al bottone Acquiesta
        lblTotale.setText("Totale Carrello: euro "+ totaleSpeso);
        txtHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeClient(controller, clienteLoggato, frameChiamante);
                frame.dispose();
            }
        });
        txtAcquista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Recuperiamo il modello attuale direttamente dalla tabellaCarrello per poi verificare se ha 0 righe
                DefaultTableModel defaultTableModel1 = (DefaultTableModel) tabellaCarrello.getModel();

                if(defaultTableModel1.getRowCount() == 0)
                {
                    JOptionPane.showMessageDialog(frame, "Il tuo carrello è vuoto! Aggiungi dei prodotti prima di acquistare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int risposta = JOptionPane.showConfirmDialog(frame, "Vuoi procedere con l'acquisto?", "Conferma l'ordine", JOptionPane.YES_NO_OPTION);
                if(risposta == JOptionPane.YES_OPTION)
                {
                    // Invochiamo il controller passando l'oggetto clienteLoggato intero
                    if(controller.finalizzaAcquisto(clienteLoggato))
                    {
                        JOptionPane.showMessageDialog(frame, "Acquisto effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

                        // Apriamo una nuova istanza pulita dello ShopClient
                        // Il suo costruttore eseguirà la query e mostrerà le giacenze ridotte del database (es. 55)
                        new ShopClient(controller, clienteLoggato, null);

                        // Chiudiamo definitivamente la finestra attuale del carrello
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

}
