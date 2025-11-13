package ru.tbank.education.school.lesson1

fun calculate(a: Double, b: Double, operation: OperationType): Double? {
    if (b == 0.0) return null
    else
        if (operation == OperationType.ADD) {
            return a + b
        }
        if (operation == OperationType.SUBTRACT) {
            return a - b
        }
        if (operation == OperationType.DIVIDE) {
            return a / b
        }
        if (operation == OperationType.MULTIPLY) {
            return a * b
        }
    return null
}

@Suppress("ReturnCount")
fun String.calculate(): Double? {
    val word = this.split(Regex("\\s+"))
    if (word.size != 3) return null
    val v1 = word[0].toDoubleOrNull() ?: return null
    val v2 = word[1]
    val operation = when (v2) {
        "+" -> OperationType.ADD
        "-" -> OperationType.SUBTRACT
        "*" -> OperationType.MULTIPLY
        "/" -> OperationType.DIVIDE
        else -> return null
    }
    val v3 = word[2].toDoubleOrNull() ?: return null
    return calculate(v1, v3, operation)
}

fun main() {
    println(calculate(10.0, 2.0, OperationType.MULTIPLY))
    println("10 плюс 2".calculate())
}