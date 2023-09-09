#lang racket

(define value-of-ds
  (λ (exp env)
    (match exp
      [`,n #:when (number? n) n]
      [`,b #:when (boolean? b) b]
      [`,y #:when (symbol? y) (apply-env-ds env y)]
      [`(* ,a ,b)
       (* (value-of-ds a env) (value-of-ds b env))]
      [`(zero? ,x)
       (zero? (value-of-ds x env))]
      [`(sub1 ,n)
       (sub1 (value-of-ds n env))]
      [`(if ,test ,conseq ,alt)
       (if (value-of-ds test env)
           (value-of-ds conseq env)
           (value-of-ds alt env))]
      [`(let ([,x ,def]) ,body)
       #:when (symbol? x)
       (let ([env (extend-env-ds x (value-of-ds def env) env)])
         (value-of-ds body env))]
      [`(lambda (,x) ,body)
       #:when (symbol? x)
       (make-closure-ds x body env)]
      [`(,rator ,y-rand)
       #:when (symbol? y-rand)
       (apply-closure-ds
        (value-of-ds rator env)
        (apply-env-ds env y-rand))]
      [`(,rator ,rand)
       (apply-closure-ds
        (value-of-ds rator env)
        (value-of-ds rand env))]
      )))

(define apply-env-ds
  (λ (env y)
    (match env
      [`(extend-env ,x ,arg ,env)
       (cond
         [(eqv? y x) arg]
         [else (apply-env-ds env y)])]
      [`(empty-env)
       (error "Free Variable" y)]
      [else (env y)])))

(define extend-env-ds
  (λ (x arg env)
    `(extend-env ,x ,arg ,env)))

(define empty-env-ds
  (λ ()
    `(empty-env)))

(define apply-closure-ds
  (λ (rator rand)
    (match rator
      [`(make-closure ,x ,body ,env)
       (value-of-ds body (extend-env-ds x rand env))]
      [else (rator rand)])))

(define make-closure-ds
  (λ (x body env)
    `(make-closure ,x ,body ,env)))

(println "value-of-ds")

(value-of-ds
   '((lambda (x) (if (zero? x)
                     #t
                     #f))
     0)
   (empty-env-ds)) ;#t

(value-of-ds 
   '((lambda (x) (if (zero? x) 
                     12 
                     47)) 
     0) 
   (empty-env-ds)) ;12

(value-of-ds
   '(let ([y (* 3 4)])
      ((lambda (x) (* x y)) (sub1 6)))
   (empty-env-ds)) ;60

(value-of-ds
   '(let ([x (* 2 3)])
      (let ([y (sub1 x)])
        (* x y)))
   (empty-env-ds)) ;30

(value-of-ds
   '(let ([x (* 2 3)])
      (let ([x (sub1 x)])
        (* x x)))
   (empty-env-ds)) ;25

(value-of-ds
   '(((lambda (f)
        (lambda (n) (if (zero? n) 1 (* n ((f f) (sub1 n))))))
      (lambda (f)
        (lambda (n) (if (zero? n) 1 (* n ((f f) (sub1 n)))))))
     5)
   (empty-env-ds)) ;120