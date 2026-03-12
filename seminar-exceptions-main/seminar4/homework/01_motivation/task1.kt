package backend.fd.seminar4.homework.motivation.exercises.task1

/**
 * Задача 1: Анализ проблемного кода
 *
 * Дан следующий код, который работает с пользовательскими данными.
 * Проанализируйте все возможные проблемы и улучшите код.
 */

// Заглушки для примера
class Database {
    fun findUser(userId: String): User? {
        // Симуляция: иногда возвращает null
        return if (userId == "999") null else User("user@example.com")
    }
}

data class User(val email: String?)

fun sendEmail(email: String, message: String) {
    println("Sending email to $email: $message")
}

// ПРОБЛЕМНЫЙ КОД - НУЖНО ПРОАНАЛИЗИРОВАТЬ И ИСПРАВИТЬ
val database = Database()

fun processUserData(userId: String) {
    val user = database.findUser(userId)
    val email = user!!.email
    sendEmail(email!!, "Welcome!")
    println("Email sent successfully")
}

/**
 * TODO: Ваши задачи:
 * 1. Перечислите все возможные ошибки в комментариях
 * 2. Опишите, что произойдет с программой при каждой ошибке
 * 3. Напишите улучшенную версию функции processUserDataImproved
 */

// Возможные ошибки:
// 1. ...
// 2. ...
// 3. ...

fun processUserDataImproved(userId: String) {
    // Ваш улучшенный код здесь
}

// Тестирование
fun main() {
    println("=== Тест 1: Нормальный пользователь ===")
    // processUserData("123")  // Раскомментируйте для теста

    println("\n=== Тест 2: Несуществующий пользователь ===")
    // processUserData("999")  // Раскомментируйте для теста

    println("\n=== Тест 3: Улучшенная версия ===")
    processUserDataImproved("123")
    processUserDataImproved("999")
}
