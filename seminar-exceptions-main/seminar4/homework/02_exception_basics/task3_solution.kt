package backend.fd.seminar4.homework.basics.solutions.task3

import kotlin.math.sqrt

/**
 * РЕШЕНИЕ Задачи 3: Множественный catch
 */

// Базовая реализация с множественным catch
fun parseAndCalculate(input: String): Double {
    return try {
        val number = input.toDouble()

        if (number < 0) {
            throw IllegalArgumentException("Negative number: $number")
        }

        val root = sqrt(number)
        root / 2

    } catch (e: NumberFormatException) {
        println("✗ Ошибка парсинга: не удалось преобразовать '$input' в число")
        0.0

    } catch (e: IllegalArgumentException) {
        println("✗ Ошибка валидации: ${e.message}")
        -1.0

    } catch (e: Exception) {
        println("✗ Неизвестная ошибка: ${e.message}")
        -99.0
    }
}

// Улучшенная версия с Result
fun parseAndCalculateSafe(input: String): Result<Double> = runCatching {
    val number = input.toDouble()
    require(number >= 0) { "Number must be non-negative, got: $number" }
    sqrt(number) / 2
}

// Версия с подробным отчетом об ошибке
sealed class CalculationResult {
    data class Success(val value: Double) : CalculationResult()
    data class ParseError(val input: String, val error: String) : CalculationResult()
    data class ValidationError(val reason: String) : CalculationResult()
    data class UnknownError(val error: Throwable) : CalculationResult()
}

fun parseAndCalculateDetailed(input: String): CalculationResult {
    return try {
        val number = input.toDouble()

        if (number < 0) {
            CalculationResult.ValidationError("Число должно быть неотрицательным, получено: $number")
        } else {
            val result = sqrt(number) / 2
            CalculationResult.Success(result)
        }

    } catch (e: NumberFormatException) {
        CalculationResult.ParseError(input, "Не удалось преобразовать в число")

    } catch (e: Exception) {
        CalculationResult.UnknownError(e)
    }
}

fun main() {
    val testCases = listOf(
        "16",      // Успех: sqrt(16)/2 = 2.0
        "abc",     // Ошибка парсинга
        "-4",      // Отрицательное число
        "0",       // Граничный случай
        "100"      // sqrt(100)/2 = 5.0
    )

    println("=== Базовая версия с множественным catch ===")
    testCases.forEach { input ->
        val result = parseAndCalculate(input)
        println("Input: '$input' -> Result: $result")
    }

    println("\n=== Версия с Result ===")
    testCases.forEach { input ->
        parseAndCalculateSafe(input)
            .onSuccess { println("Input: '$input' -> ✓ Success: $it") }
            .onFailure { println("Input: '$input' -> ✗ Error: ${it.message}") }
    }

    println("\n=== Детальная версия со Sealed class ===")
    testCases.forEach { input ->
        when (val result = parseAndCalculateDetailed(input)) {
            is CalculationResult.Success ->
                println("Input: '$input' -> ✓ Успех: ${result.value}")

            is CalculationResult.ParseError ->
                println("Input: '$input' -> ✗ Ошибка парсинга: ${result.error}")

            is CalculationResult.ValidationError ->
                println("Input: '$input' -> ✗ Ошибка валидации: ${result.reason}")

            is CalculationResult.UnknownError ->
                println("Input: '$input' -> ✗ Неизвестная ошибка: ${result.error.message}")
        }
    }

    println("\n=== Демонстрация порядка catch блоков ===")
    println("Важно: более специфичные исключения должны идти первыми!")

    // Пример правильного порядка
    try {
        throw NumberFormatException("test")
    } catch (e: NumberFormatException) {
        println("✓ Поймано как NumberFormatException")
    } catch (e: IllegalArgumentException) {
        println("Поймано как IllegalArgumentException")
    } catch (e: Exception) {
        println("Поймано как Exception")
    }
}
