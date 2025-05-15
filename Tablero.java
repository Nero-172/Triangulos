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
            if (banda.getInicio().equals(punto) || banda.getFin().equals(punto)) {
                return true;
            }
        }
        return false;
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
        
        // Imprimir filas
        for (int fila = 1; fila <= FILAS; fila++) {
            System.out.print(fila + " ");
            
            for (char col = 'A'; col <= 'M'; col++) {
                Punto punto = new Punto(col, fila);
                
                // Verificar si hay un triángulo en este punto
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
                    System.out.print(esTrianguloBlanco ? " □" : " ■");
                } else {
                    // Verificar si hay una banda que pasa por este punto
                    boolean hayBanda = false;
                    for (Banda b : bandas) {
                        if (b.pasaPorPunto(punto)) {
                            hayBanda = true;
                            break;
                        }
                    }
                    
                    if (hayBanda) {
                        System.out.print(" +");
                    } else {
                        System.out.print(" *");
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
