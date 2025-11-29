import javax.swing.ImageIcon;

public class Juego {
    // Atributos obligatorios
    private String titulo;
    private String plataforma;
    
    // Atributos opcionales y de gestión
    private String genero;
    private int añoLanzamiento;
    private double calificacion; // 1-10
    private String rutaImagen; // Para la gestión de imágenes
    private String estado; // "Completado", "En Progreso", etc.

    public Juego(String titulo, String plataforma, String genero, int año, double calificacion, String rutaImagen) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.añoLanzamiento = año;
        this.calificacion = calificacion;
        this.rutaImagen = rutaImagen;
        this.estado = "Biblioteca";
    }

    // Getters
    public String getTitulo() { 
        return titulo; 
    }
    public String getPlataforma() { 
        return plataforma; 
    }
    public String getGenero() { 
        return genero; 
    }
    public double getCalificacion() { 
        return calificacion; 
    }
    public String getRutaImagen() { 
        return rutaImagen; 
    }
    public int getañoLanzamiento() { 
        return añoLanzamiento; 
    }
     @Override
    public String toString() {
        // Se usará, por ejemplo, en JList para mostrar el título y calificación
        return titulo + " (" + calificacion + ")";
    }
}