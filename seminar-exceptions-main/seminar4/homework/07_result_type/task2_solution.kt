package backend.fd.seminar4.homework.result.solutions.task2

/**
 * РЕШЕНИЕ Задачи 2: Цепочка операций
 */

data class Order(val items: List<String>, val price: Double)
data class Receipt(val orderId: String, val amount: Double)

fun validateOrder(order: Order): Result<Order> = runCatching {
    require(order.items.isNotEmpty()) { "Order is empty" }
    order
}

fun calculatePrice(order: Order): Result<Double> = runCatching {
    require(order.price > 0) { "Price must be positive" }
    order.price
}

fun applyDiscount(price: Double): Result<Double> = runCatching {
    price * 0.9
}

fun processPayment(amount: Double): Result<Receipt> = runCatching {
    Receipt("ORDER-123", amount)
}

fun processOrderPipeline(order: Order): Result<Receipt> {
    return validateOrder(order)
        .mapCatching { validOrder ->
            println("✓ Order validated")
            calculatePrice(validOrder).getOrThrow()
        }
        .mapCatching { price ->
            println("✓ Price calculated: $price")
            applyDiscount(price).getOrThrow()
        }
        .mapCatching { discountedPrice ->
            println("✓ Discount applied: $discountedPrice")
            processPayment(discountedPrice).getOrThrow()
        }
}

fun main() {
    val validOrder = Order(listOf("item1", "item2"), 100.0)
    val emptyOrder = Order(emptyList(), 100.0)

    println("=== Valid order ===")
    processOrderPipeline(validOrder)
        .onSuccess { println("✓ Receipt: ${it.orderId}, amount=${it.amount}") }
        .onFailure { println("✗ Error: ${it.message}") }

    println("\n=== Empty order ===")
    processOrderPipeline(emptyOrder)
        .onSuccess { println("Receipt: $it") }
        .onFailure { println("✗ Error: ${it.message}") }
}
