import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.awt.event.*;
import javax.swing.Timer;

public class HomePanel extends JPanel {
    private JLabel statusLabel;
    private JLabel semesterLabel;
    private JLabel characterLabel;
    private JLabel coinLabel;

    private Image backgroundImage; //이미지 변수 추가

    private Student student;
    private MainUI mainUI;

    private JLabel guiltLabel;
    private JProgressBar guiltBar;

    private int currentCharacterStage = 0; // 0: 기본, 1: 후드티, 2: 셔츠, 3: 학사모

    public void setStudent(Student student) {
        this.student = student;
        updateStatus();
    }

    public HomePanel(MainUI mainUI, Student student) {
        this.mainUI = mainUI;
        this.student = student;

        backgroundImage = new ImageIcon("images/Homebg.png").getImage();
        setLayout(null);
        setBackground(new Color(245, 245, 245));


        statusLabel = new JLabel();
        statusLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        statusLabel.setBounds(20, 20, 560, 30);
        add(statusLabel);

        semesterLabel = new JLabel();
        semesterLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        semesterLabel.setBounds(20, 60, 400, 30);
        semesterLabel.setOpaque(true);
        semesterLabel.setBackground(new Color(255, 255, 255, 200));
        add(semesterLabel);

        guiltLabel = new JLabel("자괴감 수치:");
        guiltLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        guiltLabel.setBounds(20, 100, 150, 30);
        guiltLabel.setOpaque(true);
        guiltLabel.setBackground(new Color(255, 255, 255, 200));
        add(guiltLabel);

        guiltBar = new JProgressBar(0, 100);
        guiltBar.setForeground(Color.RED);
        guiltBar.setBounds(170, 100, 200, 30);
        add(guiltBar);

        coinLabel = new JLabel("코인 보유량: " + CoinManager.getInstance().getCoins());
        coinLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        coinLabel.setBounds(20, 140, 200, 30);
        coinLabel.setOpaque(true);
        coinLabel.setBackground(new Color(255, 255, 255, 200));
        add(coinLabel);

        characterLabel = new JLabel();
        characterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        characterLabel.setBounds(200, 400, 200, 200);

         // ✅ 이미지 아이콘 적용
        ImageIcon icon = new ImageIcon("images/lv1student.png");
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        characterLabel.setIcon(new ImageIcon(scaledImage));

        add(characterLabel);


        JButton studyButton = new JButton("공부하기");
        studyButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        studyButton.setBounds(80, 600, 120, 50);
        studyButton.addActionListener(e -> mainUI.showScreen("study"));
        add(studyButton);

        JButton calendarButton = new JButton("캘린더");
        calendarButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        calendarButton.setBounds(220, 600, 120, 50);
        calendarButton.addActionListener(e -> {
            VirtualDateManager dateManager = mainUI.getDateManager();
            List<Boolean> attendance = student.getAttendanceRecord();
            List<Integer> studyMinutes = student.getDailyStudyMinutes();

            CalendarPanel calendarPanel = new CalendarPanel(dateManager, attendance, studyMinutes);

            JOptionPane.showMessageDialog(this,
                    calendarPanel,
                    " 출석 달력 - " + mainUI.getSemesterManager().getCurrentSemester() + "학기",
                    JOptionPane.PLAIN_MESSAGE);
        });
        add(calendarButton);

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        backButton.setBounds(500, 10, 60, 40);
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        backButton.addActionListener(e -> mainUI.showScreen("menu"));
        add(backButton);

        // 하루 넘기기 버튼 이벤트
        JButton nextDayButton = new JButton("하루 넘기기");
        nextDayButton.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        nextDayButton.setBounds(400, 200, 160, 40);
        nextDayButton.addActionListener(e -> {
            VirtualDateManager dateManager = mainUI.getDateManager();
            SemesterManager semesterManager = mainUI.getSemesterManager();
            ProfessorManager professorManager = mainUI.getProfessorManager();

            int dayBefore = dateManager.getDayInCurrentSemester();
            dateManager.nextDay();
            SaveManager.saveDate(dateManager);
            student.markAttendance();
            student.recordZeroStudyIfNeeded();
            int dayAfter = dateManager.getDayInCurrentSemester();

            mainUI.getStudyPanel().resetTimer();

            if (dayBefore == 7 && dayAfter == 8) {
                semesterManager.nextSemester();
                dateManager.setSemesterStartDate(dateManager.getCurrentDate());
                SaveManager.saveSemester(semesterManager);
                SaveManager.saveDate(dateManager);
                professorManager.assignNewTasks(semesterManager.getCurrentSemester());
                SaveManager.saveProfessor(professorManager);

                student.resetAttendance();

                if (semesterManager.isGraduated()) {
                    JOptionPane.showMessageDialog(this,
                            "🎓 졸업을 축하합니다!\n이제 당신 차례입니다.",
                            "졸업", JOptionPane.INFORMATION_MESSAGE);
                    MentalManager.getInstance().resetGuilt();
                    SaveManager.deleteAll();
                    mainUI.resetToMenu();
                    return;
                }

                JOptionPane.showMessageDialog(this,
                        semesterManager.getCurrentSemester() + "학기가 시작되었습니다!\n새로운 과제가 부여됐습니다!",
                        "새 학기", JOptionPane.INFORMATION_MESSAGE);
            }

            updateStatus();
        });
        add(nextDayButton);

        JButton nextSemesterButton = new JButton("다음 학기로!");
        nextSemesterButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        nextSemesterButton.setBounds(400, 100, 160, 40);
        nextSemesterButton.addActionListener(e -> {
            SemesterManager semesterManager = mainUI.getSemesterManager();
            ProfessorManager professorManager = mainUI.getProfessorManager();

            semesterManager.nextSemester();

            if (semesterManager.isGraduated()) {
                JOptionPane.showMessageDialog(this,
                        "🎓 졸업을 축하합니다!\n이제 당신 차례입니다.",
                        "졸업", JOptionPane.INFORMATION_MESSAGE);
                MentalManager.getInstance().resetGuilt();
                SaveManager.deleteAll();
                mainUI.resetToMenu();
            } else {
                professorManager.assignNewTasks(semesterManager.getCurrentSemester());
                SaveManager.saveSemester(semesterManager);
                SaveManager.saveProfessor(professorManager);
                JOptionPane.showMessageDialog(this,
                        semesterManager.getCurrentSemester() + "학기가 시작되었습니다!\n새로운 과제가 부여됐습니다!",
                        "새 학기", JOptionPane.INFORMATION_MESSAGE);
                mainUI.getHomePanel().updateStatus();
            }
        });
        add(nextSemesterButton);

        JButton restButton = new JButton("쉬는 시간");
        restButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        restButton.setBounds(360, 600, 140, 50);
        restButton.addActionListener(e -> {
            int currentCoins = CoinManager.getInstance().getCoins();
            int result = JOptionPane.showConfirmDialog(this,
                    "코인 1개를 사용해서 쉬는 시간을 시작하시겠습니까?\n(보유 코인: " + currentCoins + "개)",
                    "쉬는 시간", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                if (CoinManager.getInstance().useCoin(1)) {
                    updateStatus();
                    new RestGameWindow();
                } else {
                    JOptionPane.showMessageDialog(this, "코인이 부족합니다!", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add(restButton);

        JButton levelUpButton = new JButton("레벨업++");
        levelUpButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        levelUpButton.setBounds(100, 680, 160, 50);
        levelUpButton.addActionListener(e -> {
            student.setLevel(student.getLevel() + 1);
            student.setExp(0);
            student.setExpToNextLevel(student.getExpToNextLevel() + 50);
            JOptionPane.showMessageDialog(this, "레벨이 강제로 1 증가했습니다!", "레벨업", JOptionPane.INFORMATION_MESSAGE);
            updateStatus();
        });
        add(levelUpButton);

        JButton guiltUpButton = new JButton("자괴감++");
        guiltUpButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        guiltUpButton.setBounds(280, 680, 160, 50);
        guiltUpButton.addActionListener(e -> {
            MentalManager.getInstance().increaseGuilt(10);
            updateStatus(); // 상태바에 반영
        });
        add(guiltUpButton);



        JButton taskButton = new JButton("과제 보기");
        taskButton.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        taskButton.setBounds(400, 150, 160, 40);
        taskButton.addActionListener(e -> {
            ProfessorManager pm = mainUI.getProfessorManager();
            List<Task> tasks = pm.getCurrentTasks();

            if (tasks.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "현재 과제가 없습니다.",
                        "과제 없음", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder sb = new StringBuilder();
                for (Task t : tasks) {
                    sb.append("📝 ").append(t.getTitle()).append(" - ").append(t.getDescription());
                    if (t.isCompleted()) {
                        sb.append(" ✅ 완료\n\n");
                    } else {
                        sb.append("\n\n");
                    }
                }
                JOptionPane.showMessageDialog(this,
                        sb.toString(), "📚 현재 과제", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(taskButton);

        updateStatus();
    }

    public void updateStatus() {
        Student s = mainUI.getStudent();
        if (s != null) {
            String name = s.getName();
            int level = s.getLevel();
            int exp = s.getExp();
            int expToNext = s.getExpToNext();
            int totalStudyMinutes = s.getTotalStudyMinutes();

            statusLabel.setText(String.format("%s | 레벨 %d | 경험치 %d/%d | 누적 공부 %d분",
                    name, level, exp, expToNext, totalStudyMinutes));

            if (guiltBar != null) {
                int guilt = MentalManager.getInstance().getGuilt();
                guiltBar.setValue(guilt);
            }

            if (coinLabel != null) {
                coinLabel.setText("코인 보유량: " + CoinManager.getInstance().getCoins());
            }

            SemesterManager sm = mainUI.getSemesterManager();
            VirtualDateManager dm = mainUI.getDateManager();
            if (sm != null && semesterLabel != null && dm != null) {
                semesterLabel.setText("현재: " + sm.getCurrentSemester() + "학기 " + dm.getDayInCurrentSemester() + "일차");
            }

            int newStage = getCharacterStage(level);
            if (newStage > currentCharacterStage) {
                playCharacterChangeAnimation(newStage);
            }
        }
    }

    private int getCharacterStage(int level) {
        if (level >= 15) return 3;
        if (level >= 10) return 2;
        if (level >= 5) return 1;
        return 0;
    }

    private void playCharacterChangeAnimation(int newStage) {
        JOptionPane.showMessageDialog(this, "어라? 캐릭터의 상태가?");

        Timer shakeTimer = new Timer(50, null);
        int originalX = characterLabel.getX();
        final int[] count = {0};

        shakeTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int offset = (count[0] % 2 == 0) ? 10 : -10;
                characterLabel.setLocation(originalX + offset, characterLabel.getY());
                count[0]++;
                if (count[0] >= 20) {
                    shakeTimer.stop();
                    characterLabel.setLocation(originalX, characterLabel.getY());
                    fadeOutCharacter(newStage);
                }
            }
        });

        shakeTimer.start();
    }

    private void fadeOutCharacter(int newStage) {
        Timer fadeTimer = new Timer(100, null);
        final float[] alpha = {1.0f};

        fadeTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] -= 0.1f;
                characterLabel.setForeground(new Color(0, 0, 0, Math.max(0, (int) (255 * alpha[0]))));
                if (alpha[0] <= 0) {
                    fadeTimer.stop();
                    updateCharacterAppearance(newStage);
                }
            }
        });

        fadeTimer.start();
    }

    private void updateCharacterAppearance(int stage) {
        currentCharacterStage = stage;

        String imagePath;
        switch (stage) {
            case 1 -> imagePath = "images/lv5student.png";
            case 2 -> imagePath = "images/lv10student.png";
            case 3 -> imagePath = "images/lv15student.png";
            default -> imagePath = "images/lv1student.png";
        }

        // ✅ 이미지 스케일링 적용
        ImageIcon rawIcon = new ImageIcon(imagePath);
        Image scaled = rawIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        characterLabel.setIcon(new ImageIcon(scaled));
        characterLabel.setText(null); // 텍스트 제거

        characterLabel.setForeground(Color.BLACK);

        JOptionPane.showMessageDialog(this, "축하합니다! 진화했습니다!");
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
