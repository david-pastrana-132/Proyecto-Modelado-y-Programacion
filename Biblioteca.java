import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Añadimos Serializable aquí también
public class Biblioteca implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Juego> listaMaestra;
    private List<Subcoleccion> misColecciones;

    public Biblioteca() {
        this.listaMaestra = new ArrayList<>();
        this.misColecciones = new ArrayList<>();
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
        listaMaestra.add(juego);
        guardarEnDisco(); // Auto-guardar cada vez que agregamos
    }
    
    // Método nuevo para guardar cambios tras editar
    public void notificarCambio() {
        guardarEnDisco();
    }

    //Métodos de gestión de subcolecciones
    public void crearSubcoleccion(String nombre) {
        misColecciones.add(new Subcoleccion(nombre));
        guardarEnDisco(); // Auto-guardar
    }

    public List<Juego> getTodosLosJuegos() { return listaMaestra; }
    public List<Subcoleccion> getMisColecciones() { return misColecciones; }

    //Método de filtrado para subcolecciones
    public List<Juego> filtrarPorPlataforma(String plataformaBuscada) {
            return listaMaestra.stream()
                    // Verificamos si la lista del juego CONTIENE la plataforma buscada
                    .filter(j -> j.getPlataformas().contains(plataformaBuscada))
                    .collect(Collectors.toList());
    }

    public void eliminarJuegoGlobal(Juego juego) {
        // 1. Borrar de la lista maestra
        listaMaestra.remove(juego);
        
        // 2. Borrar de todas las subcolecciones (Favoritos, etc.)
        for (Subcoleccion sub : misColecciones) {
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
        return listaMaestra.stream().filter(j -> {
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
            if (coincide && añoMin != null) {
                if (j.getAñoLanzamiento() < añoMin) coincide = false;
            }
            if (coincide && añoMax != null) {
                if (j.getAñoLanzamiento() > añoMax) coincide = false;
            }

            return coincide;
        }).collect(Collectors.toList());
    }

    // --- NUEVO MÉTODO PARA DETECTAR DUPLICADOS ---
    public boolean existeJuego(String titulo) {
        if (titulo == null || titulo.isEmpty()) return false;
        
        // Busca en la lista maestra ignorando mayúsculas/minúsculas
        for (Juego j : listaMaestra) {
            if (j.getTitulo().equalsIgnoreCase(titulo.trim())) {
                return true; // ¡Encontrado!
            }
        }
        return false; // No existe
    }

    /**
     * Busca juegos que se escriban casi igual al título nuevo.
     * Retorna una lista de sugerencias.
     */
    public List<String> buscarPosiblesDuplicados(String tituloNuevo) {
        List<String> similares = new ArrayList<>();
        String tituloA = tituloNuevo.toLowerCase().trim();
        
        for (Juego j : listaMaestra) {
            String tituloB = j.getTitulo().toLowerCase();
            
            // Calculamos la "distancia" (cuántos caracteres hay de diferencia)
            int distancia = calcularLevenshtein(tituloA, tituloB);
            
            // Si hay menos de 3 cambios de diferencia y no son idénticos
            // (Ej: "Zelda" vs "Zeldda" tiene distancia 1)
            if (distancia > 0 && distancia <= 2) { 
                similares.add(j.getTitulo());
            }
            
            // También detecta si uno contiene al otro (Ej: "God of War" vs "God of War 3")
            // Esto es opcional, pero útil.
            if (distancia > 2 && (tituloA.contains(tituloB) || tituloB.contains(tituloA))) {
                 // Solo si la diferencia de longitud es pequeña (para evitar falsos positivos)
                 if (Math.abs(tituloA.length() - tituloB.length()) < 4) {
                     similares.add(j.getTitulo());
                 }
            }
        }
        return similares;
    }

    /**
     * Algoritmo de Levenshtein.
     * Calcula el número de ediciones (borrar, insertar, sustituir)
     */
    private int calcularLevenshtein(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int costo = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(
                    dp[i - 1][j] + 1,      // Borrado
                    dp[i][j - 1] + 1),     // Inserción
                    dp[i - 1][j - 1] + costo); // Sustitución
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
