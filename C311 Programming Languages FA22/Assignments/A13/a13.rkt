#lang pie

(claim intriguing-word Atom)
(define intriguing-word 'left)
intriguing-word

(claim lucky-num Nat)
(define lucky-num 55)
lucky-num

(claim to-go-order (Pair Nat Atom))
(define to-go-order (cons 56 'right))
to-go-order

(claim MyFirstType U)
(define MyFirstType Nat)
MyFirstType

(claim my-thing-and-Atom (Pair MyFirstType U))
(define my-thing-and-Atom (cons 5 Nat))
my-thing-and-Atom

; Problem 6: with-nats
(claim with-Nats
  (-> (-> Nat Nat
        Nat)
      (Pair Nat Nat)
    Nat))
(define with-Nats
  (λ (func p)
    (func (car p) (cdr p))))

(check-same Nat (with-Nats (λ (n m) n) (cons 1 2)) 1)
(check-same Nat (with-Nats (λ (n m) (add1 m)) (cons 1 2)) 3)

; Problem 7: at-least-two?
(claim at-least-two?
  (-> Nat
    Atom))
(define at-least-two?
  (λ (n)
    (which-Nat n 'nil (λ (a)
                        (which-Nat a 'nil (λ (b)
                                            't))))))

(check-same Atom (at-least-two? 0) 'nil)
(check-same Atom (at-least-two? 1) 'nil)
(check-same Atom (at-least-two? 41) 't)


; Problem 8: expt
(claim + (-> Nat Nat
           Nat))
(define +
  (λ (n m)
    (rec-Nat n m (λ (k k+m)
                   (add1 k+m)))))
 
(claim * (-> Nat Nat
           Nat))
(define *
  (λ (n m)
    (rec-Nat n 0 (λ (k k*m)
                   (+ m k*m)))))

(claim expt (-> Nat Nat
              Nat))
(define expt
  (λ (n m)
    (rec-Nat m 1 (λ (k k**m)
                   (* n k**m)))))

(expt 2 3)
(expt 3 2)


; Problem 9: map
(claim map
  (Π ((A U)
      (B U))
    (→ (→ A B) (List A)
       (List B))))
(define map
  (λ (A B func ls)
    (rec-List ls (the (List B) nil) (λ (car-ls cdr-ls res-ls)
                                      (:: (func car-ls) res-ls)))))

(map Nat Nat (λ (n) (add1 n)) (:: 1
                                (:: 2
                                  (:: 3
                                    (:: 4
                                      (:: 5 nil))))))


; Problem 10: nth

; Can't figure out how to get the types to line up

;(claim nth-cdr
;  (Π ([A U])
;    (-> (List A) Nat
;        (List A))))
;(define nth-cdr
;  (λ (A)
;    (λ (ls n)
;      (rec-List ls (the (List A) nil) (λ (car-ls cdr-ls res-ls)
;                                        cdr-ls)))))
;
;(claim nth
;  (Π ((A U))
;    (→ (List A) A Nat
;       A)))
;(define nth
;  (λ (A)
;    (λ (ls a n)
;      (rec-List ls a (λ (car-ls cdr-ls res-ls)
;                       res-ls)))))
;
;(nth-cdr Atom (:: 'one
;                (:: 'two
;                  (:: 'three
;                    (:: 'four
;                      (:: 'five nil)))))
;  3)


; Problem 11: vec-second
(claim vec-second
  (Π ([A U]
      [len Nat])
    (-> (Vec A (add1 (add1 len)))
        A)))
(define vec-second
  (λ (A len vec)
    (head (tail vec))))

(claim rugbrod-vec
  (Vec Atom 5))
(define rugbrod-vec
  (vec:: 'rye-flour
    (vec:: 'rye-kernel
      (vec:: 'water
        (vec:: 'sourdough
          (vec:: 'salt vecnil))))))

(vec-second Atom 3 rugbrod-vec)