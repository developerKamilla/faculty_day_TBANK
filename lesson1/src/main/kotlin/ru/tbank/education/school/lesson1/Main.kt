package ru.tbank.education.school.lesson1

fun main() {
    print("Here comes the moneeeey")
    println("Hello World!")
    val a = 1
    val q: Short? = null // значение может быть нулем (указали тип Short)
    val b = "cccc"
    val c = 's'
    val d = 1.23
    val e = true
    val f = "hello $d  world"
    var g = 1 // в отличии от val ожно менять значение
    g = 2

    val array = arrayOf(1, 2, 3)
    val doubleArray =
        Array(2) { Array(2) { 0 } }
    println(doubleArray[0][0])

    for (i in 1 .. 10) {
        println(i)
    }
    for (elem in array) {
        print(elem)
    }




}
