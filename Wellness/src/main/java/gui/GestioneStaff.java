package gui;

import controller.Controller;
import model.utenti.Staff;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GestioneStaff {

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;

    public GestioneStaff(Controller controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;

        frame = new JFrame("Wellness In Cloud - Visualizza Staff");
        frame.setSize(650, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titolo = new JLabel("Elenco Staff", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titolo, BorderLayout.NORTH);

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        ArrayList<Staff> listaStaff = controller.getListaStaff();
        if (listaStaff.isEmpty()) {
            listaPanel.add(new JLabel("Nessuno staff presente."));
        } else {
            for (Staff s : listaStaff) {
                JPanel riga = new JPanel(new BorderLayout(10, 0));
                riga.setBorder(BorderFactory.createEtchedBorder());
                String info = s.getNome() + " " + s.getCognome()
                        + "   |   " + s.getEmail()
                        + "   |   Ruolo: " + s.getRuolo()
                        + "   |   Qualifica: " + s.getQualifica();
                riga.add(new JLabel(info), BorderLayout.CENTER);

                JButton btnTurni = new JButton("Gestisci Turni");
                btnTurni.addActionListener(e -> {
                    new GestioneTurni(controller, frame, s);
                    frame.setVisible(false);
                });
                riga.add(btnTurni, BorderLayout.EAST);
                listaPanel.add(riga);
            }
        }

        mainPanel.add(new JScrollPane(listaPanel), BorderLayout.CENTER);

        JButton btnIndietro = new JButton("Indietro");
        btnIndietro.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });
        mainPanel.add(btnIndietro, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
