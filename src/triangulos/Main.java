package triangulos;
/**
 * Clase principal que inicia el programa
 * @author Tu Nombre
 * @author Tu Número
 */
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Configurar la salida para mostrar caracteres UTF-8
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        
        // Iniciar el sistema
        Sistema sistema = new Sistema();
        sistema.iniciar();
    }
}
