package ru.tbank.education.school.lesson1

fun sumEvenNumbers(numbers: Array<Int>): Int {
    var kol = 0
    for (elem in numbers) {
        if (elem % 2 == 0) {
            kol += 1
        }
    }
    return kol
}
