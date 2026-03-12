package backend.fd.seminar4.homework.comparison.solutions.task1

/**
 * РЕШЕНИЕ Задачи 1: Четыре способа
 */

data class User(val id: Int, val name: String)

val users = listOf(User(1, "Alice"), User(2, "Bob"))

// 1. Исключения
fun findUserException(id: Int): User =
    users.find { it.id == id } ?: throw NoSuchElementException("User $id not found")

// 2. Nullable
fun findUserNullable(id: Int): User? = users.find { it.id == id }

// 3. Result
fun findUserResult(id: Int): Result<User> = runCatching {
    users.find { it.id == id } ?: throw NoSuchElementException("User $id not found")
}

// 4. Sealed class
sealed class UserResult {
    data class Success(val user: User) : UserResult()
    data class NotFound(val id: Int) : UserResult()
}

fun findUserSealed(id: Int): UserResult {
    val user = users.find { it.id == id }
    return if (user != null) UserResult.Success(user) else UserResult.NotFound(id)
}

fun main() {
    println("=== 1. Исключения ===")
    try {
        println(findUserException(1))
    } catch (e: Exception) {
        println("✗ ${e.message}")
    }

    println("\n=== 2. Nullable ===")
    findUserNullable(1)?.let { println("✓ $it") } ?: println("✗ Not found")

    println("\n=== 3. Result ===")
    findUserResult(1)
        .onSuccess { println("✓ $it") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== 4. Sealed class ===")
    when (val result = findUserSealed(1)) {
        is UserResult.Success -> println("✓ ${result.user}")
        is UserResult.NotFound -> println("✗ Not found: ${result.id}")
    }

    println("\n=== КОГДА ИСПОЛЬЗОВАТЬ ===")
    println("Exceptions: Ошибки программиста, критичные ситуации")
    println("Nullable: Опциональные значения, простые случаи")
    println("Result: Ожидаемые ошибки, внешние ресурсы")
    println("Sealed: Несколько исходов, type-safety")
}
