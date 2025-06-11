import javax.swing.*;
import java.awt.*;

public class FadeTransition {
    public static void fadeOut(JPanel panel, Runnable afterFadeOut) {
        JWindow fadeWindow = new JWindow();
        fadeWindow.setLayout(new BorderLayout());
        fadeWindow.setSize(panel.getSize());
        fadeWindow.setLocationRelativeTo(panel);

        FadePanel overlay = new FadePanel(panel);
        fadeWindow.add(overlay);
        fadeWindow.setVisible(true);

        Timer timer = new Timer(15, null); // 부드러운 전환을 위한 타이머 간격
        final float[] alpha = {0.0f};

        timer.addActionListener(e -> {
            alpha[0] += 0.05f;
            overlay.setAlpha(Math.min(1.0f, alpha[0]));
            overlay.repaint();

            if (alpha[0] >= 1.0f) {
                timer.stop();
                fadeWindow.dispose();
                afterFadeOut.run();
            }
        });

        timer.start();
    }

    // 오버레이 페인트를 위한 커스텀 패널
    static class FadePanel extends JPanel {
        private float alpha = 0.0f;
        private final JPanel sourcePanel;

        public FadePanel(JPanel sourcePanel) {
            this.sourcePanel = sourcePanel;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            // 배경을 검은색 투명도로 페인트
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }
}
