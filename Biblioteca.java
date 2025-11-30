import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Añadimos Serializable aquí también
public class Biblioteca implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Juego> juegos;
    private List<Subcoleccion> subcolecciones;

    public Biblioteca() {
        this.juegos = new ArrayList<>();
        this.subcolecciones = new ArrayList<>();
    }

    // --- MÉTODOS DE PERSISTENCIA (GUARDAR/CARGAR) ---
    
    // Guarda esta instancia de biblioteca en un archivo
    public void guardarEnDisco() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos_biblioteca.dat"))) {
            out.writeObject(this);
            System.out.println("Datos guardados correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método estático para cargar la biblioteca guardada
    public static Biblioteca cargarDesdeDisco() {
        File archivo = new File("datos_biblioteca.dat");
        if (archivo.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
                return (Biblioteca) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return new Biblioteca(); // Si falla, retorna una nueva vacía
            }
        } else {
            return null; // Retorna null para indicar que es la primera vez (y cargar datos prueba)
        }
    }

    // --- MÉTODOS NORMALES ---

    public void agregarJuego(Juego juego) {
        juegos.add(juego);
        guardarEnDisco(); // Auto-guardar cada vez que agregamos
    }
    
    // Método nuevo para guardar cambios tras editar
    public void notificarCambio() {
        guardarEnDisco();
    }

    public void crearSubcoleccion(String nombre) {
        subcolecciones.add(new Subcoleccion(nombre));
        guardarEnDisco(); // Auto-guardar
    }

    public List<Juego> getTodosLosJuegos() { return juegos; }
    public List<Subcoleccion> getsubcolecciones() { return subcolecciones; }

    public List<Juego> filtrarPorPlataforma(String plataformaBuscada) {
            return juegos.stream()
                    // Verificamos si la lista del juego CONTIENE la plataforma buscada
                    .filter(j -> j.getPlataformas().contains(plataformaBuscada))
                    .collect(Collectors.toList());
    }

    public void eliminarJuegoGlobal(Juego juego) {
        // 1. Borrar de la lista maestra
        juegos.remove(juego);
        
        // 2. Borrar de todas las subcolecciones (Favoritos, etc.)
        for (Subcoleccion sub : subcolecciones) {
            sub.eliminarJuego(juego);
        }
        
        // 3. Guardar cambios en disco
        guardarEnDisco();
    }

    /**
     * Filtro Maestro: Busca por texto y/o filtros específicos.
     * Si un parámetro es null o vacío, se ignora ese criterio.
     */
    public List<Juego> buscarJuegos(String texto, String plataforma, String genero, Integer añoMin, Integer añoMax) {
        return juegos.stream().filter(j -> {
            boolean coincide = true;

            // 1. Filtro por Texto (Busca en Título o Desarrollador)
            if (texto != null && !texto.trim().isEmpty()) {
                String q = texto.toLowerCase();
                boolean enTitulo = j.getTitulo().toLowerCase().contains(q);
                boolean enDev = j.getDesarrollador() != null && j.getDesarrollador().toLowerCase().contains(q);
                if (!enTitulo && !enDev) coincide = false;
            }

            // 2. Filtro por Plataforma (Revisamos si la lista de plataformas del juego contiene la buscada)
            if (coincide && plataforma != null && !plataforma.equals("Todas")) {
                if (!j.getPlataformas().contains(plataforma)) coincide = false;
            }

            // 3. Filtro por Género (Contiene texto parcial, ej: "RPG" encuentra "Action RPG")
            if (coincide && genero != null && !genero.trim().isEmpty()) {
                if (j.getGenero() == null || !j.getGenero().toLowerCase().contains(genero.toLowerCase())) {
                    coincide = false;
                }
            }

            // 4. Filtro por Año (Rango)
            if (coincide && anioMin != null) {
                if (j.getAñoLanzamiento() < añoMin) coincide = false;
            }
            if (coincide && anioMax != null) {
                if (j.getAñoLanzamiento() > añoMax) coincide = false;
            }

            return coincide;
        }).collect(Collectors.toList());
    }
}
