package backend.fd.seminar4.homework.trycatch.solutions.task1

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

/**
 * РЕШЕНИЕ Задачи 1: Правильное использование finally
 */

// Решение с явным try-finally
fun processFile(filename: String): List<String> {
    var reader: BufferedReader? = null

    try {
        reader = BufferedReader(FileReader(filename))
        return reader.readLines().filter { it.isNotBlank() }

    } catch (e: FileNotFoundException) {
        println("✗ Файл не найден: ${e.message}")
        return emptyList()

    } catch (e: IOException) {
        println("✗ Ошибка чтения файла: ${e.message}")
        return emptyList()

    } finally {
        try {
            reader?.close()
            println("✓ Файл закрыт")
        } catch (e: IOException) {
            println("✗ Ошибка при закрытии файла: ${e.message}")
        }
    }
}

// Решение с use (рекомендуемый подход)
fun processFileImproved(filename: String): List<String> {
    return try {
        BufferedReader(FileReader(filename)).use { reader ->
            reader.readLines().filter { it.isNotBlank() }
        }
    } catch (e: FileNotFoundException) {
        println("✗ Файл не найден: ${e.message}")
        emptyList()
    } catch (e: IOException) {
        println("✗ Ошибка чтения: ${e.message}")
        emptyList()
    }
}

// Версия с Result
fun processFileSafe(filename: String): Result<List<String>> = runCatching {
    BufferedReader(FileReader(filename)).use { reader ->
        reader.readLines().filter { it.isNotBlank() }
    }
}

fun main() {
    // Создание тестового файла
    val testFile = File("test_lines.txt")
    testFile.writeText(
        """
        First line

        Second line

        Third line
    """.trimIndent()
    )

    println("=== Тест 1: Чтение с try-finally ===")
    val lines = processFile("test_lines.txt")
    println("Прочитано строк: ${lines.size}")
    lines.forEach { println("  - $it") }

    println("\n=== Тест 2: Несуществующий файл ===")
    val empty = processFile("nonexistent.txt")
    println("Прочитано строк: ${empty.size}")

    println("\n=== Тест 3: Версия с use ===")
    val linesImproved = processFileImproved("test_lines.txt")
    println("Прочитано строк: ${linesImproved.size}")

    println("\n=== Тест 4: Safe версия ===")
    processFileSafe("test_lines.txt")
        .onSuccess { println("✓ Успех: ${it.size} строк") }
        .onFailure { println("✗ Ошибка: ${it.message}") }

    // Очистка
    testFile.delete()
}
