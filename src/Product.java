import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

class VerticalScrollPanel extends JPanel implements Scrollable {
    public VerticalScrollPanel() {
        setOpaque(false);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return visibleRect.height;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}

class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius;
    private Color borderColor;
    private int borderThickness;

    public RoundedPanel(int radius, Color bgColor, Color bdColor, int bdThickness) {
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.borderColor = bdColor;
        this.borderThickness = bdThickness;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offset = (borderColor != null && borderThickness > 0) ? borderThickness / 2 : 0;
        int adjustedWidth = getWidth() - borderThickness;
        int adjustedHeight = getHeight() - borderThickness;

        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
            g2.fillRoundRect(offset, offset, adjustedWidth, adjustedHeight, cornerRadius, cornerRadius);
        }

        if (borderColor != null && borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.drawRoundRect(offset, offset, adjustedWidth, adjustedHeight, cornerRadius, cornerRadius);
        }
    }
}

public class Product extends JPanel {
    private final JPanel contenedorPantallas;
    private final CardLayout gestorCartas;

    private JLabel imageLabel;
    private JLabel titleLabel;
    private JTextPane descriptionArea;
    private JLabel priceLabel;
    private RoundedPanel cartButtonPanel;

    public Product(JPanel contenedorPantallas, CardLayout gestorCartas) {
        this.contenedorPantallas = contenedorPantallas;
        this.gestorCartas = gestorCartas;

        setOpaque(false);
        setLayout(new BorderLayout());

        initializeComponents();
    }

    private void initializeComponents() {
        Color lightBlue = Color.decode("#DEECFF");
        Color textColor = Color.decode("#2F3640");

        VerticalScrollPanel contentPanel = new VerticalScrollPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(Box.createVerticalStrut(150));


        RoundedPanel imageContainer = new RoundedPanel(40, Color.WHITE, lightBlue, 15);
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(300, 200));
        imageContainer.setMaximumSize(new Dimension(300, 200));
        imageContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        contentPanel.add(imageContainer);

        contentPanel.add(Box.createVerticalStrut(20));
        titleLabel = new JLabel("Cargando...");
        titleLabel.setFont(App.font().deriveFont(Font.PLAIN, 36f));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        contentPanel.add(Box.createVerticalStrut(20));


        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // Columna Izquierda: Descripción
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setOpaque(false);
        descPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        JLabel descTitle = new JLabel(App.getBundle().getString("Description"));
        descTitle.setFont(App.font().deriveFont(Font.PLAIN, 24f));
        descTitle.setForeground(textColor);
        descTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        descPanel.add(descTitle);
        descPanel.add(Box.createVerticalStrut(10));
        
        RoundedPanel descriptionContainer = new RoundedPanel(20, Color.decode("#DEECFF"), lightBlue, 5);
        descriptionContainer.setLayout(new BorderLayout());
        descriptionContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        descriptionContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        descriptionArea = new JTextPane();
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        descriptionArea.setForeground(textColor);
        descriptionArea.setMargin(new Insets(0, 5, 0, 0));

        StyledDocument doc = descriptionArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        descriptionContainer.add(descriptionArea, BorderLayout.CENTER);
        descPanel.add(descriptionContainer);
        infoPanel.add(descPanel);


        Color green = Color.decode("#85BB65");
        // Columna Derecha: Precio y Carrito
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setOpaque(false);
        JLabel priceTitle = new JLabel(App.getBundle().getString("Price"));
        priceTitle.setFont(App.font().deriveFont(Font.PLAIN, 24f));
        priceTitle.setForeground(textColor);
        priceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pricePanel.add(priceTitle);
        pricePanel.add(Box.createVerticalStrut(10));
        
        priceLabel = new JLabel();
        priceLabel.setFont(App.font().deriveFont(Font.PLAIN, 24f));
        priceLabel.setForeground(green);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pricePanel.add(priceLabel);
        pricePanel.add(Box.createVerticalStrut(15));

        cartButtonPanel = new RoundedPanel(50, green, null, 0);
        cartButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        cartButtonPanel.setMaximumSize(new Dimension(100, 50));
        cartButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        cartButtonPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cartButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Acción al hacer clic
        cartButtonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Añadido al carrito: " + titleLabel.getText());
            }
        });
        
        JLabel plusIcon = new JLabel("\u002B"); // Icono + Unicode
        plusIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        plusIcon.setForeground(Color.WHITE);
        cartButtonPanel.add(plusIcon);
        
        // Icono Carrito (JLabel vacío, se actualizará en setData)
        JLabel cartIconLabel = new JLabel();
        cartButtonPanel.add(cartIconLabel);
        
        pricePanel.add(cartButtonPanel);
        infoPanel.add(pricePanel);

        contentPanel.add(infoPanel);

        contentPanel.add(Box.createVerticalStrut(30)); // Espaciado final para permitir la superposición del koi


        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false); // Hace el fondo del scroll invisible
        scrollPane.setBorder(null); // Quita el borde gris por defecto
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Hace que la rueda del ratón baje suavemente
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Bloquea el scroll horizontal

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        add(scrollPane, BorderLayout.CENTER);
    }

    // --- Método clave para actualizar los datos de la pantalla ---
    public void setProductData(String title, String description, String priceUnit, String productImagePath,
            String cartIconImagePath) {
        titleLabel.setText(title);
        descriptionArea.setText(description);
        priceLabel.setText(priceUnit + " €/ud");

        updateIcon(imageLabel, productImagePath, 250, 150);

        JLabel cartIconLabel = (JLabel) cartButtonPanel.getComponent(1);
        updateIcon(cartIconLabel, cartIconImagePath, 30, 30);

        revalidate();
        repaint();
    }

    private void updateIcon(JLabel label, String imagePath, int width, int height) {
        try {
            if (new File(imagePath).exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            } else {
                label.setIcon(null); // Borrar icono si no se encuentra imagen
                System.err.println("Imagen no encontrada: " + imagePath);
            }
        } catch (Exception e) {
            label.setIcon(null);
            System.err.println("Error al cargar imagen: " + imagePath);
        }
    }

}