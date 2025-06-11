import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RestGameWindow extends JFrame {
    private GamePanel gamePanel;
    private JLabel timeLabel;

    public RestGameWindow() {
        setTitle("F를 피해라!");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        add(gamePanel);
        setVisible(true);

        gamePanel.startWithCountdown();
    }

    class GamePanel extends JPanel {
        private int playerX = 300;
        private int playerY = 700;
        private final int PLAYER_SIZE = 20;
        private final int PLAYER_SPEED = 9;

        private List<Rectangle> fList = new ArrayList<>();
        private Random random = new Random();

        private Timer gameTimer;
        private Timer spawnTimer;

        private boolean moveLeft = false;
        private boolean moveRight = false;

        private long startTime;
        private final int GAME_DURATION = 10_000; // 원래는 1분 - 테스트용 10초

        private JLabel countdownLabel;

        public GamePanel() {
            setBackground(Color.BLACK);
            setFocusable(true);
            setLayout(null);
            setupKeyBindings();

            countdownLabel = new JLabel("", SwingConstants.CENTER);
            countdownLabel.setFont(new Font("맑은 고딕", Font.BOLD, 100));
            countdownLabel.setForeground(Color.YELLOW);
            countdownLabel.setBounds(0, 300, 600, 100);
            add(countdownLabel);

            timeLabel = new JLabel("남은 시간: 60초", SwingConstants.CENTER);
            timeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
            timeLabel.setForeground(Color.WHITE);
            timeLabel.setBounds(0, 20, 600, 30); // 위치와 크기 조정
            add(timeLabel);

        }

        private void setupKeyBindings() {
            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(KeyStroke.getKeyStroke("pressed LEFT"), "leftPressed");
            im.put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
            im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "rightPressed");
            im.put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");

            am.put("leftPressed", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { moveLeft = true; }
            });
            am.put("leftReleased", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { moveLeft = false; }
            });
            am.put("rightPressed", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { moveRight = true; }
            });
            am.put("rightReleased", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { moveRight = false; }
            });
        }

        public void startWithCountdown() {
            String[] texts = {"3", "2", "1", "START!"};
            final int[] index = {0};

            Timer countdownTimer = new Timer(1000, null);
            countdownTimer.addActionListener(new ActionListener() {
                float opacity = 1f;
                Timer fadeTimer;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (index[0] >= texts.length) {
                        countdownTimer.stop();
                        remove(countdownLabel);
                        repaint();
                        startGame();
                        return;
                    }

                    countdownLabel.setText(texts[index[0]]);
                    countdownLabel.setFont(new Font("맑은 고딕", Font.BOLD, 100));
                    countdownLabel.setForeground(Color.YELLOW);
                    countdownLabel.setVisible(true);
                    countdownLabel.setOpaque(false);
                    countdownLabel.repaint();

                    opacity = 1f;
                    if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();
                    fadeTimer = new Timer(50, new ActionListener() {
                        float currentOpacity = 1f;
                        @Override
                        public void actionPerformed(ActionEvent e2) {
                            currentOpacity -= 0.05f;
                            countdownLabel.setForeground(new Color(1f, 1f, 0f, currentOpacity));
                            if (currentOpacity <= 0f) {
                                fadeTimer.stop();
                            }
                        }
                    });
                    fadeTimer.start();

                    index[0]++;
                }
            });
            countdownTimer.setInitialDelay(0);
            countdownTimer.start();
        }

        public void startGame() {
            startTime = System.currentTimeMillis();

            gameTimer = new Timer(10, e -> {
                updateGame();
                repaint();
            });

            spawnTimer = new Timer(70, e -> {
                int x = random.nextInt(getWidth() - 20);
                fList.add(new Rectangle(x, 0, 30, 30));
            });

            gameTimer.start();
            spawnTimer.start();
        }

        private void updateGame() {
            if (moveLeft && playerX > 0) playerX -= PLAYER_SPEED;
            if (moveRight && playerX + PLAYER_SIZE < getWidth()) playerX += PLAYER_SPEED;

            long elapsed = System.currentTimeMillis() - startTime;
            int remaining = (int)((GAME_DURATION - elapsed) / 1000);
            timeLabel.setText("남은 시간: " + remaining + "초");

            Iterator<Rectangle> iterator = fList.iterator();
            while (iterator.hasNext()) {
                Rectangle f = iterator.next();
                f.y += 9;

                Rectangle player = new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
                if (f.intersects(player)) {
                    endGame(false);
                    return;
                }
                if (f.y > getHeight()) iterator.remove();
            }

            if (System.currentTimeMillis() - startTime >= GAME_DURATION) {
                endGame(true);
            }
        }

        private void endGame(boolean survived) {
            gameTimer.stop();
            spawnTimer.stop();
            String message = survived ? "축하합니다! F를 면했습니다!" : "아이고! 교수님이 F를 주셨습니다!";
            JOptionPane.showMessageDialog(this, message);
            RestGameWindow.this.dispose();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GREEN);
            g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

            g.setColor(Color.RED);
            for (Rectangle f : fList) {
                g.setFont(new Font("Monospaced", Font.BOLD, 25));
                g.drawString("F", f.x, f.y);
            }
        }
    }
}
