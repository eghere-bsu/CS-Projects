# Project 3: HashTable 

* Author: Ethan Ghere
* Class: CS321
* Semester: Fall

## Overview

This program inserts keys into two hash tables: one uses Linear Probing 
while the other uses Double Hashing. The program may use different input 
keys based on  a user's command line argument selection. The purpose of 
this program is to analyze the differences in linear and double hashing. 

## Compiling and Using

The files necessary for compilation exist in the zip file attached to this
README. These are the following files in this attachment:

HashTest.java 		    - the driver class for the program.
HashObject.java		    - the objects used for hashing.
HashTable.java		    - represents a table of HashObjects. Has two children:
	LinearProbing.java  - for linear hashing
	DoubleHashing.java  - for double hashing
TwinPrimeGenerator.java - generates the twin primes used for the hash tables.
README.md				- this file.

To compile, run the command:
```
javac *.javac
```
The classes should have compiled corectly. The program takes command line 
arguments as follows:
```
java HashTest <input-type> <load-factor> <debug-level>
```
Where:
```
<input-type> determines how the keys to be inserted are generated:
   - 1: Uses a random integer 
   - 2: Uses the current system time
   - 3: Uses a file 'word-list'
<load-factor> is the specified load factor for the program to stop inserting.
   - the load factor should be a decimal between 0 and 1.
<debug-level> determines the level of output the program will give:
   - 0: The program will display the number of elements, duplicates,
        specified load factor, and average number of probes.
   - 1: In addition to mode 0, the program will also list the contents of 
        each file into two dump files titled 'linear-dump' and 'double-dump'
        for the respective hashing functions.

## Results

The results have been rounded to three decimal places.

Input source 1: random number

alpha	     linear    double
-----------------------------
0.5          1.486     1.385
0.6          1.765     1.517
0.7          2.175     1.723
0.8          2.966     2.011
0.9          5.611     2.564
0.95	    10.669     3.137
0.98	    23.922     3.999
0.99	    42.753     4.619


Input source 2: date

alpha       linear     double
-----------------------------
0.5          1.0       1.0
0.6          1.0       1.0
0.7          1.0       1.0
0.8          1.0       1.0
0.9          1.0       1.0
0.95         1.0       1.0
0.98         1.0       1.0
0.99         1.0       1.0 


Input source 3: word-list

alpha	     linear    double
-----------------------------
0.5          1.597     1.390
0.6          2.149     1.534
0.7          3.604     1.721
0.8          6.708     2.016
0.9         19.815     2.569
0.95	   110.595     3.187
0.98	   324.218     4.020 
0.99	   472.076     4.695 

## Sources used

The main sources I used were computer science tutors. You mentioned that the current
tutors learned this course under your supervision; you have taught them well, because
they certainly helped me a lot.
