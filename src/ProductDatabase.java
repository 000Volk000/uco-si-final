import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class ProductDatabase {

    public static String[][] searchProduct(String text) {
        
        String[][] products = ProductDatabase.getFishHookData();
        if (text == null || text.trim().isEmpty()) {
            return products;
        }
        List<String[]> coincidencias = new ArrayList<>();
        
        String lower = text.toLowerCase();

        for (int i = 0; i < products.length; i++) {
            String[] product = products[i];
            String name = product[0];
            
            if (name != null && name.toLowerCase().contains(lower)) {
                coincidencias.add(product);
            }
        }

        return coincidencias.toArray(new String[0][0]);
    }

    public static JPanel createItemCard(String name, String price, String desc, String imagePath) {
        return createItemCard(name, price, imagePath,desc,null);
    }
    public static JPanel createItemCard(String name, String price, String imagePath,String desc, MouseAdapter dragScrollListener) {
        JPanel contenedorPrincipal = App.getContenedor();
        CardLayout gestorCartas= App.getCardsGestor();

        RoundedPanel card = new RoundedPanel(40, Color.decode("#E1EFFF"), null, 0);
        card.setLayout(new GridBagLayout());

        card.setPreferredSize(new Dimension(367, 138));
        card.setMinimumSize(new Dimension(367, 138));
        card.setMaximumSize(new Dimension(367, 138));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();

        RoundedPanel imageContainer = new RoundedPanel(30, Color.WHITE, null, 0);
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(175, 105));
        imageContainer.setMinimumSize(new Dimension(175, 105));

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            if (new File(imagePath).exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(140, 85, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Fallo al cargar: " + imagePath);
        }
        imageContainer.add(imgLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 16, 0, 15);
        gbc.anchor = GridBagConstraints.WEST;
        card.add(imageContainer, gbc);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        String htmlName = "<html><div style='text-align: center; width: 120px;'>" + name + "</div></html>";

        JLabel nameLabel = new JLabel(htmlName);
        nameLabel.setFont(App.font().deriveFont(Font.PLAIN, 18));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(price + " \u20AC");
        priceLabel.setFont(App.font().deriveFont(Font.PLAIN, 20));
        priceLabel.setForeground(Color.decode("#8DCA79"));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(priceLabel);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 16);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(textPanel, gbc);

        card.addMouseListener(dragScrollListener);
        card.addMouseMotionListener(dragScrollListener);

        if (dragScrollListener != null) {
            card.addMouseListener(dragScrollListener);
            card.addMouseMotionListener(dragScrollListener);
        }
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                App.product.setProductData(
                        name,
                        desc,
                        price,
                        imagePath,
                        "src/assets/Products/cart.png");
                gestorCartas.show(contenedorPrincipal, "product");
            }
        });

        return card;
    }

    public static String[][] getFishHookData() {
        return new String[][] {
                { App.getBundle().getString("sebastian"), "8.99", "src/assets/fishHook/sebastian.png",
                        App.getBundle().getString("sebastianDesc") },
                { App.getBundle().getString("destructor"), "12.30", "src/assets/fishHook/wildlifeDestructor.png",
                        App.getBundle().getString("destructorDesc") },
                { App.getBundle().getString("crazyEye"), "4.99", "src/assets/fishHook/crazyEye.png",
                        App.getBundle().getString("crazyEyeDesc") },
                { App.getBundle().getString("classicHook"), "2.10", "src/assets/fishHook/hook?.png",
                        App.getBundle().getString("classicHookDesc") },
                { App.getBundle().getString("ladyHook"), "5.75", "src/assets/fishHook/lady.png",
                        App.getBundle().getString("ladyHookDesc") },
                { App.getBundle().getString("gamingHook"), "9.50", "src/assets/fishHook/gamingHook.png",
                        App.getBundle().getString("gamingHookDesc") },
                { App.getBundle().getString("joaquinCortes"), "15.00", "src/assets/fishHook/joaquinCortes.png",
                        App.getBundle().getString("joaquinCortesDesc") }
        };
    }

}