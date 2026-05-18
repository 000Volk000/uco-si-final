import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
    private Image scaleTopLeft;
    private Image scaleLowLeft;
    private Image scaleTopRight;
    private Image koi;

    public BackgroundPanel() {
        scaleTopLeft = new ImageIcon("src/assets/background/topLeft.png").getImage().getScaledInstance(247, 201,
                Image.SCALE_SMOOTH);
        scaleLowLeft = new ImageIcon("src/assets/background/bottomLeft.png").getImage().getScaledInstance(195, 192,
                Image.SCALE_SMOOTH);
        scaleTopRight = new ImageIcon("src/assets/background/topRight.png").getImage().getScaledInstance(147, 186,
                Image.SCALE_SMOOTH);
        this.koi = new ImageIcon("src/assets/background/koi.png").getImage().getScaledInstance(221, 248,
                Image.SCALE_SMOOTH);
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
            g.drawImage(koi, 176, 589, this);
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

        LoginFrame login = new LoginFrame(gestorCartas, contenedorPantallas);
        contenedorPantallas.add(login, "login");

        MainScreen mainScreen = new MainScreen();
        contenedorPantallas.add(mainScreen, "main");

        bgPanel.add(contenedorPantallas, BorderLayout.CENTER);
        gestorCartas.show(contenedorPantallas, "login");

        RegisterFrame register = new RegisterFrame(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(register, "register");
        bgPanel.add(contenedorPantallas, BorderLayout.CENTER);

        jf.setContentPane(bgPanel);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}