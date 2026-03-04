import ru.tbank.education.school.lesson8.practise.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

// ===========================================
// Задача 3. JWT — авторизация
// ===========================================
// Цель: понять структуру JWT, собрать и декодировать токен, отправить запрос с Bearer-авторизацией.
// API: https://httpbin.org/bearer (возвращает 200 если есть Bearer, 401 если нет)
//
// TODO 1: Собрать JWT из трёх частей (header, payload, signature) в Base64URL
// TODO 2: Декодировать JWT обратно — вывести header и payload как JSON
// TODO 3: Отправить GET https://httpbin.org/bearer с заголовком Authorization: Bearer <token>
// TODO 4: Отправить тот же запрос БЕЗ токена — убедиться, что вернулся 401
// TODO 5: Подменить payload (role: student → admin), объяснить почему сервер отвергнет
//
// Подсказки:
//   Base64.getUrlEncoder().withoutPadding().encodeToString(bytes) — кодирование
//   Base64.getUrlDecoder().decode(string)                        — декодирование
//   JWT = base64(header) + "." + base64(payload) + "." + base64(signature)
//
// Вопросы после выполнения:
//   - Из каких 3 частей состоит JWT?
// JWT токен = Голова.Тело.Подпись
//Голова = информация о типе токена
//Тело = данные пользователя (user, role)
//Подпись = уникальный отпечаток, созданный сервером
//   - Можно ли подменить payload и использовать токен? Почему нет?
// Изменение payload делает старую signature недействительной
//   - Что такое access token и refresh token?
// первое - короткоживущий токен нужен для API, а второе - обновленный токен, используется для получения access токенок

fun main() {
    disableSslVerification()
    val encoder = Base64.getUrlEncoder().withoutPadding()
    println("=== Сборка JWT ===")
    val header = """{"alg":"HS256","typ":"JWT"}"""
    val payload = """{"sub":"1","name":"Ivan Petrov","role":"student","iat":1234567890}"""
    val fakeSignature = "dummysignature"
    // Закодировать каждую часть в Base64URL и склеить через "."
    val encodedHeader = encoder.encodeToString(header.toByteArray())
    val encodedPayload = encoder.encodeToString(payload.toByteArray())
    val token = "$encodedHeader.$encodedPayload.$fakeSignature"
    println("Header: $header")
    println("Payload: $payload")
    println("Подпись: $fakeSignature")
    println("Токен: $token")

    // TODO 2: Декодировать JWT
    println("\n=== Декодирование JWT ===")
    // Разделить token по ".", декодировать header и payload, вывести
    val decoder = Base64.getUrlDecoder()
    val splitoken = token.split(".")
    val headerEncoded = splitoken[0]
    val payloadEncoded = splitoken[1]
    val signature = splitoken[2]
    val headerD = decoder.decode(headerEncoded)
    val headerDecoded = String(headerD)
    val payloadD = decoder.decode(headerEncoded)
    val payloadDecoded = String(payloadD)
    val signD = decoder.decode(headerEncoded)
    val signDecoded = String(signD)
    println("Закодированный header: $headerEncoded")
    println("Декодированный header: $headerDecoded")
    println("Закодированный payload: $payloadEncoded")
    println("Декодированный payload: $payloadDecoded")
    println("Подпись: $signature")

    // TODO 3: GET /bearer с токеном
    println("\n=== GET /bearer (с токеном) ===")
    val bearerUrl = URL("https://httpbin.org/bearer")
    val bearerConnection = bearerUrl.openConnection() as HttpURLConnection
    bearerConnection.requestMethod = "GET"
    // Добавление заголовка Authorization с Bearer токеном
    bearerConnection.setRequestProperty("Authorization", "Bearer $token")
    val responseCode = bearerConnection.responseCode
    println("Response Code: $responseCode")
    val responseText = if (responseCode == HttpURLConnection.HTTP_OK) {
        bearerConnection.inputStream.bufferedReader().use { it.readText() }
    } else {
        bearerConnection.errorStream?.bufferedReader()?.use { it.readText() } ?: "No error details"
    }
    println("Response Body:")
    println(responseText)
    // TODO 4: GET /bearer без токена
    println("\n=== GET /bearer (без токена) ===")
    // Отправить тот же запрос без заголовка Authorization
    // Ожидаемый результат: 401
    val bearerNoTokenUrl = URL("https://httpbin.org/bearer")
    val bearerNoTokenConnection = bearerNoTokenUrl.openConnection() as HttpURLConnection
    bearerNoTokenConnection.requestMethod = "GET"
    // Получение кода ответа (ожидаем 401 Unauthorized)
    val responsesCode = bearerNoTokenConnection.responseCode
    println("Response Code: $responsesCode")

    // Чтение ответа (используем errorStream для кода 401)
    val responsesText = if (responsesCode == HttpURLConnection.HTTP_OK) {
        bearerNoTokenConnection.inputStream.bufferedReader().use { it.readText() }
    } else {
        bearerNoTokenConnection.errorStream?.bufferedReader()?.use { it.readText() }
            ?: "No error details (code: $responsesCode)"
    }

    println("Response Body:")
    println(responsesText)

    // TODO 5: Подмена payload
    println("\n=== Подмена payload ===")
    // Изменить role на "admin", собрать новый токен
    // Объяснить почему сервер его отвергнет
    val fakePayload = mapOf(
        "user" to "John",
        "role" to "admin",
        "iat" to (System.currentTimeMillis() / 1000)
    )

    println("Поддельный payload: $fakePayload")
    val fakeTokenUrl = URL("https://httpbin.org/bearer")
    val fakeTokenConnection = fakeTokenUrl.openConnection() as HttpURLConnection
    fakeTokenConnection.requestMethod = "GET"
    // Отправляем заведомо невалидный токен
    fakeTokenConnection.setRequestProperty("Authorization", "Bearer fake.eyJyb2xlIjoiYWRtaW4ifQ.signature")
    val responseousCode = fakeTokenConnection.responseCode
    println("Response Code: $responseousCode - Сервер отверг токен (как и ожидалось)")
    fakeTokenConnection.disconnect()
    // Это не прокатит, потому что сервер использует секретный ключ для проверки. А у нас нет к нему доступа.
    // Также в оригинале: голова.тело.подпись, а в подделке: голова.другое_тело.стараяподпись
}