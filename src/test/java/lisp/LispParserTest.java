package lisp;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lisp.interpreter.LispParser;

/**
 * Pruebas unitarias para el parser LISP.
 */
public class LispParserTest {
    
    private LispParser parser;
    
    @BeforeEach
    public void setUp() {
        parser = new LispParser();
    }
    
    @Test
    @DisplayName("Parseo de entrada vacía")
    public void testParseEmptyInput() {
        assertNull(parser.parse(""));
        assertNull(parser.parse("   "));
        assertNull(parser.parse(new ArrayList<>()));
    }
    
    @Test
    @DisplayName("Parseo de números")
    public void testParseNumbers() {
        assertEquals(42, parser.parse("42"));
        assertEquals(3.14, parser.parse("3.14"));
        assertEquals(-5, parser.parse("-5"));
    }
    
    @Test
    @DisplayName("Parseo de símbolos")
    public void testParseSymbols() {
        assertEquals("x", parser.parse("x"));
        assertEquals("+", parser.parse("+"));
        assertEquals("hello", parser.parse("hello"));
    }
    
    @Test
    @DisplayName("Parseo de expresiones simples")
    public void testParseSimpleExpressions() {
        Object result = parser.parse("(+ 2 3)");
        
        assertTrue(result instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) result;
        
        assertEquals(3, list.size());
        assertEquals("+", list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test
    @DisplayName("Parseo de expresiones anidadas")
    public void testParseNestedExpressions() {
        Object result = parser.parse("(+ 5 (* 2 3))");
        
        assertTrue(result instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> outerList = (List<Object>) result;
        
        assertEquals(3, outerList.size());
        assertEquals("+", outerList.get(0));
        assertEquals(5, outerList.get(1));
        
        assertTrue(outerList.get(2) instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> innerList = (List<Object>) outerList.get(2);
        
        assertEquals(3, innerList.size());
        assertEquals("*", innerList.get(0));
        assertEquals(2, innerList.get(1));
        assertEquals(3, innerList.get(2));
    }
    
    @Test
    @DisplayName("Parseo de expresiones con quote")
    public void testParseQuoteExpressions() {
        Object result = parser.parse("'(a b c)");
        
        assertTrue(result instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> quoteList = (List<Object>) result;
        
        assertEquals(2, quoteList.size());
        assertEquals("quote", quoteList.get(0));
        
        assertTrue(quoteList.get(1) instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> quotedList = (List<Object>) quoteList.get(1);
        
        assertEquals(3, quotedList.size());
        assertEquals("a", quotedList.get(0));
        assertEquals("b", quotedList.get(1));
        assertEquals("c", quotedList.get(2));
    }
    
    @Test
    @DisplayName("Parseo de listas vacías")
    public void testParseEmptyList() {
        Object result = parser.parse("()");
        
        assertTrue(result instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> emptyList = (List<Object>) result;
        
        assertTrue(emptyList.isEmpty());
    }
    
    @Test
    @DisplayName("Error en parseo: paréntesis faltante")
    public void testParseMissingParenthesis() {
        assertThrows(RuntimeException.class, () -> parser.parse("(+ 2 3"));
    }
    
    @Test
    @DisplayName("Error en parseo: paréntesis extra")
    public void testParseExtraParenthesis() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> parser.parse("(+ 2 3))"));
        assertEquals("Error: paréntesis extra detectado", exception.getMessage());
    }
    
    
    @Test
    @DisplayName("Error en parseo: número inválido")
    public void testParseInvalidNumber() {
        // Este test verifica que el parser no intenta parsear símbolos como números
        // ya que "abc123" no es un número válido, debería manejarse como un símbolo
        assertEquals("abc123", parser.parse("abc123"));
    }
    
    @Test
    @DisplayName("Representación en formato de árbol")
    public void testPrintResult() {
        Object result = parser.parse("(+ 1 2)");
        String tree = parser.printResult(result);
        System.out.println("Salida 1: " + tree); // Para ver qué se genera realmente
        assertEquals("[+ 1 2]", tree);

        result = parser.parse("(+ 1 (* 2 3))");
        tree = parser.printResult(result);
        System.out.println("Salida 2: " + tree); // Para depuración
        assertEquals("[+ 1 [* 2 3]]", tree);
    }

    @Test
    @DisplayName("Depuración de parser")
    public void testDebugParser() {
        Object result = parser.parse("(+ 1 (* 2 3))");
        System.out.println("Estructura devuelta por el parser: " + result);
    }
}