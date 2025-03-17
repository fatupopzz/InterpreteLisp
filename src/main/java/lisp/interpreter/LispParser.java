package lisp.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser para expresiones LISP.
<<<<<<< HEAD
 * Convierte una cadena de texto en una estructura de árbol basada en listas.
 * 
 * @author Versión revisada del parser
 */
public class LispParser {
    
    private LispTokenizer tokenizer;
    private List<String> tokens;
    private int position;
    
    /**
     * Constructor del parser.
     */
    public LispParser() {
        this.tokenizer = new LispTokenizer();
    }
    
    /**
     * Analiza una expresión LISP y devuelve una estructura de árbol.
     * 
     * @param input La expresión LISP a analizar
     * @return Estructura de árbol que representa la expresión
     */
    public Object parse(String input) {
        tokens = tokenizer.tokenize(input);
        position = 0;
        return parseExpression();
    }
    
    /**
     * Analiza una expresión recursivamente.
     * 
     * @return Objeto que representa la expresión (número, símbolo o lista)
     */
    private Object parseExpression() {
        if (position >= tokens.size()) {
            throw new RuntimeException("Fin inesperado de entrada");
        }
        
        String token = tokens.get(position++);
        
        // Si es un paréntesis de apertura, es una lista
        if (token.equals("(")) {
            return parseList();
        }
        
        // Si es una comilla simple, es una forma quote abreviada
        if (token.equals("'")) {
            // Crear una lista que contiene 'quote' y la expresión siguiente
            List<Object> quoteList = new ArrayList<>();
            quoteList.add("QUOTE");
            quoteList.add(parseExpression());
            return quoteList;
        }
        
        // Si es un número, convertir a Integer o Double
        if (isNumeric(token)) {
            return parseNumber(token);
        }
        
        // De lo contrario, es un símbolo
        return token;
    }
    
    /**
     * Analiza una lista de expresiones.
     * 
     * @return Lista de objetos que representan la lista
     */
    private List<Object> parseList() {
        List<Object> elements = new ArrayList<>();
        
        // Mientras no encontremos el paréntesis de cierre
        while (position < tokens.size() && !tokens.get(position).equals(")")) {
            elements.add(parseExpression());
        }
        
        // Verificar que encontramos el paréntesis de cierre
        if (position >= tokens.size()) {
            throw new RuntimeException("Se esperaba un paréntesis de cierre");
        }
        
        // Consumir el paréntesis de cierre
        position++;
        
        return elements;
    }
    
    /**
     * Convierte un token a un valor numérico.
     * 
     * @param token El token a convertir
     * @return Integer o Double según corresponda
     */
    private Number parseNumber(String token) {
        try {
            // Intentar convertir a entero primero
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            try {
                // Si no funciona, intentar convertir a double
                return Double.parseDouble(token);
            } catch (NumberFormatException e2) {
                // Si no es un número, lanzar una excepción
                throw new RuntimeException("No se puede convertir '" + token + "' a un número");
            }
        }
    }
    
    /**
     * Verifica si un token representa un número.
     * 
     * @param token El token a verificar
     * @return true si el token representa un número
     */
    private boolean isNumeric(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Muestra una representación de la estructura de árbol en formato Python-like.
     * 
     * @param expr La estructura de árbol
     * @return String con la representación
     */
    public String printResult(Object expr) {
        if (expr instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) expr;
            StringBuilder sb = new StringBuilder("[");
            
            for (int i = 0; i < list.size(); i++) {
                sb.append(printResult(list.get(i)));
                if (i < list.size() - 1) {
                    sb.append(" ");
                }
            }
            
            sb.append("]");
            return sb.toString();
        } else {
            return expr.toString();
=======
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
>>>>>>> b0bd3541d25b80a76cd43d3595d6ffca702e8056
        }
    }
}