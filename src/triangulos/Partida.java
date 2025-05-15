package triangulos;
/**
 * Clase que representa una partida del juego
 * @author Tu Nombre
 * @author Tu Número
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Partida {
    private Jugador jugadorBlanco;
    private Jugador jugadorNegro;
    private Tablero tablero;
    private Configuracion config;
    private List<Jugada> jugadas;
    private boolean turnoBlanco;
    private int triangulasBlanco;
    private int triangulosNegro;
    private Jugador ganador;
    private List<Tablero> historialTableros;
    
    public Partida(Jugador jugadorBlanco, Jugador jugadorNegro, Configuracion config) {
        this.jugadorBlanco = jugadorBlanco;
        this.jugadorNegro = jugadorNegro;
        this.config = config;
        this.tablero = new Tablero();
        this.jugadas = new ArrayList<>();
        this.turnoBlanco = true; // Blanco comienza
        this.triangulasBlanco = 0;
        this.triangulosNegro = 0;
        this.ganador = null;
        this.historialTableros = new ArrayList<>();
        
        // Guardar el tablero inicial
        if (config.getCantidadTableros() > 1) {
            historialTableros.add(new Tablero(tablero)); // Copia del tablero inicial
        }
    }
    
    public void jugar(Scanner scanner) {
        System.out.println("\n¡Comienza la partida!");
        System.out.println("Jugador Blanco: " + jugadorBlanco.getNombre());
        System.out.println("Jugador Negro: " + jugadorNegro.getNombre());
        
        int bandasColocadas = 0;
        boolean partidaTerminada = false;
        
        // Mostrar tablero inicial
        mostrarTableros();
        
        while (!partidaTerminada && bandasColocadas < config.getCantidadBandas()) {
            String jugadorActual = turnoBlanco ? "Blanco (" + jugadorBlanco.getNombre() + ")" : "Negro (" + jugadorNegro.getNombre() + ")";
            System.out.println("\nTurno del jugador " + jugadorActual);
            System.out.println("Triángulos - Blanco: " + triangulasBlanco + " | Negro: " + triangulosNegro);
            System.out.print("Ingrese jugada (LetraFilaDirecciónCantidad, X para rendirse, H para historial): ");
            
            String entradaJugada = scanner.nextLine().toUpperCase();
            
            if (entradaJugada.equals("X")) {
                System.out.println("El jugador " + jugadorActual + " se rinde.");
                ganador = turnoBlanco ? jugadorNegro : jugadorBlanco;
                partidaTerminada = true;
                continue;
            }
            
            if (entradaJugada.equals("H")) {
                mostrarHistorial();
                continue; // No cambia el turno
            }
            
            try {
                Jugada jugada = parsearJugada(entradaJugada);
                
                // Validar jugada
                if (!esJugadaValida(jugada)) {
                    System.out.println("Jugada inválida. Intente nuevamente.");
                    continue; // No cambia el turno
                }
                
                // Realizar jugada
                int nuevosTriangulos = tablero.colocarBanda(jugada.getInicio(), jugada.getDireccion(), 
                                                          jugada.getLongitud(), turnoBlanco);
                
                // Actualizar contadores de triángulos
                if (turnoBlanco) {
                    triangulasBlanco += nuevosTriangulos;
                } else {
                    triangulosNegro += nuevosTriangulos;
                }
                
                // Guardar jugada en historial
                jugadas.add(jugada);
                bandasColocadas++;
                
                // Guardar copia del tablero para historial si es necesario
                if (config.getCantidadTableros() > 1) {
                    historialTableros.add(new Tablero(tablero));
                    if (historialTableros.size() > config.getCantidadTableros()) {
                        historialTableros.remove(0); // Mantener solo la cantidad configurada
                    }
                }
                
                // Mostrar tablero actualizado
                mostrarTableros();
                
                // Para depuración
                System.out.println("\n--- BANDAS EN EL TABLERO ---");
                for (int i = 0; i < tablero.getBandas().size(); i++) {
                    Banda b = tablero.getBandas().get(i);
                    System.out.println((i + 1) + ". De " + b.getInicio() + " a " + b.getFin() + 
                                      " (" + (b.esBlanco() ? "Blanco" : "Negro") + ")");
                }
                
                // Cambiar turno
                turnoBlanco = !turnoBlanco;
                
            } catch (Exception e) {
                System.out.println("Error en la jugada: " + e.getMessage());
                System.out.println("Formato correcto: LetraFilaDirecciónCantidad (ej: D1C3)");
            }
        }
        
        // Determinar ganador si la partida terminó normalmente
        if (!partidaTerminada) {
            System.out.println("\n¡Partida finalizada! Se han colocado todas las bandas.");
            if (triangulasBlanco > triangulosNegro) {
                ganador = jugadorBlanco;
                System.out.println("¡El jugador Blanco (" + jugadorBlanco.getNombre() + ") ha ganado!");
            } else if (triangulosNegro > triangulasBlanco) {
                ganador = jugadorNegro;
                System.out.println("¡El jugador Negro (" + jugadorNegro.getNombre() + ") ha ganado!");
            } else {
                System.out.println("¡La partida ha terminado en empate!");
            }
        }
        
        // Mostrar animación de victoria si hay ganador
        if (ganador != null) {
            mostrarAnimacionVictoria();
        }
        
        System.out.println("\nResultado final:");
        System.out.println("Triángulos - Blanco: " + triangulasBlanco + " | Negro: " + triangulosNegro);
    }
    
    private Jugada parsearJugada(String entrada) {
        if (entrada.length() < 3) {
            throw new IllegalArgumentException("Entrada demasiado corta");
        }
        
        // Extraer letra (columna)
        char columna = entrada.charAt(0);
        if (columna < 'A' || columna > 'M') {
            throw new IllegalArgumentException("Columna inválida (debe ser A-M)");
        }
        
        // Extraer fila
        int fila;
        try {
            fila = Character.getNumericValue(entrada.charAt(1));
            if (fila < 1 || fila > 7) {
                throw new IllegalArgumentException("Fila inválida (debe ser 1-7)");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Fila inválida");
        }
        
        // Extraer dirección
        char direccion = entrada.charAt(2);
        if (!esdireccionValida(direccion)) {
            throw new IllegalArgumentException("Dirección inválida (debe ser Q, E, D, C, Z, A)");
        }
        
        // Extraer longitud (opcional)
        int longitud = 4; // Por defecto
        if (entrada.length() > 3) {
            try {
                longitud = Integer.parseInt(entrada.substring(3));
                if (longitud < 1 || longitud > 4) {
                    throw new IllegalArgumentException("Longitud inválida (debe ser 1-4)");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Longitud inválida");
            }
        }
        
        // Si no se permite largo variado, forzar a 4
        if (!config.isLargoVariado()) {
            longitud = 4;
        }
        
        Punto inicio = new Punto(columna, fila);
        return new Jugada(inicio, direccion, longitud, turnoBlanco);
    }
    
    private boolean esdireccionValida(char direccion) {
        return direccion == 'Q' || direccion == 'E' || direccion == 'D' || 
               direccion == 'C' || direccion == 'Z' || direccion == 'A';
    }
    
    private boolean esJugadaValida(Jugada jugada) {
        Punto inicio = jugada.getInicio();
        
        // Verificar si el punto inicial está dentro del tablero hexagonal
        if (!esPuntoValido(inicio)) {
            System.out.println("DEBUG: Punto inicial inválido: " + inicio);
            return false;
        }
        
        // Calcular el punto final
        Punto fin = calcularPuntoFinal(inicio, jugada.getDireccion(), jugada.getLongitud());
        if (fin == null) {
            System.out.println("DEBUG: Punto final es null");
            return false;
        }
        
        // Verificar si el punto final está dentro del tablero hexagonal
        if (!esPuntoValido(fin)) {
            System.out.println("DEBUG: Punto final inválido: " + fin);
            return false;
        }
        
        // Verificar si requiere contacto (a partir del 2do movimiento)
        if (config.isRequiereContacto() && jugadas.size() > 0) {
            boolean tieneContacto = tablero.tieneContacto(inicio) || tablero.tieneContacto(fin);
            if (!tieneContacto) {
                System.out.println("DEBUG: No tiene contacto con bandas existentes");
            }
            return tieneContacto;
        }
        
        return true;
    }
    
    // Método para verificar si un punto está dentro del tablero hexagonal
    private boolean esPuntoValido(Punto punto) {
        int fila = punto.getFila();
        char columna = punto.getColumna();
        
        // Verificar límites básicos
        if (fila < 1 || fila > 7 || columna < 'A' || columna > 'M') {
            return false;
        }
        
        // Verificar si está dentro del patrón hexagonal
        int desplazamiento = Math.abs(4 - fila);
        char colMin = (char)('A' + desplazamiento);
        char colMax = (char)('M' - desplazamiento);
        
        return columna >= colMin && columna <= colMax;
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
    
    private void mostrarTableros() {
        if (config.getCantidadTableros() <= 1) {
            // Mostrar solo el tablero actual
            tablero.mostrar();
        } else {
            // Mostrar varios tableros
            for (int i = 0; i < historialTableros.size(); i++) {
                if (i > 0) {
                    System.out.print("   ");
                }
                historialTableros.get(i).mostrar(true); // Mostrar en modo compacto
            }
        }
    }
    
    private void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL DE JUGADAS ---");
        if (jugadas.isEmpty()) {
            System.out.println("No se han realizado jugadas aún.");
            return;
        }
        
        for (int i = 0; i < jugadas.size(); i++) {
            Jugada j = jugadas.get(i);
            String jugador = j.esBlanco() ? "Blanco" : "Negro";
            System.out.println((i + 1) + ". " + jugador + ": " + j);
        }
    }
    
    private void mostrarAnimacionVictoria() {
        System.out.println("\n¡VICTORIA PARA " + (ganador == jugadorBlanco ? "BLANCO" : "NEGRO") + "!");
        
        // Colores ANSI
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String BLUE = "\u001B[34m";
        final String PURPLE = "\u001B[35m";
        final String CYAN = "\u001B[36m";
        
        String[] colores = {RED, GREEN, YELLOW, BLUE, PURPLE, CYAN};
        
        try {
            for (int i = 0; i < 20; i++) {
                System.out.print("\033[H\033[2J");  // Limpiar pantalla
                System.out.flush();
                
                // Dibujar fuegos artificiales en posiciones aleatorias
                for (int j = 0; j < 10; j++) {
                    int x = (int) (Math.random() * 80);
                    int y = (int) (Math.random() * 20);
                    String color = colores[(int) (Math.random() * colores.length)];
                    
                    System.out.print("\033[" + y + ";" + x + "H");
                    System.out.print(color + "*" + RESET);
                }
                
                TimeUnit.MILLISECONDS.sleep(200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.print(RESET);
        }
    }
    
    public Jugador getGanador() {
        return ganador;
    }
}
