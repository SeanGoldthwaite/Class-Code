#lang racket

; Problem 1
(define countdown
  (λ (n)
    (cond
      [(zero? n) (cons n '())]
      [else (cons n (countdown (sub1 n)))])))

(println "Problem 1: countdown")
(countdown 5)

; Problem 2
(define insertL
  (λ (a b ls)
    (cond
      [(null? ls) '()]
      [(eqv? (car ls) a) (cons b (cons a (insertL a b (cdr ls))))]
      [else (cons (car ls) (insertL a b (cdr ls)))])))

(println "Problem 2: insertL")
(insertL 'x 'y '(x z z x y x))

; Problem 3
(define remv-1st
  (λ (a ls)
    (cond
      [(null? ls) '()]
      [(eqv? (car ls) a) (cdr ls)]
      [else (cons (car ls) (remv-1st a (cdr ls)))])))

(println "Problem 3: remv-lst")
(remv-1st 'x '(x y z x))
(remv-1st 'x '(x y z y x))
(remv-1st 'z '(a b c))

; Problem 4
(define map
  (λ (pro ls)
    (cond
      [(null? ls) '()]
      [else (cons (pro (car ls)) (map pro (cdr ls)))])))

(println "Problem 4: map")
(map sub1 '(1 2 3 4))

; Problem 5
(define filter
  (λ (pred ls)
    (cond
      [(null? ls) '()]
      [(pred (car ls)) (cons (car ls) (filter pred (cdr ls)))]
      [else (filter pred (cdr ls))])))

(println "Problem 5: filter")
(filter even? '(1 2 3 4 5 6))

; Problem 6
(define zip
  (λ (ls1 ls2)
    (cond
      [(or (null? ls1) (null? ls2)) '()]
      [else (cons (cons (car ls1) (car ls2)) (zip (cdr ls1) (cdr ls2)))])))

(println "Problem 6: zip")
(zip '(1 2 3) '(a b c))
(zip '(1 2 3 4 5 6) '(a b c))
(zip '(1 2 3) '(a b c d e f))

; Problem 7
(define list-index-ofv
  (λ (a ls)
    (cond
      [(eqv? a (car ls)) 0]
      [else (+ 1 (list-index-ofv a (cdr ls)))])))

(println "Problem 7: list-index-ofv")
(list-index-ofv 'x '(x y z x x))
(list-index-ofv 'x '(y z x x))


; Problem 8
(define append
  (λ (ls1 ls2)
    (cond
      [(null? ls1) ls2]
      [else (cons (car ls1) (append (cdr ls1) ls2))])))

(println "Problem 8: append")
(append '(42 120) '(1 2 3))
(append '(a b c) '(cat dog))

; Problem 9
(define reverse
  (λ (ls)
    (cond
      [(null? ls) '()]
      [else (append (reverse (cdr ls)) (cons (car ls) '()))])))

(println "Problem 9: reverse")
(reverse '(a 3 x))

; Problem 10
(define repeat
  (λ (ls n)
    (cond
      [(zero? n) null]
      [else (append ls (repeat ls (sub1 n)))])))

(println "Problem 10: repeat")
(repeat '(4 8 11) 4)

; Problem 11
(define same-lists*
  (λ (ls1 ls2)
    (cond
      [(not (eqv? (length ls1) (length ls2))) #f]
      [(and (null? ls1) (null? ls2)) #t]
      [(and (pair? (car ls1)) (pair? (car ls2)))
       (and (same-lists* (car ls1) (car ls2)) (same-lists* (cdr ls1) (cdr ls2)))]
      [(and (eqv? (car ls1) (car ls2))) (same-lists* (cdr ls1) (cdr ls2))]
      [else #f]
      )))

(println "Problem 11: same-lists*")
(same-lists* '() '())
(same-lists* '(1 2 3 4 5) '(1 2 3 4 5))
(same-lists* '(1 2 3 4) '(1 2 3 4 5))
(same-lists* '(a (b c) d) '(a (b) c d))
(same-lists* '((a) b (c d) d) '((a) b (c d) d))

; Problem 12
; '((w . (x . ())) y . ((z . ())))
(println "Problem 12: adding dots")
(println '((w . (x . ())) y . ((z . ()))))
(equal? '((w x) y (z)) '((w . (x . ())) y . ((z . ()))))


; Problem 13
; If the bits were in regular order this would work
#;(define binary->natural
  (λ (ls)
    (cond
      [(null? ls) 0]
      [else (+ (* (expt 2 (sub1 (length ls))) (car ls)) (binary->natural (cdr ls)))])))

; Reverse order can't be recursive because there's no way to keep track of which place a digit is in
; without passing an extra value to the function
; This seems like really bad code but I've never used Racket/Scheme/Lisp before so I don't know the right way to do this  
(define binary->natural
  (λ (ls)
    (let ((i 0)
          (x 0))
    (for ([val ls])
      (set! x (+ x (* val (expt 2 i))))
      (set! i (add1 i)))
    x)))
                    
(println "Problem 13: binary->natural")
(binary->natural `())
(binary->natural `(0 0 1))
(binary->natural `(0 0 1 1))
(binary->natural `(1 1 1 1))
(binary->natural `(1 0 1 0 1))
(binary->natural `(1 1 1 1 1 1 1 1 1 1 1 1 1))

; Problem 14
(define div
  (λ (a b)
    (cond
      [(zero? a) 0]
      [else (+ 1 (div (- a b) b))])))

(println "Problem 14: div")
(div 30 5)

; Problem 15
(define append-map
  (λ (p ls)
    (cond
      [(null? ls) `()]
      [else (append (p (car ls)) (append-map p (cdr ls)))])))

(println "Problem 15: append-map")
(append-map countdown (countdown 5))

; Problem 16
(define set-difference
  (λ (s1 s2)
    (cond
      [(null? s1) '()])))

(println "Problem 16: set-difference")


; Problem 17
(define foldr
  (λ (func acc ls)
    (cond
      [(null? ls) acc]
      [else (foldr func (func acc (car ls)) (cdr ls))])))

(println "Problem 17: foldr")
(foldr cons '() '(1 2 3 4))
(foldr + 0 '(1 2 3 4))
(foldr * 1 '(1 2 3 4))