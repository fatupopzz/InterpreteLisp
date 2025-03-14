package lisp.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona el entorno de ejecución para el intérprete LISP.
 * Mantiene un registro de variables y funciones definidas.
 * 
 * @author Fatima Navarro 24044
 */
public class LispEnvironment {
    
    //-------------------------------------------------------------------
    /** Mapa para almacenar variables (nombre -> valor) */
    private final Map<String, Object> variables;
    
    /** Mapa para almacenar funciones definidas (nombre -> definición) */
    private final Map<String, FunctionDefinition> functions;
    
    /** Referencia al entorno padre (para ámbitos anidados) */
    private final LispEnvironment parent;
    
    //-------------------------------------------------------------------
    /**
     * Constructor para crear un entorno global (sin padre).
     */
    public LispEnvironment() {
        this(null);
    }
    
    //-------------------------------------------------------------------
    /**
     * Constructor para crear un entorno con un padre.
     * Se usa para entornos locales de funciones.
     * 
     * @param parent El entorno padre
     */
    public LispEnvironment(LispEnvironment parent) {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
        this.parent = parent;
    }
    
    //-------------------------------------------------------------------
    /**
     * Obtiene el valor de una variable.
     * Busca primero en el entorno actual y luego en el padre si existe.
     * 
     * @param name Nombre de la variable
     * @return Valor de la variable
     * @throws RuntimeException si la variable no está definida
     */
    public Object getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        
        if (parent != null) {
            return parent.getVariable(name);
        }
        
        throw new RuntimeException("Variable no definida: " + name);
    }
    
    //-------------------------------------------------------------------
    /**
     * Define o actualiza una variable en el entorno actual.
     * 
     * @param name Nombre de la variable
     * @param value Valor a asignar
     * @return El valor asignado
     */
    public Object setVariable(String name, Object value) {
        variables.put(name, value);
        return value;
    }
    
    //-------------------------------------------------------------------
    /**
     * Verifica si una variable existe en este entorno o sus padres.
     * 
     * @param name Nombre de la variable
     * @return true si la variable existe
     */
    public boolean hasVariable(String name) {
        return variables.containsKey(name) || 
               (parent != null && parent.hasVariable(name));
    }
    
    //-------------------------------------------------------------------
    /**
     * Define una nueva función en el entorno actual.
     * 
     * @param name Nombre de la función
     * @param params Lista de nombres de parámetros
     * @param body Cuerpo de la función
     * @return Nombre de la función definida
     */
    public String defineFunction(String name, List<String> params, Object body) {
        functions.put(name, new FunctionDefinition(params, body));
        return name;
    }
    
    //-------------------------------------------------------------------
    /**
     * Obtiene la definición de una función.
     * Busca primero en el entorno actual y luego en el padre si existe.
     * 
     * @param name Nombre de la función
     * @return Definición de la función
     * @throws RuntimeException si la función no está definida
     */
    public FunctionDefinition getFunction(String name) {
        if (functions.containsKey(name)) {
            return functions.get(name);
        }
        
        if (parent != null) {
            return parent.getFunction(name);
        }
        
        throw new RuntimeException("Función no definida: " + name);
    }
    
    //-------------------------------------------------------------------
    /**
     * Verifica si una función existe en este entorno o sus padres.
     * 
     * @param name Nombre de la función
     * @return true si la función existe
     */
    public boolean hasFunction(String name) {
        return functions.containsKey(name) || 
               (parent != null && parent.hasFunction(name));
    }
    
    //-------------------------------------------------------------------
    /**
     * Clase interna para representar definiciones de funciones.
     * Almacena los parámetros y el cuerpo de una función.
     */
    public static class FunctionDefinition {
        private final List<String> parameters;
        private final Object body;
        
        /**
         * Constructor para una definición de función.
         * 
         * @param parameters Lista de nombres de parámetros
         * @param body Cuerpo de la función (no evaluado)
         */
        public FunctionDefinition(List<String> parameters, Object body) {
            this.parameters = parameters;
            this.body = body;
        }
        
        /**
         * Obtiene la lista de parámetros de la función.
         * 
         * @return Lista de nombres de parámetros
         */
        public List<String> getParameters() {
            return parameters;
        }
        
        /**
         * Obtiene el cuerpo de la función.
         * 
         * @return Cuerpo de la función
         */
        public Object getBody() {
            return body;
        }
    }
}