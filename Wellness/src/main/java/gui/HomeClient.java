package gui;

import controller.Controller;
import model.utenti.Cliente;

import javax.swing.*;
import java.awt.*;

public class HomeClient {

    private JPanel panel1;

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

        frame = new JFrame("Wellness In Cloud - Area Cliente");
        frame.setSize(500, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblWelcome = new JLabel("Benvenuto, " + clienteLoggato.getNome() + " " + clienteLoggato.getCognome(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblWelcome, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 8, 8));
        infoPanel.setBorder(BorderFactory.createTitledBorder("I Tuoi Dati"));

        infoPanel.add(new JLabel("  Codice Fiscale: " + clienteLoggato.getCodiceFiscale()));
        infoPanel.add(new JLabel("  Email: " + clienteLoggato.getEmail()));
        infoPanel.add(new JLabel("  Telefono: " + clienteLoggato.getTelefono()));
        infoPanel.add(new JLabel("  Indirizzo: " + clienteLoggato.getIndirizzo() + " n." + clienteLoggato.getNumeroCivico() + " (CAP: " + clienteLoggato.getCap() + ")"));

        JLabel lblStato = new JLabel("  Stato Account: " + clienteLoggato.getStatoAcc());
        lblStato.setFont(new Font("Arial", Font.BOLD, 12));
        lblStato.setForeground(clienteLoggato.getStatoAcc() == model.enums.StatoAccount.ATTIVO ? new Color(34, 139, 34) : Color.RED);
        infoPanel.add(lblStato);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Disconnetti");
        btnLogout.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.dispose();
        });
        mainPanel.add(btnLogout, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
