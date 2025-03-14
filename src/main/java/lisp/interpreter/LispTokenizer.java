package lisp.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizador simplificado para expresiones LISP.
 * Convierte una cadena de texto en una lista de tokens.
 * 
 * @author Fatima Navarro 24044
 */
public class LispTokenizer {
    
    //-------------------------------------------------------------------
    /**
     * Tokeniza una expresión LISP.
     * 
     * @param input La expresión LISP a tokenizar
     * @return Lista de tokens
     */
    public List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        
        // Preparar el input reemplazando paréntesis y otros caracteres especiales
        // con espacios para facilitar la separación
        input = input.replace("(", " ( ")
                   .replace(")", " ) ")
                   .replace("'", " ' ");
        
        // Dividir por espacios
        String[] rawTokens = input.trim().split("\\s+");
        
        // Añadir tokens no vacíos a la lista
        for (String token : rawTokens) {
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        
        return tokens;
    }
}