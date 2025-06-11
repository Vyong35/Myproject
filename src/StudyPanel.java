import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudyPanel extends JPanel {
    private MainUI mainUI;
    private Student student;
    private DailyGoalManager goalManager;
    private JLabel statusLabel;
    private JLabel goalLabel;
    private JLabel timerLabel;
    private Timer timer;
    private int remainingSeconds;
    private boolean isRunning;
    private int dailyGoalMinutes; // ✅ 열정 기반 목표 공부 시간

    private Image backgroundImage; // 배경 이미지

    public StudyPanel(MainUI mainUI, Student student) {
        this.mainUI = mainUI;
        this.student = student;
        this.goalManager = new DailyGoalManager(student);
        this.dailyGoalMinutes = goalManager.getDailyGoalMinutes(); // ✅ 열정에 따라 설정
        this.remainingSeconds = dailyGoalMinutes * 60;

        backgroundImage = new ImageIcon("images/Studybg.png").getImage();
        initUI();
    }

    private void initUI() {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("공부 중 . . .");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setBounds(30, 30, 300, 40);
        add(titleLabel);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        statusLabel.setBounds(280, 30, 300, 30);
        add(statusLabel);

        goalLabel = new JLabel("오늘의 목표: " + dailyGoalMinutes + "분"); // ✅ 변경됨
        goalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        goalLabel.setBounds(30, 100, 300, 30);
        add(goalLabel);

        timerLabel = new JLabel(String.format("남은 시간: %d분 0초", remainingSeconds / 60));
        timerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        timerLabel.setBounds(190, 250, 300, 40);
        add(timerLabel);

        JButton startButton = new JButton("시작");
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        startButton.setBounds(160, 350, 100, 40);
        startButton.setFocusable(false);
        add(startButton);

        JButton pauseButton = new JButton("중지");
        pauseButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        pauseButton.setBounds(320, 350, 100, 40);
        pauseButton.setFocusable(false);
        add(pauseButton);

        JButton exitButton = new JButton("나가기");
        exitButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        exitButton.setBounds(420, 700, 120, 50);
        exitButton.setFocusable(false);
        add(exitButton);

        // 타이머
        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingSeconds > 0) {
                    remainingSeconds--;
                    int minutes = remainingSeconds / 60;
                    int seconds = remainingSeconds % 60;
                    timerLabel.setText(String.format("남은 시간: %d분 %d초", minutes, seconds));
                } else {
                    timer.stop();
                    isRunning = false;

                    JOptionPane.showMessageDialog(StudyPanel.this, "공부 완료! 🎉 경험치를 획득합니다.");

                    int currentIndex = (mainUI.getSemesterManager().getCurrentSemester() - 1) * 7
                            + (mainUI.getDateManager().getDayInCurrentSemester() - 1);

                    student.study(dailyGoalMinutes, currentIndex);
                    goalManager.addStudyMinutes(dailyGoalMinutes);
                    CoinManager.getInstance().addCoins(3);

                    for (Task task : ProfessorManager.getInstance().getCurrentTasks()) {
                        if (task.getTitle().contains("일 이상 공부하기")) {
                            task.recordStudy(1);
                        }
                    }

                    SaveManager.saveStudent(student);
                    updateStatus();

                    if (mainUI.getHomePanel() != null) {
                        mainUI.getHomePanel().updateStatus();
                        mainUI.showScreen("home");
                    }
                }
            }
        });

        startButton.addActionListener(e -> {
            if (!isRunning && remainingSeconds > 0) {
                timer.start();
                isRunning = true;
            }
        });

        pauseButton.addActionListener(e -> {
            if (isRunning) {
                timer.stop();
                isRunning = false;
            }
        });

        exitButton.addActionListener(e -> {
            if (isRunning) timer.stop();
            isRunning = false;

            boolean goalMet = remainingSeconds <= 0;
            if (!goalMet) {
                AlertMessage.show(this, "공부 중도 포기!!", "자괴감 수치가 증가했습니다.");
                MentalManager.getInstance().increaseGuilt(5);
                SaveManager.saveMentalManager(MentalManager.getInstance());
            }

            if (mainUI.getHomePanel() != null) {
                mainUI.getHomePanel().updateStatus();
            }
            mainUI.showScreen("home");
        });

        updateStatus();
    }

    private void updateStatus() {
        if (statusLabel != null && student != null) {
            statusLabel.setText(String.format("레벨 %d | 경험치 %d/%d | 누적 공부 %d분",
                    student.getLevel(),
                    student.getExp(),
                    student.getExpToNext(),
                    student.getTotalStudyMinutes()));
        }
    }

    public void resetTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        isRunning = false;
        remainingSeconds = dailyGoalMinutes * 60;
        timerLabel.setText(String.format("남은 시간: %d분 0초", remainingSeconds / 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
