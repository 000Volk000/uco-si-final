import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainScreen extends JPanel {

    private int carouselIndex = 0;

    private final String[] carouselTitles = {
            "Caña de Pescar Volkswagen",
            "Pez con cara de gilipollas",
            "Anzuelo Gaming"
    };

    private final String[] carouselImages = {
            "src/assets/rod/volkswagen.png",
            "src/assets/fish/gilipollas.png",
            "src/assets/hook/gaming.png"
    };

    private JLabel imageLabel;
    private JLabel itemTitleLabel;

    public MainScreen(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(null);

        JLabel headerText = new JLabel("Destacados");
        headerText.setFont(new Font("Inika", Font.BOLD, 24));
        headerText.setBounds(88, 130, 225, 51);
        headerText.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(headerText);

        JPanel carouselContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.decode("#DEECFF"));
                g2d.fillRoundRect(15, 0, 371, 147, 50, 50);
                g2d.dispose();
            }
        };
        carouselContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        carouselContainer.setOpaque(false);
        carouselContainer.setBounds(0, 186, 401, 147);
        carouselContainer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        itemTitleLabel = new JLabel("", SwingConstants.CENTER);
        itemTitleLabel.setFont(new Font("Inika", Font.BOLD, 14));

        carouselContainer.add(imageLabel, BorderLayout.CENTER);
        carouselContainer.add(itemTitleLabel, BorderLayout.SOUTH);
        carouselContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickX = e.getX();
                int panelWidth = carouselContainer.getWidth();

                if (clickX < panelWidth / 2) {
                    carouselIndex = (carouselIndex - 1 + carouselTitles.length) % carouselTitles.length;
                } else {
                    carouselIndex = (carouselIndex + 1) % carouselTitles.length;
                }
                updateCarousel();
            }
        });
        updateCarousel();
        contentPanel.add(carouselContainer);

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

    private void updateCarousel() {
        itemTitleLabel.setText(carouselTitles[carouselIndex]);

        ImageIcon originalIcon = new ImageIcon(carouselImages[carouselIndex]);
        if (originalIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.err.println("Fallo de carga en imagen: " + carouselImages[carouselIndex]);
            imageLabel.setIcon(null);
            return;
        }

        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        int targetHeight = 100;
        int targetWidth = (originalWidth * targetHeight) / originalHeight;

        Image scaledImage = originalIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
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