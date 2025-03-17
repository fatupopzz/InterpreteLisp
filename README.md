# Intérprete LISP

## Descripción del Proyecto
Este proyecto implementa un intérprete básico para un subconjunto del lenguaje LISP, siguiendo un enfoque basado en mapas de funciones que prioriza la eficiencia, modularidad y extensibilidad. El intérprete permite ejecutar operaciones aritméticas, definir funciones, realizar operaciones condicionales y soporta recursividad.

## Características Implementadas
- **Operaciones aritméticas**: +, -, *, /
- **Instrucción QUOTE o '**: Para interrumpir el proceso de evaluación
- **Definición de funciones**: DEFUN
- **Asignación de variables**: SETQ
- **Predicados**: ATOM, LIST, EQUAL, <, >
- **Condicionales**: COND
- **Recursividad**: Soporte completo para funciones recursivas

## Arquitectura del Proyecto
El proyecto sigue una arquitectura modular que separa las responsabilidades en componentes claramente definidos:

```
InterpreteLisp/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── lisp/
│   │           ├── interpreter/
│   │           │   ├── LispInterpreter.java  # Integración de componentes
│   │           │   ├── LispTokenizer.java    # Procesa texto a tokens
│   │           │   ├── LispParser.java       # Tokens a estructura de datos
│   │           │   ├── LispEvaluator.java    # Evalúa expresiones usando mapas de funciones
│   │           │   └── LispException.java    # Manejo de errores específicos
│   │           ├── environment/
│   │           │   └── LispEnvironment.java  # Manejo de variables y funciones
│   │           └── Main.java                 # REPL (Read-Eval-Print-Loop)
│   └── test/
│       └── java/
│           └── lisp/
│               ├── LispTokenizerTest.java
│               ├── LispParserTest.java
│               ├── LispEvaluatorTest.java
│               └── LispInterpreterTest.java
```

## Enfoque basado en Mapas de Funciones
En lugar de utilizar grandes bloques `switch-case`, este intérprete emplea un enfoque basado en mapas de funciones:

1. **Registro de Operadores**: Cada operador LISP se registra en un mapa junto con su implementación
2. **Interfaz Funcional**: Se define una interfaz `LispOperator` que unifica el comportamiento de todos los operadores
3. **Evaluación Dinámica**: El evaluador busca en el mapa la implementación correspondiente al operador encontrado
4. **Extensibilidad**: Agregar nuevos operadores es tan simple como registrarlos en el mapa

```java
// Ejemplo de registro de operadores
operators.put("+", this::evaluateAdd);
operators.put("setq", this::handleSetq);
operators.put("cond", this::handleCond);
```

## Proceso REPL (Read-Eval-Print-Loop)

El intérprete implementa el ciclo REPL fundamental en LISP:

1. **Read**: El texto de entrada se convierte en tokens y luego en una estructura de datos
2. **Eval**: La estructura de datos se evalúa según las reglas de LISP
3. **Print**: El resultado se muestra al usuario
4. **Loop**: El proceso se repite

## Manejo de Tipos de Datos
- **Valores atómicos**: Números (enteros y de punto flotante), símbolos, strings
- **Listas**: Representadas como `List<Object>` en Java
- **Entorno**: Implementado como un mapa anidado para variables y funciones

## Manejo Inteligente de Tipos Numéricos
El intérprete incluye un sistema que determina automáticamente si un resultado debe representarse como entero o número de punto flotante:

```java
private Number toAppropriateNumber(double value, boolean preferInteger) {
    if (Math.floor(value) == value && !Double.isInfinite(value)) {
        if (preferInteger || (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE)) {
            return (int) value;
        }
    }
    return value;
}
```

## Cómo Ejecutar el Intérprete

### Requisitos Previos
- Java 11 o superior
- Maven

### Compilar el Proyecto
```bash
mvn clean compile
```

### Ejecutar Pruebas
```bash
mvn test
```

### Iniciar el REPL
```bash
mvn exec:java -Dexec.mainClass="lisp.Main"
```

### Ejecutar un Archivo LISP
```bash
mvn exec:java -Dexec.mainClass="lisp.Main" -Dexec.args="ruta/al/archivo.lisp"
```

## Ejemplos de Uso

### Operaciones Aritméticas
```lisp
(+ 2 3)           ; => 5
(- 10 5)          ; => 5
(* 2 3 4)         ; => 24
(/ 10 2)          ; => 5
```

### Variables
```lisp
(setq x 42)       ; => 42
(+ x 10)          ; => 52
```

### Funciones
```lisp
(defun suma (a b) (+ a b))
(suma 2 3)        ; => 5

(defun factorial (n)
  (cond ((equal n 0) 1)
        (t (* n (factorial (- n 1))))))
        
(factorial 5)     ; => 120
```

### Condicionales
```lisp
(cond ((> 3 2) "mayor")
      ((< 3 2) "menor")
      (t "igual"))  ; => "mayor"
```

## Equipo de Desarrollo
- Integrantes
- Fatima Navarro 24044
- Andrés Ismalej 24005
