package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;

public class LoginFrame {
    private JPanel loginPanel;
    private JTextField txtEmail;
    private JTextField txtPassword;
    private JButton accediButton;
    private JButton btnRegistrazione;


    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public JButton getBtnRegistrazione() {
        return btnRegistrazione;
    }

    public LoginFrame() {
        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller controller=new Controller();
                String Email=txtEmail.getText();
                String Password=txtPassword.getText();
                controller.login(Email, Password);

            }
        });
    }

}
