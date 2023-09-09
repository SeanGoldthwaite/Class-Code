#lang racket

(require predicates)

; Problem 1
(define list-ref
  (lambda (ls n)
    (letrec
        ([nth-cdr (lambda (n)
                    (cond
                      [(null? ls) (raise exn:fail:contract)] ;Modify to throw exn:fail:contract 
                      [(zero? n) ls]
                      [else
                       (set! ls (cdr ls))
                       (nth-cdr (sub1 n))])
                    )])
      (car (nth-cdr n)))))

(println '(Problem 1: list-ref))
(list-ref '(a b c) 2)
(list-ref '(a b c) 0)

; Problem 2
(define union
  (λ (ls1 ls2)
    (cond
      [(null? ls1) ls2]
      [(null? ls2) ls1]
      [(not (memv (car ls1) ls2)) (cons (car ls1) (union (cdr ls1) ls2))]
      [else (union (cdr ls1) ls2)])))

(println '(Problem 2: union))
(union '() '())
(union '(x) '())
(union '(x) '(x))
(union '(x y) '(x z))

; Problem 3
(define stretch
  (λ (pred x)
    (or? pred (λ (y) (eqv? x y)))))

(println '(Problem 3: stretch))
((stretch even? 1) 0)
((stretch even? 1) 1)
((stretch even? 1) 2)
((stretch even? 1) 3)
(filter (stretch even? 1) '(0 1 2 3 4 5))
(filter (stretch (stretch even? 1) 3) '(0 1 2 3 4 5))
(filter (stretch (stretch (stretch even? 1) 3) 7) '(0 1 2 3 4 5))

;Problem 4
(define walk-symbol
  (λ (x ls)
    (define val (assv x ls))
    (cond
      [(false? val) x]
      [(or (number? (cdr val)) (symbol? (cdr val))) (walk-symbol (cdr val) ls)]
      [(pair? (cdr val)) (cdr val)]
    )))

(println '(Problem 4))
(walk-symbol 'a '((a . 5)))
(walk-symbol 'a '((b . c) (a . b)))
(walk-symbol 'a '((a . 5) (b . 6) (c . a)))
(walk-symbol 'c '((a . 5) (b . (a . c)) (c . a)))
(walk-symbol 'b '((a . 5) (b . ((c . a))) (c . a)))
(walk-symbol 'd '((a . 5) (b . (1 2)) (c . a) (e . c) (d . e)))
(walk-symbol 'd '((a . 5) (b . 6) (c . f) (e . c) (d . e)))

#;(match exp
  [`,y #:when (symbol? y)]
  [`(lambda (,x) ,body) #:when (symbol? x) ]
  [`(,rator ,rand) ]
  [else ])

; Problem 5
(define lambda-exp?
  (λ (E)
    (letrec
      ([p
        (λ (e)
          (match e
            [`,y #:when (symbol? y) #t]
            [`(lambda (,x) ,body) #:when (symbol? x) (p body)]
            [`(,rator ,rand) (and (p rator) (p rand))]
            [else #f]))])
      (p E))))

(println '(Problem 5: lambda-exp?))
(lambda-exp? 'x)
(lambda-exp? '(lambda (x) x))
(lambda-exp? '(lambda (f) (lambda (x) (f (x x)))))
(lambda-exp? '(lambda (x) (lambda (y) (y x))))
(lambda-exp? '(lambda (z) ((lambda (y) (a z)) (h (lambda (x) (h a))))))
(lambda-exp? '(lambda (lambda) lambda))
(lambda-exp? '((lambda (lambda) lambda) (lambda (y) y)))
(lambda-exp? '((lambda (x) x) (lambda (x) x)))
(lambda-exp? '((lambda (5) x) (lambda (x) x)))
(lambda-exp? '((lambda (x) x) (lambda (x) x) (lambda (x) x)))
(lambda-exp? '((lambda (lambda (x) x) x)  (lambda (x) x)))

; Problem 6
(define var-occurs?
  (λ (var exp)
    (match exp
      [`,y #:when (symbol? y) (eqv? var y)]
      [`(lambda (,x) ,body) #:when (symbol? x) (var-occurs? var body)]
      [`(,rator ,rand) (or (var-occurs? var rator) (var-occurs? var rand))]
      [else #f]
      )))

(println '(Problem 6: var-occurs?))
(var-occurs? 'x 'x)
(var-occurs? 'x '(lambda (x) y))
(var-occurs? 'x '(lambda (y) x))
(var-occurs? 'x '((z y) x))

; Problem 7
(define vars
  (λ (exp)
    (letrec
        ([v
          (λ (exp ls)
            (match exp
               [`,y #:when (symbol? y) (cons y ls)]
               [`(lambda (,x) ,body) #:when (symbol? x) (v body ls)]
               [`(,rator ,rand) (append (v rator ls) (v rand ls))]
               [else '()]
              ))])
      (v exp '()))))

(println '(Problem 7: vars))
(vars 'x)
(vars '(lambda (x) x))
(vars '((lambda (y) (x x)) (x y)))
(vars '(lambda (z) ((lambda (y) (a z)) (h (lambda (x) (h a))))))

; Problem 8
(define unique-vars
  (λ (exp)
    (letrec
        ([w
          (λ (exp ls)
            (match exp
               [`,y #:when (symbol? y) (cons y ls)]
               [`(lambda (,x) ,body) #:when (symbol? x) (union (w x ls) (w body ls))]
               [`(,rator ,rand) (union (w rator ls) (w rand ls))]
               [else '()]
              ))])
      (w exp '()))))


(println '(Problem 7: unique-vars))
(unique-vars '((lambda (y) (x x)) (x y)))
(unique-vars '((lambda (z) (lambda (y) (z y))) x))
(unique-vars '((lambda (a) (a b)) ((lambda (c) (a c)) (b a))))

; Problem 9
(define var-occurs-free?
  (λ (var exp)
    (match exp
      [`,y #:when (symbol? y) (eqv? var y)]
      [`(lambda (,x) ,body) #:when (symbol? x)
                            (and (not (eqv? var x)) (var-occurs-free? var body))]
      [`(,rator ,rand) (or (var-occurs-free? var rator) (var-occurs-free? var rand))]
      [else #f])))

(println '(Problem 9: var-occurs-free?))
(var-occurs-free? 'x 'x)
(var-occurs-free? 'x '(lambda (y) y))
(var-occurs-free? 'x '(lambda (x) (x y)))
(var-occurs-free? 'x '(lambda (x) (lambda (x) x)))
(var-occurs-free? 'y '(lambda (x) (x y)))
(var-occurs-free? 'y '((lambda (y) (x y)) (lambda (x) (x y))))
(var-occurs-free? 'x '((lambda (x) (x x)) (x x)))

; Problem 10
(define var-occurs-bound?
  (λ (var exp)
    (match exp
      [`,y #:when (symbol? y) #f]
      [`(lambda (,x) ,body) #:when (symbol? x)
                            (or (var-occurs-bound? var body)
                                (and (eqv? var x) (var-occurs-free? var body)))]
      [`(,rator ,rand) (or (var-occurs-bound? var rator) (var-occurs-bound? var rand))]
      [else #f])))

(println '(Problem 10: var-occurs-bound?))
(var-occurs-bound? 'x 'x)
(var-occurs-bound? 'x '(lambda (x) x))
(var-occurs-bound? 'y '(lambda (x) x))
(var-occurs-bound? 'x '((lambda (x) (x x)) (x x)))
(var-occurs-bound? 'z '(lambda (y) (lambda (x) (y z))))
(var-occurs-bound? 'z '(lambda (y) (lambda (z) (y z))))
(var-occurs-bound? 'x '(lambda (x) y))
(var-occurs-bound? 'x '(lambda (x) (lambda (x) x)))

; Problem 11
(define unique-free-vars
  (λ (exp)
    (letrec
        ([v
          (λ (vars exp)
            (cond
              [(null? vars) vars]
              [(not (var-occurs-free? (car vars) exp)) (v (cdr vars) exp)]
              [else (cons (car vars) (v (cdr vars) exp))]
            ))])
      (v (unique-vars exp) exp))))

(println '(Problem 11: unique-free-vars))
(unique-free-vars 'x)
(unique-free-vars '(lambda (x) (x y)))
(unique-free-vars '((lambda (x) ((x y) e)) (lambda (c) (x (lambda (x) (x (e c)))))))

; Problem 12
(define unique-bound-vars
  (λ (exp)
    (letrec
        ([v
          (λ (vars exp)
            (cond
              [(null? vars) vars]
              [(not (var-occurs-bound? (car vars) exp)) (v (cdr vars) exp)]
              [else (cons (car vars) (v (cdr vars) exp))]
            ))])
      (v (unique-vars exp) exp))))

(println '(Problem 11: unique-bound-vars))
(unique-bound-vars 'x)
(unique-bound-vars '(lambda (x) y))
(unique-bound-vars '(lambda (x) (x y)))
(unique-bound-vars '((lambda (x) ((x y) e)) (lambda (c) (x (lambda (x) (x (e c)))))))
(unique-bound-vars '(lambda (y) y))
(unique-bound-vars '(lambda (x) (y z)))
(unique-bound-vars '(lambda (x) (lambda (x) x)))