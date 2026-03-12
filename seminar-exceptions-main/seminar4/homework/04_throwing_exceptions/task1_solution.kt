package backend.fd.seminar4.homework.throwing.solutions.task1

/**
 * РЕШЕНИЕ Задачи 1: Валидация параметров
 */

data class User(val username: String, val email: String, val age: Int)

// Базовая версия с require
fun registerUser(username: String, email: String, age: Int): User {
    require(username.isNotBlank()) { "Username cannot be empty" }
    require(username.length in 3..20) { "Username must be between 3 and 20 characters, got ${username.length}" }
    require(email.contains("@")) { "Email must contain @, got: $email" }
    require(age in 18..120) { "Age must be between 18 and 120, got: $age" }

    return User(username, email, age)
}

// Версия с кастомными исключениями
sealed class ValidationException(message: String) : Exception(message)

class InvalidUsernameException(username: String, reason: String) :
    ValidationException("Invalid username '$username': $reason")

class InvalidEmailException(email: String) :
    ValidationException("Invalid email '$email': must contain @")

class InvalidAgeException(age: Int) :
    ValidationException("Invalid age $age: must be between 18 and 120")

fun registerUserStrict(username: String, email: String, age: Int): User {
    when {
        username.isBlank() -> throw InvalidUsernameException(username, "cannot be empty")
        username.length < 3 -> throw InvalidUsernameException(username, "too short (min 3 chars)")
        username.length > 20 -> throw InvalidUsernameException(username, "too long (max 20 chars)")
    }

    if (!email.contains("@")) {
        throw InvalidEmailException(email)
    }

    if (age !in 18..120) {
        throw InvalidAgeException(age)
    }

    return User(username, email, age)
}

// Версия с накоплением всех ошибок
data class ValidationErrors(val errors: List<String>) : Exception(errors.joinToString("; "))

fun registerUserSafe(username: String, email: String, age: Int): Result<User> {
    val errors = mutableListOf<String>()

    if (username.isBlank()) errors.add("Username cannot be empty")
    if (username.length < 3) errors.add("Username too short (min 3)")
    if (username.length > 20) errors.add("Username too long (max 20)")
    if (!email.contains("@")) errors.add("Email must contain @")
    if (age < 18) errors.add("Age must be at least 18")
    if (age > 120) errors.add("Age must be at most 120")

    return if (errors.isEmpty()) {
        Result.success(User(username, email, age))
    } else {
        Result.failure(ValidationErrors(errors))
    }
}

fun main() {
    println("=== Тест 1: Валидный пользователь ===")
    try {
        val user = registerUser("john_doe", "john@example.com", 25)
        println("✓ Пользователь создан: $user")
    } catch (e: Exception) {
        println("✗ Ошибка: ${e.message}")
    }

    println("\n=== Тест 2: Невалидный username (короткий) ===")
    try {
        registerUser("ab", "test@test.com", 25)
    } catch (e: IllegalArgumentException) {
        println("✗ ${e.message}")
    }

    println("\n=== Тест 3: Невалидный age ===")
    try {
        registerUser("john", "john@example.com", 15)
    } catch (e: IllegalArgumentException) {
        println("✗ ${e.message}")
    }

    println("\n=== Тест 4: Строгая валидация ===")
    try {
        registerUserStrict("a", "invalid-email", 200)
    } catch (e: ValidationException) {
        println("✗ ${e.message}")
    }

    println("\n=== Тест 5: Safe версия со всеми ошибками ===")
    registerUserSafe("", "no-at-sign", 15)
        .onSuccess { println("✓ $it") }
        .onFailure { println("✗ Все ошибки:\n  ${it.message}") }

    println("\n=== Тест 6: Успешная safe валидация ===")
    registerUserSafe("alice", "alice@example.com", 30)
        .onSuccess { println("✓ Пользователь создан: $it") }
}
