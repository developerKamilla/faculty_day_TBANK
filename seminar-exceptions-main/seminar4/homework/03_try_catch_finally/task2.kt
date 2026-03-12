package backend.fd.seminar4.homework.trycatch.exercises.task2

/**
 * Задача 2: Порядок выполнения try-catch-finally
 *
 * Исследуйте порядок выполнения блоков и возвращаемые значения.
 */

/**
 * TODO: Проанализируйте эту функцию
 */
fun mysteryFunction(): Int {
    try {
        println("Try")
        return 1
    } catch (e: Exception) {
        println("Catch")
        return 2
    } finally {
        println("Finally")
        return 3  // ПРОБЛЕМА: finally перекрывает return из try!
    }
}

/**
 * Вопросы (ответьте в комментариях):
 *
 * 1. Что выведет функция?
 * Ответ: ...
 *
 * 2. Какое значение вернется?
 * Ответ: ...
 *
 * 3. Почему так происходит?
 * Ответ: ...
 */

/**
 * TODO: Перепишите функцию корректно
 * Finally НЕ должен возвращать значение!
 */
fun correctedFunction(): Int {
    // TODO: Исправьте код
    try {
        println("Try")
        return 1
    } catch (e: Exception) {
        println("Catch")
        return 2
    } finally {
        println("Finally")
        // НЕ возвращайте значение из finally!
    }
}

/**
 * TODO: Реализуйте функцию, демонстрирующую правильное использование finally
 */
fun processWithCleanup(input: String): Result<Int> {
    var resource: String? = null

    return try {
        // Инициализация ресурса
        resource = "Resource for $input"
        println("Ресурс создан: $resource")

        // Обработка
        val result = input.toInt()
        Result.success(result)

    } catch (e: NumberFormatException) {
        println("Ошибка: ${e.message}")
        Result.failure(e)

    } finally {
        // Очистка ресурса
        if (resource != null) {
            println("Очистка ресурса: $resource")
            resource = null
        }
    }
}

/**
 * TODO: Дополнительное задание
 * Напишите функцию, которая демонстрирует все возможные пути выполнения
 */
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
        // Не используйте return здесь!
    }
}

fun main() {
    println("=== Тест 1: Mystery Function ===")
    val result = mysteryFunction()
    println("Результат: $result\n")

    println("=== Тест 2: Corrected Function ===")
    val corrected = correctedFunction()
    println("Результат: $corrected\n")

    println("=== Тест 3: Process with Cleanup ===")
    processWithCleanup("42")
    processWithCleanup("invalid")

    println("\n=== Тест 4: Execution Paths ===")
    println("Результат 1: ${demonstrateExecutionPaths(1)}")
    println("\nРезультат 2: ${demonstrateExecutionPaths(2)}")
    println("\nРезультат 3: ${demonstrateExecutionPaths(3)}")
    println("\nРезультат 4: ${demonstrateExecutionPaths(4)}")
}
