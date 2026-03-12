package backend.fd.seminar4.homework.throwing.exercises.task3

/**
 * Задача 3: Когда НЕ нужно выбрасывать исключения
 *
 * Научитесь различать ситуации, когда лучше использовать
 * исключения, null или специальные значения.
 */

/**
 * TODO: Реализуйте функцию поиска пользователя
 *
 * Вопрос: Должна ли эта функция выбрасывать исключение или возвращать null?
 * Обоснуйте свой выбор в комментарии.
 *
 * Обоснование: ...
 */
data class User(val id: Int, val name: String, val email: String)

val users = listOf(
    User(1, "Alice", "alice@example.com"),
    User(2, "Bob", "bob@example.com"),
    User(3, "Charlie", "charlie@example.com")
)

fun findUser(id: Int): User? {
    // TODO: Реализуйте
    // Вопрос: null или исключение?
    return users.find { it.id == id }
}

// Пример использования
fun exampleFindUser() {
    val user = findUser(1)
    if (user != null) {
        println("Найден: ${user.name}")
    } else {
        println("Пользователь не найден")
    }
}

/**
 * TODO: Реализуйте функцию деления чисел
 *
 * Вопрос: Должна ли эта функция выбрасывать исключение
 * при делении на ноль или возвращать специальное значение?
 *
 * Обоснование: ...
 */
fun divide(a: Double, b: Double): Double {
    // TODO: Реализуйте
    // Вопрос: исключение, null, Double.NaN, или что-то еще?
    if (b == 0.0) {
        throw ArithmeticException("Division by zero")
    }
    return a / b
}

/**
 * TODO: Реализуйте функцию парсинга JSON
 *
 * Вопрос: Должна ли эта функция выбрасывать исключение
 * при невалидном JSON?
 *
 * Обоснование: ...
 */
data class JsonObject(val data: Map<String, String>)

fun parseJson(json: String): JsonObject {
    // TODO: Реализуйте (упрощенная версия)
    // Вопрос: исключение или null?

    if (json.isBlank() || !json.startsWith("{")) {
        throw IllegalArgumentException("Invalid JSON format")
    }

    // Упрощенный парсинг
    return JsonObject(emptyMap())
}

/**
 * TODO: Создайте таблицу решений
 *
 * Заполните таблицу для каждого случая:
 *
 * | Функция      | Решение          | Обоснование                          |
 * |--------------|------------------|--------------------------------------|
 * | findUser     | null/исключение  | ...                                  |
 * | divide       | исключение/NaN   | ...                                  |
 * | parseJson    | исключение/null  | ...                                  |
 */

/**
 * TODO: Реализуйте все три подхода для функции поиска
 */

// Подход 1: Исключение
fun findUserException(id: Int): User {
    return users.find { it.id == id }
        ?: throw NoSuchElementException("User $id not found")
}

// Подход 2: Nullable
fun findUserNullable(id: Int): User? {
    return users.find { it.id == id }
}

// Подход 3: Result
fun findUserResult(id: Int): Result<User> = runCatching {
    users.find { it.id == id }
        ?: throw NoSuchElementException("User $id not found")
}

// Подход 4: Sealed class
sealed class FindUserResult {
    data class Success(val user: User) : FindUserResult()
    data class NotFound(val id: Int) : FindUserResult()
}

fun findUserSealed(id: Int): FindUserResult {
    val user = users.find { it.id == id }
    return if (user != null) {
        FindUserResult.Success(user)
    } else {
        FindUserResult.NotFound(id)
    }
}

/**
 * TODO: Напишите код использования для каждого подхода
 * и сравните удобство
 */
fun demonstrateApproaches() {
    println("=== Подход 1: Исключения ===")
    try {
        val user = findUserException(1)
        println("Найден: ${user.name}")
    } catch (e: NoSuchElementException) {
        println("Не найден: ${e.message}")
    }

    println("\n=== Подход 2: Nullable ===")
    // TODO: Реализуйте

    println("\n=== Подход 3: Result ===")
    // TODO: Реализуйте

    println("\n=== Подход 4: Sealed class ===")
    // TODO: Реализуйте
}

/**
 * TODO: Для каждой ситуации определите лучший подход:
 */
fun bestPractices() {
    /**
     * Используйте ИСКЛЮЧЕНИЯ когда:
     * 1. ...
     * 2. ...
     * 3. ...
     */

    /**
     * Используйте NULL когда:
     * 1. ...
     * 2. ...
     * 3. ...
     */

    /**
     * Используйте RESULT когда:
     * 1. ...
     * 2. ...
     * 3. ...
     */

    /**
     * Используйте SEALED CLASS когда:
     * 1. ...
     * 2. ...
     * 3. ...
     */
}

fun main() {
    println("=== Демонстрация подходов ===")
    demonstrateApproaches()

    println("\n=== Тест findUser ===")
    exampleFindUser()

    println("\n=== Тест divide ===")
    try {
        println("10 / 2 = ${divide(10.0, 2.0)}")
        println("10 / 0 = ${divide(10.0, 0.0)}")
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }

    println("\n=== Тест parseJson ===")
    try {
        val json = parseJson("{\"key\": \"value\"}")
        println("JSON распознан: $json")
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }
}
