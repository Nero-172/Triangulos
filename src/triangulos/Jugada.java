package triangulos;
/**
 * Clase que representa una jugada realizada
 * @author Tu Nombre
 * @author Tu Número
 */
public class Jugada {
    private Punto inicio;
    private char direccion;
    private int longitud;
    private boolean esBlanco;
    
    public Jugada(Punto inicio, char direccion, int longitud, boolean esBlanco) {
        this.inicio = inicio;
        this.direccion = direccion;
        this.longitud = longitud;
        this.esBlanco = esBlanco;
    }
    
    public Punto getInicio() {
        return inicio;
    }
    
    public char getDireccion() {
        return direccion;
    }
    
    public int getLongitud() {
        return longitud;
    }
    
    public boolean esBlanco() {
        return esBlanco;
    }
    
    @Override
    public String toString() {
        String dirStr = "";
        switch (direccion) {
            case 'Q': dirStr = "noroeste (arriba-izquierda)"; break;
            case 'E': dirStr = "noreste (arriba-derecha)"; break;
            case 'D': dirStr = "este (derecha)"; break;
            case 'C': dirStr = "sureste (abajo-derecha)"; break;
            case 'Z': dirStr = "suroeste (abajo-izquierda)"; break;
            case 'A': dirStr = "oeste (izquierda)"; break;
        }
        
        return inicio.toString() + " -> " + dirStr + " (longitud " + longitud + ")";
    }
}

