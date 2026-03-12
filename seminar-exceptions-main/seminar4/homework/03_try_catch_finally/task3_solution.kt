package backend.fd.seminar4.homework.trycatch.solutions.task3

import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.FileReader

/**
 * РЕШЕНИЕ Задачи 3: use vs try-finally
 */

/**
 * ОТВЕТЫ НА ВОПРОСЫ:
 *
 * 1. В чем разница между вариантами?
 * - try-finally: Вручную управляем закрытием ресурса
 * - use: Автоматически закрывает Closeable ресурс
 *
 * 2. Что произойдет если FileReader выбросит исключение в варианте A?
 * - reader не будет инициализирован
 * - finally попытается закрыть несуществующий reader
 * - Возможна утечка ресурсов!
 *
 * 3. Как use решает эту проблему?
 * - use гарантирует вызов close() даже при исключении
 * - Работает как try-with-resources в Java
 * - Более безопасный и короткий код
 *
 * 4. Преимущества use:
 * - Меньше кода
 * - Автоматическое управление ресурсами
 * - Невозможно забыть закрыть ресурс
 * - Правильная обработка вложенных исключений
 */

// Вариант A: try-finally (ПРОБЛЕМНЫЙ при ошибке открытия)
fun readFirstLineTryFinally(path: String): String? {
    val reader = BufferedReader(FileReader(path))  // Может упасть!
    try {
        return reader.readLine()
    } finally {
        reader.close()  // Не выполнится если FileReader упал
    }
}

// Вариант B: use (ПРАВИЛЬНЫЙ)
fun readFirstLineUse(path: String): String? {
    return BufferedReader(FileReader(path)).use { reader ->
        reader.readLine()
    }
}

// Кастомный класс с Closeable
class DatabaseConnection(private val connectionString: String) : Closeable {
    init {
        println("✓ Подключение к БД: $connectionString")
    }

    fun executeQuery(query: String): List<String> {
        println("✓ Выполнение запроса: $query")
        return listOf("result1", "result2", "result3")
    }

    override fun close() {
        println("✓ Закрытие соединения с БД")
    }
}

// Решение с try-finally
fun queryDatabaseTryFinally(query: String): List<String> {
    val connection = DatabaseConnection("jdbc:postgresql://localhost/test")
    try {
        return connection.executeQuery(query)
    } finally {
        connection.close()
    }
}

// Решение с use (ЛУЧШЕ)
fun queryDatabaseUse(query: String): List<String> {
    return DatabaseConnection("jdbc:postgresql://localhost/test").use { connection ->
        connection.executeQuery(query)
    }
}

// Safe версия с Result
fun queryDatabaseSafe(query: String): Result<List<String>> = runCatching {
    DatabaseConnection("jdbc:postgresql://localhost/test").use { connection ->
        connection.executeQuery(query)
    }
}

// Множественные ресурсы с вложенными use
fun copyDataBetweenDatabases(sourceQuery: String, targetTable: String): Result<Unit> {
    return runCatching {
        DatabaseConnection("jdbc:postgresql://localhost/source").use { source ->
            DatabaseConnection("jdbc:postgresql://localhost/target").use { target ->
                val data = source.executeQuery(sourceQuery)
                println("✓ Копирование ${data.size} записей в $targetTable")
                // В реальности: target.executeInsert(targetTable, data)
            }
        }
    }
}

fun main() {
    // Создание тестового файла
    val testFile = File("test_readline.txt")
    testFile.writeText("First line\nSecond line\nThird line")

    println("=== Тест 1: try-finally ===")
    val line1 = readFirstLineTryFinally("test_readline.txt")
    println("Прочитано: $line1")

    println("\n=== Тест 2: use ===")
    val line2 = readFirstLineUse("test_readline.txt")
    println("Прочитано: $line2")

    println("\n=== Тест 3: Database try-finally ===")
    val result1 = queryDatabaseTryFinally("SELECT * FROM users")
    println("Результаты: ${result1.size} записей")

    println("\n=== Тест 4: Database use ===")
    val result2 = queryDatabaseUse("SELECT * FROM users")
    println("Результаты: ${result2.size} записей")

    println("\n=== Тест 5: Safe query ===")
    queryDatabaseSafe("SELECT * FROM users")
        .onSuccess { println("✓ Успех: ${it.size} записей") }
        .onFailure { println("✗ Ошибка: ${it.message}") }

    println("\n=== Тест 6: Множественные ресурсы ===")
    copyDataBetweenDatabases("SELECT * FROM source_table", "target_table")
        .onSuccess { println("✓ Данные скопированы успешно") }
        .onFailure { println("✗ Ошибка: ${it.message}") }

    // Очистка
    testFile.delete()

    println("\n=== ВЫВОДЫ: Когда использовать что ===")
    println("✓ Используйте use для:")
    println("  - Любых Closeable ресурсов")
    println("  - Файлов, потоков, соединений")
    println("  - Вложенных ресурсов")
    println("\n✗ try-finally нужен только когда:")
    println("  - Ресурс не реализует Closeable")
    println("  - Нужна кастомная логика очистки")
}
