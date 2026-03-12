package backend.fd.seminar4.homework.nulls.solutions.task2

/**
 * РЕШЕНИЕ Задачи 2: Рефакторинг
 */

data class Order(val id: String, val customer: Customer?)
data class Customer(val name: String, val email: String?)

fun findOrder(id: String): Order? = if (id == "1") Order("1", Customer("Alice", "alice@example.com")) else null

fun processOrderImproved(orderId: String): String {
    return findOrder(orderId)?.customer?.email?.let { email ->
        "Sending email to: $email"
    } ?: "Order, customer or email not found"
}

// Альтернативная версия
fun processOrderAlt(orderId: String): String =
    findOrder(orderId)?.customer?.email
        ?.let { "Sending email to: $it" }
        ?: "Order, customer or email not found"

fun main() {
    println("Old: ${processOrder("1")}")
    println("New: ${processOrderImproved("1")}")
    println("\nMissing: ${processOrderImproved("999")}")
}

fun processOrder(orderId: String): String {
    val order = findOrder(orderId)
    if (order == null) return "Order not found"
    val customer = order.customer
    if (customer == null) return "Customer not found"
    val email = customer.email
    if (email == null) return "Email not found"
    return "Sending email to: $email"
}
