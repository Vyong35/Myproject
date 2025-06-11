import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentGameUI {
    private static int level = 1;
    private static int exp = 0;
    private static int expToNext = 100;
    private static int totalMinutes = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame("대학생 키우기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 1600);

        JPanel panel = new JPanel();
        panel.setLayout(null); // 수동 배치

        JLabel titleLabel = new JLabel("🎓 대학생 키우기");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));
        titleLabel.setBounds(400, 30, 500, 60);
        panel.add(titleLabel);

        JLabel nameLabel = new JLabel("캐릭터 이름:");
        nameLabel.setBounds(100, 150, 200, 30);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(300, 150, 400, 30);
        panel.add(nameField);

        JLabel timeLabel = new JLabel("공부할 시간 (분):");
        timeLabel.setBounds(100, 220, 200, 30);
        timeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        panel.add(timeLabel);

        JTextField studyInput = new JTextField();
        studyInput.setBounds(300, 220, 400, 30);
        panel.add(studyInput);

        JButton studyButton = new JButton("📚 공부 시작");
        studyButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        studyButton.setBounds(750, 220, 200, 40);
        panel.add(studyButton);

        JLabel statusLabel = new JLabel("상태: 레벨 1, 경험치 0/100, 누적 공부 0분");
        statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 22));
        statusLabel.setBounds(100, 300, 1000, 40);
        panel.add(statusLabel);

        JLabel messageLabel = new JLabel("");
        messageLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        messageLabel.setBounds(100, 370, 1000, 40);
        panel.add(messageLabel);

        studyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String input = studyInput.getText().trim();
                if (name.isEmpty() || input.isEmpty()) {
                    messageLabel.setText("이름과 시간을 입력해주세요.");
                    return;
                }
                try {
                    int minutes = Integer.parseInt(input);
                    int gainedExp = minutes * 2;
                    exp += gainedExp;
                    totalMinutes += minutes;
                    StringBuilder levelUpMsg = new StringBuilder();

                    while (exp >= expToNext) {
                        exp -= expToNext;
                        level++;
                        expToNext += 50;
                        levelUpMsg.append(" 🎉 레벨 업! 현재 레벨: ").append(level);
                    }

                    statusLabel.setText(String.format("상태: 레벨 %d, 경험치 %d/%d, 누적 공부 %d분",
                            level, exp, expToNext, totalMinutes));
                    messageLabel.setText(name + "이(가) 공부 완료! " + gainedExp + " 경험치 획득." + levelUpMsg);
                } catch (NumberFormatException ex) {
                    messageLabel.setText("숫자를 정확히 입력해주세요.");
                }
            }
        });

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
