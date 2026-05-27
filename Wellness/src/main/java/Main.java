import gui.LoginFrame;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame=new JFrame("Login");
        LoginFrame loginFrame=new LoginFrame();

        frame.setContentPane(loginFrame.getLoginPanel());
        frame.setSize(400, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
