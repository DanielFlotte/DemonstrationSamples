## Multithreading in C++ demonstration

This program solve the next problem:

Which 4-or-less digits numbers multiplied by another 4-or-less digits numbers combined with their result form a non-zeroed [Pandigital number](https://en.wikipedia.org/wiki/Pandigital_number). As example 3 and 4 because 3 * 4 = 12 which combined are 3412 and contains the numbers from 1 to 4, being this a pandigital number.

To optimize this problem in parallel, it is not possible to run all threads to do the same job, because its not so predictable how much time every task is going to take, so I had to use my ThreadsDistributor.hpp class. This class (in a thread-safe way)saves the tasks to do and send one to every thread every time they ask for a new job, and a signal to end if needed.

Output of the program:
```
3 * 4 = 12
3 * 54 = 162
4 * 13 = 52
3 * 582 = 1746
6 * 453 = 2718
4 * 1738 = 6952
4 * 1963 = 7852
12 * 483 = 5796
18 * 297 = 5346
24 * 57 = 1368
27 * 198 = 5346
28 * 157 = 4396
34 * 52 = 1768
37 * 58 = 2146
39 * 186 = 7254
42 * 138 = 5796
48 * 159 = 7632
58 * 64 = 3712
END
```
