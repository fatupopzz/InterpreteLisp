package lisp;

import java.util.Scanner;
import lisp.interpreter.LispParser;

/**
 * Clase principal simplificada que permite al usuario ingresar una expresión LISP
 * y ver su estructura parseada en formato de árbol.
 * 
 * @author Main simplificado para LispParser
 */
public class Main {
    
    /**
     * Método principal que ejecuta el parser.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Crear una instancia del parser
        LispParser parser = new LispParser();
        
        // Crear scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);
        
        // Mostrar instrucciones
        System.out.println("Parser de LISP - Ingrese una expresión LISP");
        System.out.println("Ejemplo: (+ 2 (* V 8)) -> [+ 2 [* V 8]]");
        System.out.println("Escriba 'salir' para terminar");
        System.out.println();
        
        while (true) {
            // Solicitar entrada al usuario
            System.out.print("Expresión LISP > ");
            String expresion = scanner.nextLine().trim();
            
            // Verificar si el usuario quiere salir
            if (expresion.equalsIgnoreCase("salir")) {
                System.out.println("¡Hasta luego!");
                break;
            }
            
            // Ignorar líneas vacías
            if (expresion.isEmpty()) {
                continue;
            }
            
            try {
                // Parsear la expresión
                Object resultado = parser.parse(expresion);  // Usar parse en lugar de LispParser
                
                // Mostrar el resultado
                System.out.println("Estructura parseada: " + parser.printResult(resultado));
                System.out.println();
            } catch (Exception e) {
                // Mostrar error si ocurre alguno
                System.out.println("Error: " + e.getMessage());
                System.out.println();
            }
        }
        
        // Cerrar el scanner
        scanner.close();
    }
}