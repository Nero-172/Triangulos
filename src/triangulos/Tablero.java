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
    
    // Método calcularPuntoFinal (correcto)
    private Punto calcularPuntoFinal(Punto inicio, char direccion, int longitud) {
        char columnaInicio = inicio.getColumna();
        int filaInicio = inicio.getFila();
        
        switch (direccion) {
            case 'Q': // Noroeste (arriba-izquierda)
                return new Punto((char)(columnaInicio - longitud), filaInicio - longitud);
            case 'E': // Noreste (arriba-derecha)
                return new Punto((char)(columnaInicio + longitud), filaInicio - longitud);
            case 'D': // Este (derecha)
                return new Punto((char)(columnaInicio + longitud), filaInicio);
            case 'C': // Sureste (abajo-derecha)
                return new Punto((char)(columnaInicio + longitud), filaInicio + longitud);
            case 'Z': // Suroeste (abajo-izquierda)
                return new Punto((char)(columnaInicio - longitud), filaInicio + longitud);
            case 'A': // Oeste (izquierda)
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
                    Banda b1 = bandas.get(i);
                    Banda b2 = bandas.get(j);
                    Banda b3 = bandas.get(k);
                    
                    Triangulo posibleTriangulo = formanTriangulo(b1, b2, b3);
                    
                    if (posibleTriangulo != null) {
                        if (!trianguloYaExiste(posibleTriangulo)) {
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
        }
        
        return nuevosTriangulos;
    }
    
    private Triangulo formanTriangulo(Banda b1, Banda b2, Banda b3) {
        // Verificar si las tres bandas forman un triángulo
        // Esto implica que cada banda debe conectar con las otras dos en sus extremos
        
        List<Punto> puntos = new ArrayList<>();
        puntos.add(b1.getInicio());
        puntos.add(b1.getFin());
        puntos.add(b2.getInicio());
        puntos.add(b2.getFin());
        puntos.add(b3.getInicio());
        puntos.add(b3.getFin());
        
        // Contar cuántas veces aparece cada punto
        List<Punto> puntosUnicos = new ArrayList<>();
        List<Integer> contadores = new ArrayList<>();
        
        for (Punto p : puntos) {
            boolean encontrado = false;
            for (int i = 0; i < puntosUnicos.size(); i++) {
                if (p.equals(puntosUnicos.get(i))) {
                    contadores.set(i, contadores.get(i) + 1);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                puntosUnicos.add(p);
                contadores.add(1);
            }
        }
        
        // Verificar si hay exactamente 3 puntos que aparecen exactamente 2 veces cada uno
        List<Punto> verticesTriangulo = new ArrayList<>();
        for (int i = 0; i < puntosUnicos.size(); i++) {
            if (contadores.get(i) == 2) {
                verticesTriangulo.add(puntosUnicos.get(i));
            }
        }
        
        if (verticesTriangulo.size() == 3) {
            return new Triangulo(verticesTriangulo.get(0), verticesTriangulo.get(1), verticesTriangulo.get(2));
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
    
    public void mostrar() {
        mostrar(false);
    }
    
    // Método mostrar corregido para visualizar correctamente las bandas diagonales
    public void mostrar(boolean compacto) {
        // Crear una matriz para representar el tablero con espacio para las bandas
        int filaVisualMax = FILAS * 2 - 1;
        int colVisualMax = COLUMNAS * 2 - 1;
        char[][] tableroVisual = new char[filaVisualMax][colVisualMax];
        
        // Inicializar con espacios
        for (int i = 0; i < tableroVisual.length; i++) {
            for (int j = 0; j < tableroVisual[i].length; j++) {
                tableroVisual[i][j] = ' ';
            }
        }
        
        // Colocar asteriscos en los puntos válidos del tablero
        for (int fila = 1; fila <= FILAS; fila++) {
            int desplazamiento = Math.abs(4 - fila);
            char colMin = (char)('A' + desplazamiento);
            char colMax = (char)('M' - desplazamiento);
            
            for (char col = colMin; col <= colMax; col++) {
                Punto punto = new Punto(col, fila);
                
                // Calcular posición en la matriz visual
                int filaVisual = (fila - 1) * 2;
                int colVisual = (col - 'A') * 2;
                
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
                    tableroVisual[filaVisual][colVisual] = '□';
                } else {
                    tableroVisual[filaVisual][colVisual] = '*';
                }
            }
        }
        
        // Colocar bandas en el tablero
        for (Banda banda : bandas) {
            Punto inicio = banda.getInicio();
            Punto fin = banda.getFin();
            
            // Calcular posiciones en la matriz visual
            int filaInicioVisual = (inicio.getFila() - 1) * 2;
            int colInicioVisual = (inicio.getColumna() - 'A') * 2;
            int filaFinVisual = (fin.getFila() - 1) * 2;
            int colFinVisual = (fin.getColumna() - 'A') * 2;
            
            // Determinar la dirección de la banda
            int deltaFila = fin.getFila() - inicio.getFila();
            int deltaCol = fin.getColumna() - inicio.getColumna();
            
            // Dibujar la banda según su dirección
            if (deltaFila == 0) {
                // Banda horizontal (Este-Oeste)
                int filaVisual = filaInicioVisual;
                int colMin = Math.min(colInicioVisual, colFinVisual);
                int colMax = Math.max(colInicioVisual, colFinVisual);
                
                for (int col = colMin + 1; col < colMax; col++) {
                    tableroVisual[filaVisual][col] = '-';
                }
            } else if (deltaCol == 0) {
                // Banda vertical (Norte-Sur)
                int colVisual = colInicioVisual;
                int filaMin = Math.min(filaInicioVisual, filaFinVisual);
                int filaMax = Math.max(filaInicioVisual, filaFinVisual);
                
                for (int fila = filaMin + 1; fila < filaMax; fila++) {
                    tableroVisual[fila][colVisual] = '|';
                }
            } else {
                // Determinar si es una diagonal Noroeste-Sureste o Noreste-Suroeste
                boolean esDiagonalNOSE = (deltaCol * deltaFila > 0); // Noroeste-Sureste
                
                // Calcular los pasos para recorrer la diagonal
                int pasoFila = (deltaFila > 0) ? 1 : -1;
                int pasoCol = (deltaCol > 0) ? 1 : -1;
                
                // Dibujar la diagonal
                int filaActual = filaInicioVisual;
                int colActual = colInicioVisual;
                
                while (filaActual != filaFinVisual || colActual != colFinVisual) {
                    // Avanzar en la dirección diagonal
                    if (filaActual != filaFinVisual) filaActual += pasoFila;
                    if (colActual != colFinVisual) colActual += pasoCol;
                    
                    // Si estamos en un punto intermedio (no en un asterisco), dibujar la banda
                    if (filaActual % 2 != 0 || colActual % 2 != 0) {
                        if (filaActual >= 0 && filaActual < filaVisualMax && 
                            colActual >= 0 && colActual < colVisualMax) {
                            // Usar el carácter adecuado según la dirección
                            tableroVisual[filaActual][colActual] = esDiagonalNOSE ? '\\' : '/';
                        }
                    }
                }
            }
        }
        
        // Dibujar el tablero
        System.out.println();
        
        // Imprimir encabezado de columnas con espaciado adecuado
        System.out.print(" ");
        for (char c = 'A'; c <= 'M'; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        
        // Imprimir el tablero
        for (int i = 0; i < filaVisualMax; i++) {
            // Imprimir número de fila solo en las filas que corresponden a puntos
            if (i % 2 == 0) {
                System.out.print((i / 2 + 1));
            } else {
                System.out.print(" ");
            }
            
            // Imprimir contenido de la fila
            for (int j = 0; j < colVisualMax; j++) {
                System.out.print(tableroVisual[i][j]);
            }
            
            System.out.println();
            
            // Si estamos en modo compacto, omitir las filas intermedias
            if (compacto && i % 2 == 0 && i < filaVisualMax - 1) {
                i++;
            }
        }
        
        if (!compacto) {
            System.out.println();
        }
    }
}

