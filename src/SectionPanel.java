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

    public SectionPanel(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        this.contenedorPrincipal = contenedorPrincipal;
        this.gestorCartas = gestorCartas;

        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Este espaciador reemplaza tu setBounds y empuja el contenido hacia abajo
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

        gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

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
        RoundedPanel card = new RoundedPanel(40, Color.decode("#DEECFF"), null, 0);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(170, 220));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            if (new File(imagePath).exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Fallo al cargar: " + imagePath);
        }

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(App.font().deriveFont(Font.PLAIN, 20));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel priceLabel = new JLabel(price + " \u20AC");
        priceLabel.setFont(App.font().deriveFont(Font.PLAIN, 20));
        priceLabel.setForeground(Color.decode("#2F3640"));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        card.add(imgLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

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