package triangulos;
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
        // Verificar si el punto es uno de los extremos
        if (inicio.equals(punto) || fin.equals(punto)) {
            return true;
        }
        
        // Verificar si el punto está en la línea entre inicio y fin
        int deltaFilaTotal = fin.getFila() - inicio.getFila();
        int deltaColumnaTotal = fin.getColumna() - inicio.getColumna();
        
        // Si la banda es horizontal
        if (deltaFilaTotal == 0) {
            return punto.getFila() == inicio.getFila() && 
                   estaDentroRango(punto.getColumna(), inicio.getColumna(), fin.getColumna());
        }
        
        // Si la banda es vertical
        if (deltaColumnaTotal == 0) {
            return punto.getColumna() == inicio.getColumna() && 
                   estaDentroRango(punto.getFila(), inicio.getFila(), fin.getFila());
        }
        
        // Si la banda es diagonal
        // Verificar si la pendiente es consistente
        int deltaFilaPunto = punto.getFila() - inicio.getFila();
        int deltaColumnaPunto = punto.getColumna() - inicio.getColumna();
        
        // Verificar si el punto está en la línea diagonal
        if (deltaFilaPunto * deltaColumnaTotal == deltaColumnaPunto * deltaFilaTotal) {
            // Verificar si el punto está dentro del rango de la banda
            return estaDentroRango(punto.getFila(), inicio.getFila(), fin.getFila()) && 
                   estaDentroRango(punto.getColumna(), inicio.getColumna(), fin.getColumna());
        }
        
        return false;
    }
    
    private boolean estaDentroRango(int valor, int limite1, int limite2) {
        int min = Math.min(limite1, limite2);
        int max = Math.max(limite1, limite2);
        return valor >= min && valor <= max;
    }
    
    private boolean estaDentroRango(char valor, char limite1, char limite2) {
        char min = (char)Math.min(limite1, limite2);
        char max = (char)Math.max(limite1, limite2);
        return valor >= min && valor <= max;
    }
    
    @Override
    public String toString() {
        return inicio.toString() + " -> " + fin.toString();
    }
}


