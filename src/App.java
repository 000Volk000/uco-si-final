import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
    private Image scaleTopLeft;
    private Image scaleLowLeft;
    private Image scaleTopRight;
    private Image koi;


    public BackgroundPanel() {
        int scaletl= 220, scalell=200,scaletr=145, koi=200;
        scaleTopLeft = new ImageIcon("src/assets/topLeft.png").getImage().getScaledInstance(scaletl, -1, Image.SCALE_SMOOTH);
        scaleLowLeft = new ImageIcon("src/assets/bottomLeft.png").getImage().getScaledInstance(scalell, -1, Image.SCALE_SMOOTH);
        scaleTopRight = new ImageIcon("src/assets/topRight.png").getImage().getScaledInstance(scaletr, -1, Image.SCALE_SMOOTH);
        this.koi= new ImageIcon("src/assets/koi.png").getImage().getScaledInstance(koi, -1, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        if (scaleTopLeft != null)
            g.drawImage(scaleTopLeft, 0, 0, this);
        if (scaleLowLeft != null)
            g.drawImage(scaleLowLeft, 0, height - scaleLowLeft.getHeight(this), this);
        if (scaleTopRight != null)
            g.drawImage(scaleTopRight, width - scaleTopRight.getWidth(this), 0, this);
        if (koi != null)
            g.drawImage(koi, 175, 580, this);
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

        bgPanel.setLayout(new BorderLayout());
        CardLayout gestorCartas = new CardLayout();
        JPanel contenedorPantallas = new JPanel(gestorCartas);
        contenedorPantallas.setOpaque(false);

        loginFrame login = new loginFrame();
        contenedorPantallas.add(login, "login");
        bgPanel.add(contenedorPantallas, BorderLayout.CENTER);
        gestorCartas.show(contenedorPantallas, "login");

        jf.setContentPane(bgPanel);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}