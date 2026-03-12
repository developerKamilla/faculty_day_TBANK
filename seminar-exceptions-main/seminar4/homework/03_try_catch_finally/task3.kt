package backend.fd.seminar4.homework.trycatch.exercises.task3

import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.FileReader

/**
 * Задача 3: use vs try-finally
 *
 * Сравните два подхода к управлению ресурсами.
 */

/**
 * Вариант A: Явный try-finally
 */
fun readFirstLineTryFinally(path: String): String? {
    val reader = BufferedReader(FileReader(path))
    try {
        return reader.readLine()
    } finally {
        reader.close()
    }
}

/**
 * Вариант B: Использование use
 */
fun readFirstLineUse(path: String): String? {
    return BufferedReader(FileReader(path)).use { reader ->
        reader.readLine()
    }
}

/**
 * TODO: Ответьте на вопросы в комментариях:
 *
 * 1. В чем разница между вариантами?
 * Ответ: ...
 *
 * 2. Что произойдет если FileReader выбросит исключение в варианте A?
 * Ответ: ...
 *
 * 3. Как use решает эту проблему?
 * Ответ: ...
 *
 * 4. В чем преимущество use?
 * Ответ: ...
 */

/**
 * TODO: Создайте свой класс Connection и реализуйте работу с use
 */
class DatabaseConnection(private val connectionString: String) : Closeable {

    init {
        println("Подключение к БД: $connectionString")
    }

    fun executeQuery(query: String): List<String> {
        println("Выполнение запроса: $query")
        // Симуляция выполнения запроса
        return listOf("result1", "result2", "result3")
    }

    override fun close() {
        println("Закрытие соединения с БД")
    }
}

/**
 * TODO: Реализуйте функцию с использованием try-finally
 */
fun queryDatabaseTryFinally(query: String): List<String> {
    val connection = DatabaseConnection("jdbc:postgresql://localhost/test")

    try {
        // TODO: Выполните запрос
        return connection.executeQuery(query)
    } finally {
        // TODO: Закройте соединение
        connection.close()
    }
}

/**
 * TODO: Реализуйте функцию с использованием use
 */
fun queryDatabaseUse(query: String): List<String> {
    // TODO: Используйте use для автоматического закрытия
    return DatabaseConnection("jdbc:postgresql://localhost/test").use { connection ->
        connection.executeQuery(query)
    }
}

/**
 * TODO: Реализуйте функцию с обработкой ошибок и use
 */
fun queryDatabaseSafe(query: String): Result<List<String>> = runCatching {
    // TODO: Используйте use внутри runCatching
    DatabaseConnection("jdbc:postgresql://localhost/test").use { connection ->
        connection.executeQuery(query)
    }
}

/**
 * TODO: Дополнительное задание - множественные ресурсы
 * Напишите функцию, которая работает с несколькими ресурсами
 */
fun copyDataBetweenDatabases(sourceQuery: String, targetTable: String): Result<Unit> {
    // TODO: Откройте два соединения и скопируйте данные
    // Используйте вложенные use блоки

    return runCatching {
        DatabaseConnection("jdbc:postgresql://localhost/source").use { source ->
            DatabaseConnection("jdbc:postgresql://localhost/target").use { target ->
                val data = source.executeQuery(sourceQuery)
                println("Копирование ${data.size} записей в $targetTable")
                // target.executeInsert(targetTable, data)
            }
        }
    }
}

fun main() {
    // Создадим тестовый файл
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
        .onSuccess { println("Успех: ${it.size} записей") }
        .onFailure { println("Ошибка: ${it.message}") }

    println("\n=== Тест 6: Копирование между БД ===")
    copyDataBetweenDatabases("SELECT * FROM source_table", "target_table")
        .onSuccess { println("Данные скопированы успешно") }
        .onFailure { println("Ошибка копирования: ${it.message}") }

    // Очистка
    testFile.delete()
}
