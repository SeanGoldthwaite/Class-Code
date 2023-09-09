#lang racket
(require "mk.rkt")

(defrel (∈ Γ x τ)
  (fresh (xa τa restΓ)
         (== `((,xa . ,τa) . ,restΓ) Γ)
         (conde
          [(==  x xa) (== τa τ)]
          [(=/= x xa) (∈ restΓ x τ)])))

(defrel (!- Γ e τ)
  (conde
   [(numbero e) (== 'Nat τ)]
   [(conde
     [(== #t e)]
     [(== #f e)])
    (== 'Bool τ)]
   [(fresh (e1 e2)
           (== `(* ,e1 ,e2) e)
           (== 'Nat τ)
           (!- Γ e1 'Nat)
           (!- Γ e2 'Nat))]
   [(fresh (e1 e2)
           (== `(+ ,e1 ,e2) e)
           (== 'Nat τ)
           (!- Γ e1 'Nat)
           (!- Γ e2 'Nat))]
   [(fresh (test conseq alt)
           (== `(if ,test ,conseq ,alt) e)
           (!- Γ test 'Bool)
           (!- Γ conseq τ)
           (!- Γ alt τ))]
   [(fresh (e1)
           (== `(zero? ,e1) e)
           (== 'Bool τ)
           (!- Γ e1 'Nat))]
   [(fresh (e1 e2)
           (== `(and ,e1 ,e2) e)
           (== 'Bool τ)
           (!- Γ e1 'Bool)
           (!- Γ e2 'Bool))]
   [(fresh (e1)
           (== `(not ,e1) e)
           (== 'Bool τ)
           (!- Γ e1 'Bool))]
   [(fresh (e1)
           (== `(sub1 ,e1) e)
           (== 'Nat τ)
           (!- Γ e1 'Nat))]
   [(fresh (x body xτ bodyτ)
           (symbolo x)
           (== `(lambda (,x) ,body) e)
           (!- `((,x . ,xτ) . ,Γ) body bodyτ)
           (== `(→ ,xτ ,bodyτ) τ))]
   [(fresh (rator rand τa τb)
           (== `(,rator ,rand) e)
           (== τb τ)
           (!- Γ rand τa)
           (!- Γ rator `(→ ,τa ,τb)))]
   ; Fix wasn't explained well enough to figure this out
   [(fresh (e1 t)
           (== `(fix ,e1) e))]
   [(fresh ()
           (symbolo e)
           (∈ Γ e τ))]))

(define fix
  (lambda (e)
    (e (lambda (z) ((fix e) z)))))

(run* q (!- '() #t q)) ; Bool
(run* q (!- '() 17 q)) ; Nat
(run* q (!- '() '(zero? 24) q)) ; Bool
(run* q (!- '() '(zero? (sub1 24)) q)) ; Bool
(run* q (!- '() '(not (zero? (sub1 24))) q)) ; Bool
(run* q
      (!- '() '(zero? (sub1 (sub1 18))) q)) ; Bool
(run* q
      (!- '()  '(lambda (n) (if (zero? n) n n)) q)) ; → Nat Nat
(run* q
      (!- '() '(lambda (n)
                 (lambda (b)
                   (if (and (not b) (zero? n))
                       n n))) q)) ; (→ Nat (→ Bool Nat))
(run* q
      (!- '() '((lambda (n) (zero? n)) 5) q)) ; Bool
(run* q
      (!- '() '(if (zero? 24) 3 4) q)) ; Nat
(run* q
      (!- '() '(if (zero? 24) (zero? 3) (zero? 4)) q)) ; Bool
(run* q
      (!- '() '(lambda (x) (sub1 x)) q)) ; → Nat Nat
(run* q (!- '() (and (zero? 5) (not #t)) q)) ; Bool
(run* q (!- '() (or #f (not #t)) q)) ; Bool
(run* q
      (!- '() '(lambda (a) (lambda (x) (+ a x))) q)) ; (→ Nat (→ Nat Nat))
(run* q
      (!- '() '(lambda (f)
                 (lambda (x)
                   ((f x) x)))
          q)) ; (→ (_0 → (_0 → _1)) → (_0 → _1))
(run* q
    (!- '() '(sub1 (sub1 (sub1 6))) q)) ; Nat
(run 1 q
    (fresh (t)
      (!- '() '(lambda (f) (f f)) t))) ; ()
(length (car (run 20 (q)
             (fresh (lam a b)
               (!- '() `((,lam (,a) ,b) 5) 'Nat)
               (== `(,lam (,a) ,b) q)))))
(length (car (run 30 q (!- '() q 'Nat))))
(length (car (run 30 q (!- '() q '(Nat -> Nat)))))
(length (car (run 500 q (!- '() q '(Nat -> Nat)))))
(length (car (run 30 q (!- '() q '(Bool -> Nat)))))
(length (car (run 100 q
             (fresh (e t)
               (!- '() e t)
               (== `(,e ,t) q)))))
(length (car (run 100 q
             (fresh (g e t)
               (!- g e t)
               (== `(,g ,e ,t) q)))))
(length
   (car (run 100 q
     (fresh (g v)
       (!- g `(var ,v) 'Nat)
       (== `(,g ,v) q)))))
(run 1 q
       (fresh (g)
	 (!- g
	      '((fix (lambda (!)
		       (lambda (n)
			 (if (zero? n)
			     1
			     (* n (! (sub1 n)))))))
		5)
	      q)))
(run 1 q
       (fresh (g)
	 (!- g
	      '((fix (lambda (!)
		       (lambda (n)
			 (* n (! (sub1 n))))))
		5)
	      q)))