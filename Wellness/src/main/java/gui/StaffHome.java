package gui;

import controller.Controller;
import model.utenti.Staff;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffHome {
    private JPanel mainPanel;
    private JButton btnVerificaCertificati;
    private JButton btnGestioneClienti; // Usato per la gestione iscrizioni/clienti
    private JButton btnGestionePrenotazioni;
    private JButton btnVisualizzaTurni;
    private JButton btnGestisciTitoli;
    private JButton btnLogout;

    public JFrame frame;
    private JFrame frameChiamante;

    public JPanel getStaffPanel() {
        return mainPanel;
    }

    public StaffHome(Controller controller, Staff staff, JFrame frameChiamante) {
        this.frameChiamante = frameChiamante;

        btnVerificaCertificati.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VerificaCertificati(controller, frame);
                frame.setVisible(false);
            }
        });

        btnGestioneClienti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestioneClientiStaff(controller, frame);
                frame.setVisible(false);
            }
        });

        btnGestionePrenotazioni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Registro Prenotazioni Lezioni");
            }
        });

        btnVisualizzaTurni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Calendario Turni di Lavoro Personali");
            }
        });

        btnGestisciTitoli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestioneTitoliIngresso(controller, frame);
                frame.setVisible(false);
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int risposta = JOptionPane.showConfirmDialog(mainPanel, "Sei sicuro di voler uscire?", "Logout", JOptionPane.YES_NO_OPTION);
                if (risposta == JOptionPane.YES_OPTION) {
                    frameChiamante.setVisible(true);
                    frame.dispose();
                }
            }
        });

        frame = new JFrame("Wellness In Cloud - Area Staff");
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
