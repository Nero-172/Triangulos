package triangulos;

public class Jugada {
    private Punto inicio;
    private char direccion;
    private int uniones; 
    private boolean esBlanco;
    
    public Jugada(Punto inicio, char direccion, int uniones, boolean esBlanco) {
        this.inicio = inicio;
        this.direccion = direccion;
        this.uniones = uniones;
        this.esBlanco = esBlanco;
    }
    
    public Punto getInicio() {
        return inicio;
    }
    
    public char getDireccion() {
        return direccion;
    }
    
    public int getLongitud() {
        return uniones;
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
        
        return inicio.toString() + " -> " + dirStr + " (" + uniones + " uniones, " + (uniones + 1) + " puntos)";
    }
}
