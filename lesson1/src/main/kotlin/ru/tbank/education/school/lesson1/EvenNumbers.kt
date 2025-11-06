package ru.tbank.education.school.lesson1

fun sumEvenNumbers(numbers: Array<Int>): Int {
    for (elem in numbers) {
        if (elem % 2 == 0) {
            return elem
        }
    }
    return -1
}
