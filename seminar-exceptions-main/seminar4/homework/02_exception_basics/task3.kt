package backend.fd.seminar4.homework.basics.exercises.task3

import kotlin.math.sqrt

/**
 * Задача 3: Множественный catch
 *
 * Напишите функцию с обработкой нескольких типов исключений.
 */

/**
 * TODO: Реализуйте функцию parseAndCalculate
 *
 * Функция должна:
 * 1. Парсить строку в число (может быть NumberFormatException)
 * 2. Вычислить квадратный корень (может быть IllegalArgumentException если число < 0)
 * 3. Разделить результат на 2
 *
 * Обработайте каждый тип ошибки отдельным catch блоком:
 * - NumberFormatException -> вернуть 0.0
 * - IllegalArgumentException -> вернуть -1.0
 * - Любая другая ошибка -> вернуть -99.0
 */
fun parseAndCalculate(input: String): Double {
    try {
        // TODO: Реализуйте логику
        val number = input.toDouble()

        if (number < 0) {
            throw IllegalArgumentException("Negative number")
        }

        val root = sqrt(number)
        return root / 2

    } catch (e: NumberFormatException) {
        // TODO: обработка
        println("Ошибка парсинга: ${e.message}")
        return 0.0
    } // TODO: Добавьте другие catch блоки
}

/**
 * TODO: Улучшенная версия с Result
 */
fun parseAndCalculateSafe(input: String): Result<Double> {
    // TODO: Реализуйте с использованием runCatching
    return runCatching {
        val number = input.toDouble()
        require(number >= 0) { "Number must be non-negative" }
        sqrt(number) / 2
    }
}

/**
 * TODO: Версия с подробным отчетом об ошибке
 */
sealed class CalculationResult {
    data class Success(val value: Double) : CalculationResult()
    data class ParseError(val input: String, val error: String) : CalculationResult()
    data class ValidationError(val reason: String) : CalculationResult()
    data class UnknownError(val error: Throwable) : CalculationResult()
}

fun parseAndCalculateDetailed(input: String): CalculationResult {
    // TODO: Реализуйте с возвратом детальной информации об ошибке
    return try {
        val number = input.toDouble()
        if (number < 0) {
            CalculationResult.ValidationError("Число должно быть неотрицательным")
        } else {
            CalculationResult.Success(sqrt(number) / 2)
        }
    } catch (e: NumberFormatException) {
        CalculationResult.ParseError(input, e.message ?: "Unknown parse error")
    } catch (e: Exception) {
        CalculationResult.UnknownError(e)
    }
}

fun main() {
    val testCases = listOf(
        "16",      // Успех: sqrt(16)/2 = 2.0
        "abc",     // Ошибка парсинга
        "-4",      // Отрицательное число
        "0"        // Граничный случай
    )

    println("=== Базовая версия ===")
    testCases.forEach { input ->
        val result = parseAndCalculate(input)
        println("Input: '$input' -> Result: $result")
    }

    println("\n=== Версия с Result ===")
    testCases.forEach { input ->
        parseAndCalculateSafe(input)
            .onSuccess { println("Input: '$input' -> Success: $it") }
            .onFailure { println("Input: '$input' -> Error: ${it.message}") }
    }

    println("\n=== Детальная версия ===")
    testCases.forEach { input ->
        when (val result = parseAndCalculateDetailed(input)) {
            is CalculationResult.Success ->
                println("Input: '$input' -> Успех: ${result.value}")

            is CalculationResult.ParseError ->
                println("Input: '$input' -> Ошибка парсинга: ${result.error}")

            is CalculationResult.ValidationError ->
                println("Input: '$input' -> Ошибка валидации: ${result.reason}")

            is CalculationResult.UnknownError ->
                println("Input: '$input' -> Неизвестная ошибка: ${result.error.message}")
        }
    }
}
