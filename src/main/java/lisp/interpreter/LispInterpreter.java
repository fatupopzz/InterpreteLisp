package lisp.interpreter;

import java.util.List;

import lisp.environment.LispEnvironment;

/**
 * Intérprete LISP.
 * Integra todos los componentes del intérprete (tokenizador, parser y evaluador)
 * y proporciona una interfaz para evaluar expresiones LISP.
 * 
 * @author Equipo LISP
 */
public class LispInterpreter {
    
    private final LispTokenizer tokenizer;
    private final LispParser parser;
    private final LispEvaluator evaluator;
    private final LispEnvironment globalEnv;
    
    /**
     * Constructor que inicializa todos los componentes del intérprete.
     */
    public LispInterpreter() {
        this.tokenizer = new LispTokenizer();
        this.parser = new LispParser();
        this.evaluator = new LispEvaluator();
        this.globalEnv = new LispEnvironment();
        
        // Inicializar el entorno global con valores predefinidos
        initializeEnvironment();
    }
    
    /**
     * Inicializa el entorno global con valores predefinidos.
     */
    private void initializeEnvironment() {
        globalEnv.setVariable("t", "t");
        globalEnv.setVariable("nil", "nil");
    }
    
    /**
     * Evalúa una expresión LISP y devuelve el resultado.
     * 
     * @param input La expresión LISP a evaluar
     * @return El resultado de la evaluación
     * @throws LispException si ocurre un error durante la evaluación
     */
    public Object eval(String input) {
        try {
            // Tokenizar con información de posición para mejor manejo de errores
            List<LispTokenizer.Token> tokens = tokenizer.tokenizeWithPosition(input);
            
            if (tokens.isEmpty()) {
                return null;
            }
            
            // Convertir los tokens a String
            StringBuilder textoTokens = new StringBuilder();
            for (LispTokenizer.Token token : tokens) {
                textoTokens.append(token.getValue()).append(" ");
            }
            String expresionTexto = textoTokens.toString().trim();

            // Parsear tokens a una estructura de datos usando el texto reconstruido
            // CAMBIO: Crear una instancia de LispParser antes de llamar a parse
            LispParser parser = new LispParser();
            Object parsed = parser.parse(expresionTexto);

            // Evaluar la estructura de datos
            return evaluator.evaluate(parsed, globalEnv);
        } catch (LispException e) {
            throw e; // Propagar excepciones específicas de LISP
        } catch (Exception e) {
            throw new LispException("Error al evaluar: " + e.getMessage(), e);
        }
    }    
}