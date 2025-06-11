import java.io.*;

public class CoinManager implements Serializable {
    private static CoinManager instance;
    private int coins;

    private static final String FILE_PATH = "coin.dat";

    private CoinManager() {
        this.coins = 0;
    }

    public static CoinManager getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static void setInstance(CoinManager cm) {
        instance = cm;
    }

    public static void reset() {
        instance = new CoinManager();
        instance.save(); // 초기화된 것도 저장
    }

    public void resetCoins() {
        this.coins = 0;
        save();
    }


    public int getCoins() {
        return coins;
    }

    public boolean useCoin(int amount) {
        if (coins >= amount) {
            coins -= amount;
            save();
            return true;
        }
        return false;
    }

    public void addCoins(int amount) {
        coins += amount;
        save();
    }

    private void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("코인 저장 실패: " + e.getMessage());
        }
    }

    private static CoinManager load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (CoinManager) in.readObject();
        } catch (Exception e) {
            return new CoinManager(); // 실패 시 새로 생성
        }
    }
}
