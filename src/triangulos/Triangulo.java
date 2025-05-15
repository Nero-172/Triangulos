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
    private boolean esBlanco; // true si fue ganado por el jugador blanco
    
    public Triangulo(Punto p1, Punto p2, Punto p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
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
