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
                            return; //Interrompe l'esecuzione e non lo fa entrare nella home
                        }
                        else if(cliente.getStatoAcc() == StatoAccount.IN_REVISIONE)
                        {
                            JOptionPane.showMessageDialog(loginPanel, "Il tuo account attualmente è in revisione. Contatta la segreteria", "Account in revizione", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        JOptionPane.showMessageDialog(loginPanel,"Benvenuto"+ cliente.getNome()+"!");
                        HomeClient homeClient=new HomeClient(controller, cliente);
                        framePrincipale.setContentPane(homeClient.gethomePanel());
                    }

                    // AGGIORNAMENTO DEL FRAME
                    // Istruzioni obbligatorrie per dire a Swing di distruggere la vecchia grafica del login e disegnare la Home corretta
                    framePrincipale.validate();
                    framePrincipale.repaint();
                }
                else
                {
                    //Se il controller restituiscire null, le credenziali sono errate
                    JOptionPane.showMessageDialog(loginPanel, "Email o Password errati!", "Errore di Autenticazione", JOptionPane.ERROR_MESSAGE);
                    txtPassword.setText("");
                }
            }
        });
    }

}
