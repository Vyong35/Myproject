import java.io.Serializable;
import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class VirtualDateManager implements Serializable {
    private static VirtualDateManager instance;
    private LocalDate startDate;
    private LocalDate currentDate;
    private LocalDate semesterStartDate;


    private VirtualDateManager() {
        this.startDate = LocalDate.now(); // 시작 날짜는 최초 실행 시 기준
        this.currentDate = startDate;
    }

    public void setSemesterStartDate(LocalDate date) {
        this.semesterStartDate = date;
    }


    public static VirtualDateManager getInstance() {
        if (instance == null) {
            instance = new VirtualDateManager();
        }
        return instance;
    }

    public static void setInstance(VirtualDateManager dm) {
        instance = dm;
    }

    public static void reset() {
        instance = new VirtualDateManager();
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void nextDay() {
        currentDate = currentDate.plusDays(1);

        // 하루가 지나고 나서 현재 일차 확인
        int dayInSemester = getDayInCurrentSemester();

        // 학기 넘기기: 7일차 다음날이면 학기 증가
        if (dayInSemester == 1) {
            SemesterManager.getInstance().nextSemester();
            ProfessorManager.getInstance().assignNewTasks(SemesterManager.getInstance().getCurrentSemester());
            System.out.println("학기 자동 증가: 현재 학기 = " + SemesterManager.getInstance().getCurrentSemester());
        }
    }


    public int getDayInCurrentSemester() {
        // 1학기당 7일이라고 가정했을 때, 전체 날짜에서 몇일째인지 계산
        int daysSinceStart = (int) (currentDate.toEpochDay() - startDate.toEpochDay());
        return (daysSinceStart % 7) + 1; // 각 학기 내에서 1~7일차
    }

    public int getTotalDaysPassed() {
        return (int) (currentDate.toEpochDay() - startDate.toEpochDay());
    }

    public int getCurrentSemester() {
        return (getTotalDaysPassed() / 7) + 1;
    }

    public static VirtualDateManager loadDate() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("date.dat"))) {
            VirtualDateManager dm = (VirtualDateManager) in.readObject();
            setInstance(dm); // 싱글턴 초기화
            System.out.println("가상 날짜 불러오기 성공");
            return dm;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("가상 날짜 불러오기 실패: " + e.getMessage());
            return getInstance(); // 실패 시 기본 인스턴스 반환
        }
    }

    public static void saveDate(VirtualDateManager dm) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("date.dat"))) {
            out.writeObject(dm);
            System.out.println("가상 날짜 저장 완료");
        } catch (IOException e) {
            System.out.println("가상 날짜 저장 실패: " + e.getMessage());
        }
    }

}
