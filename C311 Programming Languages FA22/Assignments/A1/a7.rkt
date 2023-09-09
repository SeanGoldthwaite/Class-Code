#lang racket
(require racket/trace)

; Part I: let/cc
(define last-non-zero
  (lambda (ls)
    (let/cc k
      (letrec
          [(last-non-zero
            (lambda (ls)
              (cond
                [(null? ls) '()]
                [(eqv? (car ls) 0)
                 (cond
                   [(eqv? (cdr ls) (last-non-zero (cdr ls)))
                    (k (cdr ls))]
                   [else
                    (k (last-non-zero (cdr ls)))])
                 ]
                [else (cons (car ls) (last-non-zero (cdr ls)))]
                )))]
        (last-non-zero ls)))))


(println "Part I: last-non-zero")
(last-non-zero '())
(last-non-zero '(0))
(last-non-zero '(1 2 3 0 4 5))
(last-non-zero '(1 0 2 3 0 4 5))
(last-non-zero '(1 2 3 4 5))


; Part II: lex
(define lex
  (λ (exp ls)
    (letrec ([l
              (λ (lam env)
                (match lam
                  [`,n #:when (number? n) `(const ,n)]
                  [`,y #:when (symbol? y) (d y env)]
                  [`(zero? ,nexp)
                   `(zero ,(l nexp env))]
                  [`(* ,nexp1 ,nexp2)
                   `(mult ,(l nexp1 env)
                          ,(l nexp2 env))]
                  [`(,k ,exp)
                   #:when (symbol? k)
                   `(throw ,(d k env) ,(l exp (cons k env)))]
                  [`(let/cc ,k ,body)
                   `(letcc ,(l body (cons k env)))]
                  [`(lambda (,x) ,body)
                   #:when (symbol? x)
                   `(lambda ,(l body (cons x env)))]
                  [`(,rator ,rand)
                   `(app ,(l rator env)
                         ,(l rand env))]))]
             [d
              (λ (var env)
                (cond
                  [(not (member var env)) var]
                  [else (match env
                          [`() var]
                          [`(,a . ,d) #:when (eqv? a var) '(var 0)]
                          [else `(var ,(add1 (cadr (d var (cdr env)))))])]))])
      (l exp ls))))

(println "Part II: lex")

; Part III: The interpreter
(define value-of-cps
  (lambda (expr env-cps k)
    (match expr
      [`(const ,expr) (apply-k k expr)]
      [`(var ,y)
       (apply-env env-cps y k)]
      [`(sub1 ,x)
       (value-of-cps x
                     env-cps
                     (make-sub1-k k))]
      [`(zero ,x)
       (value-of-cps x
                     env-cps
                     (make-zero-k k))]
      [`(mult ,x1 ,x2)
       (value-of-cps x1
                     env-cps
                     (make-mult-outer-k x2 env-cps k))]
      [`(if ,test ,conseq ,alt)
       (value-of-cps test
                     env-cps
                     (make-if-k conseq alt env-cps k))]
      [`(letcc ,body)
       (value-of-cps body
                     (extend-env env-cps k)
                     k)]
      [`(throw ,k-exp ,v-exp)
       (value-of-cps k-exp
                     env-cps
                     (make-throw-k v-exp env-cps))]
      [`(let ,e ,body)
       (value-of-cps e
                     env-cps
                     (make-let-k body env-cps k))]
      [`(lambda ,body)
       (apply-k k (make-clos body env-cps k))]
      [`(app ,rator ,rand)
       (value-of-cps rator
                     env-cps
                     (make-rator-k rand env-cps k))])))
 
(define apply-env
  (λ (env-cps y k^)
    (match env-cps
      [`(empty-env)
       (error 'value-of "unbound identifier")]
      [`(extend-env ,env-cps^ ,k)
       (if (zero? y)
           (apply-k k^ k)
           (apply-env env-cps^ (sub1 y) k^))]
      #;[else (env-cps y k^)])))

(define empty-env
  (lambda ()
    `(empty-env)))

(define extend-env
  (λ (env-cps^ k^)
    `(extend-env ,env-cps^ ,k^)))

(define apply-clos
  (λ (rator arg k)
    (match rator
      [`(make-clos ,body ,env-cps ,k^)
       (value-of-cps body
                     (extend-env env-cps arg)
                     k)]
      #;[else (rator arg k)])))

(define make-clos
  (λ (body env-cps k)
    `(make-clos ,body ,env-cps ,k)))


(define apply-k
  (λ (k v)
    (match k
      [`(empty-k)
       v]
      [`(make-sub1-k ,k^)
       (apply-k k^ (sub1 v))]
      [`(make-zero-k ,k^)
       (apply-k k^ (zero? v))]
      [`(make-throw-k ,v-exp^ ,env-cps^)
       (value-of-cps v-exp^
                     env-cps^
                     v)]
      [`(make-rand-k ,v-rator^ ,k^)
       (apply-clos v-rator^ v k^)]
      [`(make-rator-k ,rand^ ,env-cps^ ,k^)
       (value-of-cps rand^
                     env-cps^
                     (make-rand-k v k^))]
      [`(make-mult-inner-k ,w^ ,k^)
       (apply-k k^ (* v w^))]
      [`(make-mult-outer-k ,x2^ ,env-cps^ ,k^)
       (value-of-cps x2^
                     env-cps^
                     (make-mult-inner-k v k^))]
      [`(make-let-k ,body ,env-cps ,k)
       (value-of-cps body
                     (extend-env env-cps v)
                     k)]
      [`(make-if-k ,conseq^ ,alt^ ,env-cps^ ,k^)
       (if v
           (value-of-cps conseq^ env-cps^ k^)
           (value-of-cps alt^ env-cps^ k^))]
      #;[else (k v)])))

(define empty-k
  (lambda ()
    `(empty-k)
    #;(lambda (v)
        v)))

(define make-sub1-k
  (λ (k^)
    `(make-sub1-k ,k^)))

(define make-zero-k
  (λ (k^)
    `(make-zero-k ,k^)))

(define make-throw-k
  (λ (v-exp^ env-cps^)
    `(make-throw-k ,v-exp^ ,env-cps^)))

(define make-if-k
  (λ (conseq^ alt^ env-cps^ k^)
    `(make-if-k ,conseq^ ,alt^ ,env-cps^ ,k^)))

(define make-let-k
  (λ (body^ env-cps^ k^)
    `(make-let-k ,body^ ,env-cps^ ,k^)))

(define make-rand-k
  (λ (v-rator^ k^)
    `(make-rand-k ,v-rator^ ,k^)))

(define make-rator-k
  (λ (rand^ env-cps^ k^)
    `(make-rator-k ,rand^ ,env-cps^ ,k^)))

(define make-mult-inner-k
  (λ (w^ k^)
    `(make-mult-inner-k ,w^ ,k^)))

(define make-mult-outer-k
  (λ (x2^ env-cps^ k^)
    `(make-mult-outer-k ,x2^ ,env-cps^ ,k^)))

(println "Part III: The interpreter")
(printf "Expected Output:\n5\n25\n3\n4\n6\n5\n4\n5\n25\n5\n3\n5\n5\n15\n4\n4\n1\nActual:\n")
;(trace value-of-cps)
(value-of-cps '(const 5)
              (empty-env) (empty-k)) ;5

(value-of-cps '(mult (const 5)
                     (const 5))
              (empty-env) (empty-k)) ;25

(value-of-cps '(sub1
                (sub1
                 (const 5)))
              (empty-env) (empty-k)) ;3

(value-of-cps '(if (zero (const 0))
                   (mult (const 2)
                         (const 2))
                   (const 3))
              (empty-env) (empty-k)) ;4

(value-of-cps '(app
                (app (lambda
                         (lambda (var 1)))
                     (const 6))
                (const 5))
              (empty-env) (empty-k)) ;6

(value-of-cps '(app (lambda
                        (app (lambda (var 1))
                             (const 6)))
                    (const 5))
              (empty-env) (empty-k)) ;5

(value-of-cps '(let (const 6)
                 (const 4))
              (empty-env) (empty-k)) ;4

(value-of-cps '(let (const 5)
                 (var 0))
              (empty-env) (empty-k)) ;5

(value-of-cps '(mult (const 5)
                     (let (const 5)
                       (var 0)))
              (empty-env) (empty-k)) ;25

(value-of-cps '(app (if (zero (const 4))
                        (lambda (var 0))
                        (lambda (const 5)))
                    (const 3))
              (empty-env) (empty-k)) ;5

(value-of-cps '(app (if (zero (const 0))
                        (lambda (var 0))
                        (lambda (const 5)))
                    (const 3))
              (empty-env) (empty-k)) ;3

(value-of-cps '(letcc (throw
                       (throw (var 0)
                              (const 5))
                       (const 6)))
              (empty-env) (empty-k)) ;5

(value-of-cps '(letcc (throw (const 5)
                             (throw (var 0)
                                    (const 5))))
              (empty-env) (empty-k)) ;5

(value-of-cps '(mult (const 3)
                     (letcc (throw (const 5)
                                   (throw (var 0)
                                          (const 5)))))
              (empty-env) (empty-k)) ;15

(value-of-cps '(if (zero (const 5))
                   (app (lambda
                            (app (var 0)
                                 (var 0)))
                        (lambda (app (var 0)
                                     (var 0))))
                   (const 4))
              (empty-env) (empty-k)); 4

(value-of-cps '(if (zero (const 0))
                   (const 4)
                   (app (lambda (app (var 0)
                                     (var 0)))
                        (lambda (app (var 0)
                                     (var 0)))))
              (empty-env) (empty-k)) ;4

(value-of-cps '(app (lambda
                        (app (app (var 0)
                                  (var 0))
                             (const 2)))
                    (lambda
                        (lambda 
                            (if (zero (var 0))  
                                (const 1)
                                (app (app (var 1)
                                          (var 1))
                                     (sub1 (var 0)))))))
              (empty-env) (empty-k)) ;1