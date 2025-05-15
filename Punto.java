/**
 * Clase que representa un punto en el tablero
 * @author Tu Nombre
 * @author Tu Número
 */
public class Punto {
    private char columna; // A-M
    private int fila; // 1-7
    
    public Punto(char columna, int fila) {
        this.columna = Character.toUpperCase(columna);
        this.fila = fila;
    }
    
    public char getColumna() {
        return columna;
    }
    
    public int getFila() {
        return fila;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Punto otro = (Punto) obj;
        return columna == otro.columna && fila == otro.fila;
    }
    
    @Override
    public int hashCode() {
        return 31 * columna + fila;
    }
    
    @Override
    public String toString() {
        return columna + "" + fila;
    }
}
