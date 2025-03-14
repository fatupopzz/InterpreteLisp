package lisp.interpreter;

import java.util.ArrayList;
import java.util.List;

import lisp.environment.LispEnvironment;
import lisp.environment.LispEnvironment.FunctionDefinition;

/**
 * Evaluador de expresiones LISP.
 * Se encarga de evaluar estructuras de datos que representan expresiones LISP.
 * 
 * @author Fatima Navarro 24044
 */
public class LispEvaluator {
    
    //-------------------------------------------------------------------
    /**
     * Evalúa una expresión LISP en el entorno proporcionado.
     * 
     * @param expr La expresión a evaluar
     * @param env El entorno de ejecución
     * @return El resultado de la evaluación
     */
    public Object evaluate(Object expr, LispEnvironment env) {
        // Si es un número, se evalúa a sí mismo
        if (expr instanceof Number) {
            return expr;
        }
        
        // Si es un símbolo (String), buscar su valor en el entorno
        if (expr instanceof String) {
            String symbol = (String) expr;
            
            // Verificar si es una variable
            if (env.hasVariable(symbol)) {
                return env.getVariable(symbol);
            }
            
            // Si no es una variable, retorna el símbolo mismo
            return symbol;
        }
        
        // Si no es una lista, no se puede evaluar más
        if (!(expr instanceof List)) {
            return expr;
        }
        
        // Obtener la lista
        List<?> list = (List<?>) expr;
        
        // Lista vacía se evalúa a sí misma
        if (list.isEmpty()) {
            return list;
        }
        
        // Obtener el primer elemento (operador o nombre de función)
        Object first = list.get(0);
        String operator = first.toString();
        
        // Formas especiales
        switch (operator) {
            case "quote": // (quote expr)
                return handleQuote(list);
                
            case "setq": // (setq variable valor)
                return handleSetq(list, env);
                
            case "defun": // (defun nombre (params) cuerpo)
                return handleDefun(list, env);
                
            case "cond": // (cond (condición1 resultado1) (condición2 resultado2) ...)
                return handleCond(list, env);
        }
        
        // Operaciones aritméticas y predicados
        switch (operator) {
            case "+":
                return evaluateAdd(list, env);
            case "-":
                return evaluateSubtract(list, env);
            case "*":
                return evaluateMultiply(list, env);
            case "/":
                return evaluateDivide(list, env);
            case "equal":
                return evaluateEqual(list, env);
            case "<":
                return evaluateLessThan(list, env);
            case ">":
                return evaluateGreaterThan(list, env);
            case "atom":
                return evaluateAtom(list, env);
            case "list":
                return evaluateList(list, env);
        }
        
        // Si no es una forma especial ni operación básica, es una aplicación de función
        return applyUserFunction(operator, list, env);
    }
    
    //-------------------------------------------------------------------
    /**
     * Maneja la forma especial 'quote'.
     */
    private Object handleQuote(List<?> list) {
        if (list.size() != 2) {
            throw new RuntimeException("Error: quote requiere exactamente un argumento");
        }
        return list.get(1);
    }
    
    //-------------------------------------------------------------------
    /**
     * Maneja la forma especial 'setq'.
     */
    private Object handleSetq(List<?> list, LispEnvironment env) {
        if (list.size() != 3) {
            throw new RuntimeException("Error: setq requiere exactamente dos argumentos");
        }
        
        String variable = list.get(1).toString();
        Object value = evaluate(list.get(2), env);
        
        return env.setVariable(variable, value);
    }
    
    //-------------------------------------------------------------------
    /**
     * Maneja la forma especial 'defun'.
     */
    private Object handleDefun(List<?> list, LispEnvironment env) {
        if (list.size() != 4) {
            throw new RuntimeException("Error: defun requiere exactamente tres argumentos");
        }
        
        String functionName = list.get(1).toString();
        
        // Parámetros
        if (!(list.get(2) instanceof List)) {
            throw new RuntimeException("Error: los parámetros de defun deben ser una lista");
        }
        
        List<?> paramsList = (List<?>) list.get(2);
        List<String> params = new ArrayList<>();
        
        for (Object param : paramsList) {
            params.add(param.toString());
        }
        
        // Cuerpo de la función
        Object body = list.get(3);
        
        return env.defineFunction(functionName, params, body);
    }
    
    //-------------------------------------------------------------------
    /**
     * Maneja la forma especial 'cond'.
     */
    private Object handleCond(List<?> list, LispEnvironment env) {
        for (int i = 1; i < list.size(); i++) {
            Object clause = list.get(i);
            
            if (!(clause instanceof List)) {
                throw new RuntimeException("Error: cláusula de cond debe ser una lista");
            }
            
            List<?> clauseList = (List<?>) clause;
            if (clauseList.size() != 2) {
                throw new RuntimeException("Error: cláusula de cond debe tener exactamente dos elementos");
            }
            
            Object condition = clauseList.get(0);
            Object result = clauseList.get(1);
            
            // Si la condición es 't', es el caso por defecto
            if (condition.equals("t") || isTrue(evaluate(condition, env))) {
                return evaluate(result, env);
            }
        }
        
        // Si ninguna condición se cumple, retornar nil
        return "nil";
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la operación de suma.
     */
    private Object evaluateAdd(List<?> list, LispEnvironment env) {
        // Evaluar los argumentos
        List<Object> args = evaluateArguments(list, env);
        
        if (args.isEmpty()) {
            return 0; // Suma sin argumentos es 0
        }
        
        double result = 0;
        boolean allIntegers = true;
        
        for (Object arg : args) {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Error: + requiere argumentos numéricos");
            }
            
            Number num = (Number) arg;
            result += num.doubleValue();
            
            // Verificar si es un entero
            if (allIntegers && !(num instanceof Integer)) {
                allIntegers = false;
            }
        }
        
        // Si todos son enteros, retornar un entero
        return allIntegers ? (int) result : result;
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la operación de resta.
     */
    private Object evaluateSubtract(List<?> list, LispEnvironment env) {
        // Evaluar los argumentos
        List<Object> args = evaluateArguments(list, env);
        
        if (args.isEmpty()) {
            throw new RuntimeException("Error: - requiere al menos un argumento");
        }
        
        if (!(args.get(0) instanceof Number)) {
            throw new RuntimeException("Error: - requiere argumentos numéricos");
        }
        
        Number firstArg = (Number) args.get(0);
        double result = firstArg.doubleValue();
        boolean allIntegers = firstArg instanceof Integer;
        
        // Si solo hay un argumento, negar
        if (args.size() == 1) {
            return allIntegers ? -firstArg.intValue() : -result;
        }
        
        // Restar el resto de argumentos
        for (int i = 1; i < args.size(); i++) {
            Object arg = args.get(i);
            
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Error: - requiere argumentos numéricos");
            }
            
            Number num = (Number) arg;
            result -= num.doubleValue();
            
            // Verificar si es un entero
            if (allIntegers && !(num instanceof Integer)) {
                allIntegers = false;
            }
        }
        
        // Si todos son enteros, retornar un entero
        return allIntegers ? (int) result : result;
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la operación de multiplicación.
     */
    private Object evaluateMultiply(List<?> list, LispEnvironment env) {
        // Evaluar los argumentos
        List<Object> args = evaluateArguments(list, env);
        
        if (args.isEmpty()) {
            return 1; // Multiplicación sin argumentos es 1
        }
        
        double result = 1;
        boolean allIntegers = true;
        
        for (Object arg : args) {
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Error: * requiere argumentos numéricos");
            }
            
            Number num = (Number) arg;
            result *= num.doubleValue();
            
            // Verificar si es un entero
            if (allIntegers && !(num instanceof Integer)) {
                allIntegers = false;
            }
        }
        
        // Si todos son enteros, retornar un entero
        return allIntegers ? (int) result : result;
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la operación de división.
     */
    private Object evaluateDivide(List<?> list, LispEnvironment env) {
        // Evaluar los argumentos
        List<Object> args = evaluateArguments(list, env);
        
        if (args.isEmpty()) {
            throw new RuntimeException("Error: / requiere al menos un argumento");
        }
        
        if (!(args.get(0) instanceof Number)) {
            throw new RuntimeException("Error: / requiere argumentos numéricos");
        }
        
        Number firstArg = (Number) args.get(0);
        double result = firstArg.doubleValue();
        
        // Si solo hay un argumento, invertir
        if (args.size() == 1) {
            return 1.0 / result;
        }
        
        // Dividir por el resto de argumentos
        for (int i = 1; i < args.size(); i++) {
            Object arg = args.get(i);
            
            if (!(arg instanceof Number)) {
                throw new RuntimeException("Error: / requiere argumentos numéricos");
            }
            
            Number num = (Number) arg;
            double divisor = num.doubleValue();
            
            if (divisor == 0) {
                throw new RuntimeException("Error: división por cero");
            }
            
            result /= divisor;
        }
        
        // Intentar retornar un entero si el resultado es un número entero
        if (result == (int) result) {
            return (int) result;
        }
        
        return result;
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la operación de igualdad.
     */
    private Object evaluateEqual(List<?> list, LispEnvironment env) {
        if (list.size() != 3) {
            throw new RuntimeException("Error: equal requiere exactamente dos argumentos");
        }
        
        Object arg1 = evaluate(list.get(1), env);
        Object arg2 = evaluate(list.get(2), env);
        
        return arg1.equals(arg2) ? "t" : "nil";
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la operación "menor que".
     */
    private Object evaluateLessThan(List<?> list, LispEnvironment env) {
        if (list.size() != 3) {
            throw new RuntimeException("Error: < requiere exactamente dos argumentos");
        }
        
        Object arg1 = evaluate(list.get(1), env);
        Object arg2 = evaluate(list.get(2), env);
        
        if (!(arg1 instanceof Number) || !(arg2 instanceof Number)) {
            throw new RuntimeException("Error: < requiere argumentos numéricos");
        }
        
        double num1 = ((Number) arg1).doubleValue();
        double num2 = ((Number) arg2).doubleValue();
        
        return num1 < num2 ? "t" : "nil";
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la operación "mayor que".
     */
    private Object evaluateGreaterThan(List<?> list, LispEnvironment env) {
        if (list.size() != 3) {
            throw new RuntimeException("Error: > requiere exactamente dos argumentos");
        }
        
        Object arg1 = evaluate(list.get(1), env);
        Object arg2 = evaluate(list.get(2), env);
        
        if (!(arg1 instanceof Number) || !(arg2 instanceof Number)) {
            throw new RuntimeException("Error: > requiere argumentos numéricos");
        }
        
        double num1 = ((Number) arg1).doubleValue();
        double num2 = ((Number) arg2).doubleValue();
        
        return num1 > num2 ? "t" : "nil";
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa el predicado 'atom'.
     */
    private Object evaluateAtom(List<?> list, LispEnvironment env) {
        if (list.size() != 2) {
            throw new RuntimeException("Error: atom requiere exactamente un argumento");
        }
        
        Object arg = evaluate(list.get(1), env);
        return !(arg instanceof List) || ((List<?>) arg).isEmpty() ? "t" : "nil";
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa la función 'list'.
     */
    private Object evaluateList(List<?> list, LispEnvironment env) {
        List<Object> result = new ArrayList<>();
        
        for (int i = 1; i < list.size(); i++) {
            result.add(evaluate(list.get(i), env));
        }
        
        return result;
    }
    
    //-------------------------------------------------------------------
    /**
     * Aplica una función definida por el usuario.
     */
    private Object applyUserFunction(String functionName, List<?> list, LispEnvironment env) {
        if (!env.hasFunction(functionName)) {
            throw new RuntimeException("Función no definida: " + functionName);
        }
        
        // Obtener la definición de la función
        FunctionDefinition function = env.getFunction(functionName);
        List<String> params = function.getParameters();
        
        // Evaluar argumentos
        List<Object> args = evaluateArguments(list, env);
        
        if (params.size() != args.size()) {
            throw new RuntimeException("Error: la función " + functionName + 
                                      " espera " + params.size() + " argumentos, pero recibió " + args.size());
        }
        
        // Crear un nuevo entorno para la ejecución de la función
        LispEnvironment functionEnv = new LispEnvironment(env);
        
        // Asignar argumentos a parámetros
        for (int i = 0; i < params.size(); i++) {
            functionEnv.setVariable(params.get(i), args.get(i));
        }
        
        // Evaluar el cuerpo de la función en el nuevo entorno
        return evaluate(function.getBody(), functionEnv);
    }
    
    //-------------------------------------------------------------------
    /**
     * Evalúa todos los argumentos de una lista (excepto el primero, que es el operador).
     */
    private List<Object> evaluateArguments(List<?> list, LispEnvironment env) {
        List<Object> evaluatedArgs = new ArrayList<>();
        
        // Empezar desde el segundo elemento (índice 1)
        for (int i = 1; i < list.size(); i++) {
            evaluatedArgs.add(evaluate(list.get(i), env));
        }
        
        return evaluatedArgs;
    }
    
    //-------------------------------------------------------------------
    /**
     * Determina si un valor LISP es verdadero.
     * En LISP, todo es verdadero excepto nil.
     */
    private boolean isTrue(Object value) {
        return !"nil".equals(value);
    }
}