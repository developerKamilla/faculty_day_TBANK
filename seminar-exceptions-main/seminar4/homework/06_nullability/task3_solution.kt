package backend.fd.seminar4.homework.nulls.solutions.task3

/**
 * РЕШЕНИЕ Задачи 3: Когда использовать !!
 */

data class User(val name: String, val age: Int, val email: String)

fun findUser(id: String): User? = if (id == "1") User("Alice", 25, "alice@example.com") else null

/**
 * ОТВЕТЫ:
 *
 * 1. Что не так?
 * - Множественные !! - код упадет при null
 * - После первой проверки user!! дальнейшие проверки избыточны
 * - Плохая читаемость
 *
 * 2. Когда !! оправдан?
 * - После явной проверки на null
 * - В тестах
 * - Когда ТОЧНО знаем что значение не null
 * - Лучше использовать редко!
 *
 * 3. Решения:
 */

// Решение 1: Safe call + let
fun processUserSafe(userId: String) {
    findUser(userId)?.let { user ->
        println("User: ${user.name}, ${user.age}, ${user.email}")
    } ?: println("User not found")
}

// Решение 2: Early return
fun processUserEarlyReturn(userId: String) {
    val user = findUser(userId) ?: run {
        println("User not found")
        return
    }
    println("User: ${user.name}, ${user.age}, ${user.email}")
}

// Решение 3: Когда !! оправдан (после проверки)
fun processUserJustified(userId: String) {
    val user = findUser(userId)
    if (user != null) {
        // Здесь !! оправдан (но лучше smart cast)
        println("User: ${user.name}, ${user.age}, ${user.email}")
    } else {
        println("User not found")
    }
}

fun main() {
    println("=== Safe версия ===")
    processUserSafe("1")
    processUserSafe("999")

    println("\n=== Early return ===")
    processUserEarlyReturn("1")
    processUserEarlyReturn("999")

    println("\n=== Justified !! ===")
    processUserJustified("1")

    println("\n=== ПРАВИЛА ===")
    println("✗ Избегайте !! где возможно")
    println("✓ Используйте safe call (?.) и let")
    println("✓ Используйте Elvis (?:) для значений по умолчанию")
    println("✓ !! только после явной проверки (и то лучше smart cast)")
}
