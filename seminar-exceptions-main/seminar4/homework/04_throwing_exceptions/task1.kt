package backend.fd.seminar4.homework.throwing.exercises.task1

/**
 * Задача 1: Валидация параметров
 *
 * Реализуйте функцию регистрации пользователя с валидацией.
 */

data class User(
    val username: String,
    val email: String,
    val age: Int
)

/**
 * TODO: Реализуйте функцию registerUser с валидацией
 *
 * Требования валидации:
 * 1. username не пустой и длиной от 3 до 20 символов
 * 2. email содержит символ @
 * 3. age от 18 до 120
 *
 * При невалидных данных выбрасывайте IllegalArgumentException
 * с информативным сообщением
 */
fun registerUser(username: String, email: String, age: Int): User {
    // TODO: Используйте require для валидации
    // Пример: require(username.isNotEmpty()) { "Username cannot be empty" }

    require(username.isNotBlank()) { "Username cannot be empty" }
    // TODO: Добавьте остальные проверки

    return User(username, email, age)
}

/**
 * TODO: Улучшенная версия с собственными исключениями
 */
sealed class ValidationException(message: String) : Exception(message)

class InvalidUsernameException(username: String, reason: String) :
    ValidationException("Invalid username '$username': $reason")

class InvalidEmailException(email: String) :
    ValidationException("Invalid email '$email': must contain @")

class InvalidAgeException(age: Int) :
    ValidationException("Invalid age $age: must be between 18 and 120")

fun registerUserStrict(username: String, email: String, age: Int): User {
    // TODO: Используйте специфичные исключения
    when {
        username.isBlank() -> throw InvalidUsernameException(username, "cannot be empty")
        username.length < 3 -> throw InvalidUsernameException(username, "too short (min 3 chars)")
        username.length > 20 -> throw InvalidUsernameException(username, "too long (max 20 chars)")
    }

    // TODO: Добавьте проверки email и age

    return User(username, email, age)
}

/**
 * TODO: Версия с Result и накоплением ошибок
 */
data class ValidationErrors(val errors: List<String>) : Exception(errors.joinToString("; "))

fun registerUserSafe(username: String, email: String, age: Int): Result<User> {
    val errors = mutableListOf<String>()

    // TODO: Соберите все ошибки валидации
    if (username.isBlank()) errors.add("Username cannot be empty")
    // TODO: Добавьте остальные проверки

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
        println("Пользователь создан: $user")
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }

    println("\n=== Тест 2: Невалидный username ===")
    try {
        registerUser("ab", "test@test.com", 25)
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }

    println("\n=== Тест 3: Невалидный age ===")
    try {
        registerUser("john", "john@example.com", 15)
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }

    println("\n=== Тест 4: Строгая валидация ===")
    try {
        registerUserStrict("a", "invalid-email", 200)
    } catch (e: ValidationException) {
        println("Ошибка: ${e.message}")
    }

    println("\n=== Тест 5: Safe версия со всеми ошибками ===")
    registerUserSafe("", "no-at-sign", 15)
        .onSuccess { println("Успех: $it") }
        .onFailure { println("Все ошибки: ${it.message}") }
}
