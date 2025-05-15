package triangulos;
/**
 * Clase que representa una banda elástica en el tablero
 * @author Tu Nombre
 * @author Tu Número
 */
public class Banda {
    private Punto inicio;
    private Punto fin;
    private boolean esBlanco; // true si fue colocada por el jugador blanco
    
    public Banda(Punto inicio, Punto fin, boolean esBlanco) {
        this.inicio = inicio;
        this.fin = fin;
        this.esBlanco = esBlanco;
    }
    
    public Punto getInicio() {
        return inicio;
    }
    
    public Punto getFin() {
        return fin;
    }
    
    public boolean esBlanco() {
        return esBlanco;
    }
    
    public boolean pasaPorPunto(Punto punto) {
        // Verificar si el punto está en los extremos
        if (inicio.equals(punto) || fin.equals(punto)) {
            return true;
        }
        
        // Obtener las diferencias
        int deltaCol = fin.getColumna() - inicio.getColumna();
        int deltaFila = fin.getFila() - inicio.getFila();
        
        // Para bandas horizontales (Este-Oeste)
        if (deltaFila == 0) {
            if (punto.getFila() == inicio.getFila()) {
                char colMin = (char) Math.min(inicio.getColumna(), fin.getColumna());
                char colMax = (char) Math.max(inicio.getColumna(), fin.getColumna());
                return punto.getColumna() >= colMin && punto.getColumna() <= colMax;
            }
            return false;
        }
        
        // Para bandas diagonales
        // Verificar si el punto está en la línea entre inicio y fin
        
        // Si es una diagonal perfecta (45 grados)
        if (Math.abs(deltaCol) == Math.abs(deltaFila)) {
            // Determinar la dirección
            int pasoFila = deltaFila > 0 ? 1 : -1;
            int pasoCol = deltaCol > 0 ? 1 : -1;
            
            // Recorrer la diagonal desde el inicio
            char col = inicio.getColumna();
            int fila = inicio.getFila();
            
            while (true) {
                if (col == punto.getColumna() && fila == punto.getFila()) {
                    return true;
                }
                
                if (col == fin.getColumna() && fila == fin.getFila()) {
                    break; // Llegamos al final sin encontrar el punto
                }
                
                col += pasoCol;
                fila += pasoFila;
            }
        }
        
        return false;
    }
    
    // Determina la dirección de la banda
    public char getDireccionVisual() {
        int deltaCol = fin.getColumna() - inicio.getColumna();
        int deltaFila = fin.getFila() - inicio.getFila();
        
        // Si es horizontal (Este-Oeste)
        if (deltaFila == 0) {
            return '-';
        }
        
        // Si es diagonal Noreste (E) o Suroeste (Z)
        if ((deltaCol > 0 && deltaFila < 0) || (deltaCol < 0 && deltaFila > 0)) {
            return '/';
        }
        // Si es diagonal Noroeste (Q) o Sureste (C)
        else {
            return '\\';
        }
    }
    
    @Override
    public String toString() {
        return "Banda de " + inicio + " a " + fin;
    }
}
