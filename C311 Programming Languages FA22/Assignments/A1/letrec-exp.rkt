#lang racket
(require racket/trace)

(define value-of-ds
  (lambda (exp env)
    (match exp
      [`,b #:when (boolean? b) b]
      [`,n #:when (number? n)  n]
      [`(zero? ,n)
        (zero? (value-of-ds n env))]
      [`(sub1 ,n)
        (sub1 (value-of-ds n env))]
      [`(+ ,n1 ,n2)
        (+ (value-of-ds n1 env)
           (value-of-ds n2 env))]
      [`(* ,n1 ,n2)
        (* (value-of-ds n1 env)
           (value-of-ds n2 env))]
      [`(if ,test ,conseq ,alt)
        (if (value-of-ds test env)
            (value-of-ds conseq env)
            (value-of-ds alt env))]
      [`(letrec ,1/2-closure ,body)
       (value-of-ds body (extend-rec-env 1/2-closure env))]
      [`(random ,n)
        (random (value-of-ds n env))]
      [`,y #:when (symbol? y)
        (apply-env-ds env y)]
      [`(lambda (,x) ,body)
        (make-closure x body env)]
      [`(,rator ,y-rand)
        (apply-closure-ds
         (value-of-ds rator env)
         (value-of-ds y-rand env))])))

(define extend-rec-env
  (λ (hclosure env)
    `(rec-env ,hclosure ,env)))

(define apply-env-ds
  (λ (env y)
    (match env
      [`(extend-env ,x ,arg ,env)
       (cond
         [(eqv? y x) arg]
         [else (apply-env-ds env y)])]
      [`(rec-env ,hclosure ,env)
       (cond
         [(assv y hclosure)
          => (λ (p)
               (let ([val-e (cadr p)])
                 (value-of-ds val-e (extend-rec-env hclosure env))))])]
      [`(empty-env)
       (error "Free Variable" y)]
      [else (env y)])))

(define extend-env
  (λ (x arg env)
    `(extend-env ,x ,arg ,env)))

(define empty-env-ds
  (λ ()
    `(empty-env)))

(define apply-closure-ds
    (λ (rator rand)
      (match rator
        [`(make-closure ,x ,body ,env)
         (value-of-ds body (extend-env x rand env))]
        [else (rator rand)])))

(define make-closure
  (λ (x body env)
    `(make-closure ,x ,body ,env)))

(value-of-ds '(letrec ([x 22]
                         [y 20]
                         [z (+ x y)])
                 z)
               (empty-env-ds))

(value-of-ds '(letrec ([even? (lambda (n)
                                  (if (zero? n)
                                      #t
                                      (odd? (sub1 n))))]
                         [odd? (lambda (n)
                                 (if (zero? n)
                                     #f
                                     (even? (sub1 n))))])
                  (even? 11))
               (empty-env-ds))

(value-of-ds '(letrec ([even? (lambda (n)
                                  (if (zero? n)
                                      #t
                                      (odd? (sub1 n))))]
                         [odd? (lambda (n)
                                 (if (zero? n)
                                     #f
                                     (even? (sub1 n))))])
                  (even? 42))
               (empty-env-ds))