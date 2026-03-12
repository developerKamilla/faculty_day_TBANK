package backend.fd.seminar4.homework.comparison.solutions.task2

/**
 * РЕШЕНИЕ Задачи 2: Рефакторинг legacy кода
 */

// Sealed class для всех исходов
sealed class PaymentResult {
    data class Success(val orderId: String) : PaymentResult()
    data class OrderNotFound(val orderId: String) : PaymentResult()
    data class InvalidAmount(val amount: Double) : PaymentResult()
    data class PaymentFailed(val reason: String) : PaymentResult()
}

fun processPaymentNew(orderId: String): Result<PaymentResult> = runCatching {
    if (orderId == "999") {
        PaymentResult.OrderNotFound(orderId)
    } else if (orderId == "invalid") {
        PaymentResult.InvalidAmount(-100.0)
    } else {
        PaymentResult.Success(orderId)
    }
}

fun main() {
    println("=== Success ===")
    processPaymentNew("123").onSuccess { result ->
        when (result) {
            is PaymentResult.Success -> println("✓ Payment successful: ${result.orderId}")
            is PaymentResult.OrderNotFound -> println("✗ Order not found: ${result.orderId}")
            is PaymentResult.InvalidAmount -> println("✗ Invalid amount: ${result.amount}")
            is PaymentResult.PaymentFailed -> println("✗ Payment failed: ${result.reason}")
        }
    }

    println("\n=== Not found ===")
    processPaymentNew("999").onSuccess { result ->
        when (result) {
            is PaymentResult.Success -> println("✓ Success")
            is PaymentResult.OrderNotFound -> println("✗ Order ${result.orderId} not found")
            is PaymentResult.InvalidAmount -> println("✗ Invalid")
            is PaymentResult.PaymentFailed -> println("✗ Failed")
        }
    }

    println("\n=== ПРЕИМУЩЕСТВА ===")
    println("✓ Нет magic numbers")
    println("✓ Type-safe exhaustive when")
    println("✓ Информативные сообщения")
    println("✓ Легко добавлять новые случаи")
}
