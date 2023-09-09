#lang racket
(require racket/trace)

(define lex
  (λ (exp ls)
    (letrec ([l (λ (lam env)
                  (match lam
                    [`,y #:when (symbol? y) (d y env)]
                    [`(lambda (,x) ,body)
                     #:when (symbol? x)
                     `(lambda ,(l body (cons x env)))]
                    [`(,rator ,rand)
                     `(,(l rator env) ,(l rand env))]))]
             [d (λ (var env)
                  (cond
                    [(not (member var env)) var]
                    [else (match env
                            [`() var]
                            [`(,a . ,d) #:when (eqv? a var) '(var 0)]
                            [else `(var ,(add1 (cadr (d var (cdr env)))))])]))])
      (l exp ls))))

(println "lex")
(lex '(lambda (x) x) 
     '())

(lex '(lambda (y) (lambda (x) y)) 
       '())

(lex '(lambda (y) (lambda (x) (x y))) 
     '())

(lex '(lambda (x) (lambda (x) (x x))) 
       '())

(lex '(lambda (x) (lambda (x) (y x))) 
       '())

(lex '(lambda (y) ((lambda (x) (x y)) (lambda (c) (lambda (d) (y c))))) 
       '())

(lex '(lambda (a)
          (lambda (b)
            (lambda (c)
              (lambda (a)
                (lambda (b)
                  (lambda (d)
                    (lambda (a)
                      (lambda (e)
                        (((((a b) c) d) e) a))))))))) 
       '())

(lex '(lambda (a)
          (lambda (b)
	    (lambda (c)
	      (lambda (w)
	        (lambda (x)
		  (lambda (y)
		    ((lambda (a)
		       (lambda (b)
			 (lambda (c)
			   (((((a b) c) w) x) y))))
		     (lambda (w)
		       (lambda (x)
			 (lambda (y)
			   (((((a b) c) w) x) y))))))))))) 
       '())

(lex '(lambda (a)
          (lambda (b)
	    (lambda (c)
	      (lambda (w)
	        (lambda (x)
		  (lambda (y)
		    ((lambda (a)
		       (lambda (b)
			 (lambda (c)
			   (((((a b) c) w) x) y))))
		     (lambda (w)
		       (lambda (x)
			 (lambda (y)
			   (((((a b) c) w) h) y))))))))))) 
       '())


(define value-of
  (λ (exp env)
    (match exp
      [`,n #:when (number? n) n]
      [`,b #:when (boolean? b) b]
      [`,y #:when (symbol? y) (env y)]
      [`(* ,a ,b)
       (* (value-of a env) (value-of b env))]
      [`(zero? ,x)
       (zero? (value-of x env))]
      [`(sub1 ,n)
       (sub1 (value-of n env))]
      [`(if ,test ,conseq ,alt)
       (if (value-of test env)
           (value-of conseq env)
           (value-of alt env))]
      [`(let ([,x ,def]) ,body)
       #:when (symbol? x)
       (value-of body (λ (y)
                        (cond
                          [(eqv? x y) (value-of def env)]
                          [else (env y)])))]
      [`(lambda (,x) ,body)
       #:when (symbol? x)
       (λ (arg)
         (value-of body (λ (y)
                          (cond
                            [(eqv? x y) arg]
                            [else (env y)]))))]
      [`(,rator ,rand)
       ((value-of rator env) (value-of rand env))]
      )))

(println "Representation dependant value-of")

(value-of
   '((lambda (x) (if (zero? x)
                     #t
                     #f))
     0)
   (lambda (y) (error 'value-of "unbound variable ~s" y))) ;#t

(value-of 
   '((lambda (x) (if (zero? x) 
                     12 
                     47)) 
     0) 
   (lambda (y) (error 'value-of "unbound variable ~s" y))) ;12

(value-of
   '(let ([y (* 3 4)])
      ((lambda (x) (* x y)) (sub1 6)))
   (lambda (y) (error 'value-of "unbound variable ~s" y))) ;60

(value-of
   '(let ([x (* 2 3)])
      (let ([y (sub1 x)])
        (* x y)))
   (lambda (y) (error 'value-of "unbound variable ~s" y))) ;30

(value-of
   '(let ([x (* 2 3)])
      (let ([x (sub1 x)])
        (* x x)))
   (lambda (y) (error 'value-of "unbound variable ~s" y))) ;25

(value-of 
   '(let ((! (lambda (x) (* x x))))
      (let ((! (lambda (n)
                 (if (zero? n) 1 (* n (! (sub1 n)))))))
        (! 5)))
   (lambda (y) (error 'value-of "unbound variable ~s" y))) ;120

(value-of
   '(((lambda (f)
        (lambda (n) (if (zero? n) 1 (* n ((f f) (sub1 n))))))
      (lambda (f)
        (lambda (n) (if (zero? n) 1 (* n ((f f) (sub1 n)))))))
     5)
   (lambda (y) (error 'value-of "unbound variable ~s" y)))


(define value-of-fn
  (λ (exp env)
    (match exp
      [`,n #:when (number? n) n]
      [`,b #:when (boolean? b) b]
      [`,y #:when (symbol? y) (apply-env-fn env y)]
      [`(* ,a ,b)
       (* (value-of-fn a env) (value-of-fn b env))]
      [`(zero? ,x)
       (zero? (value-of-fn x env))]
      [`(sub1 ,n)
       (sub1 (value-of-fn n env))]
      [`(if ,test ,conseq ,alt)
       (if (value-of-fn test env)
           (value-of-fn conseq env)
           (value-of-fn alt env))]
      [`(let ([,x ,def]) ,body)
       #:when (symbol? x)
       (let ([env (extend-env-fn x (value-of-fn def env) env)])
         (value-of-fn body env))]
      [`(lambda (,x) ,body)
       #:when (symbol? x)
       (make-closure-fn x body env)]
      [`(,rator ,rand)
       (apply-closure-fn
        (value-of-fn rator env)
        (value-of-fn rand env))]
      )))

(define apply-env-fn
  (λ (env y)
    (match env
      [`(extend-env ,x ,arg ,env)
       (cond
         [(eqv? y x) arg]
         [else (apply-env-fn env y)])]
      [`(empty-env)
       (error "Free Variable" y)]
      [else (env y)])))

(define extend-env-fn
  (λ (x arg env)
    `(extend-env ,x ,arg ,env)))

(define empty-env-fn
  (λ ()
    `(empty-env)))

(define apply-closure-fn
  (λ (rator rand)
    (match rator
      [`(make-closure ,x ,body ,env)
       (value-of-fn body (extend-env-fn x rand env))]
      [else (rator rand)])))

(define make-closure-fn
  (λ (x body env)
    `(make-closure ,x ,body ,env)))

(println "Representation indepepndent value-of-fn")

(value-of-fn
   '((lambda (x) (if (zero? x)
                     #t
                     #f))
     0)
   (empty-env-fn)) ;#t

(value-of-fn 
   '((lambda (x) (if (zero? x) 
                     12 
                     47)) 
     0) 
   (empty-env-fn)) ;12

(value-of-fn
   '(let ([y (* 3 4)])
      ((lambda (x) (* x y)) (sub1 6)))
   (empty-env-fn)) ;60

(value-of-fn
   '(let ([x (* 2 3)])
      (let ([y (sub1 x)])
        (* x y)))
   (empty-env-fn)) ;30

(value-of-fn
   '(let ([x (* 2 3)])
      (let ([x (sub1 x)])
        (* x x)))
   (empty-env-fn)) ;25

(value-of-fn
   '(((lambda (f)
        (lambda (n) (if (zero? n) 1 (* n ((f f) (sub1 n))))))
      (lambda (f)
        (lambda (n) (if (zero? n) 1 (* n ((f f) (sub1 n)))))))
     5)
   (empty-env-fn)) ;120