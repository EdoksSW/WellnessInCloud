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

    public AdminHome(Controller controller, Admin admin) {

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
                int risposta= JOptionPane.showConfirmDialog(mainPanel,
                        "Sei sicuro di voler uscire?", "Conferma Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(risposta==JOptionPane.YES_OPTION)
                {
                }
                //Recuepro i JFrame principale di questo pannello
                JFrame framePrincipale=(JFrame) SwingUtilities.getWindowAncestor(mainPanel);

                //Istanzio la schermata di Login
                LoginFrame login=new LoginFrame(controller);

                //Ora sostituiamo il panel di Admin con quello di Login
                framePrincipale.setContentPane(login.getLoginPanel());

                //scuotiamo la memoria grafica e ridisegnamo il login
                framePrincipale.validate();
                framePrincipale.repaint();

                JOptionPane.showMessageDialog(null,"Loout effettuato con successo. Arrivederci!");
            }
        });
    }

    public JPanel getAdminPanel() {
        return mainPanel;
    }


}
