import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CalendarPanel extends JPanel {
    public CalendarPanel(VirtualDateManager dateManager, List<Boolean> attendanceRecord, List<Integer> dailyStudyMinutes) {
        setLayout(new BorderLayout());

        int currentSemester = dateManager.getCurrentSemester();

        JPanel calendarGrid = new JPanel(new GridLayout(1, 7, 10, 10));
        calendarGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int day = 1; day <= 7; day++) {
            int index = (currentSemester - 1) * 7 + (day - 1);
            String display = day + "일차";
            JLabel label = new JLabel(display, SwingConstants.CENTER);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            if (index < dailyStudyMinutes.size()) {
                int minutes = dailyStudyMinutes.get(index);
                if (minutes >= 6) {
                    label.setForeground(Color.GREEN.darker());
                    label.setText("● " + display);
                } else if (minutes >= 1) {
                    label.setForeground(Color.ORANGE.darker());
                    label.setText("▲ " + display);
                } else {
                    label.setForeground(Color.RED.darker());
                    label.setText("X " + display);
                }
            }

            final int dayIndex = index;
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (dayIndex < dailyStudyMinutes.size()) {
                        int studyMinutes = dailyStudyMinutes.get(dayIndex);
                        JOptionPane.showMessageDialog(
                                CalendarPanel.this,
                                (dayIndex + 1) + "일차 공부 시간: " + studyMinutes + "분",
                                "공부 기록",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                CalendarPanel.this,
                                (dayIndex + 1) + "일차에는 아직 공부 기록이 없습니다.",
                                "정보 없음",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            });

            calendarGrid.add(label);
        }

        add(calendarGrid, BorderLayout.CENTER);
    }
}