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
    // Test
    val o = "abcd $d"
    println(o)
    var k = 1
    k += 1
    println(k)
    if (q == null) {
        println("$q is null")
    }



}
