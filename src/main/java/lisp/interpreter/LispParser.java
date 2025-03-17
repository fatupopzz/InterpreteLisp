package lisp.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser para expresiones LISP.
 * Convierte una lista de tokens en una estructura de datos que representa la expresión.
 * 
 * @author Equipo LISP
 */
public class LispParser {
    
    /**
     * Parsea una lista de tokens en una estructura de datos que representa la expresión LISP.
     * 
     * @param tokens La lista de tokens a parsear
     * @return Una estructura de datos que representa la expresión (lista o valor primitivo)
     * @throws LispException si la expresión no es válida
     */
    public Object parse(List<String> tokens) {
        if (tokens.isEmpty()) {
            return null;
        }
        
        TokenIterator iterator = new TokenIterator(tokens);
        return parseExpression(iterator);
    }
    
    /**
     * Parsea una lista de tokens con posición.
     * 
     * @param tokens La lista de tokens con posición
     * @return Una estructura de datos que representa la expresión
     * @throws LispException si la expresión no es válida
     */
    public Object parsear(List<LispTokenizer.Token> tokens) {
        if (tokens.isEmpty()) {
            return null;
        }
        
        // Extraer los valores de los tokens
        List<String> tokenValues = new ArrayList<>();
        for (LispTokenizer.Token token : tokens) {
            tokenValues.add(token.getValue());
        }
        
        return parse(tokenValues);
    }
    
    /**
     * Clase auxiliar para iterar sobre los tokens.
     */
    private static class TokenIterator {
        private final List<String> tokens;
        private int position;
        
        public TokenIterator(List<String> tokens) {
            this.tokens = tokens;
            this.position = 0;
        }
        
        public boolean hasNext() {
            return position < tokens.size();
        }
        
        public String next() {
            if (!hasNext()) {
                throw new LispException("Error de sintaxis: fin inesperado de entrada");
            }
            return tokens.get(position++);
        }
        
        public String peek() {
            if (!hasNext()) {
                throw new LispException("Error de sintaxis: fin inesperado de entrada");
            }
            return tokens.get(position);
        }
    }
    
    /**
     * Parsea una expresión a partir de la posición actual del iterador.
     * 
     * @param iterator El iterador de tokens
     * @return El resultado del parsing
     * @throws LispException si la expresión no es válida
     */
    private Object parseExpression(TokenIterator iterator) {
        String token = iterator.next();
        
        if ("(".equals(token)) {
            // Parsear una lista
            List<Object> list = new ArrayList<>();
            
            // Manejar lista vacía
            if (iterator.hasNext() && ")".equals(iterator.peek())) {
                iterator.next(); // Consumir el paréntesis de cierre
                return list;
            }
            
            // Parsear elementos de la lista
            while (iterator.hasNext() && !")".equals(iterator.peek())) {
                list.add(parseExpression(iterator));
            }
            
            // Verificar que la lista esté cerrada
            if (!iterator.hasNext()) {
                throw new LispException("Error de sintaxis: falta paréntesis de cierre");
            }
            
            // Consumir el paréntesis de cierre
            iterator.next();
            
            return list;
        } else if ("'".equals(token)) {
            // Manejar quote
            List<Object> quoted = new ArrayList<>();
            quoted.add("quote");
            quoted.add(parseExpression(iterator));
            return quoted;
        } else if (")".equals(token)) {
            throw new LispException("Error de sintaxis: paréntesis de cierre inesperado");
        } else {
            // Parsear un átomo
            return parseAtom(token);
        }
    }
    
    /**
     * Parsea un átomo (número o símbolo).
     * 
     * @param token El token a parsear
     * @return El átomo parseado (número o símbolo)
     */
    private Object parseAtom(String token) {
        // Intentar parsear como número
        try {
            // Si es un número con punto decimal, interpretarlo como double
            if (token.contains(".")) {
                return Double.parseDouble(token);
            }
            
            // Si es un número sin punto decimal, interpretarlo como entero
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            // No es un número, retornarlo como símbolo
            return token;
        }
    }
}