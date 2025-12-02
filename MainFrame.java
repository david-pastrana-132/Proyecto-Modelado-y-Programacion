//Integrantes del equipo: Pastrana Trujillo David. Barbosa Olivares Keit Ehecatl

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

// Clase principal de la aplicación del gestor de videojuegos
public class MainFrame extends JFrame {

    // Atributos principales de la ventana y la biblioteca de juegos 
    private Biblioteca biblioteca;
    private JPanel gridPanel;
    private JPanel statsPanel; 
    private JPanel sidebarContent;
    private List<Juego> vistaActual; 
    private JTextField txtBuscador; 

    // Constructor de la ventana principal 
    public MainFrame() {
        biblioteca = Biblioteca.cargarDesdeDisco();
        boolean primeraVez = (biblioteca == null);
        // Si no hay datos guardados, creamos una nueva biblioteca
        if (primeraVez) biblioteca = new Biblioteca();
        configurarVentana();
        inicializarComponentes();
        if (primeraVez) cargarDatosPrueba();
    
        actualizarSidebar();
        vistaActual = biblioteca.getTodosLosJuegos();
        mostrarJuegos(vistaActual);
    }

    // Configuración básica de la ventana 
    private void configurarVentana() {
        setTitle("Gestor de Videojuegos");
        setSize(1280, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    // Inicialización y disposición de los componentes en la ventana  
    private void inicializarComponentes() {
        // --- BARRA LATERAL ---
        JPanel sidebarContainer = new JPanel(new BorderLayout());
        sidebarContainer.setBackground(Estilos.FONDO_OSCURO);
        sidebarContainer.setPreferredSize(new Dimension(240, 0));

        // Logo o título de la aplicación
        JLabel logo = new JLabel("MI BIBLIOTECA");
        logo.setForeground(Estilos.ACENTO);
        logo.setFont(Estilos.FUENTE_TITULO);
        logo.setBorder(new EmptyBorder(25, 20, 25, 20));
        sidebarContainer.add(logo, BorderLayout.NORTH);

        sidebarContent = new JPanel();
        sidebarContent.setLayout(new BoxLayout(sidebarContent, BoxLayout.Y_AXIS));
        sidebarContent.setBackground(Estilos.FONDO_OSCURO);
        
        // Contenedor scrollable para la sidebar
        JScrollPane scrollSidebar = new JScrollPane(sidebarContent);
        scrollSidebar.setBorder(null);
        sidebarContainer.add(scrollSidebar, BorderLayout.CENTER);

        // Botón para crear nueva subcolección
        JButton btnNuevaCol = new JButton("+ Nueva Subcolección");
        estilizarBoton(btnNuevaCol, true);
        btnNuevaCol.addActionListener(e -> dialogoNuevaColeccion());
        sidebarContainer.add(btnNuevaCol, BorderLayout.SOUTH);

        add(sidebarContainer, BorderLayout.WEST);

        // --- PANEL CENTRAL ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Estilos.FONDO_PANEL);

      // Contenedor superior (Header + Stats)
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(Estilos.FONDO_PANEL);

        // 1. Header (Botón Agregar)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.setBackground(Estilos.FONDO_PANEL);
        headerPanel.setBorder(new EmptyBorder(10, 10, 0, 20));

        JButton btnAddGame = new JButton("AGREGAR JUEGO NUEVO");
        estilizarBoton(btnAddGame, true);
        btnAddGame.addActionListener(e -> mostrarDialogoJuego(null)); 
        headerPanel.add(btnAddGame);
        
        // 2. Panel de estadísticas 
        statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setBackground(Estilos.FONDO_OSCURO); // Fondo oscuro para resaltar
        statsPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Estilos.ACENTO));
        statsPanel.setVisible(false); // Oculto por defecto

        topContainer.add(headerPanel);
        topContainer.add(statsPanel);
        
        mainContent.add(topContainer, BorderLayout.NORTH);


        // --- GRID DE JUEGOS ---
        
        // 1. Usamos GridLayout(0, 8) -> 0 filas, 8 columnas
        gridPanel = new JPanel(new GridLayout(0, 8, 15, 15));
        gridPanel.setBackground(Estilos.FONDO_PANEL);
        
        // 2. Truco para que los juegos no se estiren verticalmente si hay pocos
        // Metemos el grid dentro de un panel BorderLayout al Norte
        JPanel contenedorGrid = new JPanel(new BorderLayout());
        contenedorGrid.setBackground(Estilos.FONDO_PANEL);
        contenedorGrid.add(gridPanel, BorderLayout.NORTH);
        
        // 3. Scroll Pane configurado
        JScrollPane scrollGrid = new JScrollPane(contenedorGrid); // <--- OJO AQUÍ
        scrollGrid.setBorder(null);
        scrollGrid.getVerticalScrollBar().setUnitIncrement(16);
        
        // Configurar políticas de scroll para forzar vertical
        scrollGrid.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollGrid.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        mainContent.add(scrollGrid, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
    }

    // --- DIÁLOGO DE DETALLES DEL JUEGO ---
   private void mostrarDetallesVisuales(Juego juego) {
        JDialog dialog = new JDialog(this, "Detalles: " + juego.getTitulo(), true);
        dialog.setSize(950, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Estilos.FONDO_PANEL);

        // --- 1. IMAGEN (Izquierda) ---
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(Color.BLACK);
        panelImagen.setPreferredSize(new Dimension(380, 0));
        
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        boolean imgOk = false;
        if(juego.getRutaImagen() != null && !juego.getRutaImagen().isEmpty()) {
            if(new java.io.File(juego.getRutaImagen()).exists()) {
                ImageIcon icon = new ImageIcon(juego.getRutaImagen());
                Image img = icon.getImage().getScaledInstance(380, 550, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(img));
                imgOk = true;
            }
        }
        if(!imgOk) { 
            lblImg.setText("SIN CARÁTULA"); 
            lblImg.setForeground(Color.GRAY);
            lblImg.setFont(new Font("Segoe UI", Font.BOLD, 18));
        }
        panelImagen.add(lblImg, BorderLayout.CENTER);
        dialog.add(panelImagen, BorderLayout.WEST);

        // --- 2. INFORMACIÓN (Centro) ---
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Estilos.FONDO_PANEL);
        panelInfo.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título
        JLabel lblTitulo = new JLabel(juego.getTitulo());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Estilos.ACENTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblTitulo);

        // Plataforma y Año
        JLabel lblSub = new JLabel(juego.getPlataformasTexto() + "  •  " + juego.getAñoLanzamiento());
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSub.setForeground(Estilos.TEXTO_SECUNDARIO);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblSub);

        panelInfo.add(Box.createVerticalStrut(25));

        // Estadísticas rápidas (calificación, precio, etc.)
        JPanel pnlStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlStats.setBackground(Estilos.FONDO_PANEL);
        pnlStats.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pnlStats.add(crearBadge("★ " + juego.getCalificacion() + "/10", Color.YELLOW));
        pnlStats.add(Box.createHorizontalStrut(15));
        pnlStats.add(crearBadge("$" + juego.getPrecio(), Color.GREEN));
        pnlStats.add(Box.createHorizontalStrut(15));
        pnlStats.add(crearBadge(juego.getClasificacionEdades(), Color.ORANGE));
        pnlStats.add(Box.createHorizontalStrut(15));
        pnlStats.add(crearBadge(juego.getDuracionHoras() + " Horas", Color.CYAN));
        
        panelInfo.add(pnlStats);
        panelInfo.add(Box.createVerticalStrut(30));

        // Usamos GridBagLayout para tener control total de las columnas
        // y evitar que se separen demasiado.
        JPanel gridDetalles = new JPanel(new GridBagLayout());
        gridDetalles.setBackground(Estilos.FONDO_PANEL);
        gridDetalles.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Añadimos los datos fila por fila
        agregarFilaGrid(gridDetalles, 0, "Género:", juego.getGenero());
        agregarFilaGrid(gridDetalles, 1, "Desarrollador:", juego.getDesarrollador());
        agregarFilaGrid(gridDetalles, 2, "Editor:", juego.getEditores());
        agregarFilaGrid(gridDetalles, 3, "Jugadores:", juego.getNumJugadores() + " Jugador(es)");
        agregarFilaGrid(gridDetalles, 4, "Conectividad:", juego.getConectividad());
        
        // Envolvemos el grid en un panel FlowLayout LEFT para que no se estire
        JPanel wrapperGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapperGrid.setBackground(Estilos.FONDO_PANEL);
        wrapperGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapperGrid.add(gridDetalles);
        
        panelInfo.add(wrapperGrid);
        panelInfo.add(Box.createVerticalStrut(30));

        // Comentarios
        JLabel lblDesc = new JLabel("COMENTARIOS");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDesc.setForeground(Estilos.TEXTO_SECUNDARIO);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblDesc);
        
        panelInfo.add(Box.createVerticalStrut(10));
        
        // Área de texto para comentarios
        JTextArea txtDesc = new JTextArea(juego.getComentarios());
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(Estilos.FONDO_OSCURO);
        txtDesc.setForeground(Estilos.TEXTO_BLANCO);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesc.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        scrollDesc.setBorder(null);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(scrollDesc);

        dialog.add(panelInfo, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // --- DIÁLOGO DE CREAR/EDITAR JUEGO ---
        private void mostrarDialogoJuego(Juego juegoAEditar) {
        boolean esEdicion = (juegoAEditar != null);
        JDialog dialog = new JDialog(this, esEdicion ? "Editar Juego" : "Nuevo Juego", true);
        dialog.setSize(700, 750); // Un poco más ancha para las plataformas
        dialog.setLocationRelativeTo(this);
        
        // Panel principal con scroll
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(Estilos.FONDO_PANEL);
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setBorder(null);

        // Campos Simples
        JTextField txtTitulo = new JTextField(esEdicion ? juegoAEditar.getTitulo() : "");
    
        //         -- PANEL DE PLATAFORMAS (Checkboxes) ---
        JPanel pnlPlataformas = new JPanel(new GridLayout(0, 2)); // 2 columnas de consolas
        pnlPlataformas.setBackground(Estilos.FONDO_PANEL);
        pnlPlataformas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Estilos.TEXTO_SECUNDARIO), "Plataformas", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            Estilos.FUENTE_NORMAL, Estilos.TEXTO_BLANCO));
            
        java.util.List<JCheckBox> listaChecks = new java.util.ArrayList<>();
        
        // Creamos una Checkbox por cada consola disponible
        for (String consola : DatosComunes.LISTA) {
            JCheckBox chk = new JCheckBox(consola);
            chk.setForeground(Estilos.TEXTO_BLANCO);
            chk.setBackground(Estilos.FONDO_PANEL);
            chk.setFocusPainted(false);
            
            // Si editamos, marcar las que el juego ya tiene
            if (esEdicion && juegoAEditar.getPlataformas().contains(consola)) {
                chk.setSelected(true);
            }
            
            listaChecks.add(chk);
            pnlPlataformas.add(chk);
        }
        // Scroll pequeño para las plataformas por si son muchas
        JScrollPane scrollPlats = new JScrollPane(pnlPlataformas);
        scrollPlats.setPreferredSize(new Dimension(0, 150)); 
        // -------------------------------------------------

        // Creamos la lista desplegable usando la lista de arriba
        JComboBox<String> cbGenero = new JComboBox<>(DatosComunes.GENEROS);

        // Si estamos editando, seleccionamos el género que ya tenía el juego
        if(esEdicion) {
            cbGenero.setSelectedItem(juegoAEditar.getGenero());
        }        
        JTextField txtaño = new JTextField(esEdicion ? String.valueOf(juegoAEditar.getAñoLanzamiento()) : "---");
        JTextField txtRating = new JTextField(esEdicion ? String.valueOf(juegoAEditar.getCalificacion()) : "---");
        JTextField txtDesarrollador = new JTextField(esEdicion ? juegoAEditar.getDesarrollador() : "---");
        JTextField txtEditores = new JTextField(esEdicion ? juegoAEditar.getEditores() : "---");
        JTextField txtDuracion = new JTextField(esEdicion ? String.valueOf(juegoAEditar.getDuracionHoras()) : "---");
        JTextField txtPrecio = new JTextField(esEdicion ? String.valueOf(juegoAEditar.getPrecio()) : "---");
        JTextField txtJugadores = new JTextField(esEdicion ? String.valueOf(juegoAEditar.getNumJugadores()) : "---");
        
        JComboBox<String> cbConectividad = new JComboBox<>(new String[]{"Local", "Online", "Local y Online", "Desconocido"});
        if(esEdicion) cbConectividad.setSelectedItem(juegoAEditar.getConectividad());

        JComboBox<String> cbESRB = new JComboBox<>(new String[]{"E", "E10+", "T", "M", "AO", "RP"});
        if(esEdicion) cbESRB.setSelectedItem(juegoAEditar.getClasificacionEdades());

        JTextArea txtComentarios = new JTextArea(esEdicion ? juegoAEditar.getComentarios() : "");
        txtComentarios.setRows(3);
        
        // Botón para seleccionar imagen
        JButton btnImagen = new JButton(esEdicion ? "Cambiar Imagen..." : "Seleccionar Imagen...");
        estilizarBoton(btnImagen, false);
        final String[] rutaImagenSeleccionada = { esEdicion ? juegoAEditar.getRutaImagen() : null };
        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png"));
            if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                rutaImagenSeleccionada[0] = fc.getSelectedFile().getAbsolutePath();
                btnImagen.setText("¡Imagen Cargada!");
                btnImagen.setBackground(Color.GREEN);
            }
        });

        // Agregar Componentes
        agregarCampo(formPanel, "Título (*):", txtTitulo);
        // El panel de plataformas ocupa el ancho 
        formPanel.add(crearLabel("Plataformas:")); 
        formPanel.add(scrollPlats);
        
        agregarCampo(formPanel, "Género:", cbGenero); // <--- Usamos cbGenero        
        agregarCampo(formPanel, "Año:", txtaño);
        agregarCampo(formPanel, "Calificación:", txtRating);
        agregarCampo(formPanel, "Desarrollador:", txtDesarrollador);
        agregarCampo(formPanel, "Editor:", txtEditores);
        agregarCampo(formPanel, "Duración (h):", txtDuracion);
        agregarCampo(formPanel, "Precio ($):", txtPrecio);
        agregarCampo(formPanel, "Jugadores:", txtJugadores);
        agregarCampo(formPanel, "Conectividad:", cbConectividad);
        agregarCampo(formPanel, "Clasificación:", cbESRB);
        agregarCampo(formPanel, "Carátula:", btnImagen);
        formPanel.add(crearLabel("Comentarios:")); formPanel.add(new JScrollPane(txtComentarios));

        JButton btnGuardar = new JButton("GUARDAR DATOS");
        estilizarBoton(btnGuardar, true);

        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            try {
                // 1. VALIDACIÓN DE TÍTULO OBLIGATORIO
                String nuevoTitulo = txtTitulo.getText().trim();
                if (nuevoTitulo.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. VALIDACIÓN DE DUPLICADOS 
                // Si estamos CREANDO un juego nuevo y ya existe uno con ese nombre...
                if (!esEdicion && biblioteca.existeJuego(nuevoTitulo)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "¡El juego '" + nuevoTitulo + "' ya existe en tu biblioteca!\nNo se permiten duplicados.", 
                        "Juego Duplicado", 
                        JOptionPane.WARNING_MESSAGE);
                    return; // DETIENE EL GUARDADO AQUÍ
                }
                
                // Si estamos EDITANDO y cambiamos el nombre a uno que YA existe (de otro juego)...
                if (esEdicion && !juegoAEditar.getTitulo().equalsIgnoreCase(nuevoTitulo)) {
                     if (biblioteca.existeJuego(nuevoTitulo)) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Ya existe otro juego con el nombre '" + nuevoTitulo + "'.", 
                            "Nombre no disponible", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                     }
                }

                // 3. NUEVA VALIDACIÓN DE "POSIBLES ERRORES DE DEDO" 
                if (!esEdicion) { // Solo al crear nuevos
                    java.util.List<String> parecidos = biblioteca.buscarPosiblesDuplicados(nuevoTitulo);
                    
                    if (!parecidos.isEmpty()) {
                        StringBuilder msg = new StringBuilder();
                        msg.append("El título '").append(nuevoTitulo).append("' es muy similar a un juego que ya tienes:\n\n");
                        for (String s : parecidos) {
                            msg.append(" - ").append(s).append("\n");
                        }
                        msg.append("\n¿Seguro que no es un error tipográfico?\n");
                        msg.append("Si presionas 'Sí', se creará el juego de todas formas.");

                        int respuesta = JOptionPane.showConfirmDialog(dialog, 
                            msg.toString(), 
                            "¿Posible Duplicado?", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.WARNING_MESSAGE);

                        if (respuesta == JOptionPane.NO_OPTION) {
                            return; // El usuario se arrepintió, cancelamos el guardado
                        }
                    }
                }

                // 4. RECOLECCIÓN DE DATOS 
                java.util.List<String> platsSeleccionadas = new java.util.ArrayList<>();
                for(JCheckBox chk : listaChecks) {
                    if(chk.isSelected()) platsSeleccionadas.add(chk.getText());
                }
                if(platsSeleccionadas.isEmpty()) platsSeleccionadas.add("Desconocido");

                // Parseo de números
                int año = Integer.parseInt(txtaño.getText().isEmpty() ? "0" : txtaño.getText());
                double calificacion = Double.parseDouble(txtRating.getText().isEmpty() ? "0" : txtRating.getText());
                int duracion = Integer.parseInt(txtDuracion.getText().isEmpty() ? "0" : txtDuracion.getText());
                double precio = Double.parseDouble(txtPrecio.getText().isEmpty() ? "0" : txtPrecio.getText());
                int numJug = Integer.parseInt(txtJugadores.getText().isEmpty() ? "1" : txtJugadores.getText());
                
                // 5. CREACIÓN O ACTUALIZACIÓN DEL JUEGO
                if (esEdicion) {
                    // MODO EDITAR
                    juegoAEditar.setTitulo(nuevoTitulo); // Usamos la variable trimeada
                    juegoAEditar.setPlataformas(platsSeleccionadas);
                    juegoAEditar.setGenero((String) cbGenero.getSelectedItem());
                    juegoAEditar.setAñoLanzamiento(año);
                    juegoAEditar.setCalificacion(calificacion);
                    juegoAEditar.setDesarrollador(txtDesarrollador.getText());
                    juegoAEditar.setEditores(txtEditores.getText());
                    juegoAEditar.setDuracionHoras(duracion);
                    juegoAEditar.setPrecio(precio);
                    juegoAEditar.setComentarios(txtComentarios.getText());
                    juegoAEditar.setNumJugadores(numJug);
                    juegoAEditar.setConectividad((String) cbConectividad.getSelectedItem());
                    juegoAEditar.setClasificacionEdades((String) cbESRB.getSelectedItem());
                    if(rutaImagenSeleccionada[0] != null) juegoAEditar.setRutaImagen(rutaImagenSeleccionada[0]);
                    
                    biblioteca.notificarCambio();
                } else {
                    // MODO CREAR
                    Juego nuevo = new Juego(
                        nuevoTitulo, 
                        platsSeleccionadas, 
                        (String) cbGenero.getSelectedItem(),
                        año, 
                        calificacion, 
                        rutaImagenSeleccionada[0], 
                        txtDesarrollador.getText(), 
                        txtEditores.getText(), 
                        duracion, 
                        precio, 
                        txtComentarios.getText(), 
                        numJug, 
                        (String) cbConectividad.getSelectedItem(), 
                        (String) cbESRB.getSelectedItem()
                    );
                    biblioteca.agregarJuego(nuevo);
                }
                
                mostrarJuegos(vistaActual);
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: Verifica que los campos numéricos (Año, Precio, etc.) sean válidos.");
            }
        });

        dialog.add(scrollForm, BorderLayout.CENTER);
        dialog.add(btnGuardar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    

    // --- MÉTODO PRINCIPAL PARA MOSTRAR JUEGOS ---
    private void mostrarJuegos(List<Juego> listaJuegos) {
        this.vistaActual = listaJuegos;
        gridPanel.removeAll();
        for (Juego j : listaJuegos) {
            gridPanel.add(new GameCard(
                j, 
                biblioteca.getMisColecciones(), 
                juegoAEditar -> mostrarDialogoJuego(juegoAEditar), // Editar
                juegoAVer -> mostrarDetallesVisuales(juegoAVer),   // Ver Detalles
                juegoABorrar -> accionEliminarJuego(juegoABorrar)  // <--- NUEVO: Eliminar
            ));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    
   private void actualizarSidebar() {
        sidebarContent.removeAll();

        // --- 1. SECCIÓN DE BÚSQUEDA ---
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new BoxLayout(panelBusqueda, BoxLayout.Y_AXIS));
        panelBusqueda.setBackground(Estilos.FONDO_OSCURO);
        panelBusqueda.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Etiqueta de búsqueda
        JLabel lblBuscar = new JLabel("🔍 Buscar:");
        lblBuscar.setForeground(Estilos.TEXTO_SECUNDARIO);
        lblBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo de texto para búsqueda
        txtBuscador = new JTextField();
        txtBuscador.setMaximumSize(new Dimension(200, 30));
        txtBuscador.addActionListener(e -> ejecutarBusquedaBasica()); // Al dar Enter busca

        // Botón de búsqueda
        JButton btnBuscar = new JButton("Ir");
        estilizarBoton(btnBuscar, true);
        btnBuscar.addActionListener(e -> ejecutarBusquedaBasica());

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(Box.createVerticalStrut(5));
        panelBusqueda.add(txtBuscador);
        panelBusqueda.add(Box.createVerticalStrut(5));
        panelBusqueda.add(btnBuscar);
        panelBusqueda.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebarContent.add(panelBusqueda);
        
        // --- 2. BOTÓN DE FILTROS AVANZADOS ---
        agregarSeparador("FILTROS");
        
        JButton btnFiltros = new JButton("⚙️ Filtros Avanzados");
        estilizarBoton(btnFiltros, false); 
        btnFiltros.setBackground(Estilos.FONDO_PANEL); // Un poco más claro para destacar
        btnFiltros.setMaximumSize(new Dimension(180, 35));
        btnFiltros.addActionListener(e -> mostrarDialogoFiltros());
        
        sidebarContent.add(btnFiltros);
        sidebarContent.add(Box.createVerticalStrut(10));

        // --- 3. BOTÓN "VER TODO" ---
        agregarBotonSidebar("🏠 Ver Todo", e -> {
            txtBuscador.setText(""); 
            statsPanel.setVisible(false); // <--- AGREGAR ESTO: Ocultar stats al ver todo
            mostrarJuegos(biblioteca.getTodosLosJuegos());
        });

        // --- 4. MIS COLECCIONES ---
        agregarSeparador("MIS COLECCIONES");
        if(biblioteca.getMisColecciones().isEmpty()) {
             JLabel l = new JLabel("(Vacío)");
             l.setForeground(Color.GRAY);
             l.setAlignmentX(Component.CENTER_ALIGNMENT);
             sidebarContent.add(l);
        }
       for (Subcoleccion sub : biblioteca.getMisColecciones()) {
            // CAMBIA LA ACCIÓN DEL BOTÓN AQUÍ:
            agregarBotonSidebar("📂 " + sub.toString(), e -> mostrarSubcoleccionCompleta(sub));
        }

        sidebarContent.revalidate();
        sidebarContent.repaint();
    }

    // Diálogo para crear nueva subcolección
    private void dialogoNuevaColeccion() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la colección:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            biblioteca.crearSubcoleccion(nombre);
            actualizarSidebar();
        }
    }
    
    // Método auxiliar para crear etiquetas estilizadas
    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Estilos.TEXTO_BLANCO);
        l.setFont(Estilos.FUENTE_NORMAL);
        return l;
    }

    // Método auxiliar para crear badges de estadísticas
    private void agregarBotonSidebar(String texto, ActionListener accion) {
        JButton btn = new JButton(texto);
        estilizarBoton(btn, false);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.addActionListener(accion);
        sidebarContent.add(btn);
        sidebarContent.add(Box.createVerticalStrut(5));
    }

    // Método auxiliar para agregar separadores de sección en la sidebar
    private void agregarSeparador(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Estilos.TEXTO_SECUNDARIO);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setBorder(new EmptyBorder(15, 10, 5, 0));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarContent.add(l);
    }

    // Método para estilizar botones de la aplicación
    private void estilizarBoton(JButton btn, boolean esPrincipal) {
        btn.setFocusPainted(false); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (esPrincipal) {
            // Botones de Acción (Agregar, Guardar)
            btn.setBackground(Estilos.ACENTO);
            btn.setForeground(Estilos.FONDO_OSCURO);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            btn.setOpaque(true);
        } else {
            // Botones del Menú Lateral ¿
            btn.setBackground(Estilos.FONDO_OSCURO); 
            btn.setForeground(Estilos.TEXTO_BLANCO);
            
            btn.setBorderPainted(false);       
            btn.setContentAreaFilled(false);   
            btn.setOpaque(true);               
            
            // Borde interno para que el texto no toque los lados
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

            // Efecto que cambia de color al pasar el mouse
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(Estilos.FONDO_PANEL); // Color más claro al pasar mouse
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(Estilos.FONDO_OSCURO); // Vuelve al color original
                }
            });
        }
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // Carga de datos de prueba para la primera ejecución
    private void cargarDatosPrueba() {
        // Ejemplo de juego multiplataforma
        java.util.List<String> platsZelda = new java.util.ArrayList<>();
        platsZelda.add("Nintendo Switch");
        platsZelda.add("Wii U");

        biblioteca.agregarJuego(new Juego(
            "The Legend of Zelda: Breath of the Wild", platsZelda, "Aventura", 2017, 9.8, "imagenes/zelda_botw.jpg",
            "Nintendo EPD", "Nintendo", 50, 59.99, "Obra maestra", 1, "Local", "E10+"
        ));

        java.util.List<String> platsGOW = new java.util.ArrayList<>();
        platsGOW.add("PlayStation 4");
        platsGOW.add("PlayStation 5");

        biblioteca.agregarJuego(new Juego(
            "God of War (2018)", platsGOW, "Acción", 2018, 9.6, "imagenes/gow_2018.jpg",
            "SIE Santa Monica Studio", "Sony Interactive Entertainment", 30, 39.99, "Épico", 1, "Local", "M"
        ));
        
        java.util.List<String> platsHalo = new java.util.ArrayList<>();
        platsHalo.add("Xbox Series X/S");
        platsHalo.add("PC");
        platsHalo.add("Xbox One");

        biblioteca.agregarJuego(new Juego(
            "Halo Infinite", platsHalo, "FPS", 2021, 8.5, "imagenes/halo_infinite.jpg",
            "343 Industries", "Xbox Game Studios", 15, 60.00, "Multijugador", 1, "Online", "T"
        ));

        java.util.List<String> platsMarioOdyssey = new java.util.ArrayList<>();
        platsMarioOdyssey.add("Nintendo Switch");
        platsMarioOdyssey.add("Nintendo Switch 2");

        biblioteca.agregarJuego(new Juego(
            "Super Mario Odyssey", platsMarioOdyssey, "Plataformas", 2017, 9.7, "imagenes/mario_odyssey.jpg",
            "Nintendo EPD", "Nintendo", 20, 49.99, "Divertido y creativo", 1, "Local", "E"
        ));

        java.util.List<String> platsCyberpunk = new java.util.ArrayList<>();
        platsCyberpunk.add("PC");
        platsCyberpunk.add("PlayStation 4");
        platsCyberpunk.add("PlayStation 5");
        platsCyberpunk.add("Nintendo Switch 2");
        platsCyberpunk.add("Xbox One");
        platsCyberpunk.add("Xbox Series X/S");
        biblioteca.agregarJuego(new Juego(
            "Cyberpunk 2077", platsCyberpunk, "RPG", 2020, 7.5, "imagenes/cyberpunk_2077.jpg",
            "CD Projekt Red", "CD Projekt", 40, 59.99, "¿Excelente juego pero lleno de bugs", 1, "Online", "M"
        ));

        java.util.List<String> platsTLoZOOT = new java.util.ArrayList<>();
        platsTLoZOOT.add("Nintendo 64");
        platsTLoZOOT.add("Nintendo Switch");
        platsTLoZOOT.add("Nintendo Switch 2");
        platsTLoZOOT.add("Wii U");
        platsTLoZOOT.add("GameCube");
        platsTLoZOOT.add("Wii");
        biblioteca.agregarJuego(new Juego(
            "The Legend of Zelda: Ocarina of Time", platsTLoZOOT, "Aventura", 1998, 9.9, "imagenes/zelda_oot.jpg",
            "Nintendo EAD", "Nintendo", 25, 39.99, "Clásico atemporal", 1, "Local", "E"
        ));
        
        java.util.List<String> platsFortnite = new java.util.ArrayList<>();
        platsFortnite.add("PC");
        platsFortnite.add("PlayStation 4");
        platsFortnite.add("PlayStation 5");
        platsFortnite.add("Nintendo Switch");
        platsFortnite.add("NIntendo Switch 2");
        platsFortnite.add("Xbox One");
        platsFortnite.add("Xbox Series X/S");
        biblioteca.agregarJuego(new Juego(
            "Fortnite", platsFortnite, "Battle Royale", 2017, 8.0, "imagenes/fortnite.jpg",
            "Epic Games", "Epic Games", 0, 0.00, "Popular juego multijugador", 100, "Online", "T"
        ));

        java.util.List<String> platsSuperSmashBros = new java.util.ArrayList<>();
        platsSuperSmashBros.add("Nintendo Switch");
        platsSuperSmashBros.add("Wii U");
        platsSuperSmashBros.add("Wii");
        platsSuperSmashBros.add("Nintendo 64");
        platsSuperSmashBros.add("Nintendo DS");
        platsSuperSmashBros.add("Nintendo 3DS");
        biblioteca.agregarJuego(new Juego(
            "Super Smash Bros", platsSuperSmashBros, "Lucha", 1999, 9.03, "imagenes/smash.jpg",
            "Bandai Namco", "Nintendo", 4, 59.99, "El más clasico juego de luchas multijugador", 8, "Online", "T"
        ));

        java.util.List<String> platsHollowKnight = new java.util.ArrayList<>();
        platsHollowKnight.add("Nintendo Switch");
        platsHollowKnight.add("Xbox One");
        platsHollowKnight.add("PlayStation 4");
        platsHollowKnight.add("PC");
        biblioteca.agregarJuego(new Juego(
            "Hollow Knight", platsHollowKnight, "Metroidvania", 2017, 9.2, "imagenes/hollow_knight.jpg",
            "Team Cherry", "Team Cherry", 65, 14.99, "El mejor indie estilo metroidvania", 1, "Local", "E10+"
        ));

        java.util.List<String> platsClashRoyale = new java.util.ArrayList<>();
        platsClashRoyale.add("Mobile (Android/iOS)");
        biblioteca.agregarJuego(new Juego(
            "Clash Royale", platsClashRoyale, "Estrategia", 2016, 8.12, "imagenes/clash_royale.jpg",
            "Supercell", "Supercell", 0, 0.00, "Lo más adictivo que he jugado en mi vida", 1, "Online", "E"
        ));

        java.util.List<String> platsSuperMetalSlugX = new java.util.ArrayList<>();
        platsSuperMetalSlugX.add("Pc");
        platsSuperMetalSlugX.add("PlayStation 1");
        platsSuperMetalSlugX.add("Arcade");
        platsSuperMetalSlugX.add("Neo Geo");
        platsSuperMetalSlugX.add("Mobile (Android/iOS)");
        platsSuperMetalSlugX.add("PSP");
        biblioteca.agregarJuego(new Juego(
            "Metal Slug X", platsSuperMetalSlugX, "Acción", 2003, 7.75, "imagenes/metal_slug_x.jpg",
            "SNK", "SNK", 1, 7.99, "Lo que todos jugaban en clase de computacion en primaria", 2, "Local", "T"
        ));
        
        biblioteca.crearSubcoleccion("Favoritos");
    }
    
    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        panel.add(crearLabel(etiqueta));
        panel.add(campo);
    }

    // --- CAMBIO 1: BÚSQUEDA BÁSICA ---
    private void ejecutarBusquedaBasica() {
        String texto = txtBuscador.getText();
        // Llama al método de biblioteca solo con texto, resto null
        List<Juego> resultados = biblioteca.buscarJuegos(texto, null, null, null, null);
        mostrarJuegos(resultados);
    }

    // --- DIÁLOGO DE FILTROS AVANZADOS ---
    private void mostrarDialogoFiltros() {
        JDialog dialog = new JDialog(this, "Filtros Avanzados", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Estilos.FONDO_PANEL);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 15));
        form.setBackground(Estilos.FONDO_PANEL);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Plataforma (Añadimos "Todas" al principio)
        java.util.List<String> opcionesPlat = new java.util.ArrayList<>();
        opcionesPlat.add("Todas");
        for(String s : DatosComunes.LISTA) opcionesPlat.add(s);
        JComboBox<String> cbPlataforma = new JComboBox<>(opcionesPlat.toArray(new String[0]));

        // 2. Género
        JTextField txtGenero = new JTextField();

        // 3. Rango de Años
        JPanel panelaños = new JPanel(new GridLayout(1, 2, 5, 0));
        panelaños.setBackground(Estilos.FONDO_PANEL);
        JTextField txtAñoMin = new JTextField();
        JTextField txtAñoMax = new JTextField();
        txtAñoMin.setToolTipText("Desde");
        txtAñoMax.setToolTipText("Hasta");
        panelaños.add(txtAñoMin);
        panelaños.add(txtAñoMax);

        // Etiquetas
        form.add(crearLabel("Plataforma:"));
        form.add(cbPlataforma);
        form.add(crearLabel("Género (contiene):"));
        form.add(txtGenero);
        form.add(crearLabel("Año (Desde - Hasta):"));
        form.add(panelaños);

        // Botones
        JPanel panelBtn = new JPanel();
        panelBtn.setBackground(Estilos.FONDO_PANEL);
        
        JButton btnAplicar = new JButton("Aplicar Filtros");
        estilizarBoton(btnAplicar, true);
        
        JButton btnLimpiar = new JButton("Limpiar");
        estilizarBoton(btnLimpiar, false);

        btnAplicar.addActionListener(e -> {
            try {
                String plat = (String) cbPlataforma.getSelectedItem();
                String gen = txtGenero.getText();
                
                Integer min = txtAñoMin.getText().isEmpty() ? null : Integer.parseInt(txtAñoMin.getText());
                Integer max = txtAñoMax.getText().isEmpty() ? null : Integer.parseInt(txtAñoMax.getText());

                // Mantenemos lo que haya escrito en la barra de búsqueda también
                String busquedaTexto = txtBuscador.getText();

                List<Juego> resultados = biblioteca.buscarJuegos(busquedaTexto, plat, gen, min, max);
                mostrarJuegos(resultados);
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor, ingresa años válidos (números).");
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtBuscador.setText("");
            mostrarJuegos(biblioteca.getTodosLosJuegos());
            dialog.dispose();
        });

        panelBtn.add(btnLimpiar);
        panelBtn.add(btnAplicar);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(panelBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // --- ACCIÓN DE ELIMINAR JUEGO ---
    private void accionEliminarJuego(Juego juego) {
        int respuesta = JOptionPane.showConfirmDialog(
            this, 
            "¿Estás seguro de que quieres eliminar '" + juego.getTitulo() + "'?\nEsta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE // Ícono de advertencia
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            biblioteca.eliminarJuegoGlobal(juego);
            // Refrescar la vista actual para que desaparezca la tarjeta
            // (Si estamos viendo una búsqueda, se actualiza la búsqueda. Si es "ver todo", se actualiza todo)
            gridPanel.removeAll();
            mostrarJuegos(vistaActual); 
            // Nota: Si 'vistaActual' era una copia filtrada, puede que necesites refrescar desde la fuente
            // Para asegurar, forzamos un repintado simple:
            if (vistaActual.contains(juego)) {
                vistaActual.remove(juego); // Lo quitamos visualmente de la lista temporal
            }
            mostrarJuegos(vistaActual);
            
            JOptionPane.showMessageDialog(this, "Juego eliminado correctamente.");
        }
    }

    // --- MOSTRAR SUBCOLECCIÓN COMPLETA ---
    private void mostrarSubcoleccionCompleta(Subcoleccion sub) {
        statsPanel.removeAll(); // Limpiar datos anteriores
        statsPanel.setVisible(true); // Mostrar la barra

        // 1. Título de la colección
        JLabel lblTitulo = new JLabel(sub.getNombre().toUpperCase());
        lblTitulo.setForeground(Estilos.ACENTO);
        lblTitulo.setFont(Estilos.FUENTE_TITULO);
        statsPanel.add(lblTitulo);
        
        statsPanel.add(new JSeparator(SwingConstants.VERTICAL));

        // 2. Datos calculados (Usando los métodos de la clase Subcoleccion)
        agregarDatoEstadistico("🎮 Juegos", String.valueOf(sub.getNumeroJuegos()));
        
        // Promedio (formateado a 1 decimal)
        String promedio = String.format("%.1f", sub.getPromedioCalificacion());
        agregarDatoEstadistico("⭐ Promedio", promedio + "/10");
        
        agregarDatoEstadistico("⏳ Total Horas", sub.getDuracionTotal() + "h");
        
        Juego mejor = sub.getJuegoMejorValorado();
        if (mejor != null) {
            agregarDatoEstadistico("🏆 Mejor Juego", mejor.getTitulo());
        } else {
            agregarDatoEstadistico("🏆 Mejor Juego", "---");
        }

        statsPanel.revalidate();
        statsPanel.repaint();

        mostrarJuegos(sub.getJuegos());
    }

    // Método auxiliar para agregar un dato estadístico a la barra
    private void agregarDatoEstadistico(String titulo, String valor) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(Estilos.FONDO_OSCURO);
        
        JLabel t = new JLabel(titulo);
        t.setForeground(Color.GRAY);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        JLabel v = new JLabel(valor);
        v.setForeground(Estilos.TEXTO_BLANCO);
        v.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        p.add(t);
        p.add(v);
        
        statsPanel.add(Box.createHorizontalStrut(15)); // Espacio entre datos
        statsPanel.add(p);
    }

    // Método auxiliar para agregar una fila al grid de detalles
    private void agregarFilaGrid(JPanel panel, int fila, String titulo, String valor) {
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 0, 12, 15); // Margen inferior y derecho
        gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda
        
        // Columna 0: TÍTULO (Etiqueta)
        gbc.gridx = 0;
        JLabel t = new JLabel(titulo);
        t.setForeground(Color.GRAY);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(t, gbc);

        // Columna 1: VALOR
        gbc.gridx = 1;
        JLabel v = new JLabel((valor == null || valor.isEmpty()) ? "---" : valor);
        v.setForeground(Estilos.ACENTO); // Color azul brillante
        v.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(v, gbc);
    }

    // Método auxiliar para crear las etiquetas de colores (Rating, Precio, etc.)
    private JLabel crearBadge(String texto, Color colorTexto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(colorTexto);
        
        // Borde redondeado del color del texto 
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorTexto, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        return l;
    }

    // --- MÉTODO PRINCIPAL ---
    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
