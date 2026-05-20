import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class Account extends JPanel {

    public Account(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        App.chargeFont();
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblGreeting = new JLabel(App.getBundle().getString("Hello") + ", " + App.getName() + "!");
        lblGreeting.setFont(new Font("Inika", Font.BOLD, 32));
        lblGreeting.setForeground(new Color(30, 41, 59));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblGreeting, gbc);

        JLabel lblAvatar = new JLabel();
        ImageIcon iconAvatar = new ImageIcon("src/assets/Account/userPhoto.png");
        Image imgAvatar = iconAvatar.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        lblAvatar.setIcon(new ImageIcon(imgAvatar));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(lblAvatar, gbc);

        JPanel controlsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 80, 80);
                g2.dispose();
            }
        };
        controlsPanel.setOpaque(false);
        controlsPanel.setLayout(new GridBagLayout());
        controlsPanel.setBackground(new Color(222, 236, 255, 180));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbcControl = new GridBagConstraints();

        JButton btnPass = createCustomButton(App.getBundle().getString("ChangePassword"), new Color(137, 146, 200));
        gbcControl.gridx = 0;
        gbcControl.gridy = 0;
        gbcControl.insets = new Insets(0, 0, 20, 0);
        controlsPanel.add(btnPass, gbcControl);

        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        langPanel.setOpaque(false);
        JLabel lblLanguage = new JLabel(App.getBundle().getString("ChangeLanguage"));
        lblLanguage.setFont(new Font("Inika", Font.PLAIN, 16));
        langPanel.add(lblLanguage);

        // Desplegable
        String[] languages = { "Español", "Ingles" };
        JComboBox<String> langCombo = new JComboBox<>(languages) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#B5B9F0"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(Color.decode("#676DC1"));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                super.paintComponent(g);
                g2.dispose();
            }
        };
        if (App.getBundle().getString("LG").equals("Español"))
            langCombo.setSelectedIndex(0);
        else if (App.getBundle().getString("LG").equals("Ingles"))
            langCombo.setSelectedIndex(1);

        langCombo.setPreferredSize(new Dimension(90, 35));
        langCombo.setFont(new Font("Inika", Font.PLAIN, 16));
        langCombo.setBackground(Color.WHITE);
        langCombo.setFocusable(false);
        langCombo.setOpaque(false);

        langCombo.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        langCombo.setRenderer(new DefaultListCellRenderer() {
            private boolean isHovered = false;
            private boolean isClosed = false;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);

                this.isHovered = isSelected;
                this.isClosed = (index == -1);

                setOpaque(false);
                if (value != null && value.equals("Español")) {
                    try {
                        ImageIcon icon = new ImageIcon("src/assets/Account/Spain.png");
                        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(img));
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setVerticalAlignment(SwingConstants.CENTER);
                    } catch (Exception ex) {
                        System.err.println("Falta icono bandera: " + ex.getMessage());
                    }
                } else if (value != null && value.equals("Ingles")) {
                    try {
                        ImageIcon icon = new ImageIcon("src/assets/Account/Ingles.png");
                        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(img));
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setVerticalAlignment(SwingConstants.CENTER);
                    } catch (Exception ex) {
                        System.err.println("Falta icono bandera: " + ex.getMessage());
                    }
                }

                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                return this;
            }

            @Override
            protected void paintComponent(Graphics g) {
                if (isHovered && !isClosed) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(Color.decode("#B5B9F0"));

                    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
                    g2.dispose();
                }

                super.paintComponent(g);
            }
        });

        langCombo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            }

            @Override
            protected JButton createArrowButton() {
                JButton arrowBtn = new JButton("▼");
                arrowBtn.setMargin(new Insets(0, 0, 0, 0));
                arrowBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
                arrowBtn.setBorderPainted(false);
                arrowBtn.setContentAreaFilled(false);
                arrowBtn.setFocusPainted(false);
                arrowBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                arrowBtn.setOpaque(false);
                return arrowBtn;
            }

            @Override
            protected javax.swing.plaf.basic.ComboPopup createPopup() {
                // 1. Creamos el popup e inyectamos nuestro propio dibujo de fondo redondeado
                javax.swing.plaf.basic.BasicComboPopup popup = new javax.swing.plaf.basic.BasicComboPopup(comboBox) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g2.setColor(Color.decode("#B5B9F0"));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                        g2.setColor(Color.decode("#676DC1"));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                        g2.dispose();
                    }

                };
                popup.setOpaque(false);
                popup.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

                JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
                scrollPane.setOpaque(false);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setBorder(null);

                popup.getList().setOpaque(false);

                return popup;
            }
        });

        langCombo.addActionListener(e -> {
            if (langCombo.getSelectedIndex() == 0)
                App.setLocale(new Locale.Builder().setLanguage("es").setRegion("ES").build());
            else if (langCombo.getSelectedIndex() == 1)
                App.setLocale(new Locale.Builder().setLanguage("en").setRegion("GB").build());

            App.refresh(contenedorPrincipal, gestorCartas, "account");
        });

        langPanel.add(langCombo);

        gbcControl.gridy = 1;
        gbcControl.insets = new Insets(0, 0, 80, 0);
        controlsPanel.add(langPanel, gbcControl);

        // Botón Cerrar Sesión
        JButton btnLogout = createCustomButton(App.getBundle().getString("LogOut"), new Color(200, 123, 118));
        btnLogout.addActionListener(e -> {
            App.setNavBarVisibility(false);
            gestorCartas.show(contenedorPrincipal, "login");
        });
        gbcControl.gridy = 2;
        gbcControl.insets = new Insets(0, 0, 0, 0);
        controlsPanel.add(btnLogout, gbcControl);

        gbc.gridy = 2;
        add(controlsPanel, gbc);
    }

    private JButton createCustomButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);

        btn.setFont(new Font("Inika", Font.PLAIN, 18));

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}