package lisp.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser para expresiones LISP.
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
        }
    }
}