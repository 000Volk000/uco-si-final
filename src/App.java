import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.List;

class BackgroundPanel extends JPanel {
    private Image scaleTopLeft;
    private Image scaleLowLeft;
    private Image scaleTopRight;
    private Image koi;

    public BackgroundPanel() {
        scaleTopLeft = new ImageIcon("src/assets/background/topLeft.png").getImage().getScaledInstance(247, 201,
                Image.SCALE_SMOOTH);
        scaleLowLeft = new ImageIcon("src/assets/background/bottomLeft.png").getImage().getScaledInstance(195, 192,
                Image.SCALE_SMOOTH);
        scaleTopRight = new ImageIcon("src/assets/background/topRight.png").getImage().getScaledInstance(147, 186,
                Image.SCALE_SMOOTH);
        this.koi = new ImageIcon("src/assets/background/koi.png").getImage().getScaledInstance(221, 248,
                Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        if (scaleTopLeft != null)
            g.drawImage(scaleTopLeft, 0, 0, this);
        if (scaleLowLeft != null)
            g.drawImage(scaleLowLeft, 0, height - scaleLowLeft.getHeight(this), this);
        if (scaleTopRight != null)
            g.drawImage(scaleTopRight, width - scaleTopRight.getWidth(this), 0, this);
        if (koi != null)
            g.drawImage(koi, 176, 589, this);
    }
}

public class App {
    private static ResourceBundle bundle;
    private static JPanel bottomBar;
    private static String username;

    private static JButton btnHome;
    private static JButton btnCart;
    private static JButton btnHistory;
    private static JButton btnAccount;
    private static JButton btnSearch;

    public static String getName() {
        return username;
    }

    public static void setName(String newUsername) {
        username = newUsername;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static void setNavBarVisibility(boolean state) {
        bottomBar.setVisible(state);
    }

    public static void setLocale(Locale newLocale) {
        bundle = ResourceBundle.getBundle("assets.bundle.Bundle", newLocale);
    }

    public static void main(String[] args) {
        // Canva creation
        bundle = Deflanguage();
        JFrame jf = new JFrame("Pezqueñín");

        jf.setSize(402, 874);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        // Background
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setBackground(Color.decode("#B0C2DB"));

        bgPanel.setLayout(new BorderLayout());

        CardLayout gestorCartas = new CardLayout();
        JPanel contenedorPantallas = new JPanel(gestorCartas);
        contenedorPantallas.setOpaque(false);

        // Navbar
        bottomBar = new JPanel(new GridLayout(1, 4));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setPreferredSize(new Dimension(402, 65));
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        btnHome = createNavButton("src/assets/bottomBar/home.png");
        btnCart = createNavButton("src/assets/bottomBar/cart.png");
        btnHistory = createNavButton("src/assets/bottomBar/history.png");
        btnAccount = createNavButton("src/assets/bottomBar/account.png");
        btnSearch = createNavButton("src/assets/bottomBar/search.png");


        btnHome.addActionListener(e -> {
            gestorCartas.show(contenedorPantallas, "main");
            updateNavSelection("main");
        });

        btnSearch.addActionListener(e -> {
            gestorCartas.show(contenedorPantallas, "search");
            updateNavSelection("search");
        });

        btnCart.addActionListener(e -> {
            gestorCartas.show(contenedorPantallas, "cart");
            updateNavSelection("cart");
        });

        btnHistory.addActionListener(e -> {
            gestorCartas.show(contenedorPantallas, "history");
            updateNavSelection("history");
        });

        btnAccount.addActionListener(e -> {
            gestorCartas.show(contenedorPantallas, "account");
            updateNavSelection("account");
        });

        bottomBar.add(wrapButton(btnHome));
        bottomBar.add(wrapButton(btnSearch));
        bottomBar.add(wrapButton(btnCart));
        bottomBar.add(wrapButton(btnHistory));
        bottomBar.add(wrapButton(btnAccount));

        // Main
        MainScreen mainScreen = new MainScreen(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(mainScreen, "main");

        // Login
        LoginFrame login = new LoginFrame(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(login, "login");

        bgPanel.add(contenedorPantallas, BorderLayout.CENTER);
        gestorCartas.show(contenedorPantallas, "login");

        // Register
        RegisterFrame register = new RegisterFrame(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(register, "register");
        bgPanel.add(contenedorPantallas, BorderLayout.CENTER);

        Account account = new Account(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(account, "account");
        bgPanel.add(contenedorPantallas, BorderLayout.CENTER);

        bgPanel.add(bottomBar, BorderLayout.SOUTH);
        setNavBarVisibility(false);
        jf.setContentPane(bgPanel);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static ResourceBundle Deflanguage() {
        Locale currentLocale = Locale.getDefault();
        // currentLocale= Locale.of("en", "GB");
        if (!((currentLocale.getLanguage().equals("es") && currentLocale.getCountry().equals("ES"))
                || (currentLocale.getLanguage().equals("en") && currentLocale.getCountry().equals("GB")))) {
            currentLocale = new Locale.Builder().setLanguage("es").setRegion("ES").build();
        }
        ResourceBundle bundle_text = ResourceBundle.getBundle("assets.bundle.Bundle", currentLocale);
        return bundle_text;

    }

    private static JButton createNavButton(String iconPath) {
        JButton btn = new JButton();
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + iconPath);
        }
        return btn;
    }

    // warp para que no se cambie el raton en todo el navbar y solo en los iconos
    private static JPanel wrapButton(JButton btn) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(btn);
        return wrapper;
    }

    public static void refresh(JPanel contenedor, CardLayout gestor, String current) {
        String[] names = { "account", "main", "login", "register" };
        List<Class<? extends JPanel>> classes = List.of(Account.class, MainScreen.class, LoginFrame.class,
                RegisterFrame.class);
        contenedor.removeAll();
        try {
            for (int i = 0; i < classes.size(); i++) {
                Object nuevoPanel = classes.get(i).getConstructor(JPanel.class, CardLayout.class)
                        .newInstance(contenedor, gestor);
                contenedor.add((Component) nuevoPanel, names[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gestor.show(contenedor, current);

        contenedor.revalidate();
        contenedor.repaint();
    }

    private static void updateButtonIcon(JButton btn, String iconPath) {
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + iconPath);
        }
    }


    public static void updateNavSelection(String selectedTab) {
        updateButtonIcon(btnHome, "src/assets/bottomBar/home.png");
        updateButtonIcon(btnCart, "src/assets/bottomBar/cart.png");
        updateButtonIcon(btnHistory, "src/assets/bottomBar/history.png");
        updateButtonIcon(btnAccount, "src/assets/bottomBar/account.png");
        updateButtonIcon(btnSearch, "src/assets/bottomBar/search.png");


        switch (selectedTab) {
            case "main":
                updateButtonIcon(btnHome, "src/assets/bottomBar/homeSelected.png");
                break;
            case "cart":
                updateButtonIcon(btnCart, "src/assets/bottomBar/cartSelected.png");
                break;
            case "history":
                updateButtonIcon(btnHistory, "src/assets/bottomBar/historySelected.png");
                break;
            case "account":
                updateButtonIcon(btnAccount, "src/assets/bottomBar/accountSelected.png");
                break;
            case "search":
                updateButtonIcon(btnSearch, "src/assets/bottomBar/searchSelected.png");
                break;
        }
    }
}