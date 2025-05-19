package triangulos;

import java.util.*;

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
        // Para triángulos en un tablero hexagonal, necesitamos encontrar un punto válido dentro del triángulo
    
        // Ordenar los puntos por fila y columna para facilitar el análisis
        List<Punto> puntos = new ArrayList<>();
        puntos.add(p1);
        puntos.add(p2);
        puntos.add(p3);
        Collections.sort(puntos, (a, b) -> {
            if (a.getFila() != b.getFila()) {
                return Integer.compare(a.getFila(), b.getFila());
            }
            return Character.compare(a.getColumna(), b.getColumna());
        });
        
        // Buscar puntos dentro del triángulo (no en los vértices)
        Set<Punto> puntosValidos = generarPuntosTablero();
        for (Punto p : puntosValidos) {
            // Excluir los vértices del triángulo
            if (p.equals(p1) || p.equals(p2) || p.equals(p3)) {
                continue;
            }
            
            // Verificar si el punto está dentro del triángulo
            if (estaPuntoEnTriangulo(p, p1, p2, p3)) {
                return p;
            }
        }
        
        // Si no encontramos un punto dentro, intentamos con el punto más cercano al centro
        float filaPromedio = (p1.getFila() + p2.getFila() + p3.getFila()) / 3.0f;
        float colPromedio = (p1.getColumna() + p2.getColumna() + p3.getColumna()) / 3.0f;
        
        Punto mejorPunto = null;
        double distanciaMinima = Double.MAX_VALUE;
        
        for (Punto p : puntosValidos) {
            // Excluir los vértices del triángulo
            if (p.equals(p1) || p.equals(p2) || p.equals(p3)) {
                continue;
            }
            
            double distancia = Math.pow(p.getFila() - filaPromedio, 2) + 
                              Math.pow(p.getColumna() - colPromedio, 2);
            
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                mejorPunto = p;
            }
        }
        
        // Si encontramos un punto cercano, lo usamos
        if (mejorPunto != null) {
            return mejorPunto;
        }
        
        // Si todo falla, usamos el punto medio entre los dos puntos más cercanos
        Punto medio = new Punto(
            (char)((p1.getColumna() + p2.getColumna() + p3.getColumna()) / 3),
            (p1.getFila() + p2.getFila() + p3.getFila()) / 3
        );
        
        return medio;
    }
    
    // Verifica si un punto es válido en el tablero hexagonal
    private boolean esValidoEnTablero(Punto p) {
        int fila = p.getFila();
        char col = p.getColumna();
        boolean noCumple = false;
        
        // Verificar límites básicos
        if (fila < 1 || fila > 7 || col < 'A' || col > 'M') {
            return noCumple;
        }
        
        // Verificar si está dentro del patrón hexagonal
        int desplazamiento = Math.abs(4 - fila);
        char colMin = (char)('A' + desplazamiento);
        char colMax = (char)('M' - desplazamiento);
        
        // Verificar si la columna está en el patrón (salta de 2 en 2)
        if (col < colMin || col > colMax) {
            return noCumple;
        }
        
        // En el patrón hexagonal, las columnas válidas son A, C, E, G, I, K, M en la fila 4
        // y se desplazan según la fila
        return (col - colMin) % 2 == 0;
    }
    
    // Verifica si un punto está dentro del triángulo
    private boolean estaEnTriangulo(Punto p) {
        // Simplificación: si el punto es uno de los vértices, está en el triángulo
        return p.equals(p1) || p.equals(p2) || p.equals(p3);
    }
    
    // Calcula el punto central entre dos puntos
    private Punto puntoCentral(Punto a, Punto b) {
        int filaMedia = (a.getFila() + b.getFila()) / 2;
        char colMedia = (char) ((a.getColumna() + b.getColumna()) / 2);
        return new Punto(colMedia, filaMedia);
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

    private boolean estaPuntoEnTriangulo(Punto p, Punto a, Punto b, Punto c) {
        // Verificamos si el punto está dentro del triángulo usando el método de área
        double areaTotal = calcularArea(a, b, c);
        double area1 = calcularArea(p, b, c);
        double area2 = calcularArea(a, p, c);
        double area3 = calcularArea(a, b, p);
        
        // Si la suma de las áreas de los subtriángulos es igual al área total,
        // entonces el punto está dentro del triángulo
        return Math.abs(areaTotal - (area1 + area2 + area3)) < 0.1;
    }

    private double calcularArea(Punto a, Punto b, Punto c) {
        // Calcular el área de un triángulo usando la fórmula de determinante
        return Math.abs((a.getColumna() * (b.getFila() - c.getFila()) + 
                         b.getColumna() * (c.getFila() - a.getFila()) + 
                         c.getColumna() * (a.getFila() - b.getFila())) / 2.0);
    }

    private Set<Punto> generarPuntosTablero() {
        Set<Punto> puntos = new HashSet<>();
        
        for (int fila = 1; fila <= 7; fila++) {
            int desplazamiento = Math.abs(4 - fila);
            char colMin = (char)('A' + desplazamiento);
            char colMax = (char)('M' - desplazamiento);
            
            for (char col = colMin; col <= colMax; col += 2) {
                puntos.add(new Punto(col, fila));
            }
        }
        
        return puntos;
    }
}
