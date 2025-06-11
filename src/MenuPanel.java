import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MenuPanel extends JPanel {
    private Image backgroundImage;

    public MenuPanel(MainUI mainUI) {


        File file = new File("images/MenuBackground.png");

        backgroundImage = new ImageIcon(file.getPath()).getImage();


        setLayout(null);

        CustomButton startButton = new CustomButton("공부하기!");
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        startButton.setBounds(200, 400, 200, 60);
        add(startButton);

        CustomButton exitButton = new CustomButton("이제 쉬자...");
        exitButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        exitButton.setBounds(200, 480, 200, 60);
        add(exitButton);

        CustomButton resetButton = new CustomButton("플레이어 초기화");
        resetButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        resetButton.setBounds(200, 560, 200, 50);
        add(resetButton);

        resetButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "정말 초기화할까요?\n저장된 캐릭터와 자괴감 수치가 삭제됩니다.",
                    "초기화 확인", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                SaveManager.resetAllData();
                JOptionPane.showMessageDialog(this, "초기화가 완료되었습니다!");
                mainUI.resetToMenu();
            }
        });

        startButton.addActionListener(e -> {
            System.out.println("startButton 눌림");
            if (SaveManager.hasSavedStudent()) {
                Student loaded = SaveManager.loadStudent();
                System.out.println("불러온 캐릭터: " + (loaded != null ? loaded.getName() : "null"));
                if (loaded != null) {
                    mainUI.setStudent(loaded);
                } else {
                    JOptionPane.showMessageDialog(this, "저장된 캐릭터를 불러올 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("저장된 캐릭터 없음 → 캐릭터 생성 화면으로 이동");
                mainUI.showScreen("create");
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
