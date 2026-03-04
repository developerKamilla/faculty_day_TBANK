package ru.tbank.education.school.lesson8.practise

import java.net.HttpURLConnection
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// ===========================================
// Задача 1. HTTP-запросы через HttpURLConnection
// ===========================================
// Цель: научиться отправлять GET и POST запросы, читать ответ и статус-код.
// API: https://jsonplaceholder.typicode.com
//
// TODO 1: Отправить GET /posts/1, вывести статус-код и тело ответа
// TODO 2: Отправить POST /posts с JSON-телом, вывести статус-код и тело
// TODO 3: Отправить GET /posts/9999, обработать ошибку (код != 2xx)
//
// Подсказки:
//   val connection = URL("...").openConnection() as HttpURLConnection
//   connection.requestMethod = "GET"             — задать метод
//   connection.doOutput = true                   — разрешить отправку тела
//   connection.setRequestProperty("Content-Type", "application/json") — заголовок
//   connection.outputStream.write(json.toByteArray())                 — записать тело
//   connection.responseCode                      — получить статус-код
//   connection.inputStream.bufferedReader().readText()  — прочитать тело ответа
//   connection.errorStream                       — поток ошибок (при коде 4xx/5xx)
//   connection.disconnect()                      — закрыть соединение


fun disableSslVerification() {
    val trustAll = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustAll, java.security.SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
}

fun main() {
    disableSslVerification()

    // TODO 1: GET /posts/1
    println("=== GET /posts/1 ===")
    val getUrl = URL("https://jsonplaceholder.typicode.com/posts/1")
    val getConn = getUrl.openConnection() as HttpURLConnection
    getConn.requestMethod = "GET"

    println("Код: ${getConn.responseCode}")
    val getBody = getConn.inputStream.bufferedReader().readText()
    println("Тело: $getBody")
    getConn.disconnect()
    val getUrl2 = URL("https://jsonplaceholder.typicode.com/posts")
    val getConn2 = getUrl2.openConnection() as HttpURLConnection
    getConn2.requestMethod = "POST"
    getConn2.setRequestProperty("Content-Type", "application/json")
    getConn2.doOutput = true
    val jsonString = """
    {
        "title": "Мой пост",
        "body": "Содержание поста",
        "userId": 1
    }
""".trimIndent()
    getConn2.outputStream.write(jsonString.toByteArray())
    println("Код: ${getConn2.responseCode}")
    val getBody2 = getConn2.inputStream.bufferedReader().readText()
    println("Тело: $getBody2")
    getConn2.disconnect()



    // TODO 2: POST /posts
    println("\n=== POST /posts ===")

    // TODO 3: GET /posts/9999 (несуществующий ресурс)
    println("\n=== GET /posts/9999 ===")
}