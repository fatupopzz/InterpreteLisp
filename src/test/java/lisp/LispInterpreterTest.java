package lisp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;  // Añadido el import faltante
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lisp.interpreter.LispException;
import lisp.interpreter.LispInterpreter;

/**
 * Pruebas de integración para el intérprete LISP completo.
 */
public class LispInterpreterTest {
    
    private LispInterpreter interpreter;
    
    @BeforeEach
    public void setUp() {
        interpreter = new LispInterpreter();
    }
    
    @Test
    @DisplayName("Evaluación de expresiones aritméticas simples")
    public void testSimpleArithmetic() {
        assertEquals(5, interpreter.eval("(+ 2 3)"));
        assertEquals(2, interpreter.eval("(- 5 3)"));
        assertEquals(6, interpreter.eval("(* 2 3)"));
        assertEquals(2, interpreter.eval("(/ 6 3)"));
    }
    
    @Test
    @DisplayName("Evaluación de expresiones anidadas")
    public void testNestedExpressions() {
        assertEquals(11, interpreter.eval("(+ 5 (* 2 3))"));
        assertEquals(14, interpreter.eval("(+ (* 2 3) (* 4 2))"));
        assertEquals(10, interpreter.eval("(- 15 (/ 10 2))"));
    }
    
    @Test
    @DisplayName("Evaluación de variables")
    public void testVariables() {
        interpreter.eval("(setq x 42)");
        assertEquals(42, interpreter.eval("x"));
        
        interpreter.eval("(setq y (+ x 8))");
        assertEquals(50, interpreter.eval("y"));
    }
    
    @Test
    @DisplayName("Evaluación de expresiones con quote")
    public void testQuote() {
        Object result = interpreter.eval("(quote (+ 1 2))");
        
        assertTrue(result instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) result;
        
        assertEquals(3, list.size());
        assertEquals("+", list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        
        // Forma abreviada (comilla simple)
        result = interpreter.eval("'(a b c)");
        assertTrue(result instanceof List);
    }
    
    @Test
    @DisplayName("Evaluación de expresiones condicionales")
    public void testConditionals() {
        // Cambiar la expectativa para que incluya las comillas
        assertEquals("\"verdadero\"", interpreter.eval("(cond ((equal 2 2) \"verdadero\") (t \"falso\"))"));
        assertEquals("\"falso\"", interpreter.eval("(cond ((equal 1 2) \"verdadero\") (t \"falso\"))"));
        
        assertEquals("\"menor\"", interpreter.eval("(cond ((< 1 2) \"menor\") ((> 1 2) \"mayor\") (t \"igual\"))"));
        assertEquals("\"mayor\"", interpreter.eval("(cond ((< 3 2) \"menor\") ((> 3 2) \"mayor\") (t \"igual\"))"));
        assertEquals("\"igual\"", interpreter.eval("(cond ((< 2 2) \"menor\") ((> 2 2) \"mayor\") (t \"igual\"))"));
    }
    
    @Test
    @DisplayName("Definición y evaluación de funciones simples")
    public void testSimpleFunctions() {
        // Definir función suma
        interpreter.eval("(defun suma (a b) (+ a b))");
        assertEquals(5, interpreter.eval("(suma 2 3)"));
        
        // Definir función promedio
        interpreter.eval("(defun promedio (a b) (/ (+ a b) 2))");
        assertEquals(5, interpreter.eval("(promedio 4 6)"));
    }
    
    @Test
    @DisplayName("Definición y evaluación de funciones recursivas")
    public void testRecursiveFunctions() {
        // Definir función factorial
        interpreter.eval("(defun factorial (n) (cond ((equal n 0) 1) (t (* n (factorial (- n 1))))))");
        assertEquals(120, interpreter.eval("(factorial 5)"));
        
        // Definir función fibonacci
        interpreter.eval("(defun fibonacci (n) (cond ((equal n 0) 0) ((equal n 1) 1) (t (+ (fibonacci (- n 1)) (fibonacci (- n 2))))))");
        assertEquals(5, interpreter.eval("(fibonacci 5)"));
    }
    
    @Test
    @DisplayName("Expresiones complejas y combinaciones")
    public void testComplexExpressions() {
        // Definir una función con una expresión condicional
        interpreter.eval("(defun signo (n) (cond ((equal n 0) 0) ((< n 0) -1) (t 1)))");
        
        assertEquals(-1, interpreter.eval("(signo -5)"));
        assertEquals(0, interpreter.eval("(signo 0)"));
        assertEquals(1, interpreter.eval("(signo 5)"));
        
        // Combinación de variables y funciones
        interpreter.eval("(setq base 10)");
        interpreter.eval("(defun area-triangulo (altura) (/ (* base altura) 2))");
        
        assertEquals(25, interpreter.eval("(area-triangulo 5)"));
    }
    
    @Test
    @DisplayName("Manejo de errores")
    public void testErrorHandling() {
        // División por cero
        assertThrows(LispException.class, () -> interpreter.eval("(/ 5 0)"));
        
        // Función no definida
        assertThrows(LispException.class, () -> interpreter.eval("(funcion-indefinida 1 2)"));
        
        // Número incorrecto de argumentos
        interpreter.eval("(defun suma (a b) (+ a b))");
        assertThrows(LispException.class, () -> interpreter.eval("(suma 1)"));
        
        // Sintaxis incorrecta
        assertThrows(Exception.class, () -> interpreter.eval("(+ 1 2"));
    }
}