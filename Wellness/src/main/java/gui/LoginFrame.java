package gui;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;
import gui.AdminHome.AdminHome;
import model.enums.StatoAccount;
import model.utenti.Admin;
import model.utenti.Cliente;
import model.utenti.Staff;
import model.utenti.Utente;

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
                Utente utenteLoggato=controller.login(Email, Password);
                if(utenteLoggato != null)
                {
                    // Recuperiamo il JFrame principale che contiene il pannello di login attuale
                    JFrame framePrincipale=(JFrame) SwingUtilities.getWindowAncestor(loginPanel);

                    //Controllo del tipo di Utente
                    if(utenteLoggato instanceof Admin)
                    {
                        Admin admin = (Admin) utenteLoggato;
                        JOptionPane.showMessageDialog(loginPanel, "Accesso eseguito come Amministratore" + admin.getNome());

                        //Istanziamo la GUI specifica dell'Admin e la carichiamo nel Frame
                        AdminHome adminHome=new AdminHome(controller, admin);
                        framePrincipale.setContentPane(adminHome.getAdminPanel());
                    } else if (utenteLoggato instanceof Staff)
                    {
                        Staff staff=(Staff) utenteLoggato;
                        JOptionPane.showMessageDialog(loginPanel, "Accesso eseguito come Staff"+ staff.getNome());

                        //Istanziamo la GUI specifica dello Stff
                        StaffHome staffHome=new StaffHome(controller, staff);
                        framePrincipale.setContentPane(staffHome.getStaffPanel());
                    } else if (utenteLoggato instanceof Cliente)
                    {
                        Cliente cliente=(Cliente) utenteLoggato;

                        //Se l'account è bloccato o in revisione, non deve entrare!
                        if(cliente.getStatoAcc() == StatoAccount.BLOCCATO)
                        {
                            JOptionPane.showMessageDialog(loginPanel,"Il tuo account è bloccato. Contatta la segreteria.", "Accesso Negato", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            }
        });
    }

}
