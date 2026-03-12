package backend.fd.seminar4.homework.basics.solutions.task2

import java.io.File

/**
 * РЕШЕНИЕ Задачи 2: Перехват исключений
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

// Реализация parseConfig
fun parseConfig(content: String): Config {
    val parts = content.trim().split(":")

    if (parts.size != 3) {
        throw IllegalArgumentException("Config must have format 'host:port:timeout', got: $content")
    }

    val host = parts[0]
    val port = parts[1].toInt()  // Может выбросить NumberFormatException
    val timeout = parts[2].toInt()  // Может выбросить NumberFormatException

    return Config(host, port, timeout)
}

// Решение с множественным catch
fun readConfig(filename: String): Config {
    return try {
        val file = File(filename)
        val content = file.readText()
        parseConfig(content)

    } catch (e: java.io.FileNotFoundException) {
        println("✗ Файл не найден: ${e.message}")
        println("  Используем конфигурацию по умолчанию")
        Config.default()

    } catch (e: java.io.IOException) {
        println("✗ Ошибка чтения файла: ${e.message}")
        println("  Используем конфигурацию по умолчанию")
        Config.default()

    } catch (e: NumberFormatException) {
        println("✗ Ошибка парсинга числа: ${e.message}")
        println("  Порт и timeout должны быть числами")
        Config.default()

    } catch (e: IndexOutOfBoundsException) {
        println("✗ Неверный формат конфигурации: ${e.message}")
        println("  Ожидается формат: host:port:timeout")
        Config.default()

    } catch (e: IllegalArgumentException) {
        println("✗ Некорректные данные: ${e.message}")
        Config.default()
    }
}

// Версия с Result
fun readConfigSafe(filename: String): Result<Config> = runCatching {
    val file = File(filename)
    val content = file.readText()
    parseConfig(content)
}.recoverCatching { error ->
    println("✗ Ошибка: ${error.message}")
    println("  Используем конфигурацию по умолчанию")
    Config.default()
}

fun main() {
    println("=== Тест 1: Чтение валидной конфигурации ===")
    val testFile = File("test_config.txt")
    testFile.writeText("example.com:9000:60")

    val config = readConfig("test_config.txt")
    println("✓ Config: host=${config.host}, port=${config.port}, timeout=${config.timeout}")

    println("\n=== Тест 2: Несуществующий файл ===")
    val configDefault = readConfig("nonexistent.txt")
    println("Config: host=${configDefault.host}, port=${configDefault.port}")

    println("\n=== Тест 3: Некорректный формат (ошибка парсинга числа) ===")
    testFile.writeText("example.com:invalid:60")
    val configInvalid = readConfig("test_config.txt")
    println("Config: host=${configInvalid.host}, port=${configInvalid.port}")

    println("\n=== Тест 4: Неполные данные ===")
    testFile.writeText("example.com:9000")
    val configIncomplete = readConfig("test_config.txt")
    println("Config: host=${configIncomplete.host}, port=${configIncomplete.port}")

    println("\n=== Тест 5: Safe версия ===")
    testFile.writeText("safe.com:8080:45")
    readConfigSafe("test_config.txt")
        .onSuccess { println("✓ Config: host=${it.host}, port=${it.port}") }
        .onFailure { println("✗ Ошибка: ${it.message}") }

    println("\n=== Тест 6: Safe версия с ошибкой ===")
    readConfigSafe("missing.txt")
        .onSuccess { println("Config: host=${it.host}") }
        .onFailure { println("Возвращена конфигурация по умолчанию") }

    // Очистка
    testFile.delete()
}
