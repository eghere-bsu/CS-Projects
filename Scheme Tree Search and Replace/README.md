# TSaR in Scheme
* By Ethan Ghere for CS 354-1

### Overview
 This program is a tree search and replace (TSaR) implementation in Scheme using Guile. `tsar.scm` will search a subject and replace each instance of the target item with another item. 

### Usage
 **Note:** This program is intended to be run on UNIX-based operating systems. If you wish to run this program on other operating systems, you're on your own.  
 
 To run `tsar.scm`, you will need to setup an environment first:
 1. Install `guile` from your favorite package manager.
 2. Navigate to this directory in your favorite terminal emulator.
 2. Run `tsar.scm` with the command: `guile -l tsar.scm`.

### Functionality
 The `tsar` function calls two helper functions `myequal?` and `mycopy` to perform the search-and-replace operation on a nested list data structure. 
 
 The `myequal?` function is used to compare elements in the nested list `subj` against `srch`. It determines if two nested lists are equal by recursively comparing each element in both lists using the `eq?` function. If a match is found, `mycopy` is called to copy the `repl` value. 
 
 The `mycopy` function creates a new nested list based on `repl`. If `repl` is a pair, `mycopy` calls itself on the `cdr` of `repl` until it reaches the end of the list, at which point it returns `repl`. 
 
 The `tsar` function is invoked using the following syntax:
 ```
 (tsar subj srch repl)
 ```
 Where:
 * `subj` represents the subject or list which will be manipulated.
 * `srch` is the item which will be replaced.
 * `repl` is the item that replaces `srch`. 

 For example, `(tsar '(x y z) 'y 'a)` would yield `(x a z)`.

### Testing
 This program contains a function to test the functionality of `tsar`. Invoke the function `(run-tests)` to see a list of sample tests. Each test will `PASS` or `FAIL` depending if the outpput of the program matches its expected output.
 
 You can write your own test using this syntax:
 ``` 
 (test name expected-output function-to-test)
 ```
 Where: 
 * `name` is a string. For readability purposes, I make this string identical to the function being tested. 
 * `epected-output` is the output your function should produce.
 * `function-to-test` is the function that will produce the implemented output.
 -- **Note:** When defining this parameter, prepend with `lambda ()` to ensure your test works as intended. 

 Here is an example of a `test` function:
 ```
 (test "(tsar 'z 'x 'y)" 'z (lambda () (tsar 'z 'x 'y)))
 ```
 **Note:** `test` is not limited to testing `tsar`. You can test other functions using the same syntax. This example tests an intermediate function `myequal` which is used when invoking `tsar`:
 ```
 (test "(myequal? 'a 'a)" #t (lambda () (myequal? 'a 'a)))
 ```