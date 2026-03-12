package backend.fd.seminar4.homework.nulls.solutions.task1

/**
 * РЕШЕНИЕ Задачи 1: Безопасная цепочка вызовов
 */

data class Address(val city: String, val street: String?)
data class Company(val name: String, val address: Address?)
data class User(val name: String, val company: Company?)

fun getStreet(user: User?): String {
    return user?.company?.address?.street ?: "Unknown"
}

fun getCityUppercase(user: User?): String? {
    return user?.company?.address?.city?.uppercase()
}

fun isFromMoscow(user: User?): Boolean {
    return user?.company?.address?.city == "Moscow"
}

fun main() {
    val user1 = User("Alice", Company("TechCorp", Address("Moscow", "Lenina")))
    val user2 = User("Bob", Company("StartupInc", Address("SPB", null)))
    val user3 = User("Charlie", null)

    println("=== getStreet ===")
    println("User1: ${getStreet(user1)}")  // Lenina
    println("User2: ${getStreet(user2)}")  // Unknown
    println("User3: ${getStreet(user3)}")  // Unknown

    println("\n=== getCityUppercase ===")
    println("User1: ${getCityUppercase(user1)}")  // MOSCOW
    println("User3: ${getCityUppercase(user3)}")  // null

    println("\n=== isFromMoscow ===")
    println("User1: ${isFromMoscow(user1)}")  // true
    println("User2: ${isFromMoscow(user2)}")  // false
}
