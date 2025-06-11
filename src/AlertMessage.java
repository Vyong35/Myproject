import javax.swing.*;
import java.awt.*;


public class AlertMessage {
    public static void show(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }
}

