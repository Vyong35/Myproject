import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int level;
    private int exp;
    private int totalStudyMinutes;
    private int expToNextLevel;
    private int passionLevel;

    private List<Boolean> attendanceRecord;
    private List<Integer> dailyStudyMinutes;

    public Student(String name) {
        this.name = name;
        this.level = 1;
        this.exp = 0;
        this.totalStudyMinutes = 0;
        this.attendanceRecord = new ArrayList<>();
        this.dailyStudyMinutes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getExpToNext() {
        return 10 + (level - 1) * 2;
    }

    public int getTotalStudyMinutes() {
        return totalStudyMinutes;
    }

    public List<Boolean> getAttendanceRecord() {
        return attendanceRecord;
    }

    public List<Integer> getDailyStudyMinutes() {
        return dailyStudyMinutes;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public void setPassionLevel(int level) {
        this.passionLevel = level;
    }

    public int getPassionLevel() {
        return passionLevel;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setExpToNextLevel(int expToNextLevel) {
        this.expToNextLevel = expToNextLevel;
    }

    public void addExp(int amount) {
        exp += amount;

        while (exp >= getExpToNext()) {
            exp -= getExpToNext();
            level++;

            JOptionPane.showMessageDialog(null,
                    "🎉 레벨 업! 이제 레벨 " + level + "입니다!",
                    "축하합니다!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void markAttendance() {
        attendanceRecord.add(true);
    }

    public void resetAttendance() {
        attendanceRecord.clear();
        dailyStudyMinutes.clear();
    }

    public void study(int minutes) {
        int index = (SemesterManager.getInstance().getCurrentSemester() - 1) * 7
                + (VirtualDateManager.getInstance().getDayInCurrentSemester() - 1);
        study(minutes, index);
    }

    public void study(int minutes, int index) {
        addExp(minutes);
        totalStudyMinutes += minutes;

        boolean rewardGiven = false;
        List<Task> tasks = TaskManager.getCurrentTasks();

        for (Task task : tasks) {
            boolean before = task.isRewarded();
            task.recordStudy(minutes);
            if (!before && task.isRewarded()) {
                rewardGiven = true;
            }
        }

        if (rewardGiven && MainUI.getInstance() != null) {
            MainUI.getInstance().getHomePanel().updateStatus();
        }

        ensureStudyMinutesSize(index);
        int prev = dailyStudyMinutes.get(index);
        dailyStudyMinutes.set(index, prev + minutes);
    }

    public void addStudyMinutes(int minutes) {
        totalStudyMinutes += minutes;
        int index = attendanceRecord.size() - 1;
        if (index >= 0) {
            ensureStudyMinutesSize(index);
            int prev = dailyStudyMinutes.get(index);
            dailyStudyMinutes.set(index, prev + minutes);
        }
    }

    public void recordZeroStudyIfNeeded() {
        int index = attendanceRecord.size() - 1;
        if (index >= 0) {
            ensureStudyMinutesSize(index);
        }
    }

    private void ensureStudyMinutesSize(int index) {
        while (dailyStudyMinutes.size() <= index) {
            dailyStudyMinutes.add(0);
        }
    }
}
