#lang racket
(require racket/trace)

;; STEP 1: CPS
;(define value-of-cps
;  (lambda (expr env-cps k)
;    (match expr
;      [`(const ,expr) (k expr)]
;      [`(mult ,x1 ,x2)
;       (value-of-cps x1
;                     env-cps
;                     (λ (v)
;                       (value-of-cps x2
;                                     env-cps
;                                     (λ (w)
;                                       (k (* v w))))))]
;      [`(sub1 ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (sub1 v))))]
;      [`(zero ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (zero? v))))]
;      [`(if ,test ,conseq ,alt)
;       (if (value-of-cps test env-cps (λ (v) v))
;           (value-of-cps conseq env-cps k)
;           (value-of-cps alt env-cps k))]
;      [`(letcc ,body)
;       (value-of-cps body
;                     (lambda (y k^)
;                       (if (zero? y)
;                           (k^ k)
;                           (env-cps (sub1 y) k^)))
;                     k)]
;      [`(throw ,k-exp ,v-exp)
;       (value-of-cps k-exp
;                     env-cps
;                     (λ (v-kexp)
;                       (value-of-cps v-exp
;                                     env-cps
;                                     v-kexp)))]
;      [`(let ,e ,body)
;       (value-of-cps body
;                     (λ (y k^)
;                       (if (zero? y)
;                           (value-of-cps e env-cps k)
;                           (env-cps (sub1 y) k^)))
;                     k)]
;      [`(var ,y)
;       (env-cps y k)]
;      [`(lambda ,body)
;       (k
;        (λ (arg k^)
;          (value-of-cps body
;                        (λ (y k^^)
;                          (if (zero? y)
;                              (k^^ arg)
;                              (env-cps (sub1 y) k^^)))
;                        k^)))]
;      [`(app ,rator ,rand)
;       (value-of-cps rator
;                     env-cps
;                     (λ (v-rator)
;                       (value-of-cps rand env-cps
;                                     (λ (v-rand)
;                                       (v-rator v-rand k)))))])))
; 
;(define empty-env
;  (lambda ()
;    (lambda (y)
;      (error 'value-of "unbound identifier"))))
; 
;(define empty-k
;  (lambda ()
;    (lambda (v)
;      v)))


;; STEP 2: apply-env, apply-clos, apply-k
;; STEP 3: extend-env
;; STEP 4: Add ^ to formal params of extend-env
;;         Ensure inner funcs of extend-env and empty-env use the same formal params as apply-env (y and k^)
;(define value-of-cps
;  (lambda (expr env-cps k)
;    (match expr
;      [`(const ,expr) (k expr)]
;      [`(mult ,x1 ,x2)
;       (value-of-cps x1
;                     env-cps
;                     (λ (v)
;                       (value-of-cps x2
;                                     env-cps
;                                     (λ (w)
;                                       (k (* v w))))))]
;      [`(sub1 ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (sub1 v))))]
;      [`(zero ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (zero? v))))]
;      [`(if ,test ,conseq ,alt)
;       (if (value-of-cps test env-cps (λ (v) v))
;           (value-of-cps conseq env-cps k)
;           (value-of-cps alt env-cps k))]
;      [`(letcc ,body)
;       (value-of-cps body
;                     #;(lambda (y k^)
;                       (if (zero? y)
;                           (apply-k k^ k)
;                           (apply-env env-cps (sub1 y) k^)))
;                     (extend-env env-cps k)
;                     k)]
;      [`(throw ,k-exp ,v-exp)
;       (value-of-cps k-exp
;                     env-cps
;                     (λ (v-kexp)
;                       (value-of-cps v-exp
;                                     env-cps
;                                     v-kexp)))]
;      [`(let ,e ,body)
;       (value-of-cps body
;                     #;(λ (y k^)
;                         (if (zero? y)
;                             (apply-k k^ a)
;                             (apply-env env-cps (sub1 y) k^)))
;                     (extend-env env-cps (value-of-cps e env-cps k))
;                     k)]
;      [`(var ,y)
;       (apply-env env-cps y k)]
;      [`(lambda ,body)
;       (apply-k k
;                (λ (arg k^)
;                  (value-of-cps body
;                                #;(λ (y k^^)
;                                  (if (zero? y)
;                                      (apply-k k^^ arg)
;                                      (apply-env env-cps (sub1 y) k^^)))
;                                (extend-env env-cps arg)
;                                k^)))]
;      [`(app ,rator ,rand)
;       (value-of-cps rator
;                     env-cps
;                     (λ (v-rator)
;                       (value-of-cps rand env-cps
;                                     (λ (v-rand)
;                                       (apply-clos v-rator v-rand k)))))])))
; 
;(define empty-env
;  (lambda ()
;    (lambda (y k^)
;      (error 'value-of "unbound identifier"))))
; 
;(define empty-k
;  (lambda ()
;    (lambda (v)
;      v)))
;
;(define apply-env
;  (λ (env-cps y k^)
;    (env-cps y k^)))
;
;(define apply-clos
;  (λ (rator rand k)
;    (rator rand k)))
;
;(define apply-k
;  (λ (k v)
;    (k v)))
;
;(define extend-env
;  (λ (env-cps^ k^)
;    (λ (y k^^)
;      (if (zero? y)
;          (apply-k k^^ k^)
;          (apply-env env-cps^ (sub1 y) k^^)))))


;; STEP 5: Replace functional representation of env with tagged lists
;; STEP 6: Remove the [else (env-cps y k)] from apply-env
;(define value-of-cps
;  (lambda (expr env-cps k)
;    (match expr
;      [`(const ,expr) (k expr)]
;      [`(mult ,x1 ,x2)
;       (value-of-cps x1
;                     env-cps
;                     (λ (v)
;                       (value-of-cps x2
;                                     env-cps
;                                     (λ (w)
;                                       (k (* v w))))))]
;      [`(sub1 ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (sub1 v))))]
;      [`(zero ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (zero? v))))]
;      [`(if ,test ,conseq ,alt)
;       (if (value-of-cps test env-cps (λ (v) v))
;           (value-of-cps conseq env-cps k)
;           (value-of-cps alt env-cps k))]
;      [`(letcc ,body)
;       (value-of-cps body
;                     (extend-env env-cps k)
;                     k)]
;      [`(throw ,k-exp ,v-exp)
;       (value-of-cps k-exp
;                     env-cps
;                     (λ (v-kexp)
;                       (value-of-cps v-exp
;                                     env-cps
;                                     v-kexp)))]
;      [`(let ,e ,body)
;       (value-of-cps body
;                     (extend-env env-cps (value-of-cps e env-cps k))
;                     k)]
;      [`(var ,y)
;       (apply-env env-cps y k)]
;      [`(lambda ,body)
;       (apply-k k
;                (λ (arg k^)
;                  (value-of-cps body
;                                (extend-env env-cps arg)
;                                k^)))]
;      [`(app ,rator ,rand)
;       (value-of-cps rator
;                     env-cps
;                     (λ (v-rator)
;                       (value-of-cps rand env-cps
;                                     (λ (v-rand)
;                                       (apply-clos v-rator v-rand k)))))])))
; 
;(define empty-env
;  (lambda ()
;    `(empty-env)
;    #;(lambda (y k^)
;      (error 'value-of "unbound identifier"))))
; 
;(define empty-k
;  (lambda ()
;    (lambda (v)
;      v)))
;
;(define apply-env
;  (λ (env-cps y k^)
;    (match env-cps
;      [`(empty-env)
;       (error 'value-of "unbound identifier")]
;      [`(extend-env ,env-cps^ ,k)
;       (if (zero? y)
;           (apply-k k^ k)
;           (apply-env env-cps^ (sub1 y) k^))]
;      #;[else (env-cps y k^)])))
;
;(define apply-clos
;  (λ (rator rand k)
;    (rator rand k)))
;
;(define apply-k
;  (λ (k v)
;    (k v)))
;
;(define extend-env
;  (λ (env-cps^ k^)
;    `(extend-env ,env-cps^ ,k^)
;    #;(λ (y k^^)
;      (if (zero? y)
;          (apply-k k^^ k^)
;          (apply-env env-cps^ (sub1 y) k^^)))))


;; STEP 7: Define make-clos
;; STEP 8: Replace functional representation of closures with tagged lists
;(define value-of-cps
;  (lambda (expr env-cps k)
;    (match expr
;      [`(const ,expr) (k expr)]
;      [`(mult ,x1 ,x2)
;       (value-of-cps x1
;                     env-cps
;                     (λ (v)
;                       (value-of-cps x2
;                                     env-cps
;                                     (λ (w)
;                                       (k (* v w))))))]
;      [`(sub1 ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (sub1 v))))]
;      [`(zero ,x)
;       (value-of-cps x
;                     env-cps
;                     (λ (v)
;                       (k (zero? v))))]
;      [`(if ,test ,conseq ,alt)
;       (if (value-of-cps test env-cps (λ (v) v))
;           (value-of-cps conseq env-cps k)
;           (value-of-cps alt env-cps k))]
;      [`(letcc ,body)
;       (value-of-cps body
;                     (extend-env env-cps k)
;                     k)]
;      [`(throw ,k-exp ,v-exp)
;       (value-of-cps k-exp
;                     env-cps
;                     (λ (v-kexp)
;                       (value-of-cps v-exp
;                                     env-cps
;                                     v-kexp)))]
;      [`(let ,e ,body)
;       (value-of-cps body
;                     (extend-env env-cps (value-of-cps e env-cps k))
;                     k)]
;      [`(var ,y)
;       (apply-env env-cps y k)]
;      [`(lambda ,body)
;       (apply-k k
;                #;(λ (arg k^)
;                  (value-of-cps body
;                                (extend-env env-cps arg)
;                                k^))
;                (make-clos body env-cps k))]
;      [`(app ,rator ,rand)
;       (value-of-cps rator
;                     env-cps
;                     (λ (v-rator)
;                       (value-of-cps rand env-cps
;                                     (λ (v-rand)
;                                       (apply-clos v-rator v-rand k)))))])))
; 
;(define empty-env
;  (lambda ()
;    `(empty-env)))
; 
;(define empty-k
;  (lambda ()
;    (lambda (v)
;      v)))
;
;(define apply-env
;  (λ (env-cps y k^)
;    (match env-cps
;      [`(empty-env)
;       (error 'value-of "unbound identifier")]
;      [`(extend-env ,env-cps^ ,k)
;       (if (zero? y)
;           (apply-k k^ k)
;           (apply-env env-cps^ (sub1 y) k^))]
;      #;[else (env-cps y k^)])))
;
;(define apply-clos
;  (λ (rator arg k)
;    (match rator
;      [`(make-clos ,body ,env-cps ,k^)
;       (value-of-cps body
;                     (extend-env env-cps arg)
;                     k)]
;      #;[else (rator arg k)])))
;
;(define make-clos
;  (λ (body env-cps k)
;    `(make-clos ,body ,env-cps ,k)
;    #;(λ (arg k^)
;      (value-of-cps body
;                    (extend-env env-cps arg)
;                    k^))))
;
;(define apply-k
;  (λ (k v)
;    (k v)))
;
;(define extend-env
;  (λ (env-cps^ k^)
;    `(extend-env ,env-cps^ ,k^)))


;; STEP 9: Define constructors for continuations
;; STEP 10: Add a ^ to formal params of continuation helpers (α-substitution)
;;          Also make sure the inner func of continuation helpers takes 'v'
;(define value-of-cps
;  (lambda (expr env-cps k)
;    (match expr
;      [`(const ,expr) (apply-k k expr)]
;      [`(mult ,x1 ,x2)
;       (value-of-cps x1
;                     env-cps
;                     #;(λ (v)
;                       (value-of-cps x2
;                                     env-cps
;                                     #;(λ (w)
;                                       (apply-k k (* v w)))
;                                     (make-mult-inner-k v k)))
;                     (make-mult-outer-k x2 env-cps k))]
;      [`(sub1 ,x)
;       (value-of-cps x
;                     env-cps
;                     #;(λ (v)
;                       (k (sub1 v)))
;                     (make-sub1-k k))]
;      [`(zero ,x)
;       (value-of-cps x
;                     env-cps
;                     #;(λ (v)
;                       (apply-k k (zero? v)))
;                     (make-zero-k k))]
;      [`(if ,test ,conseq ,alt)
;       (if (value-of-cps test env-cps (empty-k))
;           (value-of-cps conseq env-cps k)
;           (value-of-cps alt env-cps k))]
;      [`(letcc ,body)
;       (value-of-cps body
;                     (extend-env env-cps k)
;                     k)]
;      [`(throw ,k-exp ,v-exp)
;       (value-of-cps k-exp
;                     env-cps
;                     #;(λ (v-kexp)
;                       (value-of-cps v-exp
;                                     env-cps
;                                     v-kexp))
;                     (make-throw-k v-exp env-cps))]
;      [`(let ,e ,body)
;       (value-of-cps body
;                     (extend-env env-cps (value-of-cps e env-cps k))
;                     k)]
;      [`(var ,y)
;       (apply-env env-cps y k)]
;      [`(lambda ,body)
;       (apply-k k (make-clos body env-cps k))]
;      [`(app ,rator ,rand)
;       (value-of-cps rator
;                     env-cps
;                     #;(λ (v-rator)
;                       (value-of-cps rand
;                                     env-cps
;                                     #;(λ (v-rand)
;                                       (apply-clos v-rator v-rand k))
;                                     (make-rand-k v-rator k)))
;                     (make-rator-k rand env-cps k))])))
; 
;(define empty-env
;  (lambda ()
;    `(empty-env)))
; 
;(define empty-k
;  (lambda ()
;    (lambda (v)
;      v)))
;
;(define apply-env
;  (λ (env-cps y k^)
;    (match env-cps
;      [`(empty-env)
;       (error 'value-of "unbound identifier")]
;      [`(extend-env ,env-cps^ ,k)
;       (if (zero? y)
;           (apply-k k^ k)
;           (apply-env env-cps^ (sub1 y) k^))]
;      #;[else (env-cps y k^)])))
;
;(define apply-clos
;  (λ (rator arg k)
;    (match rator
;      [`(make-clos ,body ,env-cps ,k^)
;       (value-of-cps body
;                     (extend-env env-cps arg)
;                     k)]
;      #;[else (rator arg k)])))
;
;(define make-clos
;  (λ (body env-cps k)
;    `(make-clos ,body ,env-cps ,k)))
;
;(define apply-k
;  (λ (k v)
;    (k v)))
;
;(define extend-env
;  (λ (env-cps^ k^)
;    `(extend-env ,env-cps^ ,k^)))
;
;(define make-sub1-k
;  (λ (k^)
;    (λ (v)
;      (k^ (sub1 v)))))
;
;(define make-zero-k
;  (λ (k)
;    (λ (v)
;      (apply-k k (zero? v)))))
;
;(define make-throw-k
;  (λ (v-exp^ env-cps^)
;    (λ (v)
;      (value-of-cps v-exp^
;                    env-cps^
;                    v))))
;
;(define make-rand-k
;  (λ (v-rator^ k^)
;    (λ (v)
;      (apply-clos v-rator^ v k^))))
;
;(define make-rator-k
;  (λ (rand^ env-cps^ k^)
;    (λ (v)
;      (value-of-cps rand^
;                    env-cps^
;                    (make-rand-k v k^)))))
;
;(define make-mult-inner-k
;  (λ (w^ k^)
;    (λ (v)
;      (apply-k k^ (* v w^)))))
;
;(define make-mult-outer-k
;  (λ (x2^ env-cps^ k^)
;    (λ (v)
;      (value-of-cps x2^
;                    env-cps^
;                    (make-mult-inner-k v k^)))))


; STEP 11: Replace functional representation of continuations with tagged lists
; STEP 12: Remove [else (k v)] from apply-k
(define value-of-cps
  (lambda (expr env-cps k)
    (match expr
      [`(const ,expr) (apply-k k expr)]
      [`(mult ,x1 ,x2)
       (value-of-cps x1
                     env-cps
                     (make-mult-outer-k x2 env-cps k))]
      [`(sub1 ,x)
       (value-of-cps x
                     env-cps
                     (make-sub1-k k))]
      [`(zero ,x)
       (value-of-cps x
                     env-cps
                     (make-zero-k k))]
      [`(if ,test ,conseq ,alt)
       (if (value-of-cps test env-cps (empty-k))
           (value-of-cps conseq env-cps k)
           (value-of-cps alt env-cps k))]
      [`(letcc ,body)
       (value-of-cps body
                     (extend-env env-cps k)
                     k)]
      [`(throw ,k-exp ,v-exp)
       (value-of-cps k-exp
                     env-cps
                     (make-throw-k v-exp env-cps))]
      [`(let ,e ,body)
       (value-of-cps body
                     (extend-env env-cps (value-of-cps e env-cps k))
                     k)]
      [`(var ,y)
       (apply-env env-cps y k)]
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
      #;[else (k v)])))

(define empty-k
  (lambda ()
    `(empty-k)
    #;(lambda (v)
      v)))

(define make-sub1-k
  (λ (k^)
    `(make-sub1-k ,k^)
    #;(λ (v)
      (apply-k k^ (sub1 v)))))

(define make-zero-k
  (λ (k^)
    `(make-zero-k ,k^)
    #;(λ (v)
      (apply-k k^ (zero? v)))))

(define make-throw-k
  (λ (v-exp^ env-cps^)
    `(make-throw-k ,v-exp^ ,env-cps^)
    #;(λ (v)
      (value-of-cps v-exp^
                    env-cps^
                    v))))

(define make-rand-k
  (λ (v-rator^ k^)
    `(make-rand-k ,v-rator^ ,k^)
    #;(λ (v)
      (apply-clos v-rator^ v k^))))

(define make-rator-k
  (λ (rand^ env-cps^ k^)
    `(make-rator-k ,rand^ ,env-cps^ ,k^)
    #;(λ (v)
      (value-of-cps rand^
                    env-cps^
                    (make-rand-k v k^)))))

(define make-mult-inner-k
  (λ (w^ k^)
    `(make-mult-inner-k ,w^ ,k^)
    #;(λ (v)
      (apply-k k^ (* v w^)))))

(define make-mult-outer-k
  (λ (x2^ env-cps^ k^)
    `(make-mult-outer-k ,x2^ ,env-cps^ ,k^)
    #;(λ (v)
      (value-of-cps x2^
                    env-cps^
                    (make-mult-inner-k v k^)))))


(printf "Expected Output:\n5\n25\n3\n4\n6\n5\n4\n5\n25\n5\n3\n5\n5\n15\n4\n4\n1\nActual:\n")
;(trace value-of-cps)
(value-of-cps '(const 5)
              (empty-env) (empty-k)) ;5

(value-of-cps '(mult (const 5)
                     (const 5))
              (empty-env) (empty-k)) ;25

(value-of-cps '(sub1 (sub1 (const 5)))
              (empty-env) (empty-k)) ;3

(value-of-cps '(if (zero (const 0))
                   (mult (const 2) (const 2))
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

;(trace value-of-cps)
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