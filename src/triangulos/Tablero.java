package triangulos;
/**
 * Clase que representa el tablero de juego
 * @author Tu Nombre
 * @author Tu Número
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        
        // Si hay menos de 3 bandas, no puede haber triángulos
        if (bandas.size() < 3) {
            return 0;
        }
        
        // Generar todos los puntos válidos del tablero
        Set<Punto> puntosValidos = generarPuntosTablero();
        
        // Identificar los puntos por los que pasan bandas
        List<Punto> puntosConBandas = new ArrayList<>();
        for (Punto p : puntosValidos) {
            for (Banda b : bandas) {
                if (b.pasaPorPunto(p)) {
                    puntosConBandas.add(p);
                    break;
                }
            }
        }
        
        // Verificar todas las combinaciones posibles de 3 puntos
        for (int i = 0; i < puntosConBandas.size(); i++) {
            for (int j = i + 1; j < puntosConBandas.size(); j++) {
                for (int k = j + 1; k < puntosConBandas.size(); k++) {
                    Punto p1 = puntosConBandas.get(i);
                    Punto p2 = puntosConBandas.get(j);
                    Punto p3 = puntosConBandas.get(k);
                    
                    // Verificar si estos tres puntos forman un triángulo
                    if (formanCircuitoCerrado(p1, p2, p3)) {
                        Triangulo nuevoTriangulo = new Triangulo(p1, p2, p3);
                        
                        // Verificar si el triángulo ya existe
                        if (!trianguloYaExiste(nuevoTriangulo)) {
                            nuevoTriangulo.setEsBlanco(esBlanco);
                            triangulos.add(nuevoTriangulo);
                            nuevosTriangulos++;
                            
                            // Imprimir información sobre el triángulo formado
                            System.out.println("¡Se ha formado un triángulo! Puntos: " + 
                                              p1 + ", " + p2 + ", " + p3 + 
                                              ", Centro: " + nuevoTriangulo.getCentro());
                        }
                    }
                }
            }
        }
        
        return nuevosTriangulos;
    }
    
    // Verifica si tres puntos forman un circuito cerrado (están conectados por bandas)
    private boolean formanCircuitoCerrado(Punto p1, Punto p2, Punto p3) {
        // Verificar si hay bandas que conectan cada par de puntos
        boolean hayBandaP1P2 = estanConectados(p1, p2);
        boolean hayBandaP1P3 = estanConectados(p1, p3);
        boolean hayBandaP2P3 = estanConectados(p2, p3);
        
        // Para formar un circuito cerrado, necesitamos que los tres pares estén conectados
        return hayBandaP1P2 && hayBandaP1P3 && hayBandaP2P3;
    }
    
    // Verifica si dos puntos están conectados (directamente o a través de un punto intermedio)
    private boolean estanConectados(Punto p1, Punto p2) {
        // Verificar si hay una banda directa entre los puntos
        for (Banda b : bandas) {
            if (b.pasaPorPunto(p1) && b.pasaPorPunto(p2)) {
                return true;
            }
        }
        
        // Verificar si están en la misma fila y hay un punto intermedio
        if (p1.getFila() == p2.getFila()) {
            char colMin = (char) Math.min(p1.getColumna(), p2.getColumna());
            char colMax = (char) Math.max(p1.getColumna(), p2.getColumna());
            
            // Si hay exactamente 2 columnas de diferencia (un punto intermedio)
            if (colMax - colMin == 2) {
                char colIntermedia = (char) (colMin + 1);
                Punto puntoIntermedio = new Punto(colIntermedia, p1.getFila());
                
                // Verificar si hay bandas que pasan por el punto intermedio
                for (Banda b : bandas) {
                    if (b.pasaPorPunto(puntoIntermedio)) {
                        return true;
                    }
                }
            }
        }
        
        // Verificar si están en la misma columna y hay un punto intermedio
        if (p1.getColumna() == p2.getColumna()) {
            int filaMin = Math.min(p1.getFila(), p2.getFila());
            int filaMax = Math.max(p1.getFila(), p2.getFila());
            
            // Si hay exactamente 2 filas de diferencia (un punto intermedio)
            if (filaMax - filaMin == 2) {
                int filaIntermedia = filaMin + 1;
                Punto puntoIntermedio = new Punto(p1.getColumna(), filaIntermedia);
                
                // Verificar si hay bandas que pasan por el punto intermedio
                for (Banda b : bandas) {
                    if (b.pasaPorPunto(puntoIntermedio)) {
                        return true;
                    }
                }
            }
        }
        
        // Verificar si están en la misma diagonal y hay un punto intermedio
        int deltaFila = p2.getFila() - p1.getFila();
        int deltaCol = p2.getColumna() - p1.getColumna();
        
        // Si la pendiente es 1 o -1 (diagonal) y hay exactamente 2 unidades de diferencia
        if (Math.abs(deltaFila) == Math.abs(deltaCol) && Math.abs(deltaFila) == 2) {
            int filaIntermedia = (p1.getFila() + p2.getFila()) / 2;
            char colIntermedia = (char) ((p1.getColumna() + p2.getColumna()) / 2);
            Punto puntoIntermedio = new Punto(colIntermedia, filaIntermedia);
            
            // Verificar si hay bandas que pasan por el punto intermedio
            for (Banda b : bandas) {
                if (b.pasaPorPunto(puntoIntermedio)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // Método para generar todos los puntos válidos del tablero (patrón hexagonal)
    private Set<Punto> generarPuntosTablero() {
        Set<Punto> puntos = new HashSet<>();
        
        for (int fila = 1; fila <= FILAS; fila++) {
            int desplazamiento = Math.abs(4 - fila);
            char colMin = (char)('A' + desplazamiento);
            char colMax = (char)('M' - desplazamiento);
            
            for (char col = colMin; col <= colMax; col += 2) {
                puntos.add(new Punto(col, fila));
            }
        }
        
        return puntos;
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
        
        // Verificar si la columna está en el patrón (salta de 2 en 2)
        if (columna < colMin || columna > colMax) {
            return false;
        }
        
        // En el patrón hexagonal, las columnas válidas son A, C, E, G, I, K, M en la fila 4
        // y se desplazan según la fila
        return (columna - colMin) % 2 == 0;
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

            // Salta uno y coloca uno
            for (char col = colMin; col <= colMax; col += 2) {
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
                
                 for (int col = colInicioVisual + 1; col < colFinVisual; col += 4) {
                        if (col + 2 < colVisualMax) {
                            tableroVisual[filaInicioVisual][col] = '-';
                            tableroVisual[filaInicioVisual][col + 1] = '-';
                            tableroVisual[filaInicioVisual][col + 2] = '-';
                        }
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
                    if (colActual != filaFinVisual) colActual += pasoCol;
                    
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
        
        // Agregar dos líneas de separación entre el encabezado y el tablero
        System.out.println();
        System.out.println();
        
        // Imprimir el tablero
        // Imprimir el tablero
        for (int i = 0; i < filaVisualMax; i++) {
            System.out.print(" ");  // ← No muestra números de fila

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
    
    // Método para depurar la detección de triángulos (solo para pruebas)
    public void depurarTriangulos() {
        System.out.println("\n--- DEPURACIÓN DE TRIÁNGULOS ---");
        
        // Generar todos los puntos válidos del tablero
        Set<Punto> puntosValidos = generarPuntosTablero();
        
        // Imprimir todas las bandas
        System.out.println("Bandas actuales: " + bandas.size());
        for (int i = 0; i < bandas.size(); i++) {
            Banda b = bandas.get(i);
            System.out.println("Banda " + (i+1) + ": " + b.getInicio() + " a " + b.getFin());
        }
        
        // Imprimir todos los puntos válidos
        System.out.println("\nPuntos válidos del tablero:");
        for (Punto p : puntosValidos) {
            System.out.print(p + " ");
        }
        System.out.println();
        
        // Verificar qué puntos están en cada banda
        System.out.println("\nPuntos por los que pasan bandas:");
        for (Punto p : puntosValidos) {
            List<Integer> bandasQuePasan = new ArrayList<>();
            for (int i = 0; i < bandas.size(); i++) {
                if (bandas.get(i).pasaPorPunto(p)) {
                    bandasQuePasan.add(i + 1);
                }
            }
            if (!bandasQuePasan.isEmpty()) {
                System.out.println("  " + p + ": Bandas " + bandasQuePasan);
            }
        }
        
        // Identificar los puntos por los que pasan bandas
        List<Punto> puntosConBandas = new ArrayList<>();
        for (Punto p : puntosValidos) {
            boolean tieneBanda = false;
            for (Banda b : bandas) {
                if (b.pasaPorPunto(p)) {
                    tieneBanda = true;
                    break;
                }
            }
            if (tieneBanda) {
                puntosConBandas.add(p);
            }
        }
        
        System.out.println("\nPuntos con bandas:");
        for (Punto p : puntosConBandas) {
            System.out.print(p + " ");
        }
        System.out.println();
        
        // Verificar conexiones entre puntos
        System.out.println("\nVerificando conexiones entre puntos:");
        for (int i = 0; i < puntosConBandas.size(); i++) {
            for (int j = i + 1; j < puntosConBandas.size(); j++) {
                Punto p1 = puntosConBandas.get(i);
                Punto p2 = puntosConBandas.get(j);
                
                boolean conectados = estanConectados(p1, p2);
                System.out.println("  " + p1 + " - " + p2 + ": " + (conectados ? "Conectados" : "No conectados"));
                
                // Si están conectados, mostrar cómo están conectados
                if (conectados) {
                    // Verificar si hay una banda directa
                    boolean bandaDirecta = false;
                    for (Banda b : bandas) {
                        if (b.pasaPorPunto(p1) && b.pasaPorPunto(p2)) {
                            bandaDirecta = true;
                            System.out.println("    Conectados directamente por una banda");
                            break;
                        }
                    }
                    
                    if (!bandaDirecta) {
                        // Verificar si están en la misma fila y hay un punto intermedio
                        if (p1.getFila() == p2.getFila() && Math.abs(p1.getColumna() - p2.getColumna()) == 2) {
                            char colIntermedia = (char) ((p1.getColumna() + p2.getColumna()) / 2);
                            System.out.println("    Conectados a través del punto intermedio " + 
                                              new Punto(colIntermedia, p1.getFila()));
                        }
                        // Verificar si están en la misma columna y hay un punto intermedio
                        else if (p1.getColumna() == p2.getColumna() && Math.abs(p1.getFila() - p2.getFila()) == 2) {
                            int filaIntermedia = (p1.getFila() + p2.getFila()) / 2;
                            System.out.println("    Conectados a través del punto intermedio " + 
                                              new Punto(p1.getColumna(), filaIntermedia));
                        }
                        // Verificar si están en la misma diagonal y hay un punto intermedio
                        else if (Math.abs(p1.getFila() - p2.getFila()) == 2 && 
                                Math.abs(p1.getColumna() - p2.getColumna()) == 2) {
                            int filaIntermedia = (p1.getFila() + p2.getFila()) / 2;
                            char colIntermedia = (char) ((p1.getColumna() + p2.getColumna()) / 2);
                            System.out.println("    Conectados a través del punto intermedio " + 
                                              new Punto(colIntermedia, filaIntermedia));
                        }
                    }
                }
            }
        }
        
        // Verificar todas las combinaciones posibles de 3 puntos
        System.out.println("\nVerificando combinaciones de 3 puntos:");
        for (int i = 0; i < puntosConBandas.size(); i++) {
            for (int j = i + 1; j < puntosConBandas.size(); j++) {
                for (int k = j + 1; k < puntosConBandas.size(); k++) {
                    Punto p1 = puntosConBandas.get(i);
                    Punto p2 = puntosConBandas.get(j);
                    Punto p3 = puntosConBandas.get(k);
                    
                    // Verificar conexiones entre puntos
                    boolean hayBandaP1P2 = estanConectados(p1, p2);
                    boolean hayBandaP1P3 = estanConectados(p1, p3);
                    boolean hayBandaP2P3 = estanConectados(p2, p3);
                    
                    System.out.println("Puntos: " + p1 + ", " + p2 + ", " + p3);
                    System.out.println("  " + p1 + " - " + p2 + ": " + (hayBandaP1P2 ? "Conectados" : "No conectados"));
                    System.out.println("  " + p1 + " - " + p3 + ": " + (hayBandaP1P3 ? "Conectados" : "No conectados"));
                    System.out.println("  " + p2 + " - " + p3 + ": " + (hayBandaP2P3 ? "Conectados" : "No conectados"));
                    
                    boolean formanTriangulo = hayBandaP1P2 && hayBandaP1P3 && hayBandaP2P3;
                    System.out.println("  ¿Forman triángulo? " + (formanTriangulo ? "SÍ" : "NO"));
                    System.out.println();
                }
            }
        }
    }
}