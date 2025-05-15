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
    
    // Mejorar el método pasaPorPunto para detectar correctamente los puntos en las bandas diagonales
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
        
        // Para bandas verticales (Norte-Sur)
        if (deltaCol == 0) {
            if (punto.getColumna() == inicio.getColumna()) {
                int filaMin = Math.min(inicio.getFila(), fin.getFila());
                int filaMax = Math.max(inicio.getFila(), fin.getFila());
                return punto.getFila() >= filaMin && punto.getFila() <= filaMax;
            }
            return false;
        }
        
        // Para bandas diagonales
        // Si es una diagonal perfecta (45 grados)
        if (Math.abs(deltaCol) == Math.abs(deltaFila)) {
            // Determinar la dirección
            int pasoFila = deltaFila > 0 ? 1 : -1;
            int pasoCol = deltaCol > 0 ? 1 : -1;
            
            // Recorrer la diagonal desde el inicio
            char col = inicio.getColumna();
            int fila = inicio.getFila();
            
            while (col != fin.getColumna() || fila != fin.getFila()) {
                if (col == punto.getColumna() && fila == punto.getFila()) {
                    return true;
                }
                
                col += pasoCol;
                fila += pasoFila;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return "Banda de " + inicio + " a " + fin;
    }
}


