package gui;

import controller.Controller;
import model.utenti.Staff;

import javax.swing.*;
import java.util.ArrayList;

public class GestioneStaff {

    private JPanel mainPanel;
    private JLabel lblTitolo;
    private JScrollPane scrollPane;
    private JPanel listaPanel;
    private JPanel panelBottoni;
    private JButton btnIndietro;

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;

    public GestioneStaff(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });

        frame = new JFrame("Wellness In Cloud - Visualizza Staff");
        frame.setContentPane(mainPanel);
        frame.setSize(650, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aggiornaLista();
        frame.setVisible(true);
    }

    private void aggiornaLista() {
        listaPanel.removeAll();
        ArrayList<Staff> listaStaff = controller.getListaStaff();
        if (listaStaff.isEmpty()) {
            listaPanel.add(new JLabel("Nessuno staff presente."));
        } else {
            for (Staff s : listaStaff) {
                JPanel riga = new JPanel(new java.awt.BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String info = s.getNome() + " " + s.getCognome()
                        + "   |   " + s.getEmail()
                        + "   |   Ruolo: " + s.getRuolo()
                        + "   |   Qualifica: " + s.getQualifica();
                riga.add(new JLabel(info), java.awt.BorderLayout.CENTER);

                JButton btnTurni = new JButton("Gestisci Turni");
                btnTurni.addActionListener(e -> {
                    new GestioneTurni(controller, frame, s);
                    frame.setVisible(false);
                });
                riga.add(btnTurni, java.awt.BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }
}
