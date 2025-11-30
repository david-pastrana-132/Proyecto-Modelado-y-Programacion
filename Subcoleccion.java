import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subcoleccion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String tipo; 
    private List<Juego> juegos;

    public Subcoleccion() {
        this.juegos = new ArrayList<>();
    }

    public Subcoleccion(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.juegos = new ArrayList<>();
    }
    
    // Constructor rápido
    public Subcoleccion(String nombre) {
        this(nombre, "Personalizada");
    }

    // --- MÉTODOS DE GESTIÓN ---
    public void agregarJuego(Juego juego) {
        // Evita duplicados en la misma lista
        if (!juegos.contains(juego)) {
            juegos.add(juego);
        }
    }

    public void eliminarJuego(Juego juego) {
        juegos.remove(juego);
    }

    // --- ESTADÍSTICAS (Corregidas para el PDF) ---
    public int getNumeroJuegos() {
        return juegos.size();
    }

    public double getPromedioCalificacion() {
        if (juegos.isEmpty()) return 0.0;
        double suma = 0.0;
        for (Juego juego : juegos) {
            suma += juego.getCalificacion(); 
        }
        return suma / juegos.size();
    }

    public int getDuracionTotal() {
        int suma = 0;
        for (Juego juego : juegos) {
            suma += juego.getDuracionHoras(); 
        }
        return suma;
    }

    public Juego getJuegoMejorValorado() {
        if (juegos.isEmpty()) return null;
        Juego mejor = juegos.get(0);
        for (Juego juego : juegos) {
            if (juego.getCalificacion() > mejor.getCalificacion()) {
                mejor = juego;
            }
        }
        return mejor;
    }

    // --- GETTERS/SETTERS/TOSTRING ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public List<Juego> getJuegos() { return juegos; }
    public void setJuegos(List<Juego> juegos) { this.juegos = juegos; }

    @Override
    public String toString() { return nombre; }
}