package gui;

import controller.Controller;
import model.utenti.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// 1. Rimosso "extends JFrame"
public class AdminHome {
    private Controller controller;
    private Utente utenteLoggato;

    // --- VARIABILI COLLEGATE AL FILE .FORM ---
    private JPanel mainPanel;
    private JButton btnRegistraCliente;
    private JLabel lwlWelcome; // 2. Corretto il nome per farlo coincidere con il tuo screenshot

    public AdminHome(Controller controller, Utente utenteLoggato) {
        this.controller = controller;
        this.utenteLoggato = utenteLoggato;

        // 3. Modifica dinamicamente il testo della label usando il nome corretto
        lwlWelcome.setText("Operatore: " + utenteLoggato.getNome());

        // Aggiunge la logica al bottone
        btnRegistraCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField txtCf = new JTextField();
                JTextField txtNome = new JTextField();
                JTextField txtCognome = new JTextField();
                JTextField txtEmail = new JTextField();
                JTextField txtTelefono = new JTextField();
                JTextField txtDataNascita = new JTextField();
                JPasswordField txtPassword = new JPasswordField();
                JTextField txtIndirizzo = new JTextField();
                JTextField txtNumCivico = new JTextField();
                JTextField txtCap = new JTextField();

                JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
                panel.add(new JLabel("Codice Fiscale:")); panel.add(txtCf);
                panel.add(new JLabel("Nome:")); panel.add(txtNome);
                panel.add(new JLabel("Cognome:")); panel.add(txtCognome);
                panel.add(new JLabel("Email:")); panel.add(txtEmail);
                panel.add(new JLabel("Telefono:")); panel.add(txtTelefono);
                panel.add(new JLabel("Data Nascita (AAAA-MM-GG):")); panel.add(txtDataNascita);
                panel.add(new JLabel("Password:")); panel.add(txtPassword);
                panel.add(new JLabel("Indirizzo:")); panel.add(txtIndirizzo);
                panel.add(new JLabel("Num Civico:")); panel.add(txtNumCivico);
                panel.add(new JLabel("CAP:")); panel.add(txtCap);

                // 4. Cambiato AdminHome.this in mainPanel
                int result = JOptionPane.showConfirmDialog(mainPanel, panel,
                        "Registrazione Nuovo Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String cf = txtCf.getText().trim();
                        String nome = txtNome.getText().trim();
                        String cognome = txtCognome.getText().trim();
                        String email = txtEmail.getText().trim();
                        String telefono = txtTelefono.getText().trim();
                        String password = new String(txtPassword.getPassword());
                        String indirizzo = txtIndirizzo.getText().trim();
                        String cap = txtCap.getText().trim();

                        LocalDate dataNascita = LocalDate.parse(txtDataNascita.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        int numCivico = Integer.parseInt(txtNumCivico.getText().trim());

                        boolean esito = controller.registraCliente(
                                utenteLoggato, cf, nome, cognome, email, telefono,
                                dataNascita, password, indirizzo, numCivico, cap
                        );

                        if(esito) {
                            JOptionPane.showMessageDialog(mainPanel, "Cliente registrato con successo!");
                        } else {
                            JOptionPane.showMessageDialog(mainPanel, "Errore: dati non validi o permessi insufficienti.", "Errore", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Formato data errato. Usa AAAA-MM-GG.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Il numero civico deve essere un numero intero.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    // 5. ECCO IL METODO MANCANTE CHE RISOLVE L'ERRORE
    public JPanel getAdminPanel() {
        return mainPanel;
    }
}