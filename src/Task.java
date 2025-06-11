import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private int rewardExp;
    private int rewardGuiltReduction;
    private boolean completed;
    private boolean rewarded;

    private int progress;
    private int targetProgress;

    public Task(String title, String description, int rewardExp, int rewardGuiltReduction) {
        this.title = title;
        this.description = description;
        this.rewardExp = rewardExp;
        this.rewardGuiltReduction = rewardGuiltReduction;
        this.completed = false;
        this.rewarded = false;
        this.progress = 0;

        if (title.contains("60분")) this.targetProgress = 60;
        else if (title.contains("3일")) this.targetProgress = 3;
        else if (title.contains("2일")) this.targetProgress = 2;
        else if (title.contains("목표 2번")) this.targetProgress = 2;
        else this.targetProgress = 30;
    }

    public void recordStudy(int minutes) {
        if (completed) return;

        progress += minutes;

        if (progress >= targetProgress && !rewarded) {
            completed = true;
            rewarded = true;
            CoinManager.getInstance().addCoins(3);
        }
    }

    public boolean isRewarded() { return rewarded; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getRewardExp() { return rewardExp; }
    public int getRewardGuiltReduction() { return rewardGuiltReduction; }
    public boolean isCompleted() { return completed; }
    public int getProgress() { return progress; }
    public int getTargetProgress() { return targetProgress; }
}
