package backend.fd.seminar4.homework.nulls.exercises.task2

data class Order(val id: String, val customer: Customer?)
data class Customer(val name: String, val email: String?)

fun findOrder(id: String): Order? = if (id == "1") Order("1", Customer("Alice", "alice@example.com")) else null

// ПЛОХОЙ КОД - рефакторинг
fun processOrder(orderId: String): String {
    val order = findOrder(orderId)
    if (order == null) {
        return "Order not found"
    }

    val customer = order.customer
    if (customer == null) {
        return "Customer not found"
    }

    val email = customer.email
    if (email == null) {
        return "Email not found"
    }

    return "Sending email to: $email"
}

// TODO: Перепишите используя safe call, let, Elvis
fun processOrderImproved(orderId: String): String {
    throw NotImplementedError()
}

fun main() {
    println(processOrder("1"))
    // TODO: тест улучшенной версии
}
