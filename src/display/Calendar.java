package display;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXMonthView;

public class Calendar {
    public Calendar() {
        JFrame frame = new JFrame("Calendar");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(createMonthView());
        frame.setVisible(true);
    }
 
    private JXMonthView createMonthView() {
        // カレンダー表示コンポーネントの生成
        JXMonthView monthView = new JXMonthView();
        
        return monthView;
    }
  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Calendar();
            }
        });
    }
}