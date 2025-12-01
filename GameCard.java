import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

// Tarjeta visual para representar un juego en la interfaz gráfica 
class GameCard extends JPanel {
    private Juego juego;

    // Constructor recibe el juego a mostrar, las colecciones disponibles para añadir, y callbacks para acciones
    public GameCard(Juego juego, List<Subcoleccion> coleccionesDisponibles, 
                    Consumer<Juego> onEdit, Consumer<Juego> onDetail, Consumer<Juego> onDelete) {
        this.juego = juego;
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO_PANEL);
        
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        setPreferredSize(new Dimension(180, 260));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Imagen o título si no hay imagen
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        boolean imgCargada = false;
        if (juego.getRutaImagen() != null && !juego.getRutaImagen().isEmpty()) {
            java.io.File f = new java.io.File(juego.getRutaImagen());
            if(f.exists()){
                ImageIcon icon = new ImageIcon(juego.getRutaImagen());
                Image img = icon.getImage().getScaledInstance(180, 200, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(img));
                imgCargada = true;
            }
        } 
        if(!imgCargada) {
            lblImagen.setText("<html><center>" + juego.getTitulo() + "</center></html>");
            lblImagen.setForeground(Estilos.TEXTO_SECUNDARIO);
        }
        add(lblImagen, BorderLayout.CENTER);

        // Panel inferior con título y calificación
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Estilos.FONDO_PANEL);
        JLabel lblTitulo = new JLabel(juego.getTitulo());
        lblTitulo.setForeground(Estilos.TEXTO_BLANCO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Estrella amarilla para la calificación
        JLabel lblRating = new JLabel("★ " + juego.getCalificacion());
        lblRating.setForeground(Color.YELLOW);
        lblRating.setHorizontalAlignment(SwingConstants.CENTER);
        
        infoPanel.add(lblTitulo);
        infoPanel.add(lblRating);
        add(infoPanel, BorderLayout.SOUTH);

        // Menú contextual al hacer clic derecho
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem menuEditar = new JMenuItem("✏️ Editar");
        menuEditar.addActionListener(e -> onEdit.accept(juego));
        contextMenu.add(menuEditar);

        JMenuItem menuEliminar = new JMenuItem("🗑️ Eliminar");
        menuEliminar.setForeground(Color.RED);
        menuEliminar.addActionListener(e -> onDelete.accept(juego));
        contextMenu.add(menuEliminar);
        
        contextMenu.addSeparator();

        JMenu menuAgregar = new JMenu("📂 Añadir a...");
        if (coleccionesDisponibles.isEmpty()) {
            menuAgregar.setEnabled(false);
        } else {
            for (Subcoleccion sub : coleccionesDisponibles) {
                JMenuItem item = new JMenuItem(sub.toString());
                item.addActionListener(e -> {
                    sub.agregarJuego(this.juego);
                    JOptionPane.showMessageDialog(this, "Añadido a: " + sub.toString());
                });
                menuAgregar.add(item);
            }
        }
        contextMenu.add(menuAgregar);

        // Listeners para clics izquierdo y derecho
        MouseAdapter clickListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) { manejarClick(e); }
            public void mouseReleased(MouseEvent e) { manejarClick(e); }
            private void manejarClick(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e) && e.getID() == MouseEvent.MOUSE_RELEASED) {
                    onDetail.accept(juego);
                }
            }
        };
        this.addMouseListener(clickListener);
        lblImagen.addMouseListener(clickListener);
        infoPanel.addMouseListener(clickListener);
        lblTitulo.addMouseListener(clickListener);
    }
}