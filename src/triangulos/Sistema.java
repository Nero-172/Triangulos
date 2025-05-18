package triangulos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Sistema {
    private List<Jugador> jugadores;
    private Configuracion config;
    private List<Partida> partidas;
    private Scanner scanner;
    
    public Sistema() {
        jugadores = new ArrayList<>();
        config = new Configuracion(); // Configuración por defecto
        partidas = new ArrayList<>();
        scanner = new Scanner(System.in);
    }
    
    public void iniciar() {
        boolean salir = false;
        
        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine().toLowerCase();
            
            switch (opcion) {
                case "a":
                    registrarJugador();
                    break;
                case "b":
                    configurarPartida();
                    break;
                case "c":
                    comenzarPartida();
                    break;
                case "d":
                    mostrarRanking();
                    break;
                case "e":
                    salir = true;
                    System.out.println("Gracias por jugar. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
    
    private void mostrarMenu() {
        System.out.println("\n=================================================");
        System.out.println("Trabajo desarrollado por: Lorenzo Aldao (307239) - Jhonatan Adalid (320368)");
        System.out.println("=================================================");
        System.out.println("a) Registrar un jugador");
        System.out.println("b) Configurar la partida");
        System.out.println("c) Comienzo de partida");
        System.out.println("d) Mostrar ranking y racha");
        System.out.println("e) Terminar el programa");
        System.out.print("Seleccione una opción: ");
    }
    
    private void registrarJugador() {
        System.out.println("\n--- REGISTRO DE JUGADOR ---");
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        
        // Verificar si el nombre ya existe
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Error: Ya existe un jugador con ese nombre.");
                return;
            }
        }
        
        System.out.print("Ingrese edad: ");
        try {
            int edad = Integer.parseInt(scanner.nextLine());
            if (edad <= 0) {
                System.out.println("Error: La edad debe ser un número positivo.");
                return;
            }
            
            Jugador nuevoJugador = new Jugador(nombre, edad);
            jugadores.add(nuevoJugador);
            System.out.println("Jugador registrado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: La edad debe ser un número entero.");
        }
    }
    
    private void configurarPartida() {
        System.out.println("\n--- CONFIGURACIÓN DE PARTIDA ---");
        System.out.print("¿Desea usar configuración por defecto? (S/N): ");
        String respuesta = scanner.nextLine().toUpperCase();
        
        if (respuesta.equals("S")) {
            config = new Configuracion(); // Resetear a configuración por defecto
            System.out.println("Configuración por defecto establecida.");
            return;
        }
        
        // Configuración especial
        System.out.print("¿Requiere contacto para colocar bandas? (S/N): ");
        boolean requiereContacto = scanner.nextLine().toUpperCase().equals("S");
        
        System.out.print("¿Largo de bandas variado (1-4)? (S/N): ");
        boolean largoVariado = scanner.nextLine().toUpperCase().equals("S");
        
        System.out.print("Cantidad de bandas (por defecto 10): ");
        String cantBandasStr = scanner.nextLine();
        int cantidadBandas = cantBandasStr.isEmpty() ? 10 : Integer.parseInt(cantBandasStr);
        
        System.out.print("Cantidad de tableros en pantalla (1-4): ");
        String cantTablerosStr = scanner.nextLine();
        int cantidadTableros = cantTablerosStr.isEmpty() ? 1 : Integer.parseInt(cantTablerosStr);
        
        if (cantidadTableros < 1 || cantidadTableros > 4) {
            System.out.println("Cantidad de tableros inválida. Se usará 1.");
            cantidadTableros = 1;
        }
        
        config = new Configuracion(requiereContacto, largoVariado, cantidadBandas, cantidadTableros);
        System.out.println("Configuración especial establecida.");
    }
    
    private void comenzarPartida() {
        if (jugadores.size() < 2) {
            System.out.println("Error: Se necesitan al menos 2 jugadores registrados para comenzar una partida.");
            return;
        }
        
        System.out.println("\n--- COMIENZO DE PARTIDA ---");
        System.out.println("Jugadores disponibles:");
        
        // Ordenar jugadores alfabéticamente
        List<Jugador> jugadoresOrdenados = new ArrayList<>(jugadores);
        Collections.sort(jugadoresOrdenados, Comparator.comparing(Jugador::getNombre, String.CASE_INSENSITIVE_ORDER));
        
        for (int i = 0; i < jugadoresOrdenados.size(); i++) {
            System.out.println((i + 1) + ". " + jugadoresOrdenados.get(i).getNombre());
        }
        
        // Seleccionar jugador blanco
        System.out.print("Seleccione jugador Blanco (número): ");
        int indiceBlanco = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (indiceBlanco < 0 || indiceBlanco >= jugadoresOrdenados.size()) {
            System.out.println("Error: Número de jugador inválido.");
            return;
        }
        
        // Seleccionar jugador negro
        System.out.print("Seleccione jugador Negro (número): ");
        int indiceNegro = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (indiceNegro < 0 || indiceNegro >= jugadoresOrdenados.size() || indiceNegro == indiceBlanco) {
            System.out.println("Error: Número de jugador inválido o igual al jugador Blanco.");
            return;
        }
        
        Jugador jugadorBlanco = jugadoresOrdenados.get(indiceBlanco);
        Jugador jugadorNegro = jugadoresOrdenados.get(indiceNegro);
        
        // Crear y comenzar la partida
        Partida partida = new Partida(jugadorBlanco, jugadorNegro, config);
        partidas.add(partida);
        partida.jugar(scanner);
        
        // Actualizar estadísticas de los jugadores
        Jugador ganador = partida.getGanador();
        if (ganador != null) {
            ganador.incrementarPartidasGanadas();
            ganador.incrementarRacha();
            
            // Resetear racha del perdedor
            if (ganador == jugadorBlanco) {
                jugadorNegro.resetRacha();
            } else {
                jugadorBlanco.resetRacha();
            }
        } else {
            // En caso de empate, ambos resetean racha
            jugadorBlanco.resetRacha();
            jugadorNegro.resetRacha();
        }
    }
    
    private void mostrarRanking() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores registrados.");
            return;
        }
        
        System.out.println("\n--- RANKING DE JUGADORES ---");
        
        // Ordenar jugadores por partidas ganadas (descendente)
        List<Jugador> ranking = new ArrayList<>(jugadores);
        Collections.sort(ranking, (j1, j2) -> Integer.compare(j2.getPartidasGanadas(), j1.getPartidasGanadas()));
        
        System.out.println("Pos | Nombre | Partidas Ganadas");
        System.out.println("----|--------|----------------");
        
        for (int i = 0; i < ranking.size(); i++) {
            Jugador j = ranking.get(i);
            System.out.printf("%3d | %-6s | %d\n", (i + 1), j.getNombre(), j.getPartidasGanadas());
        }
        
        // Encontrar jugador(es) con la racha más larga
        int maxRacha = 0;
        List<Jugador> jugadoresMaxRacha = new ArrayList<>();
        
        for (Jugador j : jugadores) {
            int racha = j.getRachaMaxima();
            if (racha > maxRacha) {
                maxRacha = racha;
                jugadoresMaxRacha.clear();
                jugadoresMaxRacha.add(j);
            } else if (racha == maxRacha && racha > 0) {
                jugadoresMaxRacha.add(j);
            }
        }
        
        System.out.println("\n--- RACHA GANADORA MÁS LARGA ---");
        if (maxRacha == 0) {
            System.out.println("Ningún jugador tiene racha ganadora.");
        } else {
            System.out.println("Racha máxima: " + maxRacha + " partidas consecutivas");
            System.out.println("Jugador(es) con la racha máxima:");
            for (Jugador j : jugadoresMaxRacha) {
                System.out.println("- " + j.getNombre());
            }
        }
    }
}
