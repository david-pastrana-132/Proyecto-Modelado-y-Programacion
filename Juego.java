import java.io.Serializable;
import java.util.List; // Importar List
import java.util.ArrayList;

public class Juego implements Serializable {
    private static final long serialVersionUID = 2L; // Cambiamos versión para forzar actualización

    private String titulo;
    private List<String> plataformas; // CAMBIO: Ahora es una lista
    
    // Resto de atributos...
    private String genero;
    private int añoLanzamiento;
    private double calificacion;
    private String rutaImagen;
    private String desarrollador; 
    private String editores; 
    private int duracionHoras; 
    private double precio; 
    private String comentarios; 
    private int numJugadores; 
    private String conectividad; 
    private String clasificacionEdades; 

    // Constructor actualizado recibe List<String>
    public Juego(String titulo, List<String> plataformas, String genero, int año, double calificacion, String rutaImagen, 
                 String desarrollador, String editores, int duracionHoras, double precio, String comentarios, 
                 int numJugadores, String conectividad, String clasificacionEdades) {
        this.titulo = titulo;
        this.plataformas = plataformas;
        this.genero = genero;
        this.añoLanzamiento = año;
        this.calificacion = calificacion;
        this.rutaImagen = rutaImagen;
        this.desarrollador = desarrollador;
        this.editores = editores;
        this.duracionHoras = duracionHoras;
        this.precio = precio;
        this.comentarios = comentarios;
        this.numJugadores = numJugadores;
        this.conectividad = conectividad;
        this.clasificacionEdades = clasificacionEdades; 
    }

    // Getters y Setters actualizados
    
    // NUEVO MÉTODO ÚTIL: Convierte la lista ["PC", "Xbox"] en el texto "PC, Xbox"
    

       // --- GETTERS ---
    public String getTitulo() { return titulo; }
    public List<String> getPlataformas() { return plataformas; }
    public String getPlataformasTexto() {
        if (plataformas == null || plataformas.isEmpty()) return "Sin Plataforma";
        return String.join(", ", plataformas);
    }
    public String getGenero() { return genero; }
    public double getCalificacion() { return calificacion; }
    public String getRutaImagen() { return rutaImagen; }
    public int getAñoLanzamiento() { return añoLanzamiento; }
    public String getDesarrollador() { return desarrollador; }
    public String getEditores() { return editores; }
    public int getDuracionHoras() { return duracionHoras; }
    public double getPrecio() { return precio; }
    public String getComentarios() { return comentarios; }
    public int getNumJugadores() { return numJugadores; }
    public String getConectividad() { return conectividad; }
    public String getClasificacionEdades() { return clasificacionEdades; }   

    // --- SETTERS ---
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setPlataformas(List<String> plataformas) { this.plataformas = plataformas; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    public void setAñoLanzamiento(int añoLanzamiento) { this.añoLanzamiento = añoLanzamiento; } 
    public void setDesarrollador(String desarrollador) { this.desarrollador = desarrollador; }
    public void setEditores(String editores) { this.editores = editores; }
    public void setDuracionHoras(int duracionHoras) { this.duracionHoras = duracionHoras; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    public void setNumJugadores(int numJugadores) { this.numJugadores = numJugadores; }
    public void setConectividad(String conectividad) { this.conectividad = conectividad; }
    public void setClasificacionEdades(String clasificacionEdades) { this.clasificacionEdades = clasificacionEdades; }

    @Override
    public String toString() { return titulo; }
}


