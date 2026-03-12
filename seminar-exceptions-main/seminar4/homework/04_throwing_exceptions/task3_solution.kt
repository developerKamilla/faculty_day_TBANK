package backend.fd.seminar4.homework.throwing.solutions.task3

/**
 * РЕШЕНИЕ Задачи 3: Когда НЕ нужно выбрасывать исключения
 */

data class User(val id: Int, val name: String, val email: String)

val users = listOf(
    User(1, "Alice", "alice@example.com"),
    User(2, "Bob", "bob@example.com"),
    User(3, "Charlie", "charlie@example.com")
)

/**
 * ТАБЛИЦА РЕШЕНИЙ:
 *
 * | Функция   | Решение      | Обоснование                                          |
 * |-----------|--------------|------------------------------------------------------|
 * | findUser  | null         | Отсутствие пользователя - нормальная ситуация        |
 * | divide    | исключение   | Деление на ноль - программная ошибка                 |
 * | parseJson | исключение   | Некорректный JSON - ошибка входных данных            |
 */

// findUser: Nullable (отсутствие - норма)
fun findUser(id: Int): User? = users.find { it.id == id }

// divide: Исключение (деление на ноль - ошибка)
fun divide(a: Double, b: Double): Double {
    if (b == 0.0) throw ArithmeticException("Division by zero")
    return a / b
}

// parseJson: Исключение (некорректные данные - ошибка)
data class JsonObject(val data: Map<String, String>)

fun parseJson(json: String): JsonObject {
    if (json.isBlank() || !json.startsWith("{")) {
        throw IllegalArgumentException("Invalid JSON format: $json")
    }
    return JsonObject(emptyMap())
}

// Четыре подхода для поиска пользователя

// 1. Исключение
fun findUserException(id: Int): User =
    users.find { it.id == id } ?: throw NoSuchElementException("User $id not found")

// 2. Nullable
fun findUserNullable(id: Int): User? = users.find { it.id == id }

// 3. Result
fun findUserResult(id: Int): Result<User> = runCatching {
    users.find { it.id == id } ?: throw NoSuchElementException("User $id not found")
}

// 4. Sealed class
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

fun demonstrateApproaches() {
    println("=== 1. Исключения ===")
    try {
        val user = findUserException(1)
        println("✓ Найден: ${user.name}")

        findUserException(999)  // Упадет
    } catch (e: NoSuchElementException) {
        println("✗ ${e.message}")
    }

    println("\n=== 2. Nullable ===")
    val user2 = findUserNullable(1)
    user2?.let { println("✓ Найден: ${it.name}") }

    val missing = findUserNullable(999)
    if (missing == null) println("✗ Не найден")

    println("\n=== 3. Result ===")
    findUserResult(1)
        .onSuccess { println("✓ Найден: ${it.name}") }
        .onFailure { println("✗ ${it.message}") }

    findUserResult(999)
        .onSuccess { println("Найден: ${it.name}") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== 4. Sealed class ===")
    when (val result = findUserSealed(1)) {
        is FindUserResult.Success -> println("✓ Найден: ${result.user.name}")
        is FindUserResult.NotFound -> println("✗ Не найден: ${result.id}")
    }

    when (val result = findUserSealed(999)) {
        is FindUserResult.Success -> println("Найден: ${result.user.name}")
        is FindUserResult.NotFound -> println("✗ Не найден: ${result.id}")
    }
}

/**
 * BEST PRACTICES:
 */
fun bestPractices() {
    println("\n=== КОГДА ИСПОЛЬЗОВАТЬ ЧТО ===\n")

    println("✓ Используйте ИСКЛЮЧЕНИЯ когда:")
    println("  - Ошибка программиста (нарушение контракта)")
    println("  - Критическая ситуация требующая вмешательства")
    println("  - Невозможно продолжить выполнение")

    println("\n✓ Используйте NULL когда:")
    println("  - Отсутствие значения - нормально (поиск, фильтрация)")
    println("  - Опциональные параметры/поля")
    println("  - Простые случаи где достаточно одного варианта")

    println("\n✓ Используйте RESULT когда:")
    println("  - Операции с внешними ресурсами (файлы, сеть)")
    println("  - Нужна детальная информация об ошибке")
    println("  - Ошибки ожидаемы и должны обрабатываться")

    println("\n✓ Используйте SEALED CLASS когда:")
    println("  - Несколько различных исходов")
    println("  - Нужна дополнительная информация для каждого случая")
    println("  - Важна type-safety и exhaustive when")
}

fun main() {
    println("=== Демонстрация всех подходов ===")
    demonstrateApproaches()

    println("\n=== Примеры из таблицы ===")

    println("\n1. findUser (nullable):")
    val user = findUser(1)
    if (user != null) println("✓ ${user.name}") else println("✗ Не найден")

    println("\n2. divide (исключение):")
    try {
        println("10 / 2 = ${divide(10.0, 2.0)}")
        println("10 / 0 = ${divide(10.0, 0.0)}")
    } catch (e: ArithmeticException) {
        println("✗ ${e.message}")
    }

    println("\n3. parseJson (исключение):")
    try {
        parseJson("{\"valid\": \"json\"}")
        println("✓ JSON валиден")
    } catch (e: IllegalArgumentException) {
        println("✗ ${e.message}")
    }

    bestPractices()
}
