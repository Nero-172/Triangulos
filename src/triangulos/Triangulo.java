package triangulos;
/**
 * Clase que representa un triángulo formado en el tablero
 * @author Tu Nombre
 * @author Tu Número
 */
public class Triangulo {
    private Punto p1;
    private Punto p2;
    private Punto p3;
    private Punto centro; // Punto central del triángulo
    private boolean esBlanco; // true si fue ganado por el jugador blanco
    
    public Triangulo(Punto p1, Punto p2, Punto p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.centro = calcularCentro();
    }
    
    // Calcula el punto central del triángulo
    private Punto calcularCentro() {
        // Para triángulos, el centro es el baricentro (promedio de coordenadas)
        int filaPromedio = Math.round((p1.getFila() + p2.getFila() + p3.getFila()) / 3.0f);
        char columnaPromedio = (char) Math.round((p1.getColumna() + p2.getColumna() + p3.getColumna()) / 3.0);
        
        // Verificar si el punto calculado está dentro del tablero
        if (columnaPromedio >= 'A' && columnaPromedio <= 'M' && filaPromedio >= 1 && filaPromedio <= 7) {
            return new Punto(columnaPromedio, filaPromedio);
        }
        
        // Si no se puede determinar un centro válido, devolver null
        return null;
    }
    
    public Punto getPunto1() {
        return p1;
    }
    
    public Punto getPunto2() {
        return p2;
    }
    
    public Punto getPunto3() {
        return p3;
    }
    
    public Punto getCentro() {
        return centro;
    }
    
    public void setEsBlanco(boolean esBlanco) {
        this.esBlanco = esBlanco;
    }
    
    public boolean esBlanco() {
        return esBlanco;
    }
    
    public boolean contienePunto(Punto punto) {
        return p1.equals(punto) || p2.equals(punto) || p3.equals(punto);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Triangulo otro = (Triangulo) obj;
        
        // Dos triángulos son iguales si tienen los mismos puntos (en cualquier orden)
        return (contienePunto(otro.p1) && contienePunto(otro.p2) && contienePunto(otro.p3) &&
                otro.contienePunto(p1) && otro.contienePunto(p2) && otro.contienePunto(p3));
    }
    
    @Override
    public int hashCode() {
        // Un hash consistente independiente del orden de los puntos
        return p1.hashCode() + p2.hashCode() + p3.hashCode();
    }
}
