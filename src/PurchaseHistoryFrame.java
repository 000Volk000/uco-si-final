import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PurchaseHistoryFrame extends JPanel {

    private JPanel listPanel;
    private MouseAdapter dragScrollListener;

    public PurchaseHistoryFrame(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        App.chargeFont();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerText = new JLabel("<html><div style='text-align: center;width: 280px;'>"
                + App.getBundle().getString("History") + "</div></html>");
        headerText.setFont(App.font().deriveFont(Font.PLAIN, 40));
        headerText.setHorizontalAlignment(SwingConstants.CENTER);
        headerText.setBorder(BorderFactory.createEmptyBorder(120, 0, 20, 0));
        headerPanel.add(headerText, BorderLayout.CENTER);

        JPanel topSeparator = new JPanel();
        topSeparator.setBackground(Color.decode("#005596"));
        topSeparator.setPreferredSize(new Dimension(401, 4));
        headerPanel.add(topSeparator, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        listPanel = new VerticalScrollPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        dragScrollListener = new MouseAdapter() {
            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = e.getLocationOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    Point current = e.getLocationOnScreen();
                    int deltaY = origin.y - current.y;
                    JScrollBar vBar = scrollPane.getVerticalScrollBar();
                    vBar.setValue(vBar.getValue() + deltaY);
                    origin = current;
                }
            }
        };

        scrollPane.getViewport().addMouseListener(dragScrollListener);
        scrollPane.getViewport().addMouseMotionListener(dragScrollListener);
        listPanel.addMouseListener(dragScrollListener);
        listPanel.addMouseMotionListener(dragScrollListener);

        add(scrollPane, BorderLayout.CENTER);

        refreshHistory();
    }

    public void refreshHistory() {
        listPanel.removeAll();

        for (int index = App.purchaseHistory.size() - 1; index >= 0; index--) {
            CartItem item = App.purchaseHistory.get(index);
            listPanel.add(createHistoryItem(item));
            listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createHistoryItem(CartItem item) {
        RoundedPanel card = new RoundedPanel(40, Color.decode("#DEECFF"), null, 0);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(360, 138));
        card.setMinimumSize(new Dimension(360, 138));
        card.setMaximumSize(new Dimension(360, 138));

        GridBagConstraints gbc = new GridBagConstraints();

        RoundedPanel imageContainer = new RoundedPanel(30, Color.WHITE, null, 0);
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(130, 95));
        imageContainer.setMinimumSize(new Dimension(130, 95));

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            if (new java.io.File(item.getImagePath()).exists()) {
                ImageIcon icon = new ImageIcon(item.getImagePath());
                Image img = icon.getImage().getScaledInstance(110, 75, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Fallo al cargar: " + item.getImagePath());
        }
        imageContainer.add(imgLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 15, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        card.add(imageContainer, gbc);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        String htmlName = "<html><div style='text-align: center; width: 120px;'>" + item.getName()
                + "</div></html>";
        JLabel nameLabel = new JLabel(htmlName);
        nameLabel.setFont(App.font().deriveFont(Font.PLAIN, 18));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton reorderButton = createActionButton(App.getBundle().getString("Reorder"), Color.decode("#85BB65"));
        reorderButton.addActionListener(e -> {
            App.product.setProductData(
                    item.getName(),
                    item.getDescription(),
                    String.valueOf(item.getPrice()),
                    item.getImagePath(),
                    "src/assets/Products/cart.png");
            App.getCardsGestor().show(App.getContenedor(), "product");
            App.updateNavSelection("main");
        });

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(reorderButton);

if (item.isLastPurchase()) {
            textPanel.add(Box.createVerticalStrut(6));
            JButton trackingButton = createActionButton(App.getBundle().getString("Tracking"), Color.decode("#E9BD39"));
            
            trackingButton.addActionListener(e -> {
                App.tracking.setTrackingData(item.getName(), item.getImagePath());
                
                App.getCardsGestor().show(App.getContenedor(), "tracking");
            });
            // --------------------

            textPanel.add(trackingButton);
        }

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 16);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(textPanel, gbc);

        card.addMouseListener(dragScrollListener);
        card.addMouseMotionListener(dragScrollListener);
        imageContainer.addMouseListener(dragScrollListener);
        imageContainer.addMouseMotionListener(dragScrollListener);
        textPanel.addMouseListener(dragScrollListener);
        textPanel.addMouseMotionListener(dragScrollListener);

        return card;
    }

    private JButton createActionButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
            }

            @Override
            public boolean isContentAreaFilled() {
                return false;
            }
        };
        button.setFont(App.font().deriveFont(Font.PLAIN, 13f));
        button.setForeground(Color.BLACK);
        button.setBackground(backgroundColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(130, 26));
        button.setPreferredSize(new Dimension(130, 26));
        return button;
    }
}