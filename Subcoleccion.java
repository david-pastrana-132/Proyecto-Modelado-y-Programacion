import java.util.List;
import java.util.ArrayList;


public class Subcoleccion {
    String nombre;
    String tipo;
    List<Juego> juegos;
    //constructores
    public Subcoleccion(){

    }
    public Subcoleccion(String nombre,String tipo){
        this.nombre = nombre;
        this.tipo = tipo;
        this.juegos = new ArrayList<Juego>();
    }

    //getters
    public String getTipo() {
        return tipo;
    }
    public List<Juego> getJuegos() {
        return juegos;
    }
    public String getNombre() {
        return nombre;
    }
    
    //Setters

    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    //Métodos para añadir y eliminar juegos
    public void añadirJuego(Juego cambio){
        juegos.add(cambio);
    }
    public void eliminarJuego(Juego cambio){
        juegos.remove(cambio);
    }
    
    //Método para obtener el número de juegos de la subcolección
    public Integer getNumeroJuegos(){
        return juegos.size();
    }
    
    //Método para obtener el promedio de la calificación de los juegos en la subcolección
    public double getPromedioCalificacion(){
        Double contador = 0.0;
        for (Juego juego : juegos){
            contador =+ juego.getCalificacion();
        }
        return contador/juegos.size();
    }
    
    //Método para obtener la duración total de los juegos en la subcolección
    public Integer getDuracionTotal(){
        Integer contador = 0;
        for (Juego juego : juegos){
            contador =+ juego.getDuracionHoras();
        }
        return contador;
    }

    //Método para obtener el juego mejor valorado de la subcolección
    public Juego getJuegoMejorValorado(){
        Juego mejorValorado = juegos.get(0);
        for (Juego juego : juegos){
            if (juego.getCalificacion() >= mejorValorado.getCalificacion()){
                mejorValorado = juego;
            }
        }
        return mejorValorado;
    }
}

