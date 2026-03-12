package backend.fd.seminar4.homework.checked.solutions.task2

/**
 * РЕШЕНИЕ Задачи 2: Проектирование исключений
 */

sealed class ApiException(message: String) : Exception(message)

// 1. NetworkTimeout - CHECKED (восстанавливаемая ошибка, нужна обработка)
class NetworkTimeoutException(val url: String, val timeoutMs: Int) :
    ApiException("Network timeout after ${timeoutMs}ms for $url")

// 2. Некорректный API ключ - UNCHECKED (ошибка конфигурации/программиста)
class InvalidApiKeyException(val key: String) :
    ApiException("Invalid API key: ${key.take(8)}...")

// 3. Null параметр - UNCHECKED (ошибка программиста)
class MissingParameterException(val paramName: String) :
    ApiException("Required parameter '$paramName' is missing")

// 4. Сервер 500 - CHECKED (временная проблема, можно retry)
class ServerErrorException(val statusCode: Int, val body: String) :
    ApiException("Server error: $statusCode, body: $body")

// 5. Неожиданная структура JSON - CHECKED (может измениться API)
class UnexpectedResponseException(val expected: String, val actual: String) :
    ApiException("Expected $expected but got $actual")

/**
 * ОБОСНОВАНИЕ:
 *
 * CHECKED (должны обрабатываться):
 * - NetworkTimeout: можно повторить запрос
 * - ServerError: временная проблема сервера
 * - UnexpectedResponse: API может измениться, нужна обработка
 *
 * UNCHECKED (ошибки программиста):
 * - InvalidApiKey: конфигурация приложения неверна
 * - MissingParameter: программист забыл передать параметр
 */

class ApiClient(private val apiKey: String) {
    fun makeRequest(endpoint: String, params: Map<String, String>): Result<String> = runCatching {
        // Валидация (unchecked)
        require(apiKey.isNotBlank()) { throw InvalidApiKeyException(apiKey) }
        require(params.containsKey("userId")) { throw MissingParameterException("userId") }

        // Сетевой запрос (checked - обработаем через Result)
        // Симуляция
        when (endpoint) {
            "/timeout" -> throw NetworkTimeoutException(endpoint, 5000)
            "/error" -> throw ServerErrorException(500, "Internal error")
            else -> "Success response"
        }
    }
}

fun main() {
    val client = ApiClient("valid-key-12345")

    println("=== Тест 1: Успешный запрос ===")
    client.makeRequest("/users", mapOf("userId" to "123"))
        .onSuccess { println("✓ $it") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== Тест 2: Network timeout (checked) ===")
    client.makeRequest("/timeout", mapOf("userId" to "123"))
        .onFailure { e ->
            println("✗ Тип: ${e::class.simpleName}")
            println("  Можно повторить запрос")
        }

    println("\n=== Тест 3: Server error (checked) ===")
    client.makeRequest("/error", mapOf("userId" to "123"))
        .onFailure { println("✗ ${it.message}") }

    println("\n=== ТАБЛИЦА РЕШЕНИЙ ===")
    println("| Ситуация              | Тип       | Обоснование                    |")
    println("|-------------------    |-----------|--------------------------------|")
    println("| NetworkTimeout        | Checked   | Восстанавливаемая, retry       |")
    println("| InvalidApiKey         | Unchecked | Ошибка конфигурации            |")
    println("| MissingParameter      | Unchecked | Ошибка программиста            |")
    println("| ServerError 500       | Checked   | Временная проблема             |")
    println("| UnexpectedResponse    | Checked   | API может измениться           |")
}
