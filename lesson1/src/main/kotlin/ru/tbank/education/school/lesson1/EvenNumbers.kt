package ru.tbank.education.school.lesson1

fun sumEvenNumbers(numbers: Array<Int>) =
    numbers.filter { it % 2 == 0}.sum()