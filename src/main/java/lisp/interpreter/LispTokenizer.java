package lisp.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizador mejorado para expresiones LISP.
 * Convierte una cadena de texto en una lista de tokens con información de posición.
 * 
 * @author Equipo LISP
 */
public class LispTokenizer {
    
    /**
     * Clase para representar un token con información de posición.
     */
    public static class Token {
        private final String value;
        private final int line;
        private final int column;
        
        /**
         * Constructor.
         * 
         * @param value Valor del token
         * @param line Línea donde comienza el token
         * @param column Columna donde comienza el token
         */
        public Token(String value, int line, int column) {
            this.value = value;
            this.line = line;
            this.column = column;
        }
        
        /**
         * Obtiene el valor del token.
         * 
         * @return Valor del token
         */
        public String getValue() {
            return value;
        }
        
        /**
         * Obtiene la línea donde comienza el token.
         * 
         * @return Línea del token
         */
        public int getLine() {
            return line;
        }
        
        /**
         * Obtiene la columna donde comienza el token.
         * 
         * @return Columna del token
         */
        public int getColumn() {
            return column;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    /**
     * Tokeniza una expresión LISP.
     * 
     * @param input La expresión LISP a tokenizar
     * @return Lista de tokens
     */
    public List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        
        // Obtener tokens con posición y extraer solo los valores
        List<Token> tokensWithPosition = tokenizeWithPosition(input);
        for (Token token : tokensWithPosition) {
            tokens.add(token.getValue());
        }
        
        return tokens;
    }
    
    /**
     * Tokeniza una expresión LISP con información de posición.
     * 
     * @param input La expresión LISP a tokenizar
     * @return Lista de tokens con información de posición
     */
    public List<Token> tokenizeWithPosition(String input) {
        List<Token> tokens = new ArrayList<>();
        int line = 1;
        int column = 1;
        
        StringBuilder currentToken = new StringBuilder();
        int tokenStartColumn = 1;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            // Actualizar línea y columna
            if (c == '\n') {
                // Si hay un token en construcción, completarlo antes del salto de línea
                if (currentToken.length() > 0) {
                    tokens.add(new Token(currentToken.toString(), line, tokenStartColumn));
                    currentToken.setLength(0);
                }
                
                line++;
                column = 1;
                continue;
            }
            
            // Manejar paréntesis y apóstrofe como tokens individuales
            if (c == '(' || c == ')' || c == '\'') {
                // Si hay un token en construcción, completarlo antes
                if (currentToken.length() > 0) {
                    tokens.add(new Token(currentToken.toString(), line, tokenStartColumn));
                    currentToken.setLength(0);
                }
                
                // Agregar el token especial
                tokens.add(new Token(String.valueOf(c), line, column));
                column++;
                continue;
            }
            
            // Manejar espacios como separadores de tokens
            if (Character.isWhitespace(c)) {
                // Si hay un token en construcción, completarlo
                if (currentToken.length() > 0) {
                    tokens.add(new Token(currentToken.toString(), line, tokenStartColumn));
                    currentToken.setLength(0);
                }
                
                column++;
                continue;
            }
            
            // Si comenzamos un nuevo token, registrar su posición inicial
            if (currentToken.length() == 0) {
                tokenStartColumn = column;
            }
            
            // Agregar el carácter al token actual
            currentToken.append(c);
            column++;
        }
        
        // Si queda un token en construcción al final, agregarlo
        if (currentToken.length() > 0) {
            tokens.add(new Token(currentToken.toString(), line, tokenStartColumn));
        }
        
        return tokens;
    }
}