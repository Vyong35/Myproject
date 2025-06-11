import java.io.Serializable;

public class SemesterManager implements Serializable {
    private static SemesterManager instance;
    private int currentSemester;

    // 생성자는 private → 외부에서 new SemesterManager() 못함
    private SemesterManager() {
        this.currentSemester = 1;
    }

    // 싱글턴 객체 반환
    public static SemesterManager getInstance() {
        if (instance == null) {
            instance = new SemesterManager();
        }
        return instance;
    }

    // 저장/불러오기에서 불러온 객체로 instance 설정
    public static void setInstance(SemesterManager sm) {
        instance = sm;
    }

    // 완전히 초기화하고 다시 생성
    public static void reset() {
        instance = new SemesterManager();
    }

    // 현재 학기 번호 반환
    public int getCurrentSemester() {
        return currentSemester;
    }

    // 다음 학기로 넘김
    public void nextSemester() {
        if (!isGraduated()) {
            currentSemester++;
        }
    }

    // 졸업 여부 판단
    public boolean isGraduated() {
        return currentSemester > 8;
    }

    // 현재 학기를 1학기로 되돌림 (인스턴스는 유지)
    public void resetSemesterOnly() {
        currentSemester = 1;
    }
}
