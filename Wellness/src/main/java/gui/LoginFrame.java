package gui;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;
import model.utenti.Admin;
import model.utenti.Cliente;
import model.utenti.Staff;
import model.utenti.Utente;

public class LoginFrame {
    private JPanel loginPanel;
    private JTextField txtEmail;
    private JTextField txtPassword;
    private JButton accediButton;
    private JLabel titolo;
    private JButton btnRegistrazione;
    public JFrame frame;

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public JButton getBtnRegistrazione() {
        return btnRegistrazione;
    }

    public LoginFrame(Controller pController) {
        Controller controller = pController;

        frame = new JFrame("Login");
        frame.setContentPane(loginPanel);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = txtPassword.getText();
                Utente utenteLoggato = controller.login(email, password);

                if (utenteLoggato == null) {
                    JOptionPane.showMessageDialog(loginPanel, "Email o Password errati!", "Errore di Autenticazione", JOptionPane.ERROR_MESSAGE);
                    txtPassword.setText("");
                    return;
                }

                String messaggioNegato = controller.messaggioAccessoNegato(utenteLoggato);
                if (messaggioNegato != null) {
                    JOptionPane.showMessageDialog(loginPanel, messaggioNegato, "Accesso Negato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (utenteLoggato instanceof Admin) {
                    new AdminHome(controller, (Admin) utenteLoggato, frame);
                } else if (utenteLoggato instanceof Staff) {
                    new StaffHome(controller, (Staff) utenteLoggato, frame);
                } else if (utenteLoggato instanceof Cliente) {
                    new HomeClient(controller, (Cliente) utenteLoggato, frame);
                }

                frame.setVisible(false);
            }
        });
    }

}
