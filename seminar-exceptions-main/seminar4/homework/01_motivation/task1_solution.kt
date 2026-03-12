package backend.fd.seminar4.homework.motivation.solutions.task1


class Database {
    fun findUser(userId: String): User? {
        return if (userId == "999") null else User("user@example.com")
    }
}

data class User(val email: String?)

fun sendEmail(email: String, message: String) {
    println("Sending email to $email: $message")
}

val database = Database()

// ПРОБЛЕМНЫЙ КОД (оригинал)
fun processUserData(userId: String) {
    val user = database.findUser(userId)
    val email = user!!.email  // NPE если user == null
    sendEmail(email!!, "Welcome!")  // NPE если email == null
    println("Email sent successfully")
}

/**
 * Возможные ошибки:
 * 1. NullPointerException на user!! - если пользователь не найден в БД
 * 2. NullPointerException на email!! - если у пользователя нет email
 * 3. Нет логирования ошибок - сложно отладить проблемы
 * 4. Программа упадет полностью при ошибке
 */

// РЕШЕНИЕ: Улучшенная версия
fun processUserDataImproved(userId: String) {
    // Вариант 1: Safe calls и Elvis operator
    val user = database.findUser(userId)
    val email = user?.email

    if (email != null) {
        sendEmail(email, "Welcome!")
        println("Email sent successfully to $email")
    } else {
        println("Cannot send email: user or email not found for userId=$userId")
    }
}

// Альтернативное решение с более функциональным стилем
fun processUserDataFunctional(userId: String) {
    database.findUser(userId)
        ?.email
        ?.let { email ->
            sendEmail(email, "Welcome!")
            println("Email sent successfully to $email")
        }
        ?: println("Cannot send email: user or email not found for userId=$userId")
}

// Решение с Result для более явной обработки ошибок
fun processUserDataWithResult(userId: String): Result<Unit> = runCatching {
    val user = database.findUser(userId)
        ?: throw IllegalStateException("User not found: $userId")

    val email = user.email
        ?: throw IllegalStateException("User $userId has no email")

    sendEmail(email, "Welcome!")
    println("Email sent successfully to $email")
}

fun main() {
    println("=== Тест 1: Нормальный пользователь ===")
    processUserDataImproved("123")

    println("\n=== Тест 2: Несуществующий пользователь ===")
    processUserDataImproved("999")

    println("\n=== Тест 3: Функциональный стиль ===")
    processUserDataFunctional("123")
    processUserDataFunctional("999")

    println("\n=== Тест 4: С Result ===")
    processUserDataWithResult("123")
        .onSuccess { println("Операция успешна") }
        .onFailure { println("Ошибка: ${it.message}") }

    processUserDataWithResult("999")
        .onSuccess { println("Операция успешна") }
        .onFailure { println("Ошибка: ${it.message}") }
}
