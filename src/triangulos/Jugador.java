package triangulos;
/**
 * Clase que representa a un jugador
 * @author Tu Nombre
 * @author Tu Número
 */
public class Jugador {
    private String nombre;
    private int edad;
    private int partidasGanadas;
    private int rachaActual;
    private int rachaMaxima;
    
    public Jugador(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
        this.partidasGanadas = 0;
        this.rachaActual = 0;
        this.rachaMaxima = 0;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public int getEdad() {
        return edad;
    }
    
    public int getPartidasGanadas() {
        return partidasGanadas;
    }
    
    public void incrementarPartidasGanadas() {
        partidasGanadas++;
    }
    
    public void incrementarRacha() {
        rachaActual++;
        if (rachaActual > rachaMaxima) {
            rachaMaxima = rachaActual;
        }
    }
    
    public void resetRacha() {
        rachaActual = 0;
    }
    
    public int getRachaActual() {
        return rachaActual;
    }
    
    public int getRachaMaxima() {
        return rachaMaxima;
    }
}
