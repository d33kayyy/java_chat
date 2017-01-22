package ClientSide;


import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new ClientGUI();
    }
}
