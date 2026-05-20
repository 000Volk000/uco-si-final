import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

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

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        if (borderColor != null && borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.drawRoundRect(borderThickness / 2, borderThickness / 2, getWidth() - borderThickness, getHeight() - borderThickness, cornerRadius, cornerRadius);
        }
    }
}

public class Product extends JPanel {
    private final JPanel contenedorPantallas;
    private final CardLayout gestorCartas;

    private JLabel imageLabel;
    private JLabel titleLabel;
    private JTextArea descriptionArea;
    private JLabel priceLabel;
    private RoundedPanel cartButtonPanel;

    public Product(JPanel contenedorPantallas, CardLayout gestorCartas) {
        this.contenedorPantallas = contenedorPantallas;
        this.gestorCartas = gestorCartas;
        
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        initializeComponents();
    }

    private void initializeComponents() {
        Color lightBlue = Color.decode("#DEECFF");
        Color textColor = Color.decode("#2F3640");


        // --- Panel Superior (Botón de volver) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);
        JButton btnBack = new JButton("\u2190");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 24));
        btnBack.setForeground(textColor);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> gestorCartas.show(contenedorPantallas, "main")); // Volver a "main"
        topPanel.add(btnBack);
        add(topPanel);

        add(Box.createVerticalStrut(20)); // Espaciado

        // --- Caja de Imagen del Producto (Recuadro blanco, borde verde) ---
        RoundedPanel imageContainer = new RoundedPanel(40, Color.WHITE, lightBlue, 5);
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(300, 200));
        imageContainer.setMaximumSize(new Dimension(300, 200));
        imageContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        add(imageContainer);

        add(Box.createVerticalStrut(20)); // Espaciado

        // --- Título del Producto (Sebasnew Font("Inika", Font.BOLD, 14))tián) ---
        titleLabel = new JLabel("Cargando...");
        titleLabel.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(20)); // Espaciado

        // --- Panel de Información (Descripción y Precio en dos columnas) ---
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30)); // Márgenes laterales

            // Columna Izquierda: Descripción
            JPanel descPanel = new JPanel();
            descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
            descPanel.setOpaque(false);
            JLabel descTitle = new JLabel("Descripción");
            descTitle.setFont(App.font().deriveFont(Font.PLAIN, 18f));
            descTitle.setForeground(textColor);
            descPanel.add(descTitle);
            descPanel.add(Box.createVerticalStrut(10));
            
            descriptionArea = new JTextArea();
            descriptionArea.setEditable(false);
            descriptionArea.setOpaque(false);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setFont(App.font().deriveFont(Font.PLAIN, 14f));
            descriptionArea.setForeground(textColor);
            descriptionArea.setMargin(new Insets(0, 5, 0, 0)); // Pequeño margen izquierdo
            descPanel.add(descriptionArea);
            infoPanel.add(descPanel);

            // Columna Derecha: Precio y Carrito
            JPanel pricePanel = new JPanel();
            pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
            pricePanel.setOpaque(false);
            JLabel priceTitle = new JLabel("Precio");
            priceTitle.setFont(App.font().deriveFont(Font.PLAIN, 18f));
            priceTitle.setForeground(textColor);
            pricePanel.add(priceTitle);
            pricePanel.add(Box.createVerticalStrut(10));
            
            priceLabel = new JLabel();
            priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
            priceLabel.setForeground(lightBlue); // Color precio verde lima
            pricePanel.add(priceLabel);
            pricePanel.add(Box.createVerticalStrut(15));

            // Botón de Carrito Ovalado Verde (RoundedPanel que actúa como botón)
            cartButtonPanel = new RoundedPanel(50, lightBlue, null, 0);
            cartButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            cartButtonPanel.setMaximumSize(new Dimension(100, 50));
            cartButtonPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            // Acción al hacer clic
            cartButtonPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Añadido al carrito: " + titleLabel.getText());
                    // Implementa tu lógica de carrito aquí
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

        add(infoPanel);

        add(Box.createVerticalStrut(30)); // Espaciado final para permitir la superposición del koi

    }

    // --- Método clave para actualizar los datos de la pantalla ---
    public void setProductData(String title, String description, String priceUnit, String productImagePath, String cartIconImagePath) {
        titleLabel.setText(title);
        descriptionArea.setText(description);
        priceLabel.setText(priceUnit + " €/ud");

        // Actualizar imagen del producto
        updateIcon(imageLabel, productImagePath, 250, 150);

        // Actualizar icono de carrito dentro del botón ovalado
        JLabel cartIconLabel = (JLabel) cartButtonPanel.getComponent(1);
        updateIcon(cartIconLabel, cartIconImagePath, 30, 30);

        // Forzar repintado
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