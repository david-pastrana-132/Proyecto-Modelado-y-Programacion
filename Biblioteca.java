import java.util.List;
import java.util.ArrayList;

public class Biblioteca {
    String nombre;
    List<Juego> juegos;
    List<Subcoleccion> subcolecciones;
    
    //constructores
    public Biblioteca(){
    }
    public Biblioteca(String nombre){
        this.nombre = nombre;
        this.juegos = new ArrayList<Juego>();
        this.subcolecciones = new ArrayList<Subcoleccion>();
    }

    //getters

    public String getNombre() {
        return nombre;
    }
    public List<Juego> getJuegos() {
        return juegos;
    }
    public List<Subcoleccion> getSubcolecciones() {
        return subcolecciones;
    }

    //setters

    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setSubcolecciones(List<Subcoleccion> subcolecciones) {
        this.subcolecciones = subcolecciones;
    }
    
    //Métodos para añadir y eliminar juegos y subcolecciones a la biblioteca
    public void añadirJuego(Juego cambio){
        juegos.add(cambio);
    }
    public void eliminarJuego(Juego cambio){
        juegos.remove(cambio);
    }
    public void añadirSubcoleccion(Subcoleccion cambio){
        subcolecciones.add(cambio);
    }
    public void eliminarSubcoleccion(Subcoleccion cambio){
        subcolecciones.remove(cambio);
    }
    //Método para añadir un juego a alguna subcoleccion desde la biblioteca
    public void añadirJuego(Subcoleccion subcoleccionACambiar,Juego cambio){
        for (Subcoleccion subcoleccion:subcolecciones){
            if (subcoleccion.equals(subcoleccionACambiar)){
                subcoleccion.añadirJuego(cambio);
                break;
            }
        }
    }

    //Método para eliminar un juego de alguna subcoleccion desde la biblioteca
    public void eliminarJuego(Subcoleccion subcoleccionACambiar,Juego cambio){
        for (Subcoleccion subcoleccion:subcolecciones){
            if (subcoleccion.equals(subcoleccionACambiar)){
                subcoleccion.eliminarJuego(cambio);
                break;
            }
        }
    }
}
