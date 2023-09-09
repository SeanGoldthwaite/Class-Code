#lang racket
(require racket/trace)

(define empty-k
  (lambda ()
    (let ((once-only #f))
      (lambda (v)
        (if
         once-only
         (error 'empty-k "You can only invoke the empty continuation once")
         (begin (set! once-only #t) v))))))

; Problem 1: binary-to-decimal
(define binary-to-decimal
  (lambda (n)
    (cond
      [(null? n) 0]
      [else (+ (car n) (* 2 (binary-to-decimal (cdr n))))])))

(define binary-to-decimal-cps
  (lambda (n k)
    (cond
      [(null? n) (k 0)]
      [else (binary-to-decimal-cps (cdr n) (λ (v)
                                             (k (+ (car n) (* 2 v)))))])))

(println "Problem 1: binary-to-decimal")
(binary-to-decimal '())
(binary-to-decimal '(1))
(binary-to-decimal '(0 1))
(binary-to-decimal '(1 1 0 1))
;(trace binary-to-decimal-cps)
(binary-to-decimal-cps '() (empty-k))
(binary-to-decimal-cps '(1) (empty-k))
(binary-to-decimal-cps '(0 1) (empty-k))
(binary-to-decimal-cps '(1 1 0 1) (empty-k))


; Problem 2: star
(define star
  (lambda (m)
    (lambda (n)
      (* m n))))

(define star-cps
  (lambda (m)
    (lambda (n)
      (lambda (k)
        (k (* m n))))))

(println "Problem 2: star")
((star 2) 3)
((star ((star 2) 3)) 5)
;(trace star-cps)
(((star-cps 2) 3) (empty-k))
(((star-cps (((star-cps 2) 3) (empty-k))) 5) (empty-k))


; Problem 3: times
(define times
  (lambda (ls)
    (cond
      [(null? ls) 1]
      [(zero? (car ls)) 0]
      [else (* (car ls) (times (cdr ls)))])))

(define times-cps
  (lambda (ls k)
    (cond
      [(null? ls) (k 1)]
      [(zero? (car ls)) (k 0)]
      [else (times-cps (cdr ls) (λ (v)
                                  (k (* (car ls) v))))])))

(println "Problem 3: times")

(times '(1 2 3 4 5))
(times '(1 2 3 0 3))
;(trace times-cps)
(times-cps '(1 2 3 4 5) (empty-k))
(times-cps '(1 2 3 0 3) (empty-k))


; Problem 4: times-cps-shortcut
(define times-cps-shortcut
  (lambda (ls k)
    (cond
      [(null? ls) (k 1)]
      [(zero? (car ls)) 0]
      [else (times-cps-shortcut (cdr ls) (λ (v)
                                  (k (* (car ls) v))))])))

(println "Problem 4: times-cps-shortcut")
(times-cps-shortcut '(1 2 3 4 5) (empty-k))
(times-cps-shortcut '(1 2 3 0 3) (empty-k))


; Problem 5: remv-first-9*
(define remv-first-9*
  (lambda (ls)
    (cond
      [(null? ls) '()]
      [(pair? (car ls))
       (cond
         [(equal? (car ls) (remv-first-9* (car ls)))
          (cons (car ls) (remv-first-9* (cdr ls)))]
         [else (cons (remv-first-9* (car ls)) (cdr ls))])]
      [(eqv? (car ls) '9) (cdr ls)]
      [else (cons (car ls) (remv-first-9* (cdr ls)))])))

(define remv-first-9*-cps
  (lambda (ls k)
    (cond
      [(null? ls) (k '())]
      [(pair? (car ls))
       (remv-first-9*-cps (car ls)
                          (λ (v)
                            (cond
                              [(eqv? (car ls) v)
                               (remv-first-9*-cps (cdr ls)
                                                  (λ (w)
                                                    (k (cons (car ls) w))))]
                              [else
                               (k (cons v (cdr ls)))])))]
      [(eqv? (car ls) '9) (k (cdr ls))]
      [else (remv-first-9*-cps (cdr ls)
                               (λ (v)
                                 (k (cons (car ls) v))))])))

(println "Problem 5: remv-first-9*")
(remv-first-9* '((1 2 (3) 9)))
(remv-first-9* '(9 (9 (9 (9)))))
(remv-first-9* '(((((9) 9) 9) 9) 9))
;(trace remv-first-9*-cps)
(remv-first-9*-cps '((1 2 (3) 9)) (empty-k))
(remv-first-9*-cps '(9 (9 (9 (9)))) (empty-k))
(remv-first-9*-cps '(((((9) 9) 9) 9) 9) (empty-k))


; Problem 6: cell-count
(define cons-cell-count
  (lambda (ls)
    (cond
      [(pair? ls)
       (add1 (+ (cons-cell-count (car ls))
                (cons-cell-count (cdr ls))))]
      [else 0])))

(define cons-cell-count-cps
  (lambda (ls k)
    (cond
      [(pair? ls)
       (cons-cell-count-cps (car ls)
                            (λ (v)
                              (cons-cell-count-cps (cdr ls)
                                                   (λ (w)
                                                     (k (add1 (+ v w)))))))]
      [else (k 0)])))

(println "Problem 6: cell-count")
(cons-cell-count '(1 2 3 4))
(cons-cell-count '(1 2 (3 (4) 5) 4 ()))
(cons-cell-count-cps '(1 2 3 4) (empty-k))
(cons-cell-count-cps '(1 2 (3 (4) 5) 4 ()) (empty-k))


; Problem 7: find
(define find 
  (lambda (u s)
    (let ((pr (assv u s)))
      (if pr (find (cdr pr) s) u))))

(define find-cps 
  (lambda (u s k)
    (let ([pr (assv u s)])
      (if pr
          (find-cps (cdr pr) s (λ (v) (k v)))
          (k u)))))

(println "Problem 7: find")
(find 5 '((5 . a) (6 . b) (7 . c)))
(find 7 '((5 . a) (6 . 5) (7 . 6)))
(find 5 '((5 . 6) (9 . 6) (2 . 9)))
(find-cps 5 '((5 . a) (6 . b) (7 . c)) (empty-k))
(find-cps 7 '((5 . a) (6 . 5) (7 . 6)) (empty-k))
(find-cps 5 '((5 . 6) (9 . 6) (2 . 9)) (empty-k))


; Problem 8: ack
(define ack
  (lambda (m n)
    (cond
      [(zero? m) (add1 n)]
      [(zero? n) (ack (sub1 m) 1)]
      [else (ack (sub1 m)
                 (ack m (sub1 n)))])))

(define ack-cps
  (lambda (m n k)
    (cond
      [(zero? m) (k (add1 n))]
      [(zero? n) (ack-cps (sub1 m)
                          1
                          (λ (v) (k v)))]
      [else (ack-cps m
                     (sub1 n)
                     (λ (v)
                       (ack-cps (sub1 m)
                                v
                                (λ (w)
                                  (k w)))))])))

(println "Problem 8: ack")
(ack 1 1)
(ack 2 2)
(ack 3 2)
(ack 3 3)
(ack 3 4)
(ack-cps 1 1 (empty-k))
(ack-cps 2 2 (empty-k))
(ack-cps 3 2 (empty-k))
(ack-cps 3 3 (empty-k))
(ack-cps 3 4 (empty-k))


; Problem 9: fib
(define fib
  (lambda (n)
    ((lambda (fib)
       (fib fib n))
     (lambda (fib n)
       (cond
        [(zero? n) 0]
        [(zero? (sub1 n)) 1]
        [else (+ (fib fib (sub1 n)) (fib fib (sub1 (sub1 n))))])))))

(define fib-cps
  (lambda (n k)
    ((lambda (fib-cps)
       (fib-cps fib-cps n k))
     (lambda (fib-cps n k^)
       (cond
        [(zero? n) (k^ 0)]
        [(zero? (sub1 n)) (k^ 1)]
        [else (fib-cps fib-cps
                       (sub1 n)
                       (λ (fib-sub1-v)
                         (fib-cps fib-cps
                                  (sub1 (sub1 n))
                                  (λ (fib-sub2-v)
                                    (k^ (+ fib-sub1-v fib-sub2-v))))))])))))

(println "Problem 9: fib")
(fib 8)
(fib 10)
(fib 12)
(fib-cps 8 (empty-k))
(fib-cps 10 (empty-k))
(fib-cps 12 (empty-k))


; Problem 10: unfold
(define unfold
  (lambda (p f g seed)
    ((lambda (h)
       ((h h) seed '()))
     (lambda (h)
       (lambda (seed ans)
         (if (p seed)
             ans
             ((h h) (g seed) (cons (f seed) ans))))))))

; I don't even know whats going on in the normal function

(println "Problem 10: unfold")
(unfold null? car cdr '(a b c d e))


; Problem 11: unify
(define unify
  (lambda (u v s)
    (cond
      [(eqv? u v) s]
      [(number? u) (cons (cons u v) s)]
      [(number? v) (unify v u s)]
      [(pair? u)
       (if
        (pair? v)
        (let ((s (unify (find (car u) s) (find (car v) s) s)))
             (if s
                 (unify (find (cdr u) s) (find (cdr v) s) s)
                 #f))
        #f)]
      [else #f])))

(define unify-cps
  (lambda (u v s k)
    (cond
      [(eqv? u v) (k s)]
      [(number? u) (k (cons (cons u v) s))]
      [(number? v) (unify-cps v u s k)]
      [(pair? u)
       (if
        (pair? v)
        (unify-cps (find (car u) s)
                   (find (car v) s)
                   s
                   (λ (w)
                     (if w
                         (unify-cps (find (cdr u) w)
                                    (find (cdr v) w)
                                    w
                                    k)
                         #f)))
        #f)]
      [else #f])))

(define empty-s
  (lambda ()
    '()))

(println "Problem 11: unify")
(unify 'x 5 (empty-s))
(unify 'x 5 (unify 'y 6 (empty-s)))
(unify '(x y) '(5 6) (empty-s))
(unify 'x 5 (unify 'x 6 (empty-s)))
(unify '(x x) '(5 6) (empty-s))
(unify '(1 2 3) '(x 1 2) (empty-s))
(unify 'x 'y (empty-s))
;(trace unify-cps)
(unify-cps 'x 5 (empty-s) (empty-k))
(unify-cps 'x 5 (unify-cps 'y 6 (empty-s) (empty-k)) (empty-k))
(unify-cps '(x y) '(5 6) (empty-s) (empty-k))
(unify-cps 'x 5 (unify-cps 'x 6 (empty-s) (empty-k)) (empty-k))
(unify-cps '(x x) '(5 6) (empty-s) (empty-k))
(unify-cps '(1 2 3) '(x 1 2) (empty-s) (empty-k))
(unify-cps 'x 'y (empty-s) (empty-k))


; Problem 12: M
(define M
  (lambda (f)
    (lambda (ls)
      (cond
        ((null? ls) '())
        (else (cons (f (car ls)) ((M f) (cdr ls))))))))

(define M-cps
  (lambda (f)
    (lambda (ls)
      (lambda (k)
        (cond
          [(null? ls) (k '())]
          [else (((M-cps f)
                  (cdr ls))
                 (λ (v)
                   (k (cons (f (car ls))
                            v))))])))))

(println "Problem 12: M")
((M (lambda (n) (add1 n))) '(1 2 3 4 5))
(((M-cps (lambda (n) (add1 n))) '(1 2 3 4 5)) (empty-k))


; Problem 13: use-of-M
(define use-of-M
  ((M (lambda (n) (add1 n))) '(1 2 3 4 5)))

(define use-of-M-cps
  (((M-cps (lambda (n) (add1 n))) '(1 2 3 4 5)) (empty-k)))

(println "Problem 13: use-of-M")
use-of-M
use-of-M-cps