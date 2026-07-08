package gui;

import controller.Controller;
import model.enums.StatoAccount;
import model.utenti.Cliente;

import javax.swing.*;
import java.awt.*;

public class HomeClient {

    private JPanel panel1;
    private JLabel lblWelcome;
    private JLabel lblCodiceFiscale;
    private JLabel lblEmail;
    private JLabel lblTelefono;
    private JLabel lblIndirizzo;
    private JLabel lblStato;
    private JButton btnLogout;
    private JButton txtShop;
    private JButton txtCarrello;
    private JButton txtOrdini;
    private JButton txtPrenotazioni;

    private Controller controller;
    private Cliente clienteLoggato;

    public JFrame frame;
    private JFrame frameChiamante;

    public JPanel gethomePanel() {
        return panel1;
    }

    public HomeClient(Controller controller, Cliente clienteLoggato, JFrame frameChiamante) {
        this.controller = controller;
        this.clienteLoggato = clienteLoggato;
        this.frameChiamante = frameChiamante;

        lblWelcome.setText("Benvenuto, " + clienteLoggato.getNome() + " " + clienteLoggato.getCognome());
        lblCodiceFiscale.setText("Codice Fiscale: " + clienteLoggato.getCodiceFiscale());
        lblEmail.setText("Email: " + clienteLoggato.getEmail());
        lblTelefono.setText("Telefono: " + clienteLoggato.getTelefono());
        lblIndirizzo.setText("Indirizzo: " + clienteLoggato.getVia() + " n." + clienteLoggato.getCivico() + " (CAP: " + clienteLoggato.getCap() + ")");
        lblStato.setText("Stato Account: " + clienteLoggato.getStatoAcc());
        lblStato.setForeground(clienteLoggato.getStatoAcc() == StatoAccount.ATTIVO ? new Color(34, 139, 34) : Color.RED);

        btnLogout.addActionListener(e -> {
            if (frameChiamante != null) {
                frameChiamante.setVisible(true);
            } else {
                new LoginFrame(controller);
            }
            frame.dispose();
        });

        txtOrdini.addActionListener(e -> {
            frame.setVisible(false);
            new OrdiniClient(controller, clienteLoggato, frame);
        });

        txtCarrello.addActionListener(e -> {
            frame.setVisible(false);
            new CarrelloClient(controller, clienteLoggato, frame);
        });

        txtShop.addActionListener(e -> {
            frame.setVisible(false);
            new ShopClient(controller, clienteLoggato, frame);
        });


        txtPrenotazioni.addActionListener(e -> {
            String messaggioErrore = controller.verificaPermessoPrenotazione(clienteLoggato);

            if (messaggioErrore != null) {
                JOptionPane.showMessageDialog(panel1, messaggioErrore, "Accesso Negato", JOptionPane.WARNING_MESSAGE);
            } else {
                frame.setVisible(false);
                new PrenotazioniClient(controller, clienteLoggato, frame);
            }
        });

        frame = new JFrame("Wellness In Cloud - Area Cliente");
        frame.setContentPane(panel1);
        frame.setSize(500, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}