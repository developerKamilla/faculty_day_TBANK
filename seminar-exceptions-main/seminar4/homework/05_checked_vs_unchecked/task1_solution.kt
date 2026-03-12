package backend.fd.seminar4.homework.checked.solutions.task1

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * РЕШЕНИЕ Задачи 1: Анализ Java кода
 */

/**
 * ОТВЕТЫ:
 *
 * 1. Почему в Kotlin не нужно указывать throws?
 * Ответ: В Kotlin ВСЕ исключения являются unchecked. Нет разделения на checked/unchecked.
 *        Это сделано намеренно, т.к. checked exceptions в Java часто приводят к:
 *        - Избыточному коду с пустыми catch блоками
 *        - Проблемам с lambda и функциональным программированием
 *        - Вынужденному использованию RuntimeException для обхода системы
 *
 * 2. Нужно ли обрабатывать IOException при вызове в Kotlin?
 * Ответ: НЕТ, компилятор НЕ ТРЕБУЕТ обработки. Но обрабатывать РЕКОМЕНДУЕТСЯ!
 *        IOException - это ожидаемая ошибка при работе с файлами.
 *
 * 3. Плюсы подхода Kotlin:
 * - Меньше boilerplate кода
 * - Проще работать с lambda и функциями высшего порядка
 * - Не навязывает обработку там, где она не нужна
 * - Избавляет от пустых catch блоков
 *
 * 4. Минусы подхода Kotlin:
 * - Легко забыть обработать важные исключения
 * - Нет помощи компилятора в определении ошибочных путей
 * - Нужна дисциплина и code review
 */

// Kotlin версия (без throws)
fun readFile(path: String): String? {
    return try {
        BufferedReader(FileReader(path)).use { reader ->
            reader.readLine()
        }
    } catch (e: IOException) {
        println("✗ Ошибка чтения файла: ${e.message}")
        null
    }
}

// Версия БЕЗ обработки (компилируется!)
fun readFileNoHandling(path: String): String? {
    // В Kotlin это скомпилируется!
    // В Java не скомпилировалось бы без try-catch или throws
    return BufferedReader(FileReader(path)).use { it.readLine() }
}

// Версия с Result (рекомендуется)
fun readFileSafe(path: String): Result<String> = runCatching {
    BufferedReader(FileReader(path)).use { reader ->
        reader.readLine() ?: throw IOException("File is empty")
    }
}

fun processFile(path: String) {
    // Вариант 1: С обработкой (хорошо)
    readFile(path)?.let { content ->
        println("✓ Прочитано: $content")
    } ?: println("✗ Не удалось прочитать файл")

    // Вариант 2: Без обработки (компилируется, но опасно!)
    // val content = readFileNoHandling(path)  // Может упасть!

    // Вариант 3: С Result (лучше всего)
    readFileSafe(path)
        .onSuccess { println("✓ Прочитано: $it") }
        .onFailure { println("✗ Ошибка: ${it.message}") }
}

fun main() {
    // Создание тестового файла
    val testFile = java.io.File("test.txt")
    testFile.writeText("Hello Kotlin!")

    println("=== Тест 1: С обработкой ===")
    val content = readFile("test.txt")
    println("Результат: $content")

    println("\n=== Тест 2: Несуществующий файл ===")
    readFile("missing.txt")

    println("\n=== Тест 3: Safe версия ===")
    readFileSafe("test.txt")
        .onSuccess { println("✓ $it") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== Тест 4: processFile ===")
    processFile("test.txt")

    // Очистка
    testFile.delete()

    println("\n=== ВЫВОДЫ ===")
    println("✓ Kotlin: ВСЕ исключения unchecked")
    println("✓ Свобода: можно обрабатывать или нет")
    println("✗ Ответственность: нужна дисциплина")
    println("✓ Рекомендация: используйте Result для явной обработки")
}
