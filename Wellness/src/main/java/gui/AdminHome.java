package gui;

import controller.Controller;
import model.utenti.Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminHome {

    private JPanel mainPanel;
    private JLabel lblTitolo;
    private JButton btnGestioneCorsi;
    private JButton btnGestionePrezzario;
    private JButton btnVisualizzaStaff;
    private JButton btnLogout;

    public JPanel getAdminPanel() {
        return mainPanel;
    }

    public AdminHome(Controller controller, Admin admin) {

        btnGestioneCorsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Pianificazione Corsi e Lezioni");
            }
        });

        btnGestionePrezzario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Gestione Prezzario (Abbonamenti e Ingressi)");
            }
        });

        btnVisualizzaStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Gestione Staff e Assegnazione Turni");
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int risposta = JOptionPane.showConfirmDialog(mainPanel,
                        "Sei sicuro di voler uscire dal pannello Amministratore?",
                        "Informativa di Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (risposta == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(mainPanel, "Logout effettuato con successo.");
                    System.exit(0);
                }
            }
        });
    }
}

