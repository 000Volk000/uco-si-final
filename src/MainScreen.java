import javax.swing.*;
import java.awt.*;

public class MainScreen extends JPanel {

    public MainScreen(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new GridLayout(1, 5));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setPreferredSize(new Dimension(402, 65));
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        JButton btnHome = createNavButton("src/assets/bottomBar/homeSelected.png");
        btnHome.addActionListener(e -> gestorCartas.show(contenedorPrincipal, "Main"));
        bottomBar.add(btnHome);

        bottomBar.add(createNavButton("src/assets/bottomBar/cart.png"));
        bottomBar.add(createNavButton("src/assets/bottomBar/history.png"));
        bottomBar.add(createNavButton("src/assets/bottomBar/account.png"));

        add(bottomBar, BorderLayout.SOUTH);
    }

    private JButton createNavButton(String iconPath) {
        JButton btn = new JButton();
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + iconPath);
        }
        return btn;
    }
}