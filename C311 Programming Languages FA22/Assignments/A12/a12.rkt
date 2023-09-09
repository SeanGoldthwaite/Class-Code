#lang racket
(require "mk.rkt")
(require "numbers.rkt")
(require "monads.rkt")

; Part I
(printf "Part I - miniKanren\n")

; Problem 1
(defrel (listo ls)
  (conde
   [(== `() ls)]
   [(fresh (a d)
           (== `(,a . ,d) ls)
           (listo d))]))

(printf "\nProblem 1: listo\n")
(run 1 q (listo '(a b c d e)))
(run 1 q (listo '(a b c d . e)))
(run 4 q (listo q))
(run 4 q (listo `(a b ,q)))


; Problem 2
(defrel (facto ls o)
  (conde
   [(zeroo ls)
    (== '(1) o)]
   [(fresh (sub1-n fact-sub1)
           (*o fact-sub1 ls o)
           (minuso ls '(1) sub1-n)
           (facto sub1-n fact-sub1))]))

(printf "\nProblem 2: facto\n") 
(run 1 q (facto  q '(0 0 0 1 1 1 1)))
(run 1 q (facto (build-num 5) q))
(run 5 q (fresh (n1 n2) (facto n1 n2) (== `(,n1 ,n2) q)))


; Part II
(printf "\nPart II - Monads\n")

; Problem 1
(define findf-maybe
  (λ (pred ls)
    (cond
      [(null? ls)
       (Nothing)]
      [(pred (car ls))
       (Just (car ls))]
      [else
       (findf-maybe pred (cdr ls))])))

(printf "\nProblem 1: find-maybe\n")
(findf-maybe symbol? '(1 2 c))
(findf-maybe boolean? '(#f 1 2 c))
(findf-maybe number? '(a b c))

 ; Problem 2
(define partition-writer
  (λ (pred ls)
    (cond
      [(null? ls) (inj-writer '())]
      [(pred (car ls))
       (bind-writer (partition-writer pred (cdr ls))
                    (λ (d)
                      (inj-writer (cons (car ls) d))))]
      [else
       (bind-writer (tell (car ls))
                    (λ (_)
                      (partition-writer pred (cdr ls))))])))

(printf "\nProblem 2: partition-writer\n")
(run-writer (partition-writer even? '(1 2 3 4 5 6 7 8 9 10)))
(run-writer (partition-writer odd? '(1 2 3 4 5 6 7 8 9 10)))


; Problem 3
(define powerXpartials
  (λ (base exp)
    (cond
      [(zero? exp)
       (inj-writer 1)]
      [(zero? (sub1 exp))
       (inj-writer base)]
      [(odd? exp)
       (go-on
        (res <- (powerXpartials base (sub1 exp)))
        (tell res)
        (inj-writer (* base res)))]
      [else
       (go-on
        (res <- (powerXpartials base (/ exp 2)))
        (tell res)
        (inj-writer (* res res)))])))

(printf "\nProblem 3: powerXpartials\n")
(run-writer (powerXpartials 2 0))
(run-writer (powerXpartials 3 1))
(run-writer (powerXpartials 2 3))
(run-writer (powerXpartials 3 5))
(run-writer (powerXpartials 5 7))


; Problem 4
(define replace-with-count
  (λ (x tr)
    (cond
      [(null? tr)
       (inj-state '())]
      [(symbol? tr)
       (if (eqv? x tr)
           (go-on
            (store <- (get))
            (put (add1 store))
            (inj-state store))
           (go-on
            (inj-state tr)))]
      [(pair? tr)
       (go-on
        (a <- (replace-with-count x (car tr)))
        (d <- (replace-with-count x (cdr tr)))
        (inj-state (cons a d)))])))

(printf "\nProblem 4: replace-with-count\n")
((run-state (replace-with-count 'o '(a o (t o (e o t ((n . m) . o) . f) . t) . r))) 0)
((run-state (replace-with-count 'o '(((h (i s . o) . a) o s o e . n) . m))) 0)
((run-state (replace-with-count 'o '(o (h (o s . o) . o) . o))) 1)

(define traverse
    (lambda (inj bind f)
    (letrec
        ((trav
        (lambda (tree)
            (cond
            [(pair? tree)
                (go-on (a <- (trav (car tree)))
                    (d <- (trav (cdr tree)))
                    (inj (cons a d)))]
            [else (f tree)]))))
        trav)))

; Problem 5
(define reciprocal
  (λ (n)
    (cond
      [(zero? n)
       (Nothing)]
      [else
       (Just (/ 1 n))])))

(printf "\nProblem 5: reciprocal\n")
(reciprocal 0)
(reciprocal 2)

(define traverse-reciprocal
  (traverse Just bind-maybe reciprocal))

(traverse-reciprocal '((1 . 2) . (3 . (4 . 5))))
(traverse-reciprocal '((1 . 2) . (0 . (4 . 5))))


; Problem 6
(define halve
  (λ (n)
    (if (even? n)
        (inj-writer (/ n 2))
        (bind-writer (tell n)
                     (λ (_)
                       (inj-writer n))))))

(printf "\nProblem 6: halve\n")
(run-writer (halve 6))
(run-writer (halve 5))

(define traverse-halve
  (traverse inj-writer bind-writer halve))

(run-writer (traverse-halve '((1 . 2) . (3 . (4 . 5)))))

; Problem 7
(define state/sum
  (λ (n)
    (go-on
     (state <- (get))
     (put (+ state n))
     (inj-state state))))

(printf "\nProblem 7: state/sum")
((run-state (state/sum 5)) 0)
((run-state (state/sum 2)) 0)
((run-state (state/sum 2)) 3)

(define traverse-state/sum
  (traverse inj-state bind-state state/sum))

((run-state (traverse-state/sum '((1 . 2) . (3 . (4 . 5))))) 0)