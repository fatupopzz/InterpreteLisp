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