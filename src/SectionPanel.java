import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class SectionPanel extends JPanel {
    private JLabel titleLabel;
    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private CardLayout gestorCartas;
    private JPanel contenedorPrincipal;
    private MouseAdapter dragScrollListener;

    public SectionPanel(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        this.contenedorPrincipal = contenedorPrincipal;
        this.gestorCartas = gestorCartas;

        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerPanel.add(Box.createVerticalStrut(80));

        titleLabel = new JLabel("");
        titleLabel.setFont(App.font().deriveFont(Font.PLAIN, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 5, 0));
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(10));

        JPanel separatorLine = new JPanel();
        separatorLine.setBackground(Color.decode("#005596"));
        separatorLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        separatorLine.setPreferredSize(new Dimension(Integer.MAX_VALUE, 5));
        headerPanel.add(separatorLine);

        add(headerPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(gridPanel, BorderLayout.NORTH);

        scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
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
        wrapperPanel.addMouseListener(dragScrollListener);
        wrapperPanel.addMouseMotionListener(dragScrollListener);
        gridPanel.addMouseListener(dragScrollListener);
        gridPanel.addMouseMotionListener(dragScrollListener);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadSectionData(String sectionTitle, String[][] items) {
        titleLabel.setText(sectionTitle);
        gridPanel.removeAll();

        for (String[] item : items) {
            String name = item[0];
            String price = item[1];
            String imagePath = item[2];

            gridPanel.add(createItemCard(name, price, imagePath));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createItemCard(String name, String price, String imagePath) {
        RoundedPanel card = new RoundedPanel(40, Color.decode("#E1EFFF"), null, 0);
        card.setLayout(new GridBagLayout());

        card.setPreferredSize(new Dimension(367, 138));
        card.setMinimumSize(new Dimension(367, 138));
        card.setMaximumSize(new Dimension(367, 138));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();

        RoundedPanel imageContainer = new RoundedPanel(30, Color.WHITE, null, 0);
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(175, 105));
        imageContainer.setMinimumSize(new Dimension(175, 105));

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            if (new File(imagePath).exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(140, 85, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Fallo al cargar: " + imagePath);
        }
        imageContainer.add(imgLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 16, 0, 15);
        gbc.anchor = GridBagConstraints.WEST;
        card.add(imageContainer, gbc);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        String htmlName = "<html><div style='text-align: center; width: 120px;'>" + name + "</div></html>";

        JLabel nameLabel = new JLabel(htmlName);
        nameLabel.setFont(App.font().deriveFont(Font.PLAIN, 18));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(price + " \u20AC");
        priceLabel.setFont(App.font().deriveFont(Font.PLAIN, 20));
        priceLabel.setForeground(Color.decode("#8DCA79"));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(priceLabel);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 16);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(textPanel, gbc);

        card.addMouseListener(dragScrollListener);
        card.addMouseMotionListener(dragScrollListener);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                App.product.setProductData(
                        name,
                        "Descripción detallada pendiente de implementar en properties.",
                        price,
                        imagePath,
                        "src/assets/bottomBar/cart.png");
                gestorCartas.show(contenedorPrincipal, "product");
            }
        });

        return card;
    }
}