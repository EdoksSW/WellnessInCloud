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

        for(Ordine ordine: listaOrdini)
        {
            Object[] riga=new Object[4];
            riga[0] = ordine.getId_ordine();
            riga[1] = ordine.getData_ordine();
            riga[2] = "euro " + ordine.getTotale();
            riga[3] = ordine.getStato();

            defaultTableModel.addRow(riga);
        }

        txtTabellaOrdini.setModel(defaultTableModel);
        //Bottone Home
        txtHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true); // Fa comparire la HomeClient
                frame.dispose(); // Chiude la finestra degli ordini
            }
        });

        frame= new JFrame("Wellness - I Miei Ordini");
        frame.setContentPane(ordiniClient);
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
