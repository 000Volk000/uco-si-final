import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TrackingFrame extends JPanel {

    private JLabel imgLabel;
    private JLabel nameLabel;
    private JButton reorderButton;
    private MouseAdapter dragScrollListener;

    private String trackName = "";
    private String trackImagePath = "";
    private String trackDesc = "";
    private double trackPrice = 0.0;

    private JLabel dirLabel;
    private JLabel destLabel;
    private JLabel dateLabel;

    public TrackingFrame(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        App.chargeFont();
        setOpaque(false);
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(145, 0, 20, 0));

        // 1. Contenedor Maestro: Usa GridBagLayout para forzar un centrado absoluto
        JPanel masterContentPanel = new JPanel(new GridBagLayout());
        masterContentPanel.setOpaque(false);

        // 2. Contenedor de Tarjetas: Aquí apilamos los elementos verticalmente
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);
        // Forzamos que este bloque mida exactamente 360px de ancho
        cardsContainer.setPreferredSize(new Dimension(360, 650));
        cardsContainer.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));

        // Añadimos las tarjetas al contenedor de tarjetas
        JPanel productCard = createProductCard();
        cardsContainer.add(productCard);
        cardsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel trackingCard = createTrackingCard();
        cardsContainer.add(trackingCard);
        cardsContainer.add(Box.createVerticalGlue());

        // Centramos el contenedor de tarjetas dentro del panel maestro
        GridBagConstraints gbcMaster = new GridBagConstraints();
        gbcMaster.gridx = 0;
        gbcMaster.gridy = 0;
        gbcMaster.weightx = 1.0;
        gbcMaster.weighty = 1.0;
        gbcMaster.anchor = GridBagConstraints.NORTH; // Que se pegue arriba, pero centrado en X
        masterContentPanel.add(cardsContainer, gbcMaster);

        // 3. El ScrollPane ahora envuelve al panel maestro
        JScrollPane scrollPane = new JScrollPane(masterContentPanel);
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

        // Aplicamos los listeners a la nueva estructura
        masterContentPanel.addMouseListener(dragScrollListener);
        masterContentPanel.addMouseMotionListener(dragScrollListener);
        cardsContainer.addMouseListener(dragScrollListener);
        cardsContainer.addMouseMotionListener(dragScrollListener);
        scrollPane.getViewport().addMouseListener(dragScrollListener);
        scrollPane.getViewport().addMouseMotionListener(dragScrollListener);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setTrackingData(String productName, String imagePath, String description, double price) {
        // Guardamos los datos para usarlos luego en el botón "Volver a pedir"
        this.trackName = productName;
        this.trackImagePath = imagePath;
        this.trackDesc = description;
        this.trackPrice = price;

        // Actualizamos el nombre del producto
        nameLabel.setText("<html><div style='text-align: center; width: 120px;'>" + productName + "</div></html>");

        // REFRESCAMOS LOS DATOS DE ENVÍO
        String dirHtml = "<html><div style='width: 280px;'>"
                + App.getBundle().getString("Address") + ": " + App.address
                + "</div></html>";
        dirLabel.setText(dirHtml);

        String destHtml = "<html><div style='width: 280px;'>"
                + App.getBundle().getString("Addressee") + ": " + App.getName() // Asegúrate de que esto devuelve el
                                                                                // usuario real
                + "</div></html>";
        destLabel.setText(destHtml);

        LocalDate fechaLlegada = LocalDate.now().plusDays(2);
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaLlegada.format(formateador);

        String dateHtml = "<html><div style='width: 280px;'>"
                + App.getBundle().getString("EstimatedDate") + ": " + fechaFormateada
                + "</div></html>";
        dateLabel.setText(dateHtml);

        // Actualizamos la imagen
        try {
            if (new java.io.File(imagePath).exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(110, 75, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Fallo al cargar: " + imagePath);
        }

        revalidate();
        repaint();
    }

    private JPanel createProductCard() {
        RoundedPanel card = new RoundedPanel(40, Color.decode("#DEECFF"), null, 0);
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(360, 138));
        card.setMinimumSize(new Dimension(360, 138));
        card.setMaximumSize(new Dimension(360, 138));

        GridBagConstraints gbc = new GridBagConstraints();

        RoundedPanel imageContainer = new RoundedPanel(30, Color.WHITE, null, 0);
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(130, 95));
        imageContainer.setMinimumSize(new Dimension(130, 95));

        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageContainer.add(imgLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 15, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        card.add(imageContainer, gbc);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        nameLabel = new JLabel();
        nameLabel.setFont(App.font().deriveFont(Font.PLAIN, 18));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        reorderButton = createActionButton(App.getBundle().getString("Reorder"), Color.decode("#85BB65"));

        // --- CÓDIGO NUEVO PARA EL BOTÓN ---
        reorderButton.addActionListener(e -> {
            // Le enviamos a la vista de producto los datos que teníamos guardados
            App.product.setProductData(
                    trackName,
                    trackDesc,
                    String.valueOf(trackPrice),
                    trackImagePath,
                    "src/assets/Products/cart.png" // Asegúrate de que esta ruta al icono sea la tuya
            );
            // Cambiamos a la vista del producto
            App.navigateTo("product");
            // Opcional: Si quieres que el menú inferior marque otra pestaña
            // App.updateNavSelection("main");
        });
        // ----------------------------------
        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(reorderButton);

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

    private JPanel createTrackingCard() {
        RoundedPanel card = new RoundedPanel(40, Color.decode("#DEECFF"), null, 0);
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setMinimumSize(new Dimension(360, 420));
        card.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));

        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel mapLabel = new JLabel();
        mapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon mapIcon = new ImageIcon("src/assets/tracking/tracking.png");
            Image mapImg = mapIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            mapLabel.setIcon(new ImageIcon(mapImg));
        } catch (Exception e) {
            System.err.println("Fallo al cargar la imagen del mapa");
        }
        card.add(mapLabel);

        card.add(Box.createVerticalStrut(20));

        JPanel textContainer = new JPanel();
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.Y_AXIS));
        textContainer.setOpaque(false);
        textContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        textContainer.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));

        Font textFont = App.font().deriveFont(Font.PLAIN, 18);

        dirLabel = new JLabel();
        dirLabel.setFont(textFont);
        dirLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        destLabel = new JLabel();
        destLabel.setFont(textFont);
        destLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dateLabel = new JLabel();
        dateLabel.setFont(textFont);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textContainer.add(dirLabel);
        textContainer.add(Box.createVerticalStrut(10));
        textContainer.add(destLabel);
        textContainer.add(Box.createVerticalStrut(10));
        textContainer.add(dateLabel);

        textContainer.add(Box.createVerticalGlue());

        card.add(textContainer);

        card.addMouseListener(dragScrollListener);
        card.addMouseMotionListener(dragScrollListener);
        textContainer.addMouseListener(dragScrollListener);
        textContainer.addMouseMotionListener(dragScrollListener);
        mapLabel.addMouseListener(dragScrollListener);
        mapLabel.addMouseMotionListener(dragScrollListener);

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