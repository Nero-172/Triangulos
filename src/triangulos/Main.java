package triangulos;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        // Configurar la salida para mostrar caracteres UTF-8
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        
        // Iniciar el sistema
        Sistema sistema = new Sistema();
        sistema.iniciar();
    }
}

// Hola broski
