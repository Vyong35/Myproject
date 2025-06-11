import java.io.*;
import javax.swing.*;
import java.util.Random;

public class MentalManager implements Serializable {
    private static MentalManager instance;
    private int guilt;

    private static final String FILE_PATH = "guilt.dat";
    private static transient MainUI mainUI; // 메뉴로 돌아가기용

    private MentalManager() {
        this.guilt = 0;
    }

    public static MentalManager getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static void setInstance(MentalManager loadedInstance) {
        instance = loadedInstance;
    }

    public static void reset() {
        instance = new MentalManager();
    }

    public int getGuilt() {
        return guilt;
    }

    public void resetGuilt() {
        this.guilt = 0;
        save();
    }

    public void increaseGuilt(int amount) {
        guilt = Math.min(100, guilt + amount);
        save();

        System.out.println("현재 자괴감: " + guilt);

        showEncouragementMessage();

        if (guilt >= 100 && mainUI != null) {
            JOptionPane.showMessageDialog(null,
                    "졸업을 실패했어요 ㅠㅠ\n다시 도전해볼까요?",
                    "자괴감 폭발", JOptionPane.ERROR_MESSAGE);
            resetGuilt();
            SaveManager.deleteAll();
            mainUI.resetToMenu();
        }
    }

    public void decreaseGuilt(int amount) {
        guilt = Math.max(0, guilt - amount);
        save();
    }

    public static void setMainUI(MainUI ui) {
        mainUI = ui;
    }

    private void showEncouragementMessage() {
        String[] messages = {
                "괜찮아, 다시 시작하면 돼!",
                "오늘은 조금 쉬어도 돼!",
                "실패는 누구에게나 있어!",
                "조금씩, 천천히 해도 돼!",
                "너무 자책하지 마!",
                "그래도 시작했잖아. 그게 중요한 거야."
        };
        Random rand = new Random();
        String message = messages[rand.nextInt(messages.length)];

        // 알림음 느낌용 팝업
        JOptionPane.showMessageDialog(null, message, "응원 메시지", JOptionPane.INFORMATION_MESSAGE);
    }

    private static MentalManager load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (MentalManager) in.readObject();
        } catch (Exception e) {
            return new MentalManager(); // 파일 없거나 오류 시 새로 생성
        }
    }

    private void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("자괴감 저장 실패: " + e.getMessage());
        }
    }
}
