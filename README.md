# Intérprete LISP

## Descripción del Proyecto
Este proyecto implementa un intérprete básico para un subconjunto del lenguaje LISP. El intérprete permite ejecutar operaciones aritméticas, definir funciones, realizar operaciones condicionales y soporta recursividad.

## Características
- **Operaciones aritméticas**: +, -, *, /
- **Instrucción QUOTE o '**: Para interrumpir el proceso de evaluación
- **Definición de funciones**: DEFUN
- **Asignación de variables**: SETQ
- **Predicados**: ATOM, LIST, EQUAL, <, >
- **Condicionales**: COND
- **Recursividad**: Soporte para funciones recursivas

## Estructura del Proyecto
El proyecto sigue una arquitectura modular, separando las responsabilidades en diferentes componentes:

```
InterpreteLisp/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── lisp/
│   │           ├── interpreter/
│   │           │   ├── LispInterpreter.java  # Clase principal
│   │           │   ├── LispTokenizer.java    # Procesa texto a tokens
│   │           │   ├── LispParser.java       # Tokens a estructura de datos
│   │           │   └── LispEvaluator.java    # Evalúa expresiones
│   │           ├── environment/
│   │           │   └── LispEnvironment.java  # Manejo de variables y funciones
│   │           └── Main.java                 # Punto de entrada
│   └── test/
│       └── java/
│           └── lisp/
│               ├── LispInterpreterTest.java
│               ├── LispTokenizerTest.java
│               ├── LispParserTest.java
│               └── LispEvaluatorTest.java
├── docs/                     # Documentación
└── README.md                 # Este archivo
```

## Diagrama UML

![image](https://github.com/user-attachments/assets/4a3c05a4-138f-428a-a2c2-f949d1375e96)



## Proceso REPL (Read-Eval-Print-Loop)

El intérprete implementa el ciclo REPL que es fundamental en LISP:

1. **Read**: El texto de entrada se convierte en tokens y luego en una estructura de datos
2. **Eval**: La estructura de datos se evalúa según las reglas de LISP
3. **Print**: El resultado se muestra al usuario
4. **Loop**: El proceso se repite

## Estructuras de Datos Utilizadas

- **HashMap**: Para el entorno (variables y funciones)
- **ArrayList**: Para representar listas en LISP
- **LinkedList**: Para procesar tokens durante el parsing

## Implementación Única

Nuestro intérprete se distingue por:

1. **Arquitectura por capas**: Separación clara de responsabilidades
2. **Manejo eficiente de memoria**: Uso apropiado de estructuras de datos de Java
3. **Soporte para recursión**: Mediante un diseño cuidadoso del entorno
4. **Código limpio y legible**: Siguiendo buenas prácticas de programación

## Cómo Ejecutar

1. Compilar el proyecto:
   ```
   javac -d bin src/main/java/lisp/*.java
   ```

2. Ejecutar el intérprete:
   ```
   java -cp bin lisp.Main
   ```

3. Usar el REPL:
   ```
   lisp> (+ 2 3)
   => 5
   lisp> (defun factorial (n) (cond ((equal n 0) 1) (t (* n (factorial (- n 1))))))
   => factorial
   lisp> (factorial 5)
   => 120
   ```

## Pruebas

El proyecto incluye pruebas unitarias para verificar el correcto funcionamiento de cada componente. Para ejecutar las pruebas:

```
java -cp bin:lib/junit.jar org.junit.runner.JUnitCore lisp.LispInterpreterTest
```

## Estado del Proyecto

Este proyecto se encuentra en desarrollo como parte de la Fase 2 del curso CC2016 – Algoritmos y Estructura de Datos, Semestre I – 2025, Universidad del Valle de Guatemala.

---

Creado por Isma, fati y 
