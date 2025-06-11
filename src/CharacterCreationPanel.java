import javax.swing.*;
import java.awt.*;

public class CharacterCreationPanel extends JPanel {
    private JTextField nameField;
    private JComboBox<String> levelBox;
    private JLabel warningLabel;
    private MainUI mainUI;

    private Image backgroundImage; // ✅ 배경 이미지 변수 추가

    public CharacterCreationPanel(MainUI mainUI) {
        this.mainUI = mainUI;
        // 이미지 로드 (이미지 경로는 "images/Creatchbg.png" 기준)
        backgroundImage = new ImageIcon("images/Creatchbg.png").getImage();
        initUI();
    }

    private void initUI() {
        setLayout(null);

        nameField = new JTextField();
        nameField.setBounds(183, 280, 200, 30);
        add(nameField);

        levelBox = new JComboBox<>(new String[]{"1레벨 (6분)", "2레벨 (8분)", "3레벨 (10분)", "4레벨 (12분)", "5레벨 (15분)"});
        levelBox.setBounds(183, 450, 200, 30);
        add(levelBox);

        warningLabel = new JLabel("");
        warningLabel.setForeground(Color.RED);
        warningLabel.setBounds(183, 240, 300, 30);
        add(warningLabel);

        JButton createButton = new JButton("캐릭터 생성");
        createButton.setBounds(183, 600, 200, 40);
        add(createButton);

        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                AlertMessage.show(this, "이름 입력 오류", "이름을 입력해주세요!");
                return;
            }
            String selected = (String) levelBox.getSelectedItem();
            int passion = Integer.parseInt(selected.split("레벨")[0].trim());// 열정레벨 읽기

            Student newStudent = new Student(name);
            newStudent.setLevel(1); // 기본 시작 레벨 1로 고정 (선택사항)
            newStudent.setPassionLevel(passion); // ✅ 여기 추가!

            SaveManager.saveStudent(newStudent);
            mainUI.setStudent(newStudent);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
