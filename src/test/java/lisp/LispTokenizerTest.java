package lisp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import lisp.interpreter.LispTokenizer;

/**
 * Pruebas unitarias para el tokenizador de LISP.
 * 
 * @author Fatima Navarro 24044
 */
public class LispTokenizerTest {
    
    private final LispTokenizer tokenizer = new LispTokenizer();
    
    @Test
    public void testEmptyInput() {
        List<String> tokens = tokenizer.tokenize("");
        assertTrue(tokens.isEmpty(), "La entrada vacía debe generar una lista de tokens vacía");
    }
    
    @Test
    public void testSimpleExpression() {
        List<String> tokens = tokenizer.tokenize("(+ 2 3)");
        
        assertEquals(5, tokens.size(), "Debe haber 5 tokens");
        assertEquals("(", tokens.get(0), "Primer token debe ser '('");
        assertEquals("+", tokens.get(1), "Segundo token debe ser '+'");
        assertEquals("2", tokens.get(2), "Tercer token debe ser '2'");
        assertEquals("3", tokens.get(3), "Cuarto token debe ser '3'");
        assertEquals(")", tokens.get(4), "Quinto token debe ser ')'");
    }
    
    @Test
    public void testNestedExpression() {
        List<String> tokens = tokenizer.tokenize("(+ 5 (* 2 3))");
        
        assertEquals(9, tokens.size(), "Debe haber 9 tokens");
        assertEquals("(", tokens.get(0));
        assertEquals("+", tokens.get(1));
        assertEquals("5", tokens.get(2));
        assertEquals("(", tokens.get(3));
        assertEquals("*", tokens.get(4));
        assertEquals("2", tokens.get(5));
        assertEquals("3", tokens.get(6));
        assertEquals(")", tokens.get(7));
        assertEquals(")", tokens.get(8));
    }
    
    @Test
    public void testQuoteExpression() {
        List<String> tokens = tokenizer.tokenize("(quote (a b c))");
        
        assertEquals(8, tokens.size(), "Debe haber 8 tokens");
        assertEquals("(", tokens.get(0));
        assertEquals("quote", tokens.get(1));
        assertEquals("(", tokens.get(2));
        assertEquals("a", tokens.get(3));
        assertEquals("b", tokens.get(4));
        assertEquals("c", tokens.get(5));
        assertEquals(")", tokens.get(6));
        assertEquals(")", tokens.get(7));
    }
    
    @Test
    public void testSingleQuote() {
        List<String> tokens = tokenizer.tokenize("'(a b c)");
        
        assertEquals(6, tokens.size(), "Debe haber 6 tokens");
        assertEquals("'", tokens.get(0), "Primer token debe ser un apóstrofe");
        assertEquals("(", tokens.get(1));
        assertEquals("a", tokens.get(2));
        assertEquals("b", tokens.get(3));
        assertEquals("c", tokens.get(4));
        assertEquals(")", tokens.get(5));
    }
    
    @Test
    public void testDefunExpression() {
        List<String> tokens = tokenizer.tokenize("(defun factorial (n) (cond ((equal n 0) 1) (t (* n (factorial (- n 1))))))");
        
        assertTrue(tokens.size() > 10, "Debe haber múltiples tokens");
        assertEquals("(", tokens.get(0));
        assertEquals("defun", tokens.get(1));
        assertEquals("factorial", tokens.get(2));
        assertEquals("(", tokens.get(3));
        assertEquals("n", tokens.get(4));
        assertEquals(")", tokens.get(5));
    }
    
    @Test
    public void testExtraSpaces() {
        List<String> tokens1 = tokenizer.tokenize("(+   2   3)");
        List<String> tokens2 = tokenizer.tokenize("(+ 2 3)");
        
        assertEquals(tokens2, tokens1, "Los espacios adicionales no deben afectar el resultado");
    }
}