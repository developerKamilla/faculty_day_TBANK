package backend.fd.seminar4.homework.result.exercises.task2

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
    price * 0.9  // 10% discount
}

fun processPayment(amount: Double): Result<Receipt> = runCatching {
    Receipt("ORDER-123", amount)
}

// TODO: Используйте mapCatching для цепочки
fun processOrderPipeline(order: Order): Result<Receipt> {
    throw NotImplementedError()
}

fun main() {
    val order = Order(listOf("item1", "item2"), 100.0)
    // TODO: тест
}
