import java.time.LocalDate;

public class DailyGoalManager {
    private final Student student;
    private int todayStudyMinutes;
    private LocalDate lastStudyDate;
    private int dailyGoal = 6;
    private int dailyGoalMinutes;

    public DailyGoalManager(Student student) {
        this.student = student;
        this.todayStudyMinutes = 0;
        this.lastStudyDate = LocalDate.now();
        setGoalByPassionLevel();
    }

    private void setGoalByPassionLevel() {
        int passionLevel = student.getPassionLevel();

        switch (passionLevel) {
            case 1: dailyGoalMinutes = 6; break;
            case 2: dailyGoalMinutes = 8; break;
            case 3: dailyGoalMinutes = 10; break;
            case 4: dailyGoalMinutes = 12; break;
            case 5: dailyGoalMinutes = 15; break;
            default: dailyGoalMinutes = 8;
        }
    }


    public int getDailyGoal() {
        return dailyGoal; // ✅ 재귀 대신 필드 리턴
    }

    public void checkDateReset(MentalManager mental) {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastStudyDate)) {
            if (todayStudyMinutes < getDailyGoal()) {
                System.out.println("어제의 목표를 달성하지 못했습니다... 자괴감이 +5 증가합니다.");
                mental.increaseGuilt(5);
            }
            todayStudyMinutes = 0;
            lastStudyDate = today;
        }
    }

    public void addStudyMinutes(int minutes) {
        todayStudyMinutes += minutes;
        System.out.println("오늘 누적 공부 시간: " + todayStudyMinutes + " / " + getDailyGoal() + "분");

        if (todayStudyMinutes >= getDailyGoal()) {
            System.out.println("🎯 오늘의 열정 목표를 달성했습니다! 멋져요!");
        } else {
            System.out.println("목표까지 " + (getDailyGoal() - todayStudyMinutes) + "분 남았습니다.");
        }
    }

    public int getTodayStudyMinutes() {
        return todayStudyMinutes;
    }

    public Student getStudent() {
        return student;
    }

    public int getDailyGoalMinutes() {
        return dailyGoalMinutes;
    }
}
