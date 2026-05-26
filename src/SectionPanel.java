import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SectionPanel extends JPanel {
    private JLabel titleLabel;
    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private MouseAdapter dragScrollListener;

    public SectionPanel(JPanel contenedorPrincipal, CardLayout gestorCartas) {

        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerPanel.add(Box.createVerticalStrut(80));

        titleLabel = new JLabel("");
        titleLabel.setFont(App.font().deriveFont(Font.PLAIN, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 5, 0));
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(10));

        JPanel separatorLine = new JPanel();
        separatorLine.setBackground(Color.decode("#005596"));
        separatorLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        separatorLine.setPreferredSize(new Dimension(Integer.MAX_VALUE, 5));
        headerPanel.add(separatorLine);

        add(headerPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(gridPanel, BorderLayout.NORTH);

        scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
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
        wrapperPanel.addMouseListener(dragScrollListener);
        wrapperPanel.addMouseMotionListener(dragScrollListener);
        gridPanel.addMouseListener(dragScrollListener);
        gridPanel.addMouseMotionListener(dragScrollListener);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadSectionData(String sectionTitle, String[][] items) {
        titleLabel.setText(sectionTitle);
        gridPanel.removeAll();

        for (String[] item : items) {
            String name = item[0];
            String price = item[1];
            String imagePath = item[2];

            gridPanel.add(ProductDatabase.createItemCard(name, price, imagePath, dragScrollListener));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

}