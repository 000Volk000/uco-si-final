import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CartFrame extends JPanel {

    private JLabel totalLabel;
    private JPanel listPanel;

    private MouseAdapter dragScrollListener;

    public CartFrame(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        // Aseguramos que la fuente esté cargada
        App.chargeFont();
        setOpaque(false);
        // Color de fondo general (simulando el azul claro de la imagen)
        setLayout(new BorderLayout());

        // --- 1. PANEL SUPERIOR (HEADER) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerText = new JLabel("<html><div style='text-align: center;width: 280px;'>"
                + App.getBundle().getString("Cart") + "</div></html>");
        headerText.setFont(App.font().deriveFont(Font.PLAIN, 40));
        headerText.setHorizontalAlignment(SwingConstants.CENTER);

        headerText.setBorder(BorderFactory.createEmptyBorder(120, 0, 20, 0));
        headerPanel.add(headerText, BorderLayout.CENTER);

        JPanel topSeparator = new JPanel();
        topSeparator.setBackground(Color.decode("#005596"));
        topSeparator.setPreferredSize(new Dimension(401, 4));

        headerPanel.add(topSeparator, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        // --- 2. PANEL CENTRAL (LISTA DE PRODUCTOS) ---
        listPanel = new VerticalScrollPanel();
        listPanel.setOpaque(false);
        // Usamos BoxLayout en el eje Y para apilar los elementos verticalmente
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // --> NUEVO CÓDIGO: BLOQUEO DEL SCROLL HORIZONTAL <--
        // Forzamos a que nunca aparezca ni se permita el scroll lateral
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Aseguramos que el vertical mantenga su comportamiento natural
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Mantenemos la configuración anterior de la barra vertical
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

        // --- 3. PANEL INFERIOR (CHECKOUT) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(401, 200));
        bottomPanel.setLayout(null); // Layout nulo para posicionamiento absoluto exacto

        // Texto "Total"
        JLabel totalTextLabel = new JLabel(App.getBundle().getString("Total"));
        totalTextLabel.setFont(App.font().deriveFont(Font.PLAIN, 24));
        totalTextLabel.setBounds(30, 20, 100, 30);
        bottomPanel.add(totalTextLabel);

        totalLabel = new JLabel();
        totalLabel.setFont(App.font().deriveFont(Font.PLAIN, 24));
        totalLabel.setForeground(Color.decode("#6A8E4E"));
        totalLabel.setBounds(30, 50, 150, 30);
        bottomPanel.add(totalLabel);

        updatePrice();

        // Botón "Pagar" (Panel personalizado para bordes redondeados)
        JPanel payButton = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#92C16E")); // Color verde del botón
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        payButton.setOpaque(false);
        payButton.setBounds(30, 90, 140, 45);
        payButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel payLabel = new JLabel(App.getBundle().getString("Pay"));
        payLabel.setFont(App.font().deriveFont(Font.PLAIN, 22));
        payLabel.setForeground(Color.WHITE);
        payLabel.setHorizontalAlignment(SwingConstants.CENTER);
        payButton.add(payLabel, BorderLayout.CENTER);


        payButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Prevenir que el usuario pague si el carrito está vacío
                if (App.shoppingCart.isEmpty()) {
                    return; 
                }
                
                // Llamamos a nuestro nuevo método que dibuja el modal personalizado
                showAddressModal();
            }
        });
        bottomPanel.add(payButton);

        JPanel separatorLine = new JPanel();
        separatorLine.setBackground(Color.decode("#005596"));
        separatorLine.setBounds(0, 0, 401, 4);
        bottomPanel.add(separatorLine);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updatePrice() {
        double total = 0.0;
        for (CartItem item : App.shoppingCart) {
            total += (item.getPrice() * item.getQuantity());
        }
        totalLabel.setText(String.format("%.2f €", total));
    }

    private void showPaidMessage() {
        App.showAppMessage(App.getBundle().getString("Paid"));
    }

    public void refreshCart() {
        listPanel.removeAll(); // 1. Borramos todas las tarjetas anteriores

        // 2. Volvemos a leer la lista actualizada en tiempo real
        for (CartItem item : App.shoppingCart) {
            listPanel.add(createCartItem(item));
            listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // 3. Recalculamos el texto del dinero
        updatePrice();

        // 4. Forzamos a Swing a que recalcule los tamaños y pinte los nuevos elementos
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createCartItem(CartItem item) {
        RoundedPanel card = new RoundedPanel(40, Color.decode("#DEECFF"), null, 0);
        card.setLayout(new GridBagLayout());

        card.setPreferredSize(new Dimension(360, 138));
        card.setMinimumSize(new Dimension(360, 138));
        card.setMaximumSize(new Dimension(360, 138));

        GridBagConstraints gbc = new GridBagConstraints();

        // --- COLUMNA 0: Imagen ---
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

        // --- COLUMNA 1: Textos ---
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setPreferredSize(new Dimension(105, 85));
        textPanel.setMinimumSize(new Dimension(105, 85));
        textPanel.setMaximumSize(new Dimension(105, 85));

        JLabel nameLabel = createCartNameLabel(item.getName(), 105);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel priceLabel = new JLabel(item.getPrice() + " \u20AC");
        priceLabel.setFont(App.font().deriveFont(Font.PLAIN, 20));
        priceLabel.setForeground(Color.decode("#6A8E4E"));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(priceLabel);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        card.add(textPanel, gbc);

        // --- COLUMNA 2: Selector de Cantidad ---
        RoundedPanel quantityPanel = new RoundedPanel(20, Color.decode("#005596"), null, 0);
        quantityPanel.setLayout(new GridLayout(1, 3, 0, 0));
        quantityPanel.setPreferredSize(new Dimension(85, 35));
        quantityPanel.setMinimumSize(new Dimension(85, 35));

        JLabel minusLabel = new JLabel("-");
        minusLabel.setFont(App.font().deriveFont(Font.BOLD, 22));
        minusLabel.setForeground(Color.WHITE);
        minusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        minusLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel qtyLabel = new JLabel(String.valueOf(item.getQuantity()));
        qtyLabel.setFont(App.font().deriveFont(Font.BOLD, 18));
        qtyLabel.setForeground(Color.WHITE);
        qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel plusLabel = new JLabel("+");
        plusLabel.setFont(App.font().deriveFont(Font.BOLD, 20));
        plusLabel.setForeground(Color.WHITE);
        plusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        plusLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // CAMBIO: Lógica conectada al objeto real y recálculo del total
        minusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1); // Actualiza la memoria global
                    qtyLabel.setText(String.valueOf(item.getQuantity())); // Actualiza el número de la tarjeta
                    updatePrice(); // Recalcula el dinero inferior
                } else if (item.getQuantity() == 1) {
                    App.shoppingCart.remove(item);
                    refreshCart();
                }
            }
        });

        plusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                item.setQuantity(item.getQuantity() + 1); // Actualiza la memoria global
                qtyLabel.setText(String.valueOf(item.getQuantity())); // Actualiza el número de la tarjeta
                updatePrice(); // Recalcula el dinero inferior
            }
        });

        quantityPanel.add(minusLabel);
        quantityPanel.add(qtyLabel);
        quantityPanel.add(plusLabel);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 15);
        gbc.anchor = GridBagConstraints.EAST;
        card.add(quantityPanel, gbc);

        card.addMouseListener(dragScrollListener);
        card.addMouseMotionListener(dragScrollListener);
        imageContainer.addMouseListener(dragScrollListener);
        imageContainer.addMouseMotionListener(dragScrollListener);
        textPanel.addMouseListener(dragScrollListener);
        textPanel.addMouseMotionListener(dragScrollListener);
        quantityPanel.addMouseListener(dragScrollListener);
        quantityPanel.addMouseMotionListener(dragScrollListener);

        return card;
    }

    private JLabel createCartNameLabel(String name, int maxWidth) {
        JLabel nameLabel = new JLabel();
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setVerticalAlignment(SwingConstants.TOP);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        Font chosenFont = App.font().deriveFont(Font.PLAIN, 18f);
        String chosenText = name;

        for (int size = 18; size >= 12; size--) {
            Font trialFont = App.font().deriveFont(Font.PLAIN, (float) size);
            String fitted = fitSingleLineText(name, trialFont, maxWidth);
            chosenFont = trialFont;
            chosenText = fitted;

            if (!fitted.endsWith("...")) {
                break;
            }
        }

        nameLabel.setFont(chosenFont);
        nameLabel.setText(chosenText);
        nameLabel.setPreferredSize(new Dimension(maxWidth, 24));
        nameLabel.setMinimumSize(new Dimension(maxWidth, 24));
        nameLabel.setMaximumSize(new Dimension(maxWidth, 24));
        return nameLabel;
    }

    private String fitSingleLineText(String text, Font font, int maxWidth) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        try {
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics();

            if (metrics.stringWidth(text) <= maxWidth) {
                return text;
            }

            String ellipsis = "...";
            int availableWidth = Math.max(0, maxWidth - metrics.stringWidth(ellipsis));
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < text.length(); i++) {
                String candidate = builder.toString() + text.charAt(i);
                if (metrics.stringWidth(candidate) > availableWidth) {
                    break;
                }
                builder.append(text.charAt(i));
            }

            return builder.append(ellipsis).toString();
        } finally {
            g2.dispose();
        }
    }

    private void showAddressModal() {
        // 1. Accedemos a la capa superior nativa de la ventana principal
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        JLayeredPane layeredPane = rootPane.getLayeredPane();

        // 2. CORRECCIÓN: Creamos el fondo dibujando la transparencia a mano
        JPanel dimBackground = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // 100 es la opacidad. Puedes bajarlo a 80 si lo quieres más claro
                g2.setColor(new Color(0, 0, 0, 100)); 
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        // CRUCIAL para que Swing permita ver lo que hay debajo
        dimBackground.setOpaque(false); 
        dimBackground.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());

        // 3. Trampa: Consumimos los clics para que el usuario no pueda pulsar los botones de atrás
        dimBackground.addMouseListener(new MouseAdapter() {});

        // 4. Creamos la tarjeta del modal
        RoundedPanel modalPanel = new RoundedPanel(40, Color.decode("#DEECFF"), null, 0);
        modalPanel.setPreferredSize(new Dimension(320, 220));
        modalPanel.setLayout(new BoxLayout(modalPanel, BoxLayout.Y_AXIS));
        modalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dirección de Envío");
        titleLabel.setFont(App.font().deriveFont(Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.BLACK);
        
        modalPanel.add(titleLabel);
        modalPanel.add(Box.createVerticalStrut(20));

        JTextField addressField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g); 
            }
        };
        addressField.setOpaque(false); 
        addressField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        addressField.setFont(App.font().deriveFont(Font.PLAIN, 16));
        addressField.setMaximumSize(new Dimension(280, 35));
        addressField.setAlignmentX(Component.CENTER_ALIGNMENT);

        modalPanel.add(addressField);
        
        modalPanel.add(Box.createVerticalStrut(5));
        
        JLabel errorLabel = new JLabel("La dirección no puede estar vacía");
        errorLabel.setFont(App.font().deriveFont(Font.PLAIN, 12));
        errorLabel.setForeground(Color.decode("#E74C3C")); 
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false); 
        
        modalPanel.add(errorLabel);
        modalPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCancel = createModalButton("Cancelar", Color.decode("#E74C3C"));
        JButton btnAccept = createModalButton("Aceptar", Color.decode("#85BB65"));

        // ACCIÓN CANCELAR: Solo borramos el panel oscuro de la capa superior
        btnCancel.addActionListener(e -> {
            layeredPane.remove(dimBackground);
            layeredPane.repaint();
        }); 

        // ACCIÓN ACEPTAR
        btnAccept.addActionListener(e -> {
            String dir = addressField.getText().trim();
            if (dir.isEmpty()) {
                errorLabel.setVisible(true);
                dimBackground.revalidate(); 
                dimBackground.repaint();
            } else {
                // Guarda la dirección
                App.address = dir; 
                
                // Retiramos el modal de la pantalla al instante
                layeredPane.remove(dimBackground);
                layeredPane.repaint();
                
                // Procesamos el pedido
                App.recordPurchase(new ArrayList<>(App.shoppingCart));
                showPaidMessage();
                App.shoppingCart.clear();
                refreshCart();
            }
        });

        buttonPanel.add(btnCancel);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(btnAccept);

        modalPanel.add(buttonPanel);
        
        // 5. Añadimos todo a la capa MODAL de la aplicación
        dimBackground.add(modalPanel);
        layeredPane.add(dimBackground, JLayeredPane.MODAL_LAYER);
        
        // 6. Ponemos el foco en el campo de texto para que el usuario escriba directamente
        SwingUtilities.invokeLater(addressField::requestFocusInWindow);
    }


    private JButton createModalButton(String text, Color bgColor) {
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
        };
        button.setFont(App.font().deriveFont(Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(110, 35));
        button.setPreferredSize(new Dimension(110, 35));
        
        return button;
    }
}