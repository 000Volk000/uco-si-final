import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class RegisterFrame extends JPanel {

    public RegisterFrame(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        App.chargeFont();
        setOpaque(false);

        // 1. Cambiamos el diseño principal a BorderLayout para aislar el menú superior
        setLayout(new BorderLayout());

        // 2. PANEL SUPERIOR: Desplegable de idioma alineado a la izquierda
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false);
        topPanel.add(createLanguageCombo(contenedorPrincipal, gestorCartas));
        add(topPanel, BorderLayout.NORTH);

        // 3. PANEL CENTRAL: El formulario con GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Ajustamos el margen superior porque el topPanel ya ocupa espacio
        gbc.insets = new Insets(50, 0, 20, 0);

        JLabel title = new JLabel(App.getBundle().getString("Welcome"));
        if (App.font() != null) {
            title.setFont(App.font().deriveFont(Font.PLAIN, 36f));
        } else {
            title.setFont(new Font("Serif", Font.PLAIN, 36));
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(title, gbc);

        // Nombre / Correo
        gbc.insets = new Insets(30, 25, 2, 0);
        JLabel lblUser = new JLabel(App.getBundle().getString("NameEmail"));
        lblUser.setFont(App.font().deriveFont(Font.PLAIN, 18f));
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblUser, gbc);

        // Campo 1
        gbc.insets = new Insets(0, 0, 15, 0);
        roundedText txtUser = new roundedText(0);
        txtUser.setPreferredSize(new Dimension(320, 40));
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(txtUser, gbc);

        // Contraseña
        gbc.insets = new Insets(30, 25, 2, 0);
        JLabel lblPass = new JLabel(App.getBundle().getString("Password"));
        lblPass.setFont(App.font().deriveFont(Font.PLAIN, 18f));
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblPass, gbc);

        // Campo 2
        gbc.insets = new Insets(0, 0, 5, 0);
        passwordText txtPass = new passwordText(0);
        txtPass.setPreferredSize(new Dimension(320, 40));
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(txtPass, gbc);

        // Confirmar Contraseña
        gbc.insets = new Insets(35, 25, 2, 0);
        JLabel lblConfirmPass = new JLabel(App.getBundle().getString("RepeatPassword"));
        lblConfirmPass.setFont(App.font().deriveFont(Font.PLAIN, 18f));
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblConfirmPass, gbc);

        // Campo 3
        gbc.insets = new Insets(0, 0, 5, 0);
        passwordText txtConfirmPass = new passwordText(0);
        txtConfirmPass.setPreferredSize(new Dimension(320, 40));
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(txtConfirmPass, gbc);

        // Crear Cuenta
        gbc.insets = new Insets(40, 0, 15, 0);
        roundedButton btnRegister = new roundedButton(App.getBundle().getString("CreateAccount"));
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.setPreferredSize(new Dimension(220, 45));
        btnRegister.setFont(App.font().deriveFont(Font.PLAIN, 20f));
        btnRegister.addActionListener(e -> {
            String email = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());
            String confirmPassword = new String(txtConfirmPass.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                App.showAppMessage(App.getBundle().getString("EmptyRegisterFields"));
                return;
            }

            if (!password.equals(confirmPassword)) {
                App.showAppMessage(App.getBundle().getString("PasswordMismatch"));
                return;
            }

            if (!App.registerAccount(email, password)) {
                App.showAppMessage(App.getBundle().getString("InvalidRegister"));
                return;
            }

            App.setName(email);
            App.refresh(contenedorPrincipal, gestorCartas, "main");
            App.setNavBarVisibility(true);
            App.updateNavSelection("main");
        });
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnRegister, gbc);

        // NUEVO: ¿Ya tienes cuenta?
        gbc.insets = new Insets(10, 0, 0, 0);
        // NOTA: Asegúrate de añadir "AlreadyHaveAccount" en tu archivo Bundle, o cámbialo por texto estático
        JLabel lblAlreadyAccount = new JLabel(App.getBundle().getString("AlreadyHaveAccount")); 
        lblAlreadyAccount.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        gbc.gridy = 8;
        formPanel.add(lblAlreadyAccount, gbc);

        // NUEVO: Enlace para ir al Login
        gbc.insets = new Insets(2, 0, 0, 0);
        JLabel lblLoginLink = new JLabel("<html><u>" + App.getBundle().getString("Login") + "</u></html>");
        lblLoginLink.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        lblLoginLink.setForeground(new Color(100, 150, 255));
        lblLoginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 9;
        lblLoginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Al hacer clic, redirige a la carta "login"
                gestorCartas.show(contenedorPrincipal, "login");
            }
        });
        formPanel.add(lblLoginLink, gbc);

        // Elemento elástico para empujar todo hacia arriba
        gbc.gridy = 10;
        gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), gbc);

        // Añadimos el formulario al centro del BorderLayout
        add(formPanel, BorderLayout.CENTER);
    }

    // Método para crear el desplegable de idioma (mismo que en LoginFrame)
    private JComboBox<String> createLanguageCombo(JPanel contenedorPrincipal, CardLayout gestorCarta) {
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

            // Al refrescar, mantenemos al usuario en la vista de registro
            App.refresh(contenedorPrincipal, gestorCarta, "register");
        });

        return langCombo;
    }

    class roundedText extends JTextField {
        public roundedText(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class passwordText extends JPasswordField {
        public passwordText(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class roundedButton extends JButton {
        public roundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(110, 115, 190));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}