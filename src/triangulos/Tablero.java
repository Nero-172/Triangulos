package triangulos;

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
    
    // Método simplificado para colocar una banda 
    public int colocarBanda(List<Punto> puntos, boolean esBlanco) {
        // Crear la banda
        Banda nuevaBanda = new Banda(puntos, esBlanco);
        bandas.add(nuevaBanda);
        
        System.out.println("Banda colocada: " + nuevaBanda);
        
        // Verificar si se formaron nuevos triángulos
        return verificarTriangulos(esBlanco);
    }
    
    // Método para verificar triángulos
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
                    if (formanTriangulo(p1, p2, p3)) {
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
    
    // Método para verificar si tres puntos forman un triángulo
    private boolean formanTriangulo(Punto p1, Punto p2, Punto p3) {
        return estanConectados(p1, p2) && estanConectados(p1, p3) && estanConectados(p2, p3);
    }
    
    // Verifica si dos puntos están conectados por segmentos adyacentes de una banda
    private boolean estanConectados(Punto p1, Punto p2) {
        for (Banda banda : bandas) {
            List<Punto> puntos = banda.getPuntos();
            
            for (int i = 0; i < puntos.size() - 1; i++) {
                Punto inicio = puntos.get(i);
                Punto fin = puntos.get(i + 1);
                
                // Si estos dos puntos forman un segmento de la banda
                if ((inicio.equals(p1) && fin.equals(p2)) || 
                    (inicio.equals(p2) && fin.equals(p1))) {
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
    
    // Método simplificado para mostrar el tablero
    public void mostrar() {
        mostrar(false);
    }
    
    public void mostrar(boolean compacto) {
        char[][] tableroVisual = obtenerRepresentacionVisual();
        int filaVisualMax = tableroVisual.length;
        
        // Imprimir el tablero
        System.out.println();
        
        // Imprimir encabezado de columnas
        System.out.print("  "); // Ajuste inicial para alinear con los puntos
        for (char c = 'A'; c <= 'M'; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        System.out.println();
        
        // Imprimir el contenido del tablero
        for (int i = 0; i < filaVisualMax; i++) {
            System.out.print(" ");
            for (int j = 0; j < tableroVisual[i].length; j++) {
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
    
    // Método para dibujar una línea entre dos puntos en la matriz visual
    private void dibujarLinea(char[][] tablero, Punto p1, Punto p2) {
        // Calcular coordenadas visuales
        int fila1 = (p1.getFila() - 1) * 2;
        int col1 = (p1.getColumna() - 'A') * 2;
        int fila2 = (p2.getFila() - 1) * 2;
        int col2 = (p2.getColumna() - 'A') * 2;
        
        // Determinar el tipo de línea
        int deltaFila = p2.getFila() - p1.getFila();
        int deltaCol = p2.getColumna() - p1.getColumna();
        
        // Línea horizontal (Este-Oeste)
        if (deltaFila == 0) {
            int filaVisual = fila1;
            int colMin = Math.min(col1, col2);
            int colMax = Math.max(col1, col2);
        
            // Asegurar que se dibujen los guiones entre los puntos
            for (int col = colMin + 1; col < colMax; col++) {
                tablero[filaVisual][col] = '-';
            }
        }
        // Línea diagonal
        else {
            // Dibujar diagonal usando Bresenham
            int dx = Math.abs(col2 - col1);
            int dy = Math.abs(fila2 - fila1);
            int sx = col1 < col2 ? 1 : -1;
            int sy = fila1 < fila2 ? 1 : -1;
            int err = dx - dy;
        
            int x = col1;
            int y = fila1;
        
            char diagonal = (deltaFila * deltaCol > 0) ? '\\' : '/';
        
            while (true) {
                if (x == col2 && y == fila2) break;
            
                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    x += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y += sy;
                }
            
                // Dibujar si no es un punto del tablero
                if ((x != col1 || y != fila1) && (x != col2 || y != fila2)) {
                    if (x >= 0 && x < tablero[0].length && y >= 0 && y < tablero.length) {
                        tablero[y][x] = diagonal;
                    }
                }
            }
        }
    }
    
    // Método para obtener la representación visual del tablero como una matriz de caracteres
    public char[][] obtenerRepresentacionVisual() {
        // Crear una matriz para representar el tablero con espacio para las bandas
        int filaVisualMax = FILAS * 2 - 1;
        int colVisualMax = COLUMNAS * 2 - 1;
        char[][] tableroVisual = new char[filaVisualMax][colVisualMax];
        
        // Inicializar con espacios
        for (int i = 0; i < filaVisualMax; i++) {
            for (int j = 0; j < colVisualMax; j++) {
                tableroVisual[i][j] = ' ';
            }
        }
        
        // Colocar asteriscos en los puntos válidos del tablero
        for (int fila = 1; fila <= FILAS; fila++) {
            int desplazamiento = Math.abs(4 - fila);
            char colMin = (char)('A' + desplazamiento);
            char colMax = (char)('M' - desplazamiento);
            
            for (char col = colMin; col <= colMax; col += 2) {
                Punto punto = new Punto(col, fila);
                
                // Calcular posición en la matriz visual
                int filaVisual = (fila - 1) * 2;
                int colVisual = (col - 'A') * 2;
                
                tableroVisual[filaVisual][colVisual] = '*';
            }
        }
        
        // Dibujar las bandas
        for (Banda banda : bandas) {
            List<Punto> puntos = banda.getPuntos();
            
            // Conectar puntos consecutivos
            for (int i = 0; i < puntos.size() - 1; i++) {
                Punto p1 = puntos.get(i);
                Punto p2 = puntos.get(i + 1);
                
                dibujarLineaEnMatriz(tableroVisual, p1, p2);
            }
        }
        
        // Marcar los centros de los triángulos visualmente
        for (Triangulo t : triangulos) {
            Punto p1 = t.getPunto1();
            Punto p2 = t.getPunto2();
            Punto p3 = t.getPunto3();
            
            // Calcular las posiciones visuales de los tres puntos
            int fila1 = (p1.getFila() - 1) * 2;
            int col1 = (p1.getColumna() - 'A') * 2;
            int fila2 = (p2.getFila() - 1) * 2;
            int col2 = (p2.getColumna() - 'A') * 2;
            int fila3 = (p3.getFila() - 1) * 2;
            int col3 = (p3.getColumna() - 'A') * 2;
            
            // Ordenar los puntos por fila (de arriba a abajo)
            int[] filas = {fila1, fila2, fila3};
            int[] cols = {col1, col2, col3};
            
            // Ordenamiento simple de burbuja
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2 - i; j++) {
                    if (filas[j] > filas[j + 1]) {
                        // Intercambiar filas
                        int tempFila = filas[j];
                        filas[j] = filas[j + 1];
                        filas[j + 1] = tempFila;
                        
                        // Intercambiar columnas correspondientes
                        int tempCol = cols[j];
                        cols[j] = cols[j + 1];
                        cols[j + 1] = tempCol;
                    }
                }
            }
            
            // Ahora filas[0] es el punto más alto, filas[2] es el más bajo
            
            // Para triángulos con un punto arriba y dos abajo
            if (filas[0] < filas[1] && filas[1] == filas[2]) {
                // Colocar el marcador en la fila intermedia, entre las columnas
                int f = filas[0] + 1;
                int c = (cols[1] + cols[2]) / 2;
                
                if (f >= 0 && f < filaVisualMax && c >= 0 && c < colVisualMax && 
                    tableroVisual[f][c] == ' ') {
                    tableroVisual[f][c] = t.esBlanco() ? '□' : '■';
                }
            }
            // Para triángulos con dos puntos arriba y uno abajo
            else if (filas[0] == filas[1] && filas[1] < filas[2]) {
                // Colocar el marcador en la fila intermedia, entre las columnas
                int f = filas[0] + 1;
                int c = (cols[0] + cols[1]) / 2;
                
                if (f >= 0 && f < filaVisualMax && c >= 0 && c < colVisualMax && 
                    tableroVisual[f][c] == ' ') {
                    tableroVisual[f][c] = t.esBlanco() ? '□' : '■';
                }
            }
            // Para triángulos con tres puntos en diferentes filas
            else {
                // Colocar el marcador en la fila intermedia
                int f = filas[1];
                int c = cols[1];
                
                // Buscar un espacio vacío cerca del punto intermedio
                for (int df = -1; df <= 1; df++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int newF = f + df;
                        int newC = c + dc;
                        
                        if (newF >= 0 && newF < filaVisualMax && newC >= 0 && newC < colVisualMax && 
                            tableroVisual[newF][newC] == ' ') {
                            tableroVisual[newF][newC] = t.esBlanco() ? '□' : '■';
                            // Una vez colocado, salir de los bucles
                            df = 2; // Forzar salida del bucle externo
                            break;  // Salir del bucle interno
                        }
                    }
                }
            }
        }
        
        return tableroVisual;
    }
    
    // Método para dibujar una línea entre dos puntos en la matriz visual (sin imprimir mensajes de depuración)
    private void dibujarLineaEnMatriz(char[][] tablero, Punto p1, Punto p2) {
        // Calcular coordenadas visuales
        int fila1 = (p1.getFila() - 1) * 2;
        int col1 = (p1.getColumna() - 'A') * 2;
        int fila2 = (p2.getFila() - 1) * 2;
        int col2 = (p2.getColumna() - 'A') * 2;
        
        // Determinar el tipo de línea
        int deltaFila = p2.getFila() - p1.getFila();
        int deltaCol = p2.getColumna() - p1.getColumna();
        
        // Línea horizontal (Este-Oeste)
        if (deltaFila == 0) {
            int filaVisual = fila1;
            int colMin = Math.min(col1, col2);
            int colMax = Math.max(col1, col2);
        
            // Asegurar que se dibujen los guiones entre los puntos
            for (int col = colMin + 1; col < colMax; col++) {
                tablero[filaVisual][col] = '-';
            }
        }
        // Línea diagonal
        else {
            // Dibujar diagonal usando Bresenham
            int dx = Math.abs(col2 - col1);
            int dy = Math.abs(fila2 - fila1);
            int sx = col1 < col2 ? 1 : -1;
            int sy = fila1 < fila2 ? 1 : -1;
            int err = dx - dy;
        
            int x = col1;
            int y = fila1;
        
            char diagonal = (deltaFila * deltaCol > 0) ? '\\' : '/';
        
            while (true) {
                if (x == col2 && y == fila2) break;
            
                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    x += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y += sy;
                }
            
                // Dibujar si no es un punto del tablero
                if ((x != col1 || y != fila1) && (x != col2 || y != fila2)) {
                    if (x >= 0 && x < tablero[0].length && y >= 0 && y < tablero.length) {
                        tablero[y][x] = diagonal;
                    }
                }
            }
        }
    }
}
