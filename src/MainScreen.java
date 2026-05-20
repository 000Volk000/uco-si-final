import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.image.BufferedImage;

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

    private final String[][] sectionData = {
            { "livingBait", "src/assets/sections/livingBait.png" },
            { "fishingLine", "src/assets/sections/fishingLine.png" },
            { "fishHook", "src/assets/sections/fishHook.png" },
            { "fishingRod", "src/assets/sections/fishingRod.png" },
            { "fish", "src/assets/sections/fish.png" },
            { "stool", "src/assets/sections/stool.png" }
    };

    public MainScreen(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        App.chargeFont();
        setOpaque(false);
        setLayout(new BorderLayout());

        loadImages();

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(null);

        JLabel headerText = new JLabel(App.getBundle().getString("Featured"));
        headerText.setFont(App.font().deriveFont(Font.PLAIN, 40));
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

        JLabel sectionsHeader = new JLabel(App.getBundle().getString("Sections"));
        sectionsHeader.setFont(App.font().deriveFont(Font.PLAIN, 40));
        sectionsHeader.setHorizontalAlignment(SwingConstants.CENTER);
        sectionsHeader.setBounds(0, 360, 401, 30);
        contentPanel.add(sectionsHeader);

        JPanel separatorLine = new JPanel();
        separatorLine.setBackground(Color.decode("#005596"));
        separatorLine.setBounds(0, 410, 401, 5);
        contentPanel.add(separatorLine);

        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        MouseAdapter verticalSwipeAdapter = new MouseAdapter() {
            private Point startPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getLocationOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (startPoint == null)
                    return;
                Point currentPoint = e.getLocationOnScreen();
                int deltaY = startPoint.y - currentPoint.y;

                Container parent = SwingUtilities.getAncestorOfClass(JScrollPane.class, gridPanel);
                if (parent instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane) parent;
                    JViewport viewPort = scrollPane.getViewport();
                    Rectangle view = viewPort.getViewRect();
                    view.y += deltaY;
                    gridPanel.scrollRectToVisible(view);
                    startPoint = currentPoint;
                }
            }
        };

        gridPanel.addMouseListener(verticalSwipeAdapter);
        gridPanel.addMouseMotionListener(verticalSwipeAdapter);

        for (String[] data : sectionData) {
            String cardName = data[0];
            String imagePath = data[1];

            JPanel sectionCard = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.decode("#DEECFF"));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            sectionCard.setOpaque(false);
            sectionCard.setPreferredSize(new Dimension(172, 226));
            sectionCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            sectionCard.addMouseListener(verticalSwipeAdapter);
            sectionCard.addMouseMotionListener(verticalSwipeAdapter);

            sectionCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    gestorCartas.show(contenedorPrincipal, cardName);
                }
            });

            JLabel imgLabel = new JLabel();
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE
                    || icon.getImageLoadStatus() == MediaTracker.LOADING) {
                Image scaled = icon.getImage().getScaledInstance(145, 145, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaled));
            } else {
                System.err.println("Error cargando imagen: " + imagePath);
            }

            JLabel textLabel = new JLabel(App.getBundle().getString(cardName));
            textLabel.setFont(App.font().deriveFont(Font.PLAIN, 20));
            textLabel.setHorizontalAlignment(SwingConstants.CENTER);
            textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

            sectionCard.add(imgLabel, BorderLayout.CENTER);
            sectionCard.add(textLabel, BorderLayout.SOUTH);

            gridPanel.add(sectionCard);
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBounds(10, 415, 381, 395);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadImages() {
        loadedImages = new Image[carouselImages.length];
        for (int i = 0; i < carouselImages.length; i++) {
            try {
                BufferedImage original = ImageIO.read(new File(carouselImages[i]));

                int targetHeight = 100;
                int targetWidth = (original.getWidth() * targetHeight) / original.getHeight();

                BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaled.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
                g2d.dispose();

                loadedImages[i] = scaled;
            } catch (Exception e) {
                System.err.println("Fallo de carga en imagen: " + carouselImages[i]);
            }
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
        g2d.setFont(App.font().deriveFont(Font.PLAIN, 20));
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