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
        
        // Verificar si el punto está en la línea entre inicio y fin
        // Para bandas horizontales
        if (inicio.getFila() == fin.getFila() && punto.getFila() == inicio.getFila()) {
            char colMin = (char) Math.min(inicio.getColumna(), fin.getColumna());
            char colMax = (char) Math.max(inicio.getColumna(), fin.getColumna());
            return punto.getColumna() >= colMin && punto.getColumna() <= colMax;
        }
        
        // Para bandas verticales
        if (inicio.getColumna() == fin.getColumna() && punto.getColumna() == inicio.getColumna()) {
            int filaMin = Math.min(inicio.getFila(), fin.getFila());
            int filaMax = Math.max(inicio.getFila(), fin.getFila());
            return punto.getFila() >= filaMin && punto.getFila() <= filaMax;
        }
        
        // Para bandas diagonales
        if (Math.abs(fin.getColumna() - inicio.getColumna()) == Math.abs(fin.getFila() - inicio.getFila())) {
            // Determinar la dirección de la diagonal
            int pasoFila = inicio.getFila() < fin.getFila() ? 1 : -1;
            int pasoColumna = inicio.getColumna() < fin.getColumna() ? 1 : -1;
            
            // Recorrer la diagonal
            char col = inicio.getColumna();
            int fila = inicio.getFila();
            
            while (col != fin.getColumna() && fila != fin.getFila()) {
                if (col == punto.getColumna() && fila == punto.getFila()) {
                    return true;
                }
                col += pasoColumna;
                fila += pasoFila;
            }
        }
        
        return false;
    }
}
