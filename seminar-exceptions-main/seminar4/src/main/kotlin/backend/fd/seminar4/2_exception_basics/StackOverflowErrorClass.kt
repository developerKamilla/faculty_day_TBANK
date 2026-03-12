package backend.fd.seminar4.overview

private var i = 0

// Method to print numbers
fun printNumber(x: Int): Int {
    i += 2
    println(i)
    return i + printNumber(i + 2)
}

fun main() {
    // Recursive call without any
    // terminating condition
    printNumber(i)
}
