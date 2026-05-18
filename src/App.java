import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
    private Image topLeft;
    private Image lowLeft;
    private Image topRight;

    public BackgroundPanel() {
        topLeft = new ImageIcon("src/assets/topLeft.png").getImage();
        lowLeft = new ImageIcon("src/assets/bottomLeft.png").getImage();
        topRight = new ImageIcon("src/assets/topRight.png").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        if (topLeft != null)
            g.drawImage(topLeft, 0, 0, this);
        if (lowLeft != null)
            g.drawImage(lowLeft, 0, height - lowLeft.getHeight(this), this);
        if (topRight != null)
            g.drawImage(topRight, width - topRight.getWidth(this), 0, this);
    }
}

public class App {
    public static void main(String[] args) {
        // Canva creation
        JFrame jf = new JFrame("Pezqueñín");

        jf.setSize(402, 874);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        // Background
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setBackground(Color.decode("#B0C2DB"));

        jf.setContentPane(bgPanel);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}