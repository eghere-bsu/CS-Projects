# Project 2: Stardew Valley

* Author: Ethan Ghere
* Class: CS321 
* Semester: Fall

## Overview

This program is a CLI simulator of the popular role-playing game 
Stardew Valley. The program, MyLifeInStarDew, generates an itemized list
of daily tasks that the player will complete throughout the day, such 
as fishing, mining, or socializing. The program will simulate a player 
performing these tasks, accounting for the energy each spends, a factor
of luck, and the total amount of money the player will accumulate.

## Reflection

This projest was very fun, especially since I got a copy of Stardew Valley
to play on my own. I still haven't played too much of the game yet but 
from the small amount that I have played I have very much enjoyed it.
This was probably the most fun implementation of a data structure that I 
have done thus far. I hope more projects in the future are as encouraging
as this one.

## Compiling and Using

The files necessary to run the folder should be attached along with this
readme. To compile, run the command:
```
javac *.java 
```
The classes should have compiled correctly. The program takes command line
arguments as follows:
```
java MyLifeInStarDew <M> <T> <D> <P> <S>
```
Where: 
```
<M>: The max priority value to be simulated
<T>: The time to increment the priority in each task
<D>: The total simulation time in days 
<P>: The probability to generate a task
```
## Results 

This project was provided a testing script, `run-tests.sh`. The program 
successfully passed tests 1 thorugh 6 as intended. 

If you would like to verify this, you can also run the command to test:
```
./run-tests.sh
```
Note: You may have to give the file permission to act as an executable. You
can achieve this with the following command:
```
chmod +x run-tests.sh
```

## Sources used

The main source I used to complete this project were the CS 321 tutors available. Those people really helped clarify a lot of my problems. Other than
that, this work is my own. 

## Notes
The most important thing to noted is that I had to change a part of the 
PriorityQueueInterface class. The interface called for implementing `enqueue()` with an `Object` called task. Instead of using an `Object`, I used a `Task` as
it made more sense for this project.