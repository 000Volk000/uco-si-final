import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;


public class SearchFrame extends JPanel {
    
    private RoundedSearchField barraBusqueda;
    private JPanel contenedorResultados;
    private JScrollPane scrollPane;
    private MouseAdapter dragScrollListener;

    public SearchFrame(JPanel contenedorPrincipal, CardLayout gestorCartas) {
        setLayout(new BorderLayout());
        setOpaque(false); 

        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        panelNorte.setOpaque(false);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(140, 0, 0, 0));

        barraBusqueda = new RoundedSearchField();
        barraBusqueda.setPreferredSize(new Dimension(300, 45));
        
        barraBusqueda.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { search(); }
            @Override
            public void removeUpdate(DocumentEvent e) { search(); }
            @Override
            public void changedUpdate(DocumentEvent e) { search(); }
        });

        panelNorte.add(barraBusqueda);
        add(panelNorte, BorderLayout.NORTH);

        contenedorResultados = new JPanel(new GridLayout(0, 1, 15, 15));
        contenedorResultados.setOpaque(false);
        contenedorResultados.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(contenedorResultados, BorderLayout.NORTH);

        scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
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
        contenedorResultados.addMouseListener(dragScrollListener);
        contenedorResultados.addMouseMotionListener(dragScrollListener);

        add(scrollPane, BorderLayout.CENTER);
        
        search();
    } 

    private void search() {
        String texto = barraBusqueda.getText();
        
        String[][] datosFiltrados = ProductDatabase.searchProduct(texto);
        
        contenedorResultados.removeAll();
        
        for (String[] producto : datosFiltrados) {
            String nombre = producto[0];
            String precio = producto[1]; 
            String rutaImagen = producto[2]; 
            String desc= producto[3];
            
            // Ya no necesitamos añadir Box.createRigidArea porque GridLayout maneja el espaciado
            JPanel tarjeta = ProductDatabase.createItemCard(nombre, precio, rutaImagen, desc, dragScrollListener);
            contenedorResultados.add(tarjeta);
        }
        
        contenedorResultados.revalidate();
        contenedorResultados.repaint();
    }


    public class RoundedSearchField extends JPanel {

    private JTextField textArea;
    private int radioRedondeado;
    private Color colorFondo;

    public RoundedSearchField() {
        int columnas=25;
        int radio=40;
        Color fondo=Color.decode("#DEECFF");
        String rutaLupa="src/assets/searchBar/lens.png";
        this.radioRedondeado = radio;
        this.colorFondo = fondo;

        // Configuramos el panel contenedor principal
        setLayout(new BorderLayout(10, 0)); // 10px de separación horizontal entre lupa y texto
        setOpaque(false); // Importante para que se vea el redondeado
        // Añadimos padding interno (Arriba, Izquierda, Abajo, Derecha)
        setBorder(new EmptyBorder(5, 10, 5, 10)); 

        // 1. La Lupa (Izquierda)
        JLabel iconoLupa = new JLabel();
        try {
            if (new File(rutaLupa).exists()) {
                ImageIcon icon = new ImageIcon(rutaLupa);
                // Escalamos la lupa para que quede bien dentro (ej: 20x20)
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                iconoLupa.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar la lupa en: " + rutaLupa);
        }
        add(iconoLupa, BorderLayout.WEST);

        // 2. El campo de texto real (Centro)
        textArea = new JTextField(columnas);
        textArea.setFont(App.font().deriveFont(Font.PLAIN, 16));
        textArea.setForeground(Color.BLACK);
        // Hacemos el JTextField invisible visualmente para que solo se vea el panel redondeado
        textArea.setBorder(null); 
        textArea.setOpaque(false); 
        textArea.setBackground(new Color(0,0,0,0)); // Fondo totalmente transparente

        add(textArea, BorderLayout.CENTER);
    }

    // Método para obtener el JTextField interno (necesario para el DocumentListener)
    public JTextField getTextField() {
        return textArea;
    }

    // Método para obtener el texto fácilmente
    public String getText() {
        return textArea.getText();
    }

    // Dibujamos el fondo redondeado y el color
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(colorFondo);
        // Dibujamos el rectángulo redondeado ocupando todo el panel
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radioRedondeado, radioRedondeado);
        
        g2.dispose();
        super.paintComponent(g);
        }
    }
}