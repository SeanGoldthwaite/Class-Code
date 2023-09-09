#lang racket
(require "parenthec.rkt")

(define-registers *exp* *env-cps* *k* *y* *rator* *arg* *v*)
(define-program-counter *pc*)

(define-union expr
  (const cexp)
  (var n)
  (sub1 nexp)
  (zero nexp)
  (mult nexp1 nexp2)
  (if test conseq alt)
  (letcc body)
  (throw kexp vexp)
  (let exp body)              
  (lambda body)
  (app rator rand))

(define-label value-of-cps
    (union-case *exp* expr
      [(const num)
       (begin
         (set! *k* *k*)
         (set! *v* num)
         (set! *pc* apply-k))]
      [(var y)
       (begin
         (set! *k* *k*)
         (set! *env-cps* *env-cps*)
         (set! *y* y)
         (set! *pc* apply-env))]
      [(sub1 x)
       (begin
         (set! *k* (make-sub1-k *k*))
         (set! *exp* x)
         (set! *env-cps* *env-cps*)
         (set! *pc* value-of-cps))]
      [(zero x)
       (begin
         (set! *k* (make-zero-k *k*))
         (set! *exp* x)
         (set! *env-cps* *env-cps*)
         (set! *pc* value-of-cps))]
      [(mult x1 x2)
       (begin
         (set! *k* (make-mult-outer-k x2 *env-cps* *k*))
         (set! *exp* x1)
         (set! *env-cps* *env-cps*)
         (set! *pc* value-of-cps))]
      [(if test conseq alt)
       (begin
         (set! *k* (make-if-k conseq alt *env-cps* *k*))
         (set! *exp* test)
         (set! *env-cps* *env-cps*)
         (set! *pc* value-of-cps))]
      [(letcc body)
       (begin
         (set! *exp* body)
         (set! *env-cps* (extend-env *env-cps* *k*))
         (set! *k* *k*)
         (set! *pc* value-of-cps))]
      [(throw k-exp v-exp)
       (begin
         (set! *k* (make-throw-k v-exp *env-cps*))
         (set! *exp* k-exp)
         (set! *env-cps* *env-cps*)
         (set! *pc* value-of-cps))]
      [(let e body)
       (begin
         (set! *k* (make-let-k body *env-cps* *k*))
         (set! *exp* e)
         (set! *env-cps* *env-cps*)
         (set! *pc* value-of-cps))]
      [(lambda body)
       (begin
         (set! *v* (make-clos body *env-cps* *k*))
         (set! *k* *k*)
         (set! *pc* apply-k))]
      [(app rator rand)
       (begin
         (set! *k* (make-rator-k rand *env-cps* *k*))
         (set! *exp* rator)
         (set! *env-cps* *env-cps*)
         (set! *pc* value-of-cps))]))

(define-union envr
  (empty-env)
  (extend-env env-cps^ k))
 
(define-label apply-env
    (union-case *env-cps* envr
      [(empty-env)
       (error 'value-of "unbound identifier")]
      [(extend-env env-cps^ k)
       (if (zero? *y*)
           (begin
             (set! *k* *k*)
             (set! *v* k)
             (set! *pc* apply-k))
           (begin
             (set! *k* *k*)
             (set! *env-cps* env-cps^)
             (set! *y* (sub1 *y*))
             (set! *pc* apply-env)))]))

(define empty-env
  (lambda ()
    (envr_empty-env)))

(define extend-env
  (λ (env-cps^ k^)
    (envr_extend-env env-cps^ k^)))

(define-union clos
  (make body env-cps k))

(define-label apply-clos
    (union-case *rator* clos
      [(make body env-cps k^)
       (begin
         (set! *k* *k*)
         (set! *exp* body)
         (set! *env-cps* (extend-env env-cps *arg*))
         (set! *pc* value-of-cps))]))

(define make-clos
  (λ (body env-cps k)
    (clos_make body env-cps k)))

(define-union kt
  (empty-k jumpout)
  (make-sub1-k k)
  (make-zero-k k)
  (make-throw-k v-exp env-cps)
  (make-let-k body env-cps k)
  (make-rand-k v-rator k)
  (make-rator-k rand env-cps k)
  (make-mult-inner-k w k)
  (make-mult-outer-k x2 env-cps k)
  (make-if-k conseq alt env-cps k))

(define-label apply-k
    (union-case *k* kt
      [(empty-k jumpout)
       (dismount-trampoline jumpout)]
      [(make-sub1-k k^)
       (begin
         (set! *k* k^)
         (set! *v* (sub1 *v*))
         (set! *pc* apply-k))]
      [(make-zero-k k^)
       (begin
         (set! *k* k^)
         (set! *v* (zero? *v*))
         (set! *pc* apply-k))]
      [(make-throw-k v-exp^ env-cps^)
       (begin
         (set! *k* *v*)
         (set! *exp* v-exp^)
         (set! *env-cps* env-cps^)
         (set! *pc* value-of-cps))]
      [(make-rand-k v-rator^ k^)
       (begin
         (set! *k* k^)
         (set! *rator* v-rator^)
         (set! *arg* *v*)
         (set! *pc* apply-clos))]
      [(make-rator-k rand^ env-cps^ k^)
       (begin
         (set! *k* (make-rand-k *v* k^))
         (set! *exp* rand^)
         (set! *env-cps* env-cps^)
         (set! *pc* value-of-cps))]
      [(make-mult-inner-k w^ k^)
       (begin
         (set! *k* k^)
         (set! *v* (* *v* w^))
         (set! *pc* apply-k))]
      [(make-mult-outer-k x2^ env-cps^ k^)
       (begin
         (set! *k* (make-mult-inner-k *v* k^))
         (set! *exp* x2^)
         (set! *env-cps* env-cps^)
         (set! *pc* value-of-cps))]
      [(make-let-k body^ env-cps^ k^)
       (begin
         (set! *exp* body^)
         (set! *env-cps* (extend-env env-cps^ *v*))
         (set! *k* k^)
         (set! *pc* value-of-cps))]
      [(make-if-k conseq^ alt^ env-cps^ k^)
       (if *v*
           (begin
             (set! *k* k^)
             (set! *exp* conseq^)
             (set! *env-cps* env-cps^)
             (set! *pc* value-of-cps))
           (begin
             (set! *k* k^)
             (set! *exp* alt^)
             (set! *env-cps* env-cps^)
             (set! *pc* value-of-cps)))]))

(define empty-k
  (λ ()
    (kt_empty-k)))

(define make-sub1-k
  (λ (k^)
    (kt_make-sub1-k k^)))

(define make-zero-k
  (λ (k^)
    (kt_make-zero-k k^)))

(define make-throw-k
  (λ (v-exp^ env-cps^)
    (kt_make-throw-k v-exp^ env-cps^)))

(define make-let-k
  (λ (body^ env-cps^ k^)
    (kt_make-let-k body^ env-cps^ k^)))

(define make-rand-k
  (λ (v-rator^ k^)
    (kt_make-rand-k v-rator^ k^)))

(define make-rator-k
  (λ (rand^ env-cps^ k^)
    (kt_make-rator-k rand^ env-cps^ k^)))

(define make-mult-inner-k
  (λ (w^ k^)
    (kt_make-mult-inner-k w^ k^)))

(define make-mult-outer-k
  (λ (x2^ env-cps^ k^)
    (kt_make-mult-outer-k x2^ env-cps^ k^)))

(define make-if-k
  (λ (conseq^ alt^ env-cps^ k^)
    (kt_make-if-k conseq^ alt^ env-cps^ k^)))

(require racket/trace)

(define-label main
  ;(λ ()
    (begin
      (set! *exp*
            (expr_let 
             (expr_lambda
              (expr_lambda 
               (expr_if
                (expr_zero (expr_var 0))
                (expr_const 1)
                (expr_mult (expr_var 0) (expr_app (expr_app (expr_var 1) (expr_var 1)) (expr_sub1 (expr_var 0)))))))
             (expr_mult
              (expr_letcc
               (expr_app
                (expr_app (expr_var 1) (expr_var 1))
                (expr_throw (expr_var 0) (expr_app (expr_app (expr_var 1) (expr_var 1)) (expr_const 4)))))
              (expr_const 6))))
      (set! *env-cps* (empty-env))
      (set! *pc* value-of-cps)
      (mount-trampoline kt_empty-k *k* *pc*)
      (printf "Value of the function is ~a\n" *v*)));)

(main)