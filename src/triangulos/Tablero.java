package triangulos;
/**
 * Clase que representa el tablero de juego
 * @author Tu Nombre
 * @author Tu Número
 */
import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private static final int FILAS = 7;
    private static final int COLUMNAS = 13; // A-M
    
    private List<Banda> bandas;
    private List<Triangulo> triangulos;
    
    public Tablero() {
        bandas = new ArrayList<>();
        triangulos = new ArrayList<>();
    }
    
    // Constructor de copia para el historial
    public Tablero(Tablero otro) {
        this.bandas = new ArrayList<>(otro.bandas);
        this.triangulos = new ArrayList<>(otro.triangulos);
    }
    
    public List<Banda> getBandas() {
        return bandas;
    }
    
    public int colocarBanda(Punto inicio, char direccion, int longitud, boolean esBlanco) {
        Punto fin = calcularPuntoFinal(inicio, direccion, longitud);
        Banda nuevaBanda = new Banda(inicio, fin, esBlanco);
        bandas.add(nuevaBanda);
        
        // Verificar si se formaron nuevos triángulos
        return verificarTriangulos(esBlanco);
    }
    
    private Punto calcularPuntoFinal(Punto inicio, char direccion, int longitud) {
        char columnaInicio = inicio.getColumna();
        int filaInicio = inicio.getFila();
        
        switch (direccion) {
            case 'Q': // Noroeste
                return new Punto((char)(columnaInicio - longitud), filaInicio - longitud);
            case 'E': // Noreste
                return new Punto((char)(columnaInicio + longitud), filaInicio - longitud);
            case 'D': // Este
                return new Punto((char)(columnaInicio + longitud), filaInicio);
            case 'C': // Sureste
                return new Punto((char)(columnaInicio + longitud), filaInicio + longitud);
            case 'Z': // Suroeste
                return new Punto((char)(columnaInicio - longitud), filaInicio + longitud);
            case 'A': // Oeste
                return new Punto((char)(columnaInicio - longitud), filaInicio);
            default:
                return null;
        }
    }
    
    private int verificarTriangulos(boolean esBlanco) {
        int nuevosTriangulos = 0;
        
        // Verificar todas las combinaciones posibles de 3 bandas
        for (int i = 0; i < bandas.size(); i++) {
            for (int j = i + 1; j < bandas.size(); j++) {
                for (int k = j + 1; k < bandas.size(); k++) {
                    Triangulo posibleTriangulo = formanTriangulo(bandas.get(i), bandas.get(j), bandas.get(k));
                    
                    if (posibleTriangulo != null && !trianguloYaExiste(posibleTriangulo)) {
                        posibleTriangulo.setEsBlanco(esBlanco);
                        triangulos.add(posibleTriangulo);
                        nuevosTriangulos++;
                        
                        // Imprimir información sobre el triángulo formado
                        System.out.println("¡Se ha formado un triángulo! Puntos: " + 
                                          posibleTriangulo.getPunto1() + ", " + 
                                          posibleTriangulo.getPunto2() + ", " + 
                                          posibleTriangulo.getPunto3() + 
                                          ", Centro: " + posibleTriangulo.getCentro());
                    }
                }
            }
        }
        
        return nuevosTriangulos;
    }
    
    private Triangulo formanTriangulo(Banda b1, Banda b2, Banda b3) {
        // Verificar si las tres bandas forman un triángulo de lado 1
        // Esto implica que cada banda debe conectar con las otras dos en sus extremos
        
        List<Punto> puntos = new ArrayList<>();
        puntos.add(b1.getInicio());
        puntos.add(b1.getFin());
        puntos.add(b2.getInicio());
        puntos.add(b2.getFin());
        puntos.add(b3.getInicio());
        puntos.add(b3.getFin());
        
        // Eliminar duplicados (puntos que aparecen más de una vez)
        List<Punto> puntosUnicos = new ArrayList<>();
        for (Punto p : puntos) {
            boolean yaExiste = false;
            for (Punto existente : puntosUnicos) {
                if (p.equals(existente)) {
                    yaExiste = true;
                    break;
                }
            }
            if (!yaExiste) {
                puntosUnicos.add(p);
            }
        }
        
        // Un triángulo debe tener exactamente 3 puntos únicos
        if (puntosUnicos.size() == 3) {
            return new Triangulo(puntosUnicos.get(0), puntosUnicos.get(1), puntosUnicos.get(2));
        }
        
        return null;
    }
    
    private boolean trianguloYaExiste(Triangulo triangulo) {
        for (Triangulo t : triangulos) {
            if (t.equals(triangulo)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean tieneContacto(Punto punto) {
        for (Banda banda : bandas) {
            if (banda.pasaPorPunto(punto)) {
                return true;
            }
        }
        return false;
    }
    
    // Verifica si un punto está dentro del tablero hexagonal
    public boolean esPuntoValido(Punto punto) {
        int fila = punto.getFila();
        char columna = punto.getColumna();
        
        // Verificar límites básicos
        if (fila < 1 || fila > FILAS || columna < 'A' || columna > 'M') {
            return false;
        }
        
        // Verificar si está dentro del patrón hexagonal
        int desplazamiento = Math.abs(4 - fila);
        char colMin = (char)('A' + desplazamiento);
        char colMax = (char)('M' - desplazamiento);
        
        return columna >= colMin && columna <= colMax;
    }
    
    // Determina qué carácter usar para representar una banda en un punto específico
    private char obtenerCaracterBanda(Punto punto) {
        for (Banda banda : bandas) {
            if (banda.pasaPorPunto(punto)) {
                // Determinar la dirección de la banda
                Punto inicio = banda.getInicio();
                Punto fin = banda.getFin();
                
                // Si es horizontal (Este-Oeste)
                if (inicio.getFila() == fin.getFila()) {
                    return '-';
                }
                
                // Si es diagonal
                int deltaCol = fin.getColumna() - inicio.getColumna();
                int deltaFila = fin.getFila() - inicio.getFila();
                
                // Diagonal Noreste (E) o Suroeste (Z)
                if ((deltaCol > 0 && deltaFila < 0) || (deltaCol < 0 && deltaFila > 0)) {
                    return '/';
                }
                // Diagonal Noroeste (Q) o Sureste (C)
                else {
                    return '\\';
                }
            }
        }
        return '*'; // Punto sin banda
    }
    
    public void mostrar() {
        mostrar(false);
    }
    
    public void mostrar(boolean compacto) {
        // Dibujar el tablero
        System.out.println();
        
        // Imprimir encabezado de columnas
        System.out.print("  ");
        for (char c = 'A'; c <= 'M'; c++) {
            System.out.print(" " + c);
        }
        System.out.println();
        
        // Imprimir filas con el patrón hexagonal
        for (int fila = 1; fila <= FILAS; fila++) {
            // Añadir línea en blanco para estirar verticalmente (excepto en modo compacto)
            if (!compacto && fila > 1) {
                System.out.println();
            }
            
            System.out.print(fila + " ");
            
            // Calcular el desplazamiento para esta fila
            int desplazamiento = Math.abs(4 - fila);
            
            // Añadir espacios iniciales según el desplazamiento
            for (int i = 0; i < desplazamiento; i++) {
                System.out.print("  ");
            }
            
            // Determinar cuántas columnas mostrar en esta fila
            int columnasEnFila = COLUMNAS - 2 * desplazamiento;
            
            for (int colIndex = 0; colIndex < columnasEnFila; colIndex++) {
                char col = (char)('A' + colIndex + desplazamiento);
                Punto punto = new Punto(col, fila);
                
                // Verificar si el punto es el centro de algún triángulo
                boolean esCentroTriangulo = false;
                boolean esCentroBlanco = false;
                
                for (Triangulo t : triangulos) {
                    if (t.getCentro() != null && t.getCentro().equals(punto)) {
                        esCentroTriangulo = true;
                        esCentroBlanco = t.esBlanco();
                        break;
                    }
                }
                
                if (esCentroTriangulo) {
                    System.out.print(esCentroBlanco ? " □" : " ■");
                } else {
                    // Verificar si hay un triángulo en este punto (vértice)
                    boolean hayTriangulo = false;
                    boolean esTrianguloBlanco = false;
                    
                    for (Triangulo t : triangulos) {
                        if (t.contienePunto(punto)) {
                            hayTriangulo = true;
                            esTrianguloBlanco = t.esBlanco();
                            break;
                        }
                    }
                    
                    if (hayTriangulo) {
                        // No mostrar los vértices del triángulo como cuadrados
                        // En su lugar, mostrar la banda
                        boolean hayBanda = false;
                        char caracterBanda = '*';
                        
                        for (Banda b : bandas) {
                            if (b.pasaPorPunto(punto)) {
                                hayBanda = true;
                                caracterBanda = obtenerCaracterBanda(punto);
                                break;
                            }
                        }
                        
                        if (hayBanda) {
                            System.out.print(" " + caracterBanda);
                        } else {
                            System.out.print(" *");
                        }
                    } else {
                        // Verificar si hay una banda que pasa por este punto
                        boolean hayBanda = false;
                        char caracterBanda = '*';
                        
                        for (Banda b : bandas) {
                            if (b.pasaPorPunto(punto)) {
                                hayBanda = true;
                                caracterBanda = obtenerCaracterBanda(punto);
                                break;
                            }
                        }
                        
                        if (hayBanda) {
                            System.out.print(" " + caracterBanda);
                        } else {
                            System.out.print(" *");
                        }
                    }
                }
            }
            
            System.out.println();
        }
        
        if (!compacto) {
            System.out.println();
        }
    }
}
