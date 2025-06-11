import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    private Color baseColor = new Color(104, 159, 255); // 캐주얼한 파스텔 블루
    private Color hoverColor = new Color(66, 133, 244); // 진한 블루
    private Color borderColor = new Color(48, 101, 203); // 테두리 색
    private boolean hovered = false;

    public CustomButton(String text) {
        super(text);
        setFont(new Font("맑은 고딕", Font.BOLD, 18));
        setForeground(Color.BLACK);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hovered = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경
        g2.setColor(hovered ? hoverColor : baseColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

        // 테두리
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

        g2.dispose();
        super.paintComponent(g); // 텍스트는 기본 방식으로
    }

    @Override
    public void setContentAreaFilled(boolean b) {
        // 무시
    }
}
