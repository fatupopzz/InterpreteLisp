package lisp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lisp.environment.LispEnvironment;
import lisp.interpreter.LispEvaluator;
import lisp.interpreter.LispException;

/**
 * Pruebas unitarias para el evaluador LISP.
 */
public class LispEvaluatorTest {
    
    private LispEvaluator evaluator;
    private LispEnvironment env;
    
    @BeforeEach
    public void setUp() {
        evaluator = new LispEvaluator();
        env = new LispEnvironment();
    }
    
    // ---- Tests para valores primitivos ----
    
    @Test
    @DisplayName("Los números se evalúan a sí mismos")
    public void testEvaluateNumber() {
        assertEquals(42, evaluator.evaluate(42, env));
        assertEquals(3.14, evaluator.evaluate(3.14, env));
    }
    
    @Test
    @DisplayName("Los símbolos sin definir se evalúan a sí mismos")
    public void testEvaluateSymbol() {
        assertEquals("x", evaluator.evaluate("x", env));
    }
    
    @Test
    @DisplayName("Las listas vacías se evalúan a sí mismas")
    public void testEvaluateEmptyList() {
        List<Object> emptyList = new ArrayList<>();
        assertEquals(emptyList, evaluator.evaluate(emptyList, env));
    }
    
    // ---- Tests para operaciones aritméticas ----
    
    @Test
    @DisplayName("Prueba de operación de suma con números enteros")
    public void testEvaluateAddIntegers() {
        List<Object> expression = Arrays.asList("+", 2, 3);
        Object result = evaluator.evaluate(expression, env);
        
        assertTrue(result instanceof Integer);
        assertEquals(5, result);
    }
    
    @Test
    @DisplayName("Prueba de operación de suma con punto flotante")
    public void testEvaluateAddFloats() {
        List<Object> expression = Arrays.asList("+", 2.5, 3.5);
        Object result = evaluator.evaluate(expression, env);
        
        // Usar doubles directamente
        assertEquals(6.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    @DisplayName("Prueba de operación de suma sin argumentos")
    public void testEvaluateAddNoArgs() {
        List<Object> expression = Arrays.asList("+");
        Object result = evaluator.evaluate(expression, env);
        
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }
    
    @Test
    @DisplayName("Prueba de operación de resta con dos argumentos")
    public void testEvaluateSubtract() {
        List<Object> expression = Arrays.asList("-", 5, 3);
        Object result = evaluator.evaluate(expression, env);
        
        assertTrue(result instanceof Integer);
        assertEquals(2, result);
    }
    
    @Test
    @DisplayName("Prueba de operación de resta con un argumento (negación)")
    public void testEvaluateNegation() {
        List<Object> expression = Arrays.asList("-", 5);
        Object result = evaluator.evaluate(expression, env);
        
        // Verificar que el resultado sea un número negativo (entero o flotante)
        assertTrue(result instanceof Number);
        assertEquals(-5, ((Number)result).intValue());
    }
    
    @Test
    @DisplayName("Prueba de operación de multiplicación con varios argumentos")
    public void testEvaluateMultiply() {
        List<Object> expression = Arrays.asList("*", 2, 3, 4);
        Object result = evaluator.evaluate(expression, env);
        
        assertTrue(result instanceof Integer);
        assertEquals(24, result);
    }
    
    @Test
    @DisplayName("Prueba de operación de división")
    public void testEvaluateDivide() {
        List<Object> expression = Arrays.asList("/", 10, 2);
        Object result = evaluator.evaluate(expression, env);
        
        assertTrue(result instanceof Integer);
        assertEquals(5, result);
    }
    
    @Test
    @DisplayName("Prueba de división que resulta en un número decimal")
    public void testEvaluateDivideDecimal() {
        List<Object> expression = Arrays.asList("/", 10, 3);
        Object result = evaluator.evaluate(expression, env);
        
        assertTrue(result instanceof Double);
        assertEquals(10.0/3.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    @DisplayName("Prueba de división por cero debe lanzar excepción")
    public void testEvaluateDivideByZero() {
        List<Object> expression = Arrays.asList("/", 10, 0);
        assertThrows(LispException.class, () -> evaluator.evaluate(expression, env));
    }
    
    // ---- Tests para predicados ----
    
    @Test
    @DisplayName("Prueba de predicado equal con valores iguales")
    public void testEvaluateEqualTrue() {
        List<Object> expression = Arrays.asList("equal", 5, 5);
        assertEquals("t", evaluator.evaluate(expression, env));
    }
    
    @Test
    @DisplayName("Prueba de predicado equal con valores diferentes")
    public void testEvaluateEqualFalse() {
        List<Object> expression = Arrays.asList("equal", 5, 6);
        assertEquals("nil", evaluator.evaluate(expression, env));
    }
    
    @Test
    @DisplayName("Prueba de predicado menor que")
    public void testEvaluateLessThan() {
        List<Object> expression = Arrays.asList("<", 3, 5);
        assertEquals("t", evaluator.evaluate(expression, env));
        
        expression = Arrays.asList("<", 5, 3);
        assertEquals("nil", evaluator.evaluate(expression, env));
    }
    
    @Test
    @DisplayName("Prueba de predicado mayor que")
    public void testEvaluateGreaterThan() {
        List<Object> expression = Arrays.asList(">", 5, 3);
        assertEquals("t", evaluator.evaluate(expression, env));
        
        expression = Arrays.asList(">", 3, 5);
        assertEquals("nil", evaluator.evaluate(expression, env));
    }
    
    @Test
    @DisplayName("Prueba de predicado atom con un átomo")
    public void testEvaluateAtomWithAtom() {
        List<Object> expression = Arrays.asList("atom", 42);
        assertEquals("t", evaluator.evaluate(expression, env));
    }
    
    @Test
    @DisplayName("Prueba de predicado atom con una lista")
    public void testEvaluateAtomWithList() {
        // Opción 1: Usar quote para evitar evaluar la lista como una función
        List<Object> innerList = new ArrayList<>();
        innerList.add(1);
        innerList.add(2);
        innerList.add(3);
        
        // Usar quote para evitar la evaluación de la lista como función
        List<Object> quotedList = new ArrayList<>();
        quotedList.add("quote");
        quotedList.add(innerList);
        
        List<Object> expression = new ArrayList<>();
        expression.add("atom");
        expression.add(quotedList);
        
        assertEquals("nil", evaluator.evaluate(expression, env));
    }
    
    @Test
    @DisplayName("Prueba de función list")
    public void testEvaluateList() {
        List<Object> expression = Arrays.asList("list", 1, 2, 3);
        Object result = evaluator.evaluate(expression, env);
        
        assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        assertEquals(3, resultList.size());
        assertEquals(1, resultList.get(0));
        assertEquals(2, resultList.get(1));
        assertEquals(3, resultList.get(2));
    }
    
    // ---- Tests para formas especiales ----
    
    @Test
    @DisplayName("Prueba de forma especial quote")
    public void testEvaluateQuote() {
        List<Object> innerList = Arrays.asList("+", 2, 3);
        List<Object> expression = Arrays.asList("quote", innerList);
        
        Object result = evaluator.evaluate(expression, env);
        assertEquals(innerList, result);
    }
    
    @Test
    @DisplayName("Prueba de forma especial setq")
    public void testEvaluateSetq() {
        List<Object> expression = Arrays.asList("setq", "x", 42);
        Object result = evaluator.evaluate(expression, env);
        
        assertEquals(42, result);
        assertEquals(42, evaluator.evaluate("x", env));
    }
    
    @Test
    @DisplayName("Prueba de forma especial cond con condición verdadera")
    public void testEvaluateCondTrue() {
        List<Object> condition1 = Arrays.asList("equal", 1, 2);
        String result1 = "wrong";
        List<Object> clause1 = Arrays.asList(condition1, result1);
        
        List<Object> condition2 = Arrays.asList("equal", 2, 2);
        String result2 = "right";
        List<Object> clause2 = Arrays.asList(condition2, result2);
        
        List<Object> expression = Arrays.asList("cond", clause1, clause2);
        
        Object result = evaluator.evaluate(expression, env);
        assertEquals("right", result);
    }
    
    @Test
    @DisplayName("Prueba de forma especial cond con caso por defecto (t)")
    public void testEvaluateCondDefault() {
        List<Object> condition1 = Arrays.asList("equal", 1, 2);
        Object result1 = "wrong";
        List<Object> clause1 = Arrays.asList(condition1, result1);
        
        List<Object> clause2 = Arrays.asList("t", "default");
        
        List<Object> expression = Arrays.asList("cond", clause1, clause2);
        
        assertEquals("default", evaluator.evaluate(expression, env));
    }
    
    // ---- Tests para funciones definidas por el usuario ----
    
    @Test
    @DisplayName("Prueba de definición y llamada a función")
    public void testDefunAndCall() {
        // Definir la función suma: (defun suma (a b) (+ a b))
        List<Object> params = Arrays.asList("a", "b");
        List<Object> body = Arrays.asList("+", "a", "b");
        List<Object> defun = Arrays.asList("defun", "suma", params, body);
        
        evaluator.evaluate(defun, env);
        
        // Llamar a la función: (suma 2 3)
        List<Object> call = Arrays.asList("suma", 2, 3);
        Object result = evaluator.evaluate(call, env);
        
        assertEquals(5, result);
    }
    
    @Test
    @DisplayName("Prueba de función recursiva (factorial)")
    public void testRecursiveFunction() {
        // Definir función factorial simplificada para evitar stack overflow
        env.defineFunction(
            "factorial",
            Arrays.asList("n"),
            Arrays.asList("cond", 
                Arrays.asList(
                    Arrays.asList("equal", "n", 0), 
                    1
                ),
                Arrays.asList(
                    "t", 
                    Arrays.asList("*", "n", 
                        Arrays.asList("factorial", 
                            Arrays.asList("-", "n", 1)
                        )
                    )
                )
            )
        );
        
        // Calcular factorial de 3 (6)
        List<Object> call = Arrays.asList("factorial", 3);
        Object result = evaluator.evaluate(call, env);
        
        assertEquals(6, result);
    }
    
    // ---- Tests para manejo de errores ----
    
    @Test
    @DisplayName("Llamar a una función no definida debe lanzar excepción")
    public void testUndefinedFunction() {
        List<Object> call = Arrays.asList("undefined", 1, 2);
        assertThrows(LispException.class, () -> evaluator.evaluate(call, env));
    }
    
    @Test
    @DisplayName("Llamar a una función con número incorrecto de argumentos debe lanzar excepción")
    public void testWrongNumberOfArguments() {
        // Definir la función suma: (defun suma (a b) (+ a b))
        List<Object> params = Arrays.asList("a", "b");
        List<Object> body = Arrays.asList("+", "a", "b");
        List<Object> defun = Arrays.asList("defun", "suma", params, body);
        
        evaluator.evaluate(defun, env);
        
        // Llamar con un solo argumento: (suma 2)
        List<Object> call = Arrays.asList("suma", 2);
        assertThrows(LispException.class, () -> evaluator.evaluate(call, env));
    }
}