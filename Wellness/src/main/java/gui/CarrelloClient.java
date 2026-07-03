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
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });
        txtAcquista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean successo=controller.finalizzaAcquisto(clienteLoggato);

                if(successo)
                {
                    JOptionPane.showMessageDialog(frame,"Acquisto effettuato con successo!","Successo", JOptionPane.INFORMATION_MESSAGE);

                    //Riapriamo la finesta precedenta (la Home) e chiudiamo quella del carrello
                    frameChiamante.setVisible(true);
                    frame.dispose();
                }else
                {
                    JOptionPane.showMessageDialog(frame,"Errore durante l'acquisto. Riprovare", "Errore",JOptionPane.ERROR_MESSAGE);
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
