package triangulos;

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
            case 'Q': dirStr = "noroeste"; break;
            case 'E': dirStr = "noreste"; break;
            case 'D': dirStr = "este"; break;
            case 'C': dirStr = "sureste"; break;
            case 'Z': dirStr = "suroeste"; break;
            case 'A': dirStr = "oeste"; break;
        }
        
        return inicio.toString() + " -> " + dirStr + " (longitud " + longitud + ")";
    }
}

