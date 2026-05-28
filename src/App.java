import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    private static JPanel messageBanner;
    private static JLabel messageLabel;
    private static Timer messageTimer;
    private static JFrame mainFrame;
    private static String username;

    private static JButton btnHome;
    private static JButton btnCart;
    private static JButton btnHistory;
    private static JButton btnAccount;
    private static JButton btnSearch;

    private static Font inikaFont;

    private static CardLayout gestorCartas;
    private static JPanel contenedorPantallas;

    public static CartFrame cartFrame;
    public static PurchaseHistoryFrame historyFrame;

    public static java.util.List<CartItem> shoppingCart = new java.util.ArrayList<>();
    public static java.util.List<CartItem> purchaseHistory = new java.util.ArrayList<>();

    private static final Color MESSAGE_BACKGROUND = new Color(47, 54, 64, 235);

    public static TrackingFrame tracking;

    public static Font font() {
        return inikaFont;
    }

    public static Product product;

    public static SectionPanel sectionPanel;

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

    public static void showAppMessage(String text) {
        if (messageLabel == null || messageBanner == null) {
            return;
        }

        messageLabel.setText(text);
        updateMessageBannerBounds();
        messageBanner.setVisible(true);
        if (mainFrame != null) {
            mainFrame.getGlassPane().setVisible(true);
        }

        messageBanner.revalidate();
        messageBanner.repaint();

        if (messageTimer != null && messageTimer.isRunning()) {
            messageTimer.stop();
        }

        messageTimer = new Timer(1400, e -> {
            messageBanner.setVisible(false);
            messageBanner.revalidate();
            messageBanner.repaint();
            if (mainFrame != null) {
                mainFrame.getGlassPane().setVisible(false);
            }
        });
        messageTimer.setRepeats(false);
        messageTimer.start();
    }

    public static void setLocale(Locale newLocale) {
        bundle = ResourceBundle.getBundle("assets.bundle.Bundle", newLocale);
    }

    public static CardLayout getCardsGestor() {
        return gestorCartas;
    }

    public static JPanel getContenedor() {
        return contenedorPantallas;
    }

    public static void main(String[] args) {
        // Canva creation
        bundle = Deflanguage();
        chargeFont();
        mainFrame = new JFrame("Pezqueñín");

        mainFrame.setSize(402, 874);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);

        // Background
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setBackground(Color.decode("#B0C2DB"));

        bgPanel.setLayout(new BorderLayout());

        gestorCartas = new CardLayout();
        contenedorPantallas = new JPanel(gestorCartas);
        contenedorPantallas.setOpaque(false);

        // Navbar
        bottomBar = new JPanel(new GridLayout(1, 4));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setPreferredSize(new Dimension(402, 65));
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(inikaFont != null ? inikaFont.deriveFont(Font.PLAIN, 16f)
                : new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        messageBanner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MESSAGE_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        messageBanner.setOpaque(false);
        messageBanner.add(messageLabel, BorderLayout.CENTER);
        messageBanner.setVisible(false);
        messageBanner.setBorder(BorderFactory.createEmptyBorder(0, 12, 6, 12));
        messageBanner.setPreferredSize(new Dimension(320, 44));
        messageBanner.setMaximumSize(new Dimension(320, 44));
        messageBanner.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel messageHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        messageHolder.setOpaque(false);
        messageHolder.setBorder(BorderFactory.createEmptyBorder(0, 12, 6, 12));
        messageHolder.add(messageBanner);

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
            App.cartFrame.refreshCart();
            gestorCartas.show(contenedorPantallas, "cart");
            updateNavSelection("cart");
        });

        btnHistory.addActionListener(e -> {
            App.historyFrame.refreshHistory();
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

        // Producto
        product = new Product(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(product, "product");

        sectionPanel = new SectionPanel(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(sectionPanel, "sectionView");

        SearchFrame search = new SearchFrame(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(search, "search");

        cartFrame = new CartFrame(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(cartFrame, "cart");

        historyFrame = new PurchaseHistoryFrame(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(historyFrame, "history");


        tracking = new TrackingFrame(contenedorPantallas, gestorCartas);
        contenedorPantallas.add(tracking, "tracking");


        bgPanel.add(bottomBar, BorderLayout.SOUTH);
        setNavBarVisibility(false);
        mainFrame.setContentPane(bgPanel);

        JPanel glass = new JPanel(null);
        glass.setOpaque(false);
        glass.add(messageBanner);
        mainFrame.setGlassPane(glass);
        glass.setVisible(false);
        updateMessageBannerBounds();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
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

    private static JPanel wrapButton(JButton btn) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(btn);
        return wrapper;
    }

    private static void updateMessageBannerBounds() {
        if (mainFrame == null || messageBanner == null) {
            return;
        }

        int bannerWidth = 320;
        int bannerHeight = 44;
        int frameWidth = mainFrame.getWidth();
        int frameHeight = mainFrame.getHeight();
        int x = Math.max(12, (frameWidth - bannerWidth) / 2);
        int y = Math.max(12, frameHeight - bottomBar.getPreferredSize().height - bannerHeight - 14);

        messageBanner.setBounds(x, y, bannerWidth, bannerHeight);
    }

    public static void refresh(JPanel contenedor, CardLayout gestor, String current) {
        String[] names = { "account", "main", "login", "register", "product", "sectionView", "search",
                "cart", "history" };
        List<Class<? extends JPanel>> classes = List.of(
                Account.class, MainScreen.class, LoginFrame.class, RegisterFrame.class, Product.class,
                SectionPanel.class, SearchFrame.class, CartFrame.class, PurchaseHistoryFrame.class);

        contenedor.removeAll();

        try {
            for (int i = 0; i < classes.size(); i++) {
                Object nuevoPanel = classes.get(i).getConstructor(JPanel.class, CardLayout.class)
                        .newInstance(contenedor, gestor);
                contenedor.add((Component) nuevoPanel, names[i]);

                if (names[i].equals("product")) {
                    product = (Product) nuevoPanel;
                } else if (names[i].equals("sectionView")) {
                    sectionPanel = (SectionPanel) nuevoPanel;
                } else if (names[i].equals("cart")) {
                    cartFrame = (CartFrame) nuevoPanel;
                } else if (names[i].equals("history")) {
                    historyFrame = (PurchaseHistoryFrame) nuevoPanel;
                }
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

    public static void chargeFont() {
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

    public static void recordPurchase(java.util.List<CartItem> items) {
        for (CartItem historyItem : purchaseHistory) {
            historyItem.setLastPurchase(false);
        }

        for (CartItem item : items) {
            CartItem historyItem = item.copy();
            historyItem.setLastPurchase(true);
            purchaseHistory.add(historyItem);
        }

        if (historyFrame != null) {
            historyFrame.refreshHistory();
        }
    }
}