import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static List<Task> currentTasks = new ArrayList<>();

    public static List<Task> getCurrentTasks() {
        return currentTasks;
    }

    public static void setCurrentTasks(List<Task> tasks) {
        currentTasks = tasks;
    }

    public static void clearTasks() {
        currentTasks.clear();
    }

    public static void addTask(Task task) {
        currentTasks.add(task);
    }
}
