package backend.fd.seminar4.homework.basics.exercises.task2

import java.io.File

/**
 * Задача 2: Перехват исключений
 *
 * Напишите обработчик для чтения и парсинга конфигурационного файла.
 */

data class Config(
    val host: String,
    val port: Int,
    val timeout: Int
) {
    companion object {
        fun default() = Config("localhost", 8080, 30)
    }
}

/**
 * TODO: Реализуйте функцию parseConfig
 * Она должна парсить строку формата: "host:port:timeout"
 * Например: "localhost:8080:30"
 */
fun parseConfig(content: String): Config {
    // TODO: Реализуйте парсинг
    // Подсказка: используйте split(":") и toInt()
    // Может выбросить NumberFormatException, IndexOutOfBoundsException

    throw NotImplementedError("Реализуйте parseConfig")
}

/**
 * TODO: Реализуйте функцию readConfig с обработкой ошибок
 *
 * Требования:
 * 1. Прочитать файл
 * 2. Распарсить содержимое
 * 3. Обработать ошибки чтения файла (FileNotFoundException, IOException)
 * 4. Обработать ошибки парсинга (NumberFormatException, IndexOutOfBoundsException)
 * 5. Вернуть Config.default() при любой ошибке
 * 6. Залогировать все ошибки
 */
fun readConfig(filename: String): Config {
    // TODO: Реализуйте с обработкой ошибок
    try {
        val file = File(filename)
        val content = file.readText()
        return parseConfig(content)
    } catch (e: Exception) {
        // TODO: добавьте специфичные обработчики для FileNotFoundException и других
        // TODO: логирование и возврат default
    }

    return Config.default()
}

/**
 * Дополнительно: реализуйте версию с Result
 */
fun readConfigSafe(filename: String): Result<Config> = runCatching {
    // TODO: Реализуйте
    val file = File(filename)
    val content = file.readText()
    parseConfig(content)
}.recoverCatching {
    // TODO: логирование
    Config.default()
}

fun main() {
    println("=== Тест 1: Чтение конфигурации ===")

    // Создадим тестовый файл
    val testFile = File("test_config.txt")
    testFile.writeText("example.com:9000:60")

    val config = readConfig("test_config.txt")
    println("Config: host=${config.host}, port=${config.port}, timeout=${config.timeout}")

    println("\n=== Тест 2: Несуществующий файл ===")
    val configDefault = readConfig("nonexistent.txt")
    println("Config: host=${configDefault.host}, port=${configDefault.port}")

    println("\n=== Тест 3: Некорректный формат ===")
    testFile.writeText("invalid content")
    val configInvalid = readConfig("test_config.txt")
    println("Config: host=${configInvalid.host}, port=${configInvalid.port}")

    // Очистка
    testFile.delete()
}
