import gui.LoginFrame;
import controller.Controller;

public class Main
{
    public static void main(String[] args)
    {
        new LoginFrame(new Controller());
    }
}