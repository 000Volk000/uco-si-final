import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JPanel {

    public LoginFrame(JPanel contenedorPrincipal, CardLayout gestorCarta) {
        setOpaque(false);
        App.chargeFont();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(120, 0, 20, 0);

        JLabel title = new JLabel(App.getBundle().getString("Welcome"));
        if (App.font() != null) {
            title.setFont(App.font().deriveFont(Font.PLAIN, 36f));
        } else {
            title.setFont(new Font("Serif", Font.PLAIN, 36));
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // nombre correo
        gbc.insets = new Insets(30, 25, 2, 0);
        JLabel lblUser = new JLabel(App.getBundle().getString("NameEmail"));
        lblUser.setFont(App.font().deriveFont(Font.PLAIN, 18f));
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblUser, gbc);

        // campo1
        gbc.insets = new Insets(0, 0, 15, 0);
        roundedText txtUser = new roundedText(0);
        txtUser.setPreferredSize(new Dimension(320, 40));
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(txtUser, gbc);

        // Contraseña
        gbc.insets = new Insets(30, 25, 2, 0);
        JLabel lblPass = new JLabel(App.getBundle().getString("Password"));
        lblPass.setFont(App.font().deriveFont(Font.PLAIN, 18f));
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblPass, gbc);

        // campo2
        gbc.insets = new Insets(0, 0, 5, 0);
        passwordText txtPass = new passwordText(0);
        txtPass.setPreferredSize(new Dimension(320, 40));
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(txtPass, gbc);

        // cambiar contraseña
        gbc.insets = new Insets(0, 0, 30, 0);
        JLabel lblForgot = new JLabel("<html><u>" + App.getBundle().getString("ChangePassword") + "</u></html>");
        lblForgot.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        lblForgot.setForeground(new Color(80, 90, 120));
        lblForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblForgot.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (txtUser.getText().trim().isEmpty()) {
                    App.showAppMessage(App.getBundle().getString("EnterEmailFirst"));
                    return;
                }

                App.showAppMessage(App.getBundle().getString("PasswordChangeEmailSent"));
            }
        });
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblForgot, gbc);

        // Iniciar Sesion
        gbc.insets = new Insets(40, 0, 15, 0);
        roundedButton btnLogin = new roundedButton(App.getBundle().getString("Login"));
        btnLogin.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(220, 45));
        btnLogin.setFont(App.font().deriveFont(Font.PLAIN, 20f));
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        btnLogin.addActionListener(e -> {
            String email = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            if (!App.authenticate(email, password)) {
                App.showAppMessage(App.getBundle().getString("InvalidLogin"));
                return;
            }

            App.setName(email);
            App.refresh(contenedorPrincipal, gestorCarta, "main");
            App.setNavBarVisibility(true);
            App.updateNavSelection("main");
        });
        add(btnLogin, gbc);

        // Aun no tienes cuenta?
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel lblNoAccount = new JLabel(App.getBundle().getString("NoAccount"));
        lblNoAccount.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        gbc.gridy = 7;
        add(lblNoAccount, gbc);

        // Registrate
        gbc.insets = new Insets(2, 0, 0, 0);
        JLabel lblRegister = new JLabel("<html><u>" + App.getBundle().getString("Register") + "</u></html>");
        lblRegister.setFont(App.font().deriveFont(Font.PLAIN, 14f));
        lblRegister.setForeground(new Color(100, 150, 255));
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 8;
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                gestorCarta.show(contenedorPrincipal, "register");
            }
        });
        add(lblRegister, gbc);

        // Guarrada para hacer que todo este mas arriba
        gbc.gridy = 9;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
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
