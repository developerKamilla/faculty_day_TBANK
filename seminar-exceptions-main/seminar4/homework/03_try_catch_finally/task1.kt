package backend.fd.seminar4.homework.trycatch.exercises.task1

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

/**
 * Задача 1: Правильное использование finally
 *
 * Реализуйте функцию для работы с файлом, которая гарантирует закрытие ресурсов.
 */

/**
 * TODO: Реализуйте функцию processFile
 *
 * Требования:
 * 1. Открыть файл для чтения
 * 2. Прочитать все строки
 * 3. Отфильтровать пустые строки
 * 4. Обязательно закрыть файл в finally (даже если произошла ошибка)
 * 5. Обработать FileNotFoundException отдельно от IOException
 * 6. Вернуть пустой список при ошибках
 */
fun processFile(filename: String): List<String> {
    var reader: BufferedReader? = null

    try {
        // TODO: Реализуйте чтение файла
        reader = BufferedReader(FileReader(filename))
        val lines = mutableListOf<String>()

        // TODO: Прочитайте и отфильтруйте строки

        return lines

    } catch (e: FileNotFoundException) {
        // TODO: Обработка отсутствия файла
        println("Файл не найден: ${e.message}")
        return emptyList()

    } catch (e: IOException) {
        // TODO: Обработка ошибок чтения
        println("Ошибка чтения файла: ${e.message}")
        return emptyList()

    } finally {
        // TODO: Обязательно закрыть reader
        try {
            reader?.close()
            println("Файл закрыт")
        } catch (e: IOException) {
            println("Ошибка при закрытии файла: ${e.message}")
        }
    }
}

/**
 * TODO: Реализуйте улучшенную версию с use
 */
fun processFileImproved(filename: String): List<String> {
    // TODO: Используйте конструкцию use { }
    // Use автоматически закроет ресурс

    return try {
        BufferedReader(FileReader(filename)).use { reader ->
            // TODO: Прочитайте и отфильтруйте строки
            emptyList()
        }
    } catch (e: FileNotFoundException) {
        println("Файл не найден: ${e.message}")
        emptyList()
    } catch (e: IOException) {
        println("Ошибка чтения: ${e.message}")
        emptyList()
    }
}

/**
 * TODO: Версия с Result
 */
fun processFileSafe(filename: String): Result<List<String>> = runCatching {
    BufferedReader(FileReader(filename)).use { reader ->
        // TODO: Реализуйте
        emptyList()
    }
}

fun main() {
    // Создадим тестовый файл
    val testFile = File("test_lines.txt")
    testFile.writeText(
        """
        First line

        Second line

        Third line
    """.trimIndent()
    )

    println("=== Тест 1: Чтение существующего файла ===")
    val lines = processFile("test_lines.txt")
    println("Прочитано строк: ${lines.size}")
    lines.forEach { println("  - $it") }

    println("\n=== Тест 2: Несуществующий файл ===")
    val empty = processFile("nonexistent.txt")
    println("Прочитано строк: ${empty.size}")

    println("\n=== Тест 3: Улучшенная версия ===")
    val linesImproved = processFileImproved("test_lines.txt")
    println("Прочитано строк: ${linesImproved.size}")

    // Очистка
    testFile.delete()
}
