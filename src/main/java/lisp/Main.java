package lisp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import lisp.interpreter.LispException;
import lisp.interpreter.LispInterpreter;

/**
 * Clase principal que implementa el ciclo REPL (Read-Eval-Print-Loop).
 * 
 * @author Equipo LISP
 */
public class Main {
    
    /**
     * Punto de entrada principal del programa.
     * 
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        LispInterpreter interpreter = new LispInterpreter();
        
        if (args.length > 0) {
            // Ejecutar un archivo LISP
            String filePath = args[0];
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                runProgram(interpreter, content);
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
                System.exit(1);
            }
        } else {
            // Ejecutar el REPL
            runRepl(interpreter);
        }
    }
    
    /**
     * Ejecuta un programa LISP completo.
     * 
     * @param interpreter El intérprete LISP
     * @param program El contenido del programa
     */
    private static void runProgram(LispInterpreter interpreter, String program) {
        // Dividir el programa en expresiones separadas
        String[] expressions = program.split("(?<=\\))\\s*(?=\\()");
        
        for (String expr : expressions) {
            expr = expr.trim();
            if (!expr.isEmpty()) {
                try {
                    Object result = interpreter.eval(expr);
                    if (result != null) {
                        System.out.println(formatResult(result));
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Ejecuta el ciclo REPL (Read-Eval-Print-Loop).
     * 
     * @param interpreter El intérprete LISP
     */
    private static void runRepl(LispInterpreter interpreter) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Intérprete LISP Básico - Versión 1.0");
        System.out.println("Escribe 'exit' o 'quit' para salir");
        
        try {
            while (true) {
                System.out.print("lisp> ");
                String input = reader.readLine();
                
                if (input == null || input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    break;
                }
                
                if (input.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Object result = interpreter.eval(input);
                    System.out.println("=> " + formatResult(result));
                } catch (LispException e) {
                    System.err.println(e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
        
        System.out.println("¡Hasta pronto!");
    }
    
    /**
     * Formatea un resultado para su presentación.
     * 
     * @param result El resultado a formatear
     * @return El resultado formateado como cadena
     */
    private static String formatResult(Object result) {
        if (result == null) {
            return "nil";
        } else if (result instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) result;
            
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(formatResult(list.get(i)));
            }
            sb.append(")");
            return sb.toString();
        } else {
            return result.toString();
        }
    }
}