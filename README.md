# Intérprete LISP

## Descripción del Proyecto
Este proyecto implementa un intérprete básico para un subconjunto del lenguaje LISP. El intérprete permite ejecutar operaciones aritméticas, definir funciones, realizar operaciones condicionales y soporta recursividad.

## Características Implementadas
- **Operaciones aritméticas**: +, -, *, /
- **Instrucción QUOTE o '**: Para interrumpir el proceso de evaluación
- **Definición de funciones**: DEFUN
- **Asignación de variables**: SETQ
- **Predicados**: ATOM, LIST, EQUAL, <, >
- **Condicionales**: COND
- **Recursividad**: Soporte completo para funciones recursivas

## Estructura del Proyecto
El proyecto sigue una arquitectura modular que separa las responsabilidades en componentes claramente definidos:

```
InterpreteLisp/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── lisp/
│   │           ├── interpreter/
│   │           │   ├── LispInterpreter.java  # Clase principal del intérprete
│   │           │   ├── LispTokenizer.java    # Procesa texto a tokens
│   │           │   ├── LispParser.java       # Tokens a estructura de datos
│   │           │   ├── LispEvaluator.java    # Evalúa expresiones
│   │           │   └── LispException.java    # Manejo de errores específicos
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
├── pom.xml
└── README.md
```
## UML Y DIAGRAMAS 
![image](https://github.com/user-attachments/assets/fe3e4e42-15c6-4ef2-a13b-d0a5030e2b7c)

## DIAGRAMA DE CASOS
![image](https://github.com/user-attachments/assets/ead7ae75-be85-49a4-8a60-7c8171c72e7e)

## DIAGRAMA DE SECUENCIA 
![image](https://github.com/user-attachments/assets/d25b2984-1d5b-4208-87a4-e22b6fe20405)

## DIAGRAMA DE ESTADO 

![image](https://github.com/user-attachments/assets/fd575675-e05c-4fd6-a3ca-98350d9226dd)


## Arquitectura del Intérprete

El intérprete sigue un diseño modular con estas componentes clave:

1. **Tokenizador (LispTokenizer)**: Convierte texto de entrada en tokens individuales.
2. **Parser (LispParser)**: Transforma tokens en estructuras de datos anidadas.
3. **Evaluador (LispEvaluator)**: Procesa las estructuras de datos y ejecuta las operaciones.
4. **Entorno (LispEnvironment)**: Gestiona variables y funciones definidas.
5. **Intérprete (LispInterpreter)**: Coordina los componentes anteriores.
6. **REPL (Main)**: Proporciona la interfaz de usuario para interactuar con el intérprete.

## Proceso REPL (Read-Eval-Print-Loop)

El intérprete implementa el ciclo REPL fundamental en LISP:

1. **Read**: El texto de entrada se convierte en tokens y estructura de datos
2. **Eval**: La estructura de datos se evalúa según las reglas de LISP
3. **Print**: El resultado se muestra al usuario
4. **Loop**: El proceso se repite

## Cómo Ejecutar

### Requisitos Previos
- Java 11 o superior
- Maven

### Compilar el Proyecto
```bash
mvn clean compile
```

### Ejecutar el REPL (modo interactivo)
```bash
mvn exec:java -Dexec.mainClass="lisp.Main"
```

### Ejecutar un archivo LISP
```bash
mvn exec:java -Dexec.mainClass="lisp.Main" -Dexec.args="ruta/al/archivo.lisp"
```

## Ejemplo de archivo LISP

El proyecto incluye un archivo de ejemplo `fibonacci.lisp` que puedes utilizar para probar el intérprete:

```lisp
;; Definición de la función fibonacci
(defun fibonacci (n)
  (cond ((equal n 0) 0)
        ((equal n 1) 1)
        (t (+ (fibonacci (- n 1))
              (fibonacci (- n 2))))))

;; Calcular el 10º número de Fibonacci
(fibonacci 10)

;; Definición de la función factorial
(defun factorial (n)
  (cond ((equal n 0) 1)
        (t (* n (factorial (- n 1))))))

;; Calcular el factorial de 5
(factorial 5)

;; Convertir de Fahrenheit a Celsius
(defun fahrenheit-to-celsius (f)
  (/ (* (- f 32) 5) 9))

;; Convertir 212°F (punto de ebullición del agua) a Celsius
(fahrenheit-to-celsius 212)
```

Para ejecutar este archivo de ejemplo:

```bash
mvn exec:java -Dexec.mainClass="lisp.Main" -Dexec.args="fibonacci.lisp"
```

Al ejecutarlo, deberías ver los resultados de:
1. El décimo número de Fibonacci (55)
2. El factorial de 5 (120)
3. La conversión de 212°F a Celsius (100)

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

### Fibonacci (ejemplo recursivo)
```lisp
(defun fibonacci (n)
  (cond ((equal n 0) 0)
        ((equal n 1) 1)
        (t (+ (fibonacci (- n 1))
              (fibonacci (- n 2))))))
              
(fibonacci 10)    ; => 55
```

### Condicionales
```lisp
(cond ((> 3 2) "mayor")
      ((< 3 2) "menor")
      (t "igual"))  ; => "mayor"
```

## Estructura de Datos Utilizadas

- **HashMap**: Para el entorno (variables y funciones)
- **ArrayList**: Para representar listas en LISP
- **Árboles (implícitos)**: Mediante listas anidadas para la estructura de datos

## Equipo de Desarrollo
- Fatima Navarro 24044
- Andrés Ismalej 24005
- Adair Velasquez 24596
