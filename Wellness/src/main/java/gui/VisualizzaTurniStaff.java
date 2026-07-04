package gui;

import controller.Controller;
import model.logistica.Turno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VisualizzaTurniStaff {
    private JPanel mainPanel;
    private JTable tableTurni;
    private JButton btnIndietro;
    private DefaultTableModel modelloTabella;
    public JFrame frame;

    public VisualizzaTurniStaff(Controller controller, String codiceFiscaleStaff) {

        System.out.println("STAVO CERCANDO I TURNI PER IL CODICE FISCALE: [" + codiceFiscaleStaff + "]");

        String[] colonne = {"ID Turno", "Data", "Ora Inizio", "Ora Fine"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableTurni.setModel(modelloTabella);

        ArrayList<Turno> turni = controller.getTurniDiStaff(codiceFiscaleStaff);
        for (Turno t : turni) {
            modelloTabella.addRow(new Object[]{
                    t.getId(), t.getData(), t.getOraInizio(), t.getOraFine()
            });
        }

        btnIndietro.addActionListener(e -> {
            frame.dispose();
        });

        frame = new JFrame("I Miei Turni Assegnati");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(tableTurni), BorderLayout.CENTER);

        JPanel panelBottoni = new JPanel();
        panelBottoni.add(btnIndietro);
        mainPanel.add(panelBottoni, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}