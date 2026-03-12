package backend.fd.seminar4.homework.motivation.solutions.task3

/**
 * РЕШЕНИЕ Задачи 3: Fail Fast vs Defensive Programming
 */

// Подход A: Fail Fast
fun divideFailFast(a: Int, b: Int): Int {
    if (b == 0) throw IllegalArgumentException("Division by zero")
    return a / b
}

// Подход B: Defensive Programming
fun divideDefensive(a: Int, b: Int): Int {
    if (b == 0) return 0
    return a / b
}

/**
 * ОТВЕТЫ:
 *
 * 1. Какой подход лучше и почему?
 * Ответ: В большинстве случаев лучше Fail Fast (подход A), потому что:
 * - Деление на ноль - это программная ошибка, а не нормальная ситуация
 * - Возврат 0 скрывает проблему и может привести к неправильным расчетам
 * - Fail Fast помогает обнаружить ошибку раньше, а не когда результат уже используется
 * - Явное исключение делает код более предсказуемым
 *
 * 2. В каких ситуациях можно использовать подход B?
 * Ответ:
 * - UI компоненты, где нужно показать значение по умолчанию
 * - Калькуляторы, где нужно показать пользователю результат (но лучше показать ошибку!)
 * - Статистические функции, где отсутствие данных - нормальная ситуация
 * - Когда есть осмысленное значение по умолчанию для данного контекста
 *
 * 3. Примеры для каждого подхода:
 */

// Пример 1: Fail Fast уместен - валидация критичных данных
fun calculateSalary(hoursWorked: Double, hourlyRate: Double): Double {
    require(hoursWorked >= 0) { "Hours worked cannot be negative" }
    require(hourlyRate > 0) { "Hourly rate must be positive" }

    return hoursWorked * hourlyRate
}

// Пример 2: Fail Fast уместен - работа с БД
fun getUserById(id: Int): User {
    require(id > 0) { "User ID must be positive" }

    // Если пользователь не найден - это ошибка
    return database.find(id)
        ?: throw IllegalStateException("User with id $id must exist")
}

// Пример 3: Defensive уместен - UI отображение
fun formatPercentage(value: Double?, total: Double?): String {
    // В UI часто нужны значения по умолчанию
    if (value == null || total == null || total == 0.0) {
        return "N/A"  // Показываем пользователю понятное сообщение
    }
    return "${(value / total * 100).toInt()}%"
}

// Пример 4: Defensive уместен - опциональная статистика
fun calculateAverageRating(ratings: List<Int>): Double {
    // Отсутствие рейтингов - нормальная ситуация
    if (ratings.isEmpty()) {
        return 0.0  // Значение по умолчанию
    }
    return ratings.average()
}

// Решение с Result - золотая середина
fun divideSafe(a: Int, b: Int): Result<Int> = runCatching {
    if (b == 0) throw ArithmeticException("Division by zero")
    a / b
}

// Решение с Sealed class - явное представление всех случаев
sealed class DivisionResult {
    data class Success(val value: Int) : DivisionResult()
    object DivisionByZero : DivisionResult()
}

fun divideExplicit(a: Int, b: Int): DivisionResult {
    return if (b == 0) {
        DivisionResult.DivisionByZero
    } else {
        DivisionResult.Success(a / b)
    }
}

// Заглушки для примеров
data class User(val id: Int, val name: String)
object database {
    fun find(id: Int): User? = if (id == 1) User(1, "Alice") else null
}

/**
 * ИТОГОВАЯ ТАБЛИЦА РЕШЕНИЙ:
 *
 * | Ситуация                          | Подход          | Обоснование                                    |
 * |-----------------------------------|-----------------|------------------------------------------------|
 * | Валидация входных данных          | Fail Fast       | Ранее обнаружение ошибок программиста          |
 * | Критичные бизнес-операции         | Fail Fast       | Финансовые потери при скрытых ошибках          |
 * | Работа с БД (обязательные данные) | Fail Fast       | Отсутствие данных - ошибка состояния           |
 * | UI отображение                    | Defensive       | Пользователю нужны значения по умолчанию       |
 * | Опциональная статистика           | Defensive       | Отсутствие данных - нормально                  |
 * | API возвращающий опциональные     | Nullable/Result | Явно показываем возможность отсутствия         |
 * | Операции с внешними ресурсами     | Result          | Ошибки ожидаемы и должны обрабатываться        |
 */

fun main() {
    println("=== Тест 1: Fail Fast ===")
    try {
        println("10 / 2 = ${divideFailFast(10, 2)}")
        println("10 / 0 = ${divideFailFast(10, 0)}")
    } catch (e: Exception) {
        println("✗ Ошибка обнаружена: ${e.message}")
    }

    println("\n=== Тест 2: Defensive ===")
    println("10 / 2 = ${divideDefensive(10, 2)}")
    println("10 / 0 = ${divideDefensive(10, 0)}")  // Вернет 0, скрывает проблему!

    println("\n=== Тест 3: Safe (Result) ===")
    divideSafe(10, 2)
        .onSuccess { println("10 / 2 = $it") }
        .onFailure { println("Ошибка: ${it.message}") }

    divideSafe(10, 0)
        .onSuccess { println("10 / 0 = $it") }
        .onFailure { println("✗ Ошибка: ${it.message}") }

    println("\n=== Тест 4: Explicit (Sealed class) ===")
    when (val result = divideExplicit(10, 2)) {
        is DivisionResult.Success -> println("10 / 2 = ${result.value}")
        is DivisionResult.DivisionByZero -> println("✗ Деление на ноль")
    }

    when (val result = divideExplicit(10, 0)) {
        is DivisionResult.Success -> println("10 / 0 = ${result.value}")
        is DivisionResult.DivisionByZero -> println("✗ Деление на ноль (обработано явно)")
    }

    println("\n=== Примеры из реальной жизни ===")

    println("\n1. Fail Fast - расчет зарплаты:")
    try {
        val salary = calculateSalary(160.0, 50.0)
        println("Зарплата: $salary")

        calculateSalary(-10.0, 50.0)  // Упадет
    } catch (e: IllegalArgumentException) {
        println("✗ Ошибка в данных: ${e.message}")
    }

    println("\n2. Defensive - форматирование процента:")
    println("50 из 100: ${formatPercentage(50.0, 100.0)}")
    println("null из null: ${formatPercentage(null, null)}")
    println("50 из 0: ${formatPercentage(50.0, 0.0)}")

    println("\n3. Defensive - средний рейтинг:")
    println("Рейтинги [5, 4, 5]: ${calculateAverageRating(listOf(5, 4, 5))}")
    println("Нет рейтингов: ${calculateAverageRating(emptyList())}")
}
