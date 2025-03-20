package lisp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import lisp.interpreter.LispException;
import lisp.interpreter.LispInterpreter;

/**
 * Clase principal que implementa un REPL (Read-Eval-Print-Loop) para el intérprete LISP.
 * Permite ejecutar expresiones LISP interactivamente o desde un archivo.
 * 
 */
public class Main {
    
    /**
     * Método principal que ejecuta el intérprete LISP.
     * 
     * @param args Argumentos de línea de comandos (opcional: ruta a un archivo LISP)
     */
    public static void main(String[] args) {
        // Crear una instancia del intérprete
        LispInterpreter interpreter = new LispInterpreter();
        
        // Si se proporciona un archivo como argumento, ejecutarlo
        if (args.length > 0) {
            try {
                executeFile(args[0], interpreter);
                return;
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
                System.exit(1);
            }
        }
        
        // Si no hay argumentos o después de ejecutar el archivo, iniciar el REPL
        startREPL(interpreter);
    }
    
    /**
     * Ejecuta un archivo LISP línea por línea.
     * 
     * @param filename Ruta al archivo LISP
     * @param interpreter Intérprete LISP
     * @throws IOException Si ocurre un error al leer el archivo
     */
    private static void executeFile(String filename, LispInterpreter interpreter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder buffer = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Ignorar comentarios
                if (line.trim().startsWith(";")) {
                    continue;
                }
                
                buffer.append(line).append(" ");
                
                // Contar paréntesis abiertos y cerrados
                int openParens = countOccurrences(buffer.toString(), '(');
                int closedParens = countOccurrences(buffer.toString(), ')');
                
                // Cuando tenemos una expresión completa, evaluarla
                if (openParens > 0 && openParens == closedParens) {
                    try {
                        Object result = interpreter.eval(buffer.toString());
                        
                        if (result != null) {
                            System.out.println(result);
                        }
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    
                    buffer.setLength(0);  // Limpiar el buffer
                }
            }
            
            // Evaluar cualquier expresión restante
            if (buffer.length() > 0) {
                try {
                    Object result = interpreter.eval(buffer.toString());
                    
                    if (result != null) {
                        System.out.println(result);
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Inicia el REPL (Read-Eval-Print-Loop) para interacción con el usuario.
     * 
     * @param interpreter Intérprete LISP
     */
    private static void startREPL(LispInterpreter interpreter) {
        Scanner scanner = new Scanner(System.in);
        
        // Mostrar instrucciones
        System.out.println("Intérprete LISP - Ingrese expresiones LISP");
        System.out.println("Ejemplos: (+ 2 3), (defun factorial (n) (cond ((equal n 0) 1) (t (* n (factorial (- n 1))))))");
        System.out.println("Escriba 'salir' para terminar");
        System.out.println();
        
        StringBuilder buffer = new StringBuilder();
        
        while (true) {
            // Solicitar entrada al usuario con un prompt apropiado
            System.out.print(buffer.length() == 0 ? "lisp> " : "...> ");
            String input = scanner.nextLine().trim();
            
            // Verificar si el usuario quiere salir
            if (input.equalsIgnoreCase("salir")) {
                System.out.println("¡Hasta luego!");
                break;
            }
            
            // Ignorar líneas vacías y comentarios
            if (input.isEmpty() || input.startsWith(";")) {
                continue;
            }
            
            // Añadir la entrada al buffer
            buffer.append(input).append(" ");
            
            // Contar paréntesis abiertos y cerrados
            int openParens = countOccurrences(buffer.toString(), '(');
            int closedParens = countOccurrences(buffer.toString(), ')');
            
            // Cuando tenemos una expresión completa, evaluarla
            if (openParens > 0 && openParens == closedParens) {
                try {
                    Object result = interpreter.eval(buffer.toString());
                    
                    if (result != null) {
                        System.out.println("=> " + result);
                    }
                } catch (LispException e) {
                    System.err.println("Error: " + e.getMessage());
                    if (e.getCause() != null) {
                        System.err.println("Causa: " + e.getCause().getMessage());
                    }
                } catch (Exception e) {
                    System.err.println("Error inesperado: " + e.getMessage());
                    e.printStackTrace();
                }
                
                buffer.setLength(0);  // Limpiar el buffer
                System.out.println();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Cuenta las ocurrencias de un carácter en una cadena.
     * 
     * @param str Cadena a analizar
     * @param c Carácter a contar
     * @return Número de ocurrencias
     */
    private static int countOccurrences(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
}