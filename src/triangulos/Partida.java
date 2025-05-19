package triangulos;

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
        System.out.println("Largo de bandas por defecto: " + config.getLargoBanda());
        System.out.println("Formato de jugada: D1D2 - Une 2 puntos consecutivos");
        
        int bandasColocadas = 0;
        boolean partidaTerminada = false;
        
        // Mostrar tablero inicial
        mostrarTableros();
        
        while (!partidaTerminada && bandasColocadas < config.getCantidadBandas()) {
            String jugadorActual = turnoBlanco ? "Blanco (" + jugadorBlanco.getNombre() + ")" : "Negro (" + jugadorNegro.getNombre() + ")";
            System.out.println("\nTurno del jugador " + jugadorActual);
            System.out.println("Triángulos - Cantidad Blanco: " + triangulasBlanco + " | Cantidad Negro: " + triangulosNegro);
            System.out.print("Ingrese jugada (LetraFilaDirecciónLongitud, X para rendirse, H para historial): ");
        
            String entradaJugada = scanner.nextLine().toUpperCase().trim();
            
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
                // Parsear la jugada
                char columna = entradaJugada.charAt(0);
                int fila = Character.getNumericValue(entradaJugada.charAt(1));
                char direccion = entradaJugada.charAt(2);
                int longitud = entradaJugada.length() > 3 ? 
                               Integer.parseInt(entradaJugada.substring(3)) : 
                               config.getLargoBanda();
                               
                // Validar parámetros básicos
                if (columna < 'A' || columna > 'M') {
                    throw new IllegalArgumentException("Columna inválida (debe ser A-M)");
                }
                
                if (fila < 1 || fila > 7) {
                    throw new IllegalArgumentException("Fila inválida (debe ser 1-7)");
                }
                
                if (!esdireccionValida(direccion)) {
                    throw new IllegalArgumentException("Dirección inválida (debe ser Q, E, D, C, Z, A)");
                }
                
                if (longitud < 1 || longitud > 4) {
                    throw new IllegalArgumentException("Longitud inválida (debe ser 1-4)");
                }
                
                // Crear el punto de inicio
                Punto inicio = new Punto(columna, fila);
                
                // Verificar si el punto inicial es válido
                if (!tablero.esPuntoValido(inicio)) {
                    throw new IllegalArgumentException("Punto inicial no válido en el tablero");
                }
                
                // Generar los puntos de la banda según la dirección y longitud
                List<Punto> puntosBanda = generarPuntosBanda(inicio, direccion, longitud);
                
                // Verificar que tengamos suficientes puntos
                if (puntosBanda.size() < longitud) {
                    throw new IllegalArgumentException("No se pueden generar " + longitud + 
                                                      " puntos en esa dirección desde " + inicio);
                }
                
                // Verificar requisito de contacto (a partir del 2do movimiento)
                if (config.isRequiereContacto() && bandasColocadas > 0) {
                    boolean tieneContacto = false;
                    for (Punto p : puntosBanda) {
                        if (tablero.tieneContacto(p)) {
                            tieneContacto = true;
                            break;
                        }
                    }
                    
                    if (!tieneContacto) {
                        throw new IllegalArgumentException("La banda debe tener contacto con una banda existente");
                    }
                }
                
                // Colocar la banda en el tablero
                int nuevosTriangulos = tablero.colocarBanda(puntosBanda, turnoBlanco);
                
                // Crear y guardar la jugada en el historial
                Jugada jugada = new Jugada(inicio, direccion, longitud, turnoBlanco);
                jugadas.add(jugada);
                
                // Actualizar contadores de triángulos
                if (turnoBlanco) {
                    triangulasBlanco += nuevosTriangulos;
                } else {
                    triangulosNegro += nuevosTriangulos;
                }
                
                // Incrementar contador de bandas
                bandasColocadas++;
                
                // Guardar copia del tablero para historial
                if (config.getCantidadTableros() > 1) {
                    historialTableros.add(new Tablero(tablero));
                    if (historialTableros.size() > config.getCantidadTableros()) {
                        historialTableros.remove(0);
                    }
                }
                
                // Mostrar el tablero actualizado
                mostrarTableros();
                
                // Cambiar el turno
                turnoBlanco = !turnoBlanco;
                
            } catch (Exception e) {
                System.out.println("Error en la jugada: " + e.getMessage());
                System.out.println("Formato correcto: D1C4 - Une 4 puntos consecutivos");
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
    
    private boolean esdireccionValida(char direccion) {
        return direccion == 'Q' || direccion == 'E' || direccion == 'D' || 
               direccion == 'C' || direccion == 'Z' || direccion == 'A';
    }
    
    // Método para generar todos los puntos de una banda
    private List<Punto> generarPuntosBanda(Punto inicio, char direccion, int longitud) {
        List<Punto> puntos = new ArrayList<>();
        puntos.add(inicio);
        
        // Si la longitud es 0, solo devolver el punto inicial
        if (longitud <= 0) {
            return puntos;
        }
        // Para longitud 1 o más, necesitamos generar puntos adicionales
        
        // Determinar incrementos según dirección
        int incFila = 0;
        int incCol = 0;
        
        switch (direccion) {
            case 'Q': // Noroeste
                incFila = -1;
                incCol = -1;
                break;
            case 'E': // Noreste
                incFila = -1;
                incCol = 1;
                break;
            case 'D': // Este (horizontal)
                incFila = 0;
                incCol = 2; // Incremento de 2 para saltar al siguiente punto válido
                break;
            case 'C': // Sureste
                incFila = 1;
                incCol = 1;
                break;
            case 'Z': // Suroeste
                incFila = 1;
                incCol = -1;
                break;
            case 'A': // Oeste (horizontal)
                incFila = 0;
                incCol = -2; // Decremento de 2 para saltar al siguiente punto válido
                break;
        }
        
        // Generar puntos adicionales
        char col = inicio.getColumna();
        int fila = inicio.getFila();
        
        // Debug output
        System.out.println("Generando puntos desde " + inicio + " en dirección " + direccion + " con longitud " + longitud);
        System.out.println("Incrementos: fila=" + incFila + ", columna=" + incCol);
        
        for (int i = 1; i < longitud + 1; i++) {
            col = (char)(col + incCol);
            fila = fila + incFila;
            
            Punto nuevoPunto = new Punto(col, fila);
            System.out.println("Punto generado: " + nuevoPunto);
            
            // Verificar si el punto es válido
            if (tablero.esPuntoValido(nuevoPunto)) {
                puntos.add(nuevoPunto);
            } else {
                System.out.println("Advertencia: Punto fuera del tablero: " + nuevoPunto);
                break;
            }
        }
        
        return puntos;
    }
    
    private void mostrarTableros() {
        if (config.getCantidadTableros() <= 1) {
            // Mostrar solo el tablero actual
            tablero.mostrar();
        } else {
            // Mostrar varios tableros lado a lado
            int cantidadTableros = Math.min(historialTableros.size(), config.getCantidadTableros());
            
            // Crear una representación visual de cada tablero
            char[][][] representacionesTableros = new char[cantidadTableros][][];
            
            // Invertir el orden de los tableros para que el más reciente esté a la derecha
            for (int i = 0; i < cantidadTableros; i++) {
                // Obtener tableros en orden inverso (del más reciente al más antiguo)
                int indice = historialTableros.size() - 1 - i;
                representacionesTableros[cantidadTableros - 1 - i] = historialTableros.get(indice).obtenerRepresentacionVisual();
            }
            
            // Calcular el ancho exacto de cada tablero
            int anchoTablero = representacionesTableros[0][0].length;
            
            // Imprimir encabezados de columnas para cada tablero
            System.out.println();
            
            // Primera línea: letras de columnas
            for (int t = 0; t < cantidadTableros; t++) {
                // Espacio inicial para alinear con el primer punto
                System.out.print(" ");
                
                // Imprimir letras solo donde hay puntos
                for (char c = 'A'; c <= 'M'; c++) {
                    int colPos = (c - 'A') * 2;
                    if (colPos < anchoTablero) {
                        // Verificar si hay un asterisco en esta columna
                        boolean hayPunto = false;
                        for (int fila = 0; fila < representacionesTableros[t].length; fila++) {
                            if (representacionesTableros[t][fila][colPos] == '*') {
                                hayPunto = true;
                                break;
                            }
                        }
                        
                        if (hayPunto) {
                            System.out.print(c + " ");
                        } else {
                            System.out.print("  ");
                        }
                    }
                }
                
                // Espacio entre tableros (exactamente 3 espacios)
                if (t < cantidadTableros - 1) {
                    System.out.print("   ");
                }
            }
            
            System.out.println();
            System.out.println();
            
            // Imprimir filas de todos los tableros
            int filas = representacionesTableros[0].length;
            for (int fila = 0; fila < filas; fila++) {
                for (int t = 0; t < cantidadTableros; t++) {
                    System.out.print(" ");
                    for (int col = 0; col < representacionesTableros[t][fila].length; col++) {
                        System.out.print(representacionesTableros[t][fila][col]);
                    }
                    
                    // Espacio entre tableros (exactamente 3 espacios)
                    if (t < cantidadTableros - 1) {
                        System.out.print("   ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    
    private void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL DE JUGADAS ---");
        if (jugadas.isEmpty()) {
            System.out.println("No se han realizado jugadas aún.");
            return;
        }
        
        String historial = "";
        for (int i = 0; i < jugadas.size(); i++) {
            Jugada j = jugadas.get(i);
            // Formato básico: LetraFilaDirecciónLongitud
            historial += j.getInicio().getColumna();
            historial += j.getInicio().getFila();
            historial += j.getDireccion();
            historial += j.getLongitud();
            
            // Añadir coma si no es la última jugada
            if (i < jugadas.size() - 1) {
                historial += ", ";
            }
        }
        
        System.out.println(historial);
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

        try {
            // Mostrar la animación varias veces
            for (int animacion = 0; animacion < 1; animacion++) {

                // Primer patrón - forma de rombo/estrella con estrellas
                System.out.println("          " + GREEN + "*" + RESET + "     " + 
                                   PURPLE + "*" + RESET + "     " + 
                                   GREEN + "*" + RESET);
                System.out.println("        " + YELLOW + "*" + RESET + "         " + 
                                   CYAN + "*" + RESET);
                System.out.println("      " + GREEN + "*" + RESET + "             " + 
                                   GREEN + "*" + RESET);
                System.out.println("        " + BLUE + "*" + RESET + "         " + 
                                   RED + "*" + RESET);
                System.out.println("          " + PURPLE + "*" + RESET + "     " + 
                                   YELLOW + "*" + RESET + "     " + 
                                   PURPLE + "*" + RESET);
                TimeUnit.MILLISECONDS.sleep(400);
                System.out.println(); // Espacio entre patrones

                // Segundo patrón - forma de rombo/estrella con círculos
                System.out.println("      " + CYAN + "o" + RESET + "     " + 
                                   RED + "o" + RESET + "     " + 
                                   CYAN + "o" + RESET);
                System.out.println("    " + PURPLE + "o" + RESET + "         " + 
                                   GREEN + "o" + RESET);
                System.out.println("  " + RED + "o" + RESET + "             " + 
                                   YELLOW + "o" + RESET);
                System.out.println("    " + YELLOW + "o" + RESET + "         " + 
                                   BLUE + "o" + RESET);
                System.out.println("      " + GREEN + "o" + RESET + "     " + 
                                   PURPLE + "o" + RESET + "     " + 
                                   RED + "o" + RESET);

                System.out.println(); // Espacio entre animaciones

                // Pausa entre animaciones
                TimeUnit.MILLISECONDS.sleep(800);
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
