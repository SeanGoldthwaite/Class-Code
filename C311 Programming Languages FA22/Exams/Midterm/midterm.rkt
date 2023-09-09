#lang racket

; Problem 1
; Which ones need at least one letrec
; 1a
(letrec ([f (lambda (n)
           (if (zero? n)
               1
               (* n (f (sub1 n)))))])
  (f 5))
; Yes, f is referenced in the definition of f

; 1b
(let ([f (lambda (f)
           (lambda (n)
             (if (zero? n)
                 1
                 (* n ((f f) (sub1 n))))))])
  ((f f) 5))
; Doesn't for some reason?
; Double reference?

; 1c
(letrec ([ is-even? (lambda (n)
                   (if (zero? n)
                       #t
                       (is-odd? (sub1 n))))]
      [is-odd? (lambda (n)
                 (if (zero? n)
                     #f
                     (is-even? (sub1 n))))])
  (is-odd? 5))
; Yes, 

; Problem 3
(define take-while
  (λ (pred ls)
    (cond
      [(null? ls) ls]
      [(pred (car ls))
       (cons (car ls) (take-while pred (cdr ls)))]
      [else '()])))

(take-while (lambda (x) (< x 5)) '(3 4 5 4 3))
(take-while odd? '(1 3 5 4 3))
(take-while odd? '(1 3 5 1 3))
(take-while (lambda (x) (or (eqv? x 'a) (eqv? x 'b))) '(a b b a d a b c))

(define take-while-cps
  (λ (pred ls k)
    (cond
      [(null? ls) (k ls)]
      [(pred (car ls))
       (take-while-cps pred
                       (cdr ls)
                       (λ (v)
                         (k (cons (car ls) v))))]
      [else (k '())])))

(take-while-cps (lambda (x) (< x 5)) '(3 4 5 4 3) (λ (v) v))
(take-while-cps odd? '(1 3 5 4 3) (λ (v) v))
(take-while-cps odd? '(1 3 5 1 3) (λ (v) v))
(take-while-cps (lambda (x) (or (eqv? x 'a) (eqv? x 'b))) '(a b b a d a b c) (λ (v) v))

(define drop
  (λ (n ls)
    (cond
      [(null? ls) `()]
      [(= 0 n) ls]
      [else
       (drop (sub1 n) (cdr ls))])))

(drop 0 '(3 4 5 4 3))
(drop 10 '(1 3 5 4 3))
(drop 5 '(a b b a d a b c))

(define drop-cps
  (λ (n ls k)
    (cond
      [(null? ls) (k `())]
      [(= 0 n) (k ls)]
      [else
       (drop-cps (sub1 n)
                 (cdr ls)
                 k)])))

(drop-cps 0 '(3 4 5 4 3) (λ (v) v))
(drop-cps 10 '(1 3 5 4 3) (λ (v) v))
(drop-cps 5 '(a b b a d a b c) (λ (v) v))

; Problem 8
; How many cons?
; 8a
; goal: '(() a (b c) ())

(cons 'a (cons (cons 'b (cons 'c '())) '()))