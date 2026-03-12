package backend.fd.seminar4.homework.comparison.exercises.task2

// Legacy код
fun processPayment(orderId: String): Int {
    try {
        // -1 = not found
        // -2 = invalid amount
        // -3 = payment failed
        // 0 = success
        return 0
    } catch (e: Exception) {
        return -99  // unknown error
    }
}

// TODO: Перепишите используя Result и Sealed class
sealed class PaymentResult

fun processPaymentNew(orderId: String): Result<PaymentResult> {
    throw NotImplementedError()
}

fun main() {
    println("=== Legacy ===")
    when (processPayment("123")) {
        0 -> println("Success")
        -1 -> println("Not found")
        // ...
    }

    println("\n=== New ===")
    // TODO: использование новой версии
}
