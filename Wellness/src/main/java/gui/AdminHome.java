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
    private JButton btnLogout;

    public JFrame frame;
    private JFrame frameChiamante;

    public AdminHome(Controller controller, Admin admin, JFrame frameChiamante) {
        this.frameChiamante = frameChiamante;

        btnGestionePrezzario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Gestione Prezzario!");
            }
        });
        bntGestioneCorsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Gestione Corsi!");
            }
        });
        btnVisualizzaStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Gestione Staff!");
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
