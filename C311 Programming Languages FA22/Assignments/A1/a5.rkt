#lang racket
(require racket/trace)

(define val-of-cbv
  (lambda (exp env)
    (match exp
      [`,b #:when (boolean? b) b]
      [`,n #:when (number? n)  n]
      [`(zero? ,n)
       (zero? (val-of-cbv n env))]
      [`(sub1 ,n)
       (sub1 (val-of-cbv n env))]
      [`(* ,n1 ,n2)
       (* (val-of-cbv n1 env)
          (val-of-cbv n2 env))]
      [`(if ,test ,conseq ,alt)
       (if (val-of-cbv test env)
           (val-of-cbv conseq env)
           (val-of-cbv alt env))]
      [`(begin2 ,e1 ,e2)
       (begin (val-of-cbv e1 env)
              (val-of-cbv e2 env))]
      [`(set! ,x ,exp)
       #:when (symbol? x)
       (let ([b (apply-env env x)])
         (set-box! b (val-of-cbv exp env)))]
      [`(random ,n)
       (random (val-of-cbv n env))]
      [`,y #:when (symbol? y)
           (unbox (apply-env env y))]
      [`(lambda (,x) ,body)
       (make-closure x body env)]
      [`(,rator ,y-rand)
       (apply-closure-cbv
        (val-of-cbv rator env)
        (box (val-of-cbv y-rand env)))])))

(define apply-env
  (λ (env y)
    (match env
      [`(extend-env ,x ,arg ,env)
       (cond
         [(eqv? y x) arg]
         [else (apply-env env y)])]
      [`(empty-env)
       (error "Free Variable" y)]
      [else (env y)])))

(define extend-env
  (λ (x arg env)
    `(extend-env ,x ,arg ,env)))

(define empty-env
  (λ ()
    `(empty-env)))

(define apply-closure-cbv
  (λ (rator rand)
    (match rator
      [`(make-closure ,x ,body ,env)
       (val-of-cbv body (extend-env x rand env))]
      [else (rator rand)])))

(define make-closure
  (λ (x body env)
    `(make-closure ,x ,body ,env)))


(println "Call By Value")

(val-of-cbv
 '((lambda (x) (begin2 (set! x #t)
                       (if x 3 5))) #f)
 (empty-env))

(val-of-cbv
 '((lambda (a)
     ((lambda (p)
        (begin2
          (p a)
          a)) (lambda (x) (set! x 4)))) 3)
 (empty-env)) ;3

(val-of-cbv
 '((lambda (f)
     ((lambda (g)
        ((lambda (z) (begin2
                       (g z)
                       z))
         55))
      (lambda (y) (f y)))) (lambda (x) (set! x 44)))
 (empty-env)) ;55

(val-of-cbv
 '((lambda (swap)
     ((lambda (a)
        ((lambda (b)
           (begin2
             ((swap a) b)
             a)) 44)) 33))
   (lambda (x)
     (lambda (y)
       ((lambda (temp)
          (begin2
            (set! x y)
            (set! y temp))) x))))
 (empty-env)) ;33



;Call By Reference
(define val-of-cbr
  (lambda (exp env)
    (match exp
      [`,b #:when (boolean? b) b]
      [`,n #:when (number? n)  n]
      [`(zero? ,n)
       (zero? (val-of-cbr n env))]
      [`(sub1 ,n)
       (sub1 (val-of-cbr n env))]
      [`(* ,n1 ,n2)
       (* (val-of-cbr n1 env)
          (val-of-cbr n2 env))]
      [`(if ,test ,conseq ,alt)
       (if (val-of-cbr test env)
           (val-of-cbr conseq env)
           (val-of-cbr alt env))]
      [`(begin2 ,e1 ,e2)
       (begin (val-of-cbr e1 env)
              (val-of-cbr e2 env))]
      [`(set! ,x ,exp)
       #:when (symbol? x)
       (let ([b (apply-env env x)])
         (set-box! b (val-of-cbr exp env)))]
      [`(random ,n)
       (random (val-of-cbr n env))]
      [`,y #:when (symbol? y)
           (unbox (apply-env env y))]
      [`(lambda (,x) ,body)
       (make-closure x body env)]
      [`(,rator ,y-rand)
       #:when (symbol? y-rand)
       (apply-closure-cbr
        (val-of-cbr rator env)
        (apply-env env y-rand))]
      [`(,rator ,y-rand)
       (apply-closure-cbr
        (val-of-cbr rator env)
        (box (val-of-cbr y-rand env)))])))

(define apply-closure-cbr
  (λ (rator rand)
    (match rator
      [`(make-closure ,x ,body ,env)
       (val-of-cbr body (extend-env x rand env))]
      [else (rator rand)])))

;(trace val-of-cbr)
(println "Call By Reference")

(val-of-cbr
 '((lambda (x) (begin2 (set! x #t)
                       (if x 3 5))) #f)
 (empty-env))

(val-of-cbr
 '((lambda (a)
     ((lambda (p)
        (begin2
          (p a)
          a)) (lambda (x) (set! x 4)))) 3)
 (empty-env)) ;4

(val-of-cbr
 '((lambda (f)
     ((lambda (g)
        ((lambda (z) (begin2
                       (g z)
                       z))
         55))
      (lambda (y) (f y)))) (lambda (x) (set! x 44)))
 (empty-env)) ;44

(val-of-cbr
 '((lambda (swap)
     ((lambda (a)
        ((lambda (b)
           (begin2
             ((swap a) b)
             a)) 44)) 33))
   (lambda (x)
     (lambda (y)
       ((lambda (temp)
          (begin2
            (set! x y)
            (set! y temp))) x))))
 (empty-env)) ;44


; Call By Name
(define val-of-cbname
  (lambda (exp env)
    (match exp
      [`,b #:when (boolean? b) b]
      [`,n #:when (number? n)  n]
      [`(zero? ,n)
       (zero? (val-of-cbname n env))]
      [`(sub1 ,n)
       (sub1 (val-of-cbname n env))]
      [`(* ,n1 ,n2)
       (* (val-of-cbname n1 env)
          (val-of-cbname n2 env))]
      [`(if ,test ,conseq ,alt)
       (if (val-of-cbname test env)
           (val-of-cbname conseq env)
           (val-of-cbname alt env))]
      [`(random ,n)
       (random (val-of-cbname n env))]
      [`,y #:when (symbol? y)
           ((unbox (apply-env env y)))]
      [`(lambda (,x) ,body)
       (make-closure x body env)]
      [`(,rator ,y-rand)
       #:when (symbol? y-rand)
       (apply-closure-cbname
        (val-of-cbname rator env)
        (apply-env env y-rand))]
      [`(,rator ,y-rand)
       (apply-closure-cbname
        (val-of-cbname rator env)
        (box (λ ()
               (val-of-cbname y-rand env))))])))

(define apply-closure-cbname
  (λ (rator rand)
    (match rator
      [`(make-closure ,x ,body ,env)
       (val-of-cbname body (extend-env x rand env))]
      [else (rator rand)])))

#;(trace val-of-cbname)
(println "Call By Name")

(define random-sieve
  '((lambda (n)
      (if (zero? n)
          (if (zero? n) (if (zero? n) (if (zero? n) (if (zero? n) (if (zero? n) (if (zero? n) #t #f) #f) #f) #f) #f) #f)
          (if (zero? n) #f (if (zero? n) #f (if (zero? n) #f (if (zero? n) #f (if (zero? n) #f (if (zero? n) #f #t))))))))
    (random 2)))

(val-of-cbname random-sieve (empty-env))

; Call By Need
(define val-of-cbneed
  (lambda (exp env)
    (match exp
      [`,b #:when (boolean? b) b]
      [`,n #:when (number? n)  n]
      [`(zero? ,n)
       (zero? (val-of-cbneed n env))]
      [`(sub1 ,n)
       (sub1 (val-of-cbneed n env))]
      [`(+ ,n1 ,n2)
       (+ (value-of-ds n1 env)
          (value-of-ds n2 env))]
      [`(* ,n1 ,n2)
       (* (val-of-cbneed n1 env)
          (val-of-cbneed n2 env))]
      [`(if ,test ,conseq ,alt)
       (if (val-of-cbneed test env)
           (val-of-cbneed conseq env)
           (val-of-cbneed alt env))]
      [`(random ,n)
       (random (val-of-cbneed n env))]
      [`,y #:when (symbol? y)
           (let ([b (apply-env env y)])
             (let ([thunk (unbox b)])
               (let ([ev (thunk)])
                 (begin
                   (set-box! b (λ () ev))
                   ev))))]
      [`(lambda (,x) ,body)
       (make-closure x body env)]
      [`(,rator ,y-rand)
       #:when (symbol? y-rand)
       (apply-closure-cbneed
        (val-of-cbneed rator env)
        (apply-env env y-rand))]
      [`(,rator ,y-rand)
       (apply-closure-cbneed
        (val-of-cbneed rator env)
        (box (λ ()
               (val-of-cbneed y-rand env))))])))

(define apply-closure-cbneed
  (λ (rator rand)
    (match rator
      [`(make-closure ,x ,body ,env)
       (val-of-cbneed body (extend-env x rand env))]
      [else (rator rand)])))

(println "Call By Need")
(val-of-cbneed random-sieve (empty-env))

; Letrec
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

(define empty-env-ds
  (λ ()
    `(empty-env)))

(define apply-closure-ds
  (λ (rator rand)
    (match rator
      [`(make-closure ,x ,body ,env)
       (value-of-ds body (extend-env x rand env))]
      [else (rator rand)])))

(println "Part 2: letrec")
(value-of-ds '(letrec ([f (lambda (x) (+ (g x) 2))]
                       [g (lambda (y) (* y 2))]
                       [v 20])
                (f v)) (empty-env-ds))

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