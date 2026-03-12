package backend.fd.seminar4.homework.comparison.solutions.task3

import java.io.File

/**
 * РЕШЕНИЕ Задачи 3: Проектирование API
 */

// fileExists: Boolean (простая проверка)
fun fileExists(path: String): Boolean = File(path).exists()

// readFile: Result<String> (может быть ошибка)
fun readFile(path: String): Result<String> = runCatching {
    File(path).readText()
}

// writeFile: Result<Unit> (может быть ошибка)
fun writeFile(path: String, content: String): Result<Unit> = runCatching {
    File(path).writeText(content)
}

// copyFile: Result<Unit> (сложная операция)
fun copyFile(source: String, dest: String): Result<Unit> = runCatching {
    File(source).copyTo(File(dest), overwrite = true)
}

// getFileSize: Result<Long> (может не существовать)
fun getFileSize(path: String): Result<Long> = runCatching {
    File(path).length()
}

/**
 * SUMMARY - КОГДА ИСПОЛЬЗОВАТЬ:
 *
 * Boolean: Простые проверки да/нет (exists, isEmpty, isValid)
 *
 * T?: Опциональные значения где отсутствие - норма (find, get, first)
 *
 * Result<T>: Операции которые могут упасть (файлы, сеть, парсинг)
 *
 * Sealed class: Множество исходов с дополнительными данными
 */

fun main() {
    println("=== fileExists (Boolean) ===")
    println("File exists: ${fileExists("/tmp/test.txt")}")

    println("\n=== readFile (Result) ===")
    readFile("/tmp/test.txt")
        .onSuccess { println("✓ Content: $it") }
        .onFailure { println("✗ Error: ${it.message}") }

    println("\n=== writeFile (Result) ===")
    writeFile("/tmp/test.txt", "Hello")
        .onSuccess { println("✓ Written") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== getFileSize (Result) ===")
    getFileSize("/tmp/test.txt")
        .onSuccess { println("✓ Size: $it bytes") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== РЕКОМЕНДАЦИИ ===")
    println("Boolean: exists(), isValid(), isEmpty()")
    println("T?: find(), firstOrNull(), getOrNull()")
    println("Result<T>: read(), write(), parse(), fetch()")
    println("Sealed: authenticate(), processOrder(), validate()")
}
