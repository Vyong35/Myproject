import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    private static MainUI instance; // ✅ 싱글턴 인스턴스

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Student student;
    private StudyPanel studyPanel;

    private VirtualDateManager dateManager;
    private SemesterManager semesterManager;
    private ProfessorManager professorManager;

    private HomePanel homePanel;

    public MainUI() {
        instance = this; // ✅ 생성자에서 인스턴스 설정

        setTitle("대학생 키우기");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        // 저장된 값 불러오기 및 싱글턴 초기화
        dateManager = VirtualDateManager.loadDate();

        SemesterManager loadedSemester = SaveManager.loadSemester();
        if (loadedSemester != null) {
            SemesterManager.setInstance(loadedSemester);
        }
        semesterManager = SemesterManager.getInstance();

        ProfessorManager loadedProfessor = SaveManager.loadProfessor();
        if (loadedProfessor != null) {
            ProfessorManager.setInstance(loadedProfessor);
        }
        professorManager = ProfessorManager.getInstance();

        MentalManager loadedMental = SaveManager.loadMentalManager();
        if (loadedMental != null) {
            MentalManager.setInstance(loadedMental);
        }

        // 초기 화면 구성
        MenuPanel menuPanel = new MenuPanel(this);
        mainPanel.add(menuPanel, "menu");

        CharacterCreationPanel createPanel = new CharacterCreationPanel(this);
        mainPanel.add(createPanel, "create");

        showScreen("menu");
    }

    // ✅ 싱글턴 접근자
    public static MainUI getInstance() {
        return instance;
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void setStudent(Student student) {
        this.student = student;
        System.out.println("setStudent() 호출됨, 이름: " + student.getName());

        if (homePanel != null) {
            mainPanel.remove(homePanel);
            homePanel = null;
            System.out.println("기존 homePanel 제거됨");
        }

        if (studyPanel != null) {
            mainPanel.remove(studyPanel);
            studyPanel = null;
            System.out.println("기존 studyPanel 제거됨");
        }

        homePanel = new HomePanel(this, student);
        mainPanel.add(homePanel, "home");
        System.out.println("homePanel 추가됨");

        studyPanel = new StudyPanel(this, student);
        mainPanel.add(studyPanel, "study");
        System.out.println("studyPanel 추가됨");

        showScreen("home");
    }

    public Student getStudent() {
        return student;
    }

    public StudyPanel getStudyPanel() {
        return studyPanel;
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }

    public VirtualDateManager getDateManager() {
        return dateManager;
    }

    public SemesterManager getSemesterManager() {
        return semesterManager;
    }

    public ProfessorManager getProfessorManager() {
        return professorManager;
    }

    public void resetToMenu() {
        student = null;

        if (homePanel != null) {
            mainPanel.remove(homePanel);
            homePanel = null;
        }

        if (studyPanel != null) {
            mainPanel.remove(studyPanel);
            studyPanel = null;
        }

        SaveManager.deleteAll();

        SemesterManager.reset();
        semesterManager = SemesterManager.getInstance();

        VirtualDateManager.reset();
        dateManager = VirtualDateManager.getInstance();

        ProfessorManager.reset();
        professorManager = ProfessorManager.getInstance();

        MentalManager.reset();

        CoinManager.getInstance().resetCoins();

        mainPanel.removeAll();

        MenuPanel menuPanel = new MenuPanel(this);
        CharacterCreationPanel createPanel = new CharacterCreationPanel(this);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(createPanel, "create");

        showScreen("menu");

        MentalManager.setMainUI(this);
    }
}
