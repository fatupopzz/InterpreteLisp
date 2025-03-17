package lisp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lisp.interpreter.LispException;
import lisp.interpreter.LispParser;
import lisp.interpreter.LispTokenizer;

/**
 * Pruebas unitarias para el parser LISP.
 */
public class LispParserTest {
    
    private LispParser parser;
    private LispTokenizer tokenizer;
    
    @BeforeEach
    public void setUp() {
        parser = new LispParser();
        tokenizer = new LispTokenizer();
    }
    
    @Test
    @DisplayName("Parseo de átomos numéricos")
    public void testParseNumbers() {
        List<String> tokens = Arrays.asList("42");
        Object result = parser.parse(tokens);
        assertEquals(42, result);
        
        tokens = Arrays.asList("3.14");
        result = parser.parse(tokens);
        assertEquals(3.14, result);
    }
    
    @Test
    @DisplayName("Parseo de símbolos")
    public void testParseSymbols() {
        List<String> tokens = Arrays.asList("x");
        Object result = parser.parse(tokens);
        assertEquals("x", result);
        
        tokens = Arrays.asList("+");
        result = parser.parse(tokens);
        assertEquals("+", result);
    }
    
    @Test
    @DisplayName("Parseo de expresiones simples")
    public void testParseSimpleExpressions() {
        List<String> tokens = tokenizer.tokenize("(+ 2 3)");
        Object result = parser.parse(tokens);
        
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
        List<String> tokens = tokenizer.tokenize("(+ 5 (* 2 3))");
        Object result = parser.parse(tokens);
        
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
    @DisplayName("Parseo de expresiones complejas")
    public void testParseComplexExpressions() {
        List<String> tokens = tokenizer.tokenize("(defun factorial (n) (cond ((equal n 0) 1) (t (* n (factorial (- n 1))))))");
        Object result = parser.parse(tokens);
        
        assertTrue(result instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> defunList = (List<Object>) result;
        
        assertEquals(4, defunList.size());
        assertEquals("defun", defunList.get(0));
        assertEquals("factorial", defunList.get(1));
        
        assertTrue(defunList.get(2) instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> paramsList = (List<Object>) defunList.get(2);
        assertEquals(1, paramsList.size());
        assertEquals("n", paramsList.get(0));
        
        assertTrue(defunList.get(3) instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> bodyList = (List<Object>) defunList.get(3);
        assertEquals("cond", bodyList.get(0));
    }
    
    @Test
    @DisplayName("Parseo de expresiones con quote")
    public void testParseQuoteExpressions() {
        List<String> tokens = tokenizer.tokenize("'(a b c)");
        Object result = parser.parse(tokens);
        
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
        List<String> tokens = tokenizer.tokenize("()");
        Object result = parser.parse(tokens);
        
        assertTrue(result instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> emptyList = (List<Object>) result;
        
        assertTrue(emptyList.isEmpty());
    }
    
    @Test
    @DisplayName("Parseo de entrada vacía")
    public void testParseEmptyInput() {
        List<String> tokens = new ArrayList<>();
        Object result = parser.parse(tokens);
        
        assertNull(result);
    }
    
    @Test
    @DisplayName("Error en parseo: paréntesis faltante")
    public void testParseMissingParenthesis() {
        List<String> tokens = tokenizer.tokenize("(+ 2 3");
        
        assertThrows(LispException.class, () -> parser.parse(tokens));
    }
    
    @Test
    @DisplayName("Error en parseo: paréntesis extra")
    public void testParseExtraParenthesis() {
        List<String> tokens = tokenizer.tokenize(")");
        
        assertThrows(LispException.class, () -> parser.parse(tokens));
    }
}