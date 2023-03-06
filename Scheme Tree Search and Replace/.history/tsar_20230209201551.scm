(define (tsar subj srch repl)
   (cond
      ((equal? subj srch)
         (mycopy repl))
      ((null? subj) 
         '())
      ((pair? subj) 
         (cons (tsar (car subj) srch repl) 
               (tsar (cdr subj) srch repl)))
      (else subj)))

(define (mycopy repl)
   (if (pair? repl)
      (cons (car repl) (mycopy (cdr repl)))
      repl))

(define (myequal? x y)
   (cond
      ((and (null? x) (null? y)) #t)
      ((or (null? x) (null? y)) #f)
      ((and (pair? x) (pair? y))
         (and (myequal? (car x) (car y))
              (myequal? (cdr x) (cdr y))))
      (else (eq? x y))))

(define (get-random) (random 100))

(define (test name expected-output function-to-test)
   (display (string-append "Testing " name " : "))
   (compare expected-output function-to-test)
   (newline))

(define (compare expected-output function-to-test)
   (let ((actual-output (function-to-test)))
      (if (myequal? actual-output expected-output)
         (display "PASS")
         (begin
            (display "FAIL")
            (display "\nExpected: ")
            (display expected-output)
            (display "\nReturned: ")
            (display actual-output)))))

(define (run-tests)
   (display "Running tsar.scm tests:\n")
   ; myequal? tests
   (test "(myequal? 'a 'a)" #t (lambda () (myequal? 'a 'a)))
   (test "(myequal? 'a 'b)" #f (lambda () (myequal? 'a 'b)))
   (test "(myequal? '(a b) '(a b)" #t (lambda () (myequal? '(a b) '(a b))))
   (test "(myequal? '(a b) '(b a)" #f (lambda () (myequal? '(a b) '(b a))))
   ; tsar tests
   (test "(tsar 'z 'x 'y)" 'z (lambda () (tsar 'z 'x 'y)))
   (test "(tsar 'x 'x 'y)" 'y (lambda () (tsar 'x 'x 'y)))
   (test "(tsar 'x 'x 'x)" 'x (lambda () (tsar 'x 'x 'x)))
   (test "(tsar '() 'x 'y)" '() (lambda () (tsar '() 'x 'y)))
   (test "(tsar '(x x) 'x 'y)" '(y y) (lambda () (tsar '(x x) 'x 'y)))
   (test "(tsar '(x (x) z) 'x 'y)" '(y (y) z) (lambda () (tsar '(x (x) z) 'x 'y)))
   (test "(tsar '(x (x) z) '(x) '(y y))" '(x (y y) z) (lambda () (tsar '(x (x) z) '(x) '(y y))))
   (test "(tsar '(x (x) ((x)) z) '(x) '(y y))" '(x (y y) ((y y)) z) (lambda () (tsar '(x (x) ((x)) z) '(x) '(y y))))
   (test "(tsar '(x (x) ((x)) z) '(x) '())" '(x () (()) z) (lambda () (tsar '(x (x) ((x)) z) '(x) '())))
   (test "(tsar '(x (x) ((x)) z) '() '(y y))" '(x (x y y) ((x y y) y y) z y y) (lambda () (tsar '(x (x) ((x)) z) '() '(y y))))
   (newline))

(define (testrand) (tsar '(x (x) ((x)) z) '(x) '(RANDOM RANDOM)))