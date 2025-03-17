# Intérprete LISP Básico

## Descripción del Proyecto
Este proyecto implementa un intérprete básico para un subconjunto del lenguaje LISP, siguiendo un enfoque pragmático que prioriza la eficiencia, robustez y extensibilidad.

## Características Soportadas
- **Operaciones aritméticas**: +, -, *, /
- **Instrucción QUOTE o '**: Para interrumpir el proceso de evaluación
- **Definición de funciones**: DEFUN
- **Asignación de variables**: SETQ
- **Predicados**: ATOM, LIST, EQUAL, <, >
- **Condicionales**: COND
- **Recursividad**: Soporte para funciones recursivas

## Arquitectura del Proyecto
El proyecto sigue una arquitectura modular simple pero potente, que separa las responsabilidades en componentes claramente definidos:

```
InterpreteLisp/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── lisp/
│   │           ├── interpreter/
│   │           │   ├── LispInterpreter.java  # Integración de componentes
│   │           │   ├── LispTokenizer.java    # Procesa texto a tokens con posición
│   │           │   ├── LispParser.java       # Tokens a estructura de datos
│   │           │   ├── LispEvaluator.java    # Evalúa expresiones
│   │           │   └── LispException.java    # Manejo de errores mejorado
│   │           ├── environment/
│   │           │   └── LispEnvironment.java  # Manejo de variables y funciones
│   │           └── Main.java                 # REPL (Read-Eval-Print-Loop)
│   └── test/
│       └── java/
│           └── lisp/
│               ├── LispInterpreterTest.java
│               ├── LispTokenizerTest.java
│               ├── LispParserTest.java
│               └── LispEvaluatorTest.java
└── README.md
```

## Proceso REPL (Read-Eval-Print-Loop)

El intérprete implementa el ciclo REPL que es fundamental en LISP:

1. **Read**: El texto de entrada se convierte en tokens y luego en una estructura de datos.
   - El tokenizador incluye información de posición para mejor manejo de errores.
   - El parser convierte los tokens en una estructura de datos estándar de Java.

2. **Eval**: La estructura de datos se evalúa según las reglas de LISP.
   - El evaluador mantiene un control preciso de tipos para resultados consistentes.
   - Se maneja recursividad mediante entornos anidados.

3. **Print**: El resultado se muestra al usuario.
   - Los resultados se formatean de manera legible.

4. **Loop**: El proceso se repite para la siguiente expresión.

## Ventajas del Enfoque

1. **Simplicidad**: Diseño directo y comprensible con menos abstracciones.
2. **Mantenibilidad**: Código más legible y fácil de modificar.
3. **Robustez**: Mejor manejo de errores con información de posición.
4. **Extensibilidad**: Arquitectura modular que permite agregar características fácilmente.
5. **Rendimiento**: Uso eficiente de estructuras de datos estándar de Java.

## Estructuras de Datos Utilizadas

- **HashMap**: Para entornos (variables y funciones)
- **ArrayList**: Para representar listas en LISP
- **Objetos estándar de Java**: Enteros, dobles y cadenas para representar valores atómicos

## Cómo Ejecutar

```bash
# Compilar el proyecto
mvn compile

# Ejecutar el REPL interactivo
mvn exec:java -Dexec.mainClass="lisp.Main"

# Ejecutar un archivo LISP
mvn exec:java -Dexec.mainClass="lisp.Main" -Dexec.args="ruta/al/archivo.lisp"
```

## Ejemplos de Uso

```lisp
;; Operaciones aritméticas
(+ 2 3)          ; => 5
(- 10 5 2)       ; => 3
(* 2 3 4)        ; => 24
(/ 10 2)         ; => 5

;; Variables
(setq x 42)      ; => 42
(+ x 10)         ; => 52

;; Funciones
(defun factorial (n)
  (cond ((equal n 0) 1)
        (t (* n (factorial (- n 1))))))
        
(factorial 5)    ; => 120

;; Condicionales
(cond ((< 2 1) "Menor")
      ((> 2 1) "Mayor")
      (t "Igual"))  ; => "Mayor"
```

## Estado del Proyecto

En progreso como el progreso
