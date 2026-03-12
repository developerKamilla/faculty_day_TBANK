package backend.fd.seminar4.homework.trycatch.solutions.task2

/**
 * РЕШЕНИЕ Задачи 2: Порядок выполнения try-catch-finally
 */

// Проблемный код
fun mysteryFunction(): Int {
    try {
        println("Try")
        return 1
    } catch (e: Exception) {
        println("Catch")
        return 2
    } finally {
        println("Finally")
        return 3  // ПРОБЛЕМА!!!
    }
}

/**
 * ОТВЕТЫ:
 *
 * 1. Что выведет функция?
 * Ответ: Try
 *        Finally
 *
 * 2. Какое значение вернется?
 * Ответ: 3 (значение из finally перекрывает return из try!)
 *
 * 3. Почему так происходит?
 * Ответ: Finally выполняется ВСЕГДА, даже если try/catch содержат return.
 *        Если finally содержит return, он перекрывает return из try/catch.
 *        Это антипаттерн! Finally не должен содержать return.
 */

// Исправленная версия
fun correctedFunction(): Int {
    try {
        println("Try")
        return 1
    } catch (e: Exception) {
        println("Catch")
        return 2
    } finally {
        println("Finally")
        // НЕТ return - правильно!
    }
}

// Правильное использование finally с очисткой ресурсов
fun processWithCleanup(input: String): Result<Int> {
    var resource: String? = null

    return try {
        resource = "Resource for $input"
        println("✓ Ресурс создан: $resource")

        val result = input.toInt()
        Result.success(result)

    } catch (e: NumberFormatException) {
        println("✗ Ошибка: ${e.message}")
        Result.failure(e)

    } finally {
        if (resource != null) {
            println("✓ Очистка ресурса: $resource")
            resource = null
        }
    }
}

// Демонстрация всех путей выполнения
fun demonstrateExecutionPaths(scenario: Int): String {
    println("\n=== Сценарий $scenario ===")

    return try {
        println("1. Try блок выполняется")

        when (scenario) {
            1 -> {
                println("2. Нормальное выполнение")
                "success"
            }

            2 -> {
                println("2. Генерация исключения")
                throw IllegalStateException("Test exception")
            }

            3 -> {
                println("2. Early return из try")
                return "early return from try"
            }

            else -> "unknown"
        }

    } catch (e: Exception) {
        println("3. Catch блок: ${e.message}")

        if (scenario == 4) {
            println("4. Early return из catch")
            return "early return from catch"
        }

        "error handled"

    } finally {
        println("5. Finally всегда выполняется")
    }
}

fun main() {
    println("=== Тест 1: Mystery Function (ПЛОХОЙ КОД) ===")
    val result = mysteryFunction()
    println("Результат: $result")  // Вернет 3, а не 1!

    println("\n=== Тест 2: Corrected Function ===")
    val corrected = correctedFunction()
    println("Результат: $corrected")  // Вернет 1

    println("\n=== Тест 3: Process with Cleanup ===")
    processWithCleanup("42")
    processWithCleanup("invalid")

    println("\n=== Тест 4: Execution Paths ===")
    println("Результат 1: ${demonstrateExecutionPaths(1)}")
    println("\nРезультат 2: ${demonstrateExecutionPaths(2)}")
    println("\nРезультат 3: ${demonstrateExecutionPaths(3)}")
    println("\nРезультат 4: ${demonstrateExecutionPaths(4)}")

    println("\n=== ВАЖНО: Правила использования finally ===")
    println("✓ DO: Очистка ресурсов (закрытие файлов, соединений)")
    println("✓ DO: Логирование")
    println("✓ DO: Освобождение locks")
    println("✗ DON'T: return в finally")
    println("✗ DON'T: throw в finally (перекроет исходное исключение)")
    println("✗ DON'T: Бизнес-логика в finally")
}
