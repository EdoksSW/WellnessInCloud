package gui;

import controller.Controller;
import model.utenti.Staff;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffHome {
    private JPanel mainPanel;
    private JButton btnVerificaCertificati;
    private JButton btnGestionePrenotazioni;
    private JButton btnVisualizzaTurni;
    private JButton btnLogout;
    private JLabel lblTitolo;

    public JPanel getStaffPanel() {
        return mainPanel;
    }

    public StaffHome(Controller controller, Staff staff) {

        btnVerificaCertificati.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: Controllo Certificati Medici e Varco Accessi");
            }
        });

        btnGestionePrenotazioni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Apertura modulo: ");
            }
        });




    }
}
