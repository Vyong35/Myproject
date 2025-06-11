import javax.swing.SwingUtilities;
import java.io.File;


public class Main {
    public static void main(String[] args) {


        System.setProperty("sun.java2d.uiScale", "1.0"); // 💡 스케일 강제 고정
        SwingUtilities.invokeLater(() -> {
            MainUI ui = new MainUI();
            ui.setVisible(true);
        });
    }
}

