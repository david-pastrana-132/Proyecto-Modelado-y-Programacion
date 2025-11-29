import javax.swing.ImageIcon;

public class Juego {
    // Atributos obligatorios
    private String titulo;
    private String plataforma;
    
    // Atributos opcionales y de gestión
    private String genero;
    private int añoLanzamiento;
    private double calificacion; // 1-10
    private String desarrollador; // 
    private String editores; // 
    private int duracionHoras; // 
    private double precio; // 
    private String comentarios; // 
    private int numJugadores; //
    private String conectividad;
    private String clasificacionEdades;//
    private String rutaImagen; // Para la gestión de imágenes
    


    public Juego(String titulo, String plataforma, String genero, int año, double calificacion, String rutaImagen, 
                 String desarrollador, String editores, int duracionHoras, double precio, String comentarios, 
                 int numJugadores, String conectividad, String clasificacionEdades) {
        this.titulo = titulo;
        this.plataforma = plataforma;
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
    public String getDesarrollador() { 
        return desarrollador;
    }
    public String getEditores() { 
        return editores; 
    }
    public int getDuracionHoras() { 
        return duracionHoras; 
    }
    public double getPrecio() { 
        return precio;     
    }
    public String getComentarios() { 
        return comentarios; 
    }
    public int getNumJugadores() { 
        return numJugadores; 
    }
    public String getConectividad() { 
        return conectividad; 
    }
    public String getClasificacionEdades() { 
        return clasificacionEdades; 
    }   
     @Override
    public String toString() {
        // Se usará, por ejemplo, en JList para mostrar el título y calificación
        return titulo + " (" + calificacion + ")";
    }

    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
    }
    public void setPlataforma(String plataforma) { 
        this.plataforma = plataforma; 
    }
    public void setGenero(String genero) { 
        this.genero = genero; 
    }
    public void setCalificacion(double calificacion) { 
        this.calificacion = calificacion; 
    }
    public void setRutaImagen(String rutaImagen) { 
        this.rutaImagen = rutaImagen; 
    }
    public void setañoLanzamiento(int añoLanzamiento) { 
        this.añoLanzamiento = añoLanzamiento; 
    } 
    public void setDesarrollador(String desarrollador) { 
        this.desarrollador = desarrollador; 
    }
    public void setEditores(String editores) { 
        this.editores = editores;
    }
    public void setDuracionHoras(int duracionHoras) { 
        this.duracionHoras = duracionHoras; 
    }
    public void setPrecio(double precio) { 
        this.precio = precio;
    }
    public void setComentarios(String comentarios) { 
        this.comentarios = comentarios; 
    }
    public void setNumJugadores(int numJugadores) { 
        this.numJugadores = numJugadores;
    }
    public void setConectividad(String conectividad) { 
        this.conectividad = conectividad; 
    }
    public void setClasificacionEdades(String clasificacionEdades) { 
        this.clasificacionEdades = clasificacionEdades;
    }
}