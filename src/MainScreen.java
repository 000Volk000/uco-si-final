import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainScreen extends JPanel {

    private int carouselIndex = 0;
    private int dragOffsetX = 0;
    private int startX = 0;
    private Timer snapTimer;

    private final String[] carouselTitles = {
            App.getBundle().getString("Volkswagen"),
            App.getBundle().getString("AssholeFish"),
            App.getBundle().getString("GamingHook")
    };

    private final String[] carouselImages = {
            "src/assets/rod/volkswagen.png",
            "src/assets/fish/gilipollas.png",
            "src/assets/hook/gaming.png"
    };

    private Image[] loadedImages;
    private JPanel carouselContainer;

    public MainScreen(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        setOpaque(false);
        setLayout(new BorderLayout());

        loadImages();

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(null);

        JLabel headerText = new JLabel(App.getBundle().getString("Featured"));
        headerText.setFont(App.font().deriveFont(Font.PLAIN, 24f));
        headerText.setBounds(88, 130, 225, 51);
        headerText.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(headerText);

        carouselContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int panelWidth = getWidth();
                int panelHeight = getHeight();

                drawCard(g2d, carouselIndex, dragOffsetX, panelWidth, panelHeight);

                if (dragOffsetX < 0) {
                    int nextIndex = (carouselIndex + 1) % carouselTitles.length;
                    drawCard(g2d, nextIndex, dragOffsetX + panelWidth, panelWidth, panelHeight);
                } else if (dragOffsetX > 0) {
                    int prevIndex = (carouselIndex - 1 + carouselTitles.length) % carouselTitles.length;
                    drawCard(g2d, prevIndex, dragOffsetX - panelWidth, panelWidth, panelHeight);
                }

                g2d.dispose();
            }
        };

        carouselContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        carouselContainer.setOpaque(false);
        carouselContainer.setBounds(0, 186, 401, 147);
        carouselContainer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        MouseAdapter dragAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (snapTimer != null && snapTimer.isRunning()) {
                    snapTimer.stop();
                }
                startX = e.getX();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragOffsetX = e.getX() - startX;
                carouselContainer.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int threshold = carouselContainer.getWidth() / 3;
                int targetOffset = 0;

                if (dragOffsetX < -threshold) {
                    targetOffset = -carouselContainer.getWidth();
                } else if (dragOffsetX > threshold) {
                    targetOffset = carouselContainer.getWidth();
                }

                animateSnap(targetOffset);
            }
        };

        carouselContainer.addMouseListener(dragAdapter);
        carouselContainer.addMouseMotionListener(dragAdapter);
        contentPanel.add(carouselContainer);

        add(contentPanel, BorderLayout.CENTER);

    }

    private void loadImages() {
        loadedImages = new Image[carouselImages.length];
        for (int i = 0; i < carouselImages.length; i++) {
            ImageIcon icon = new ImageIcon(carouselImages[i]);
            if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Fallo de carga en imagen: " + carouselImages[i]);
                continue;
            }
            int originalWidth = icon.getIconWidth();
            int originalHeight = icon.getIconHeight();
            int targetHeight = 100;
            int targetWidth = (originalWidth * targetHeight) / originalHeight;

            loadedImages[i] = icon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            new ImageIcon(loadedImages[i]);
        }
    }

    private void drawCard(Graphics2D g2d, int index, int offsetX, int panelWidth, int panelHeight) {
        g2d.setColor(Color.decode("#DEECFF"));
        g2d.fillRoundRect(15 + offsetX, 0, 371, 147, 50, 50);

        Image img = loadedImages[index];
        if (img != null) {
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);
            int x = (panelWidth - imgWidth) / 2 + offsetX;
            int y = (panelHeight - imgHeight) / 2 - 15;
            g2d.drawImage(img, x, y, null);
        }

        g2d.setColor(Color.BLACK);
        g2d.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        String text = carouselTitles[index];
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = (panelWidth - textWidth) / 2 + offsetX;
        int textY = panelHeight - 15;
        g2d.drawString(text, textX, textY);
    }

    private void animateSnap(int targetOffset) {
        int step = (targetOffset - dragOffsetX) / 10;
        if (step == 0)
            step = (targetOffset > dragOffsetX) ? 1 : -1;

        final int finalStep = step;

        snapTimer = new Timer(15, e -> {
            dragOffsetX += finalStep;

            if ((finalStep > 0 && dragOffsetX >= targetOffset) || (finalStep < 0 && dragOffsetX <= targetOffset)) {
                dragOffsetX = targetOffset;
                snapTimer.stop();

                if (targetOffset < 0) {
                    carouselIndex = (carouselIndex + 1) % carouselTitles.length;
                } else if (targetOffset > 0) {
                    carouselIndex = (carouselIndex - 1 + carouselTitles.length) % carouselTitles.length;
                }

                dragOffsetX = 0;
            }
            carouselContainer.repaint();
        });
        snapTimer.start();
    }

}