import java.io.Serializable;
import java.util.List; // Importar List
import java.util.ArrayList;

public class Juego implements Serializable {
    private static final long serialVersionUID = 2L; // Cambiamos versión para forzar actualización

    private String titulo;
    private List<String> plataformas; // CAMBIO: Ahora es una lista
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
    public List<String> getPlataformas() { return plataformas; }
    
    // NUEVO MÉTODO ÚTIL: Convierte la lista ["PC", "Xbox"] en el texto "PC, Xbox"
    public String getPlataformasTexto() {
        if (plataformas == null || plataformas.isEmpty()) return "Sin Plataforma";
        return String.join(", ", plataformas);
    }

    public void setPlataformas(List<String> plataformas) { this.plataformas = plataformas; }

    // ... (El resto de getters y setters de los otros campos se mantienen igual) ...
    // Copia aquí el resto de tus getters/setters (Genero, Titulo, etc.) del código anterior
    public String getTitulo() { return titulo; }
    public void setTitulo(String t) { this.titulo = t; }
    public String getGenero() { return genero; }
    public void setGenero(String g) { this.genero = g; }
    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double c) { this.calificacion = c; }
    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String r) { this.rutaImagen = r; }
    public int getAñoLanzamiento() { return añoLanzamiento; }
    public void setAñoLanzamiento(int a) { this.añoLanzamiento = a; }
    // ... agrega el resto ...
    public String getDesarrollador() { return desarrollador; }
    public void setDesarrollador(String d) { this.desarrollador = d; }
    public String getEditores() { return editores; }
    public void setEditores(String e) { this.editores = e; }
    public int getDuracionHoras() { return duracionHoras; }
    public void setDuracionHoras(int d) { this.duracionHoras = d; }
    public double getPrecio() { return precio; }
    public void setPrecio(double p) { this.precio = p; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String c) { this.comentarios = c; }
    public int getNumJugadores() { return numJugadores; }
    public void setNumJugadores(int n) { this.numJugadores = n; }
    public String getConectividad() { return conectividad; }
    public void setConectividad(String c) { this.conectividad = c; }
    public String getClasificacionEdades() { return clasificacionEdades; }
    public void setClasificacionEdades(String c) { this.clasificacionEdades = c; }

    @Override
    public String toString() { return titulo; }
}