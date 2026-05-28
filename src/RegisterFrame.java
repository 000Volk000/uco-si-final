import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RegisterFrame extends JPanel {

    private Font inikaFont;

    public RegisterFrame(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        App.chargeFont();
        setOpaque(false);
        chargeFont();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(120, 0, 20, 0);

        JLabel title = new JLabel(App.getBundle().getString("Welcome"));
        if (inikaFont != null) {
            title.setFont(inikaFont.deriveFont(Font.PLAIN, 36f));
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
        lblUser.setFont(inikaFont.deriveFont(Font.PLAIN, 18f));
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
        lblPass.setFont(inikaFont.deriveFont(Font.PLAIN, 18f));
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

        // Confirmar Contraseña
        gbc.insets = new Insets(35, 25, 2, 0);
        JLabel lblConfirmPass = new JLabel(App.getBundle().getString("ChangePassword"));
        lblConfirmPass.setFont(inikaFont.deriveFont(Font.PLAIN, 18f));
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblConfirmPass, gbc);

        // campo3
        gbc.insets = new Insets(0, 0, 5, 0);
        passwordText txtConfirmPass = new passwordText(0);
        txtConfirmPass.setPreferredSize(new Dimension(320, 40));
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        add(txtConfirmPass, gbc);

        // Crear Cuenta
        gbc.insets = new Insets(40, 0, 15, 0);
        roundedButton btnLogin = new roundedButton(App.getBundle().getString("CreateAccount"));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(220, 45));
        btnLogin.setFont(inikaFont.deriveFont(Font.PLAIN, 20f));
        btnLogin.addActionListener(e -> {
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
            App.setNavBarVisibility(true);
            gestorCartas.show(contenedorPrincipal, "main");
            App.updateNavSelection("main");
        });
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnLogin, gbc);

        // Guarrada para hacer que todo este mas arriba
        gbc.gridy = 8;
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

    private void chargeFont() {
        try {
            File archivoFuente = new File("src/assets/fonts/Inika-Regular.ttf");
            Font fuenteBase = Font.createFont(Font.TRUETYPE_FONT, archivoFuente);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fuenteBase);
            inikaFont = fuenteBase;

        } catch (FontFormatException | IOException e) {
            System.err.println(e.getMessage());
            inikaFont = null;
        }
    }
}
