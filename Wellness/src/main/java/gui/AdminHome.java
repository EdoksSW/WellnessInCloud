package gui;

import controller.Controller;
import model.utenti.Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminHome {
    private JPanel mainPanel;
    private JButton bntGestioneCorsi;
    private JButton btnGestionePrezzario;
    private JButton btnVisualizzaStaff;
    private JButton btnGestisciTitoli;
    private JButton btnLogout;

    public JFrame frame;
    private JFrame frameChiamante;

    public AdminHome(Controller controller, Admin admin, JFrame frameChiamante) {
        this.frameChiamante = frameChiamante;

        btnGestionePrezzario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionePrezzario(controller, frame);
                frame.setVisible(false);
            }
        });
        bntGestioneCorsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestioneCorsi(controller, frame);
                frame.setVisible(false);
            }
        });
        btnVisualizzaStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestioneStaff(controller, frame);
                frame.setVisible(false);
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
                int risposta = JOptionPane.showConfirmDialog(mainPanel,
                        "Sei sicuro di voler uscire?", "Conferma Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (risposta == JOptionPane.YES_OPTION) {
                    frameChiamante.setVisible(true);
                    frame.dispose();
                }
            }
        });

        frame = new JFrame("Wellness In Cloud - Area Admin");
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public JPanel getAdminPanel() {
        return mainPanel;
    }


}
