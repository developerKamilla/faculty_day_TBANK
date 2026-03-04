package ru.tbank.education.school.lesson8.practise
import java.net.HttpURLConnection
import java.net.URL

// ===========================================
// Задача 6. Клиент для сервера заметок
// ===========================================
// Цель: написать клиент, который тестирует все эндпоинты сервера.
// Перед запуском: запустить Task6_Server.kt
//
// TODO 1: Реализовать request() — универсальную функцию отправки запросов
// TODO 2: В main() выполнить 8 шагов (ниже), вывести код и тело каждого ответа

val BASE = "http://localhost:8080/api/notes"

/** Отправить HTTP-запрос.
 *  @param url    — полный URL
 *  @param method — HTTP-метод
 *  @param body   — JSON-тело (null для GET/DELETE)
 *  @return Pair(statusCode, responseBody)
 */
fun request(url: String, method: String, body: String? = null): Pair<Int, String> {
    var connection: HttpURLConnection? = null
    connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = method
    // Если есть тело запроса (для POST/PUT)
    if (body != null) {
        // Разрешаем отправку данных
        connection.doOutput = true
        // Устанавливаем заголовок Content-Type
        connection.setRequestProperty("Content-Type", "application/json")
        // Записываем тело запроса
        connection.outputStream.use { os ->
            os.write(body.toByteArray(Charsets.UTF_8))
            os.flush()
        }
    }
    val responseCode = connection.responseCode
    val responseBody = if (responseCode in 200..299) {
        connection.inputStream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""
    } else {
        connection.errorStream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
            ?: "Error $responseCode"
    }
    return Pair(responseCode, responseBody)

}

fun main() {
    // TODO 2: выполнить 8 шагов, каждый раз вызывая request() и выводя результат

    // Шаг 1: получить все заметки
    println("=== 1. GET /api/notes — все заметки ===")
    val (code, body) = request(BASE, "GET")
    println("Status: $code")
    println("Response: $body\n")

    // Шаг 2: создать новую заметку
    println("\n=== 2. POST /api/notes — создать заметку ===")
    // JSON: {"title":"Домашка","content":"Сделать задание по сетям","tag":"учёба"}
    val newNote = """
        {
            "title": "Домашка",
            "content": "Сделать задание по сетям",
            "tag": "учёба"
        }
    """.trimIndent()
    val (code2, body2) = request(BASE, "POST", newNote)
    println("Status: $code2")
    println("Response: $body2\n")

    // Шаг 3: получить заметку по id
    println("\n=== 3. GET /api/notes/1 — одна заметка ===")
    val (code3, body3) = request("$BASE/1", "GET")
    println("Status: $code3")
    println("Response: $body3\n")

    // Шаг 4: обновить заметку
    println("\n=== 4. PUT /api/notes/1 — обновить заметку ===")
    // JSON: {"title":"Покупки (обновлено)","content":"Молоко, хлеб, яйца, сыр","tag":"личное"}
    val SuperNote = """
        {
            "title": "Покупки (обновлено)",
            "content": "Молоко, хлеб, яйца, сыр",
            "tag": "личное"
        }
    """.trimIndent()
    val (code4, body4) = request("$BASE/1", "PUT", SuperNote)
    println("Status: $code4")
    println("Response: $body4\n")

    // Шаг 5: фильтр по тегу
    println("\n=== 5. GET /api/notes?tag=учёба — фильтр по тегу ===")
    val (code5, body5) = request("$BASE?tag=личное", "GET")
    println("Status: $code5")
    println("Response: $body5\n")

    // Шаг 6: удалить заметку
    println("\n=== 6. DELETE /api/notes/1 — удалить заметку ===")
    val (code6, body6) = request("$BASE/1", "DELETE")
    println("Status: $code6")
    println("Response: \"$body6\"\n")

    // Шаг 7: запросить несуществующую заметку (ожидаем 404)
    println("\n=== 7. GET /api/notes/999 — несуществующая заметка ===")
    val (code7, body7) = request("$BASE/1000000", "GET")
    println("Status: $code7")
    println("Response: $body7\n")

    // Шаг 8: финальное состояние
    println("\n=== 8. GET /api/notes — финальное состояние ===")
    val (code8, body8) = request(BASE, "GET")
    println("Status: $code8")
    println("Response: $body8")
}

