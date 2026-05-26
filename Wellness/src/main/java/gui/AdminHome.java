package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;
import model.utenti.Admin;

public class AdminHome {
    Controller controller;
    private JPanel mainPanel;
    private JButton btnVisualizzaStaff;
    private JButton btnGestionePrezzario;
    private JButton btnGestioneCorsi;
    private JButton btnLogout;
    private JLabel lblTitoli;

    public JPanel getAdminPanel() {
        return mainPanel;
    }

    public AdminHome(Controller controller, Admin admin) {
            this.controller = controller;
            Admin adminLoggato=admin;


        btnGestioneCorsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Pianificazione Corsi e Lezioni");
            }
        });

        btnGestionePrezzario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Gestione Prezziario(Abbonamenti e Ingressi");
            }
        });

        btnVisualizzaStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Gestone Staff e Assegnazione Turni");
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int risposta = JOptionPane.showConfirmDialog(mainPanel, "sei sicuro di voler uscire?", "Logout", JOptionPane.YES_NO_OPTION);

                if (risposta==JOptionPane.YES_OPTION){
                    JOptionPane.showMessageDialog(mainPanel, "Logouteffettuato. Torno al Login.");
                    System.exit(0);
                }
            }
        });
    }
    }