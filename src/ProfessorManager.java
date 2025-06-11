import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfessorManager implements Serializable {
    private static ProfessorManager instance;

    private List<Task> currentTasks;

    private ProfessorManager() {
        this.currentTasks = new ArrayList<>();
    }

    // 싱글턴 인스턴스 반환
    public static ProfessorManager getInstance() {
        if (instance == null) {
            instance = new ProfessorManager();
        }
        return instance;
    }

    // 저장된 객체로 인스턴스 설정
    public static void setInstance(ProfessorManager pm) {
        instance = pm;
    }

    // 완전 리셋
    public static void reset() {
        instance = new ProfessorManager();
    }

    public List<Task> getCurrentTasks() {
        return currentTasks;
    }

    public void assignNewTasks(int semesterNumber) {
        currentTasks.clear();
        Random rand = new Random();

        // 연속 일수 과제 리스트
        List<Task> possibleTasks = new ArrayList<>();
        possibleTasks.add(new Task("2일 이상 공부하기", "2일 동안 공부 기록을 남겨보세요!", 30, 0));
        possibleTasks.add(new Task("3일 이상 공부하기", "3일 동안 공부 기록을 남겨보세요!", 50, 5));


        // 무작위로 하나 선택
        Task selectedTask = possibleTasks.get(rand.nextInt(possibleTasks.size()));
        currentTasks.add(selectedTask);
    }


    public void checkAndReward(Student student) {
        for (Task task : currentTasks) {
            if (!task.isCompleted()) continue;

            student.study(task.getRewardExp() / 2, 0); // 보상 경험치는 guilt 영향 없이
            if (task.getRewardGuiltReduction() > 0) {
                MentalManager.getInstance().decreaseGuilt(task.getRewardGuiltReduction());
            }
        }
    }
}
