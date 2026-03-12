package backend.fd.seminar4.homework.throwing.exercises.task2

import java.sql.SQLException

/**
 * Задача 2: Переброс исключений
 *
 * Научитесь преобразовывать исключения нижнего уровня в более понятные.
 */

/**
 * Функция низкого уровня (симуляция работы с БД)
 */
fun readFromDatabase(query: String): String {
    println("Executing query: $query")

    // Симуляция различных ошибок БД
    when {
        query.isEmpty() -> throw SQLException("Empty query")
        query.contains("invalid") -> throw SQLException("Invalid SQL syntax")
        query.contains("timeout") -> throw SQLException("Connection timeout")
        else -> return "Database result for: $query"
    }
}

/**
 * TODO: Создайте исключения высокого уровня
 */
class UserNotFoundException(userId: Int, cause: Throwable? = null) :
    Exception("User with id $userId not found", cause)

class DatabaseAccessException(message: String, cause: Throwable) :
    Exception("Database access error: $message", cause)

/**
 * TODO: Реализуйте функцию высокого уровня getUserData
 *
 * Требования:
 * 1. Вызывает readFromDatabase с запросом пользователя
 * 2. Ловит SQLException
 * 3. Выбрасывает более понятное исключение UserNotFoundException или DatabaseAccessException
 * 4. Сохраняет исходную ошибку как cause
 */
data class UserData(val id: Int, val name: String, val email: String)

fun getUserData(userId: Int): UserData {
    try {
        // TODO: Вызовите readFromDatabase
        val query = "SELECT * FROM users WHERE id = $userId"
        val result = readFromDatabase(query)

        // Симуляция парсинга результата
        return UserData(userId, "User$userId", "user$userId@example.com")

    } catch (e: SQLException) {
        // TODO: Преобразуйте SQLException в более понятное исключение
        // Подсказка: анализируйте сообщение ошибки
        if (e.message?.contains("not found") == true) {
            throw UserNotFoundException(userId, e)
        } else {
            throw DatabaseAccessException(e.message ?: "Unknown error", e)
        }
    }
}

/**
 * TODO: Реализуйте версию с Result
 */
fun getUserDataSafe(userId: Int): Result<UserData> = runCatching {
    getUserData(userId)
}

/**
 * TODO: Многоуровневая обработка
 * Создайте цепочку функций с разными уровнями абстракции
 */

// Уровень 1: Data Access Layer
object UserRepository {
    fun findById(id: Int): String {
        // TODO: Используйте readFromDatabase
        return readFromDatabase("SELECT * FROM users WHERE id = $id")
    }
}

// Уровень 2: Business Logic Layer
class UserService {
    fun getUser(id: Int): UserData {
        try {
            // TODO: Используйте UserRepository
            val data = UserRepository.findById(id)
            return UserData(id, "Name", "email@example.com")
        } catch (e: SQLException) {
            // TODO: Преобразуйте в бизнес-исключение
            throw UserNotFoundException(id, e)
        }
    }
}

// Уровень 3: Presentation Layer
class UserController {
    private val userService = UserService()

    fun handleGetUser(userId: String): String {
        return try {
            // TODO: Используйте UserService
            val id = userId.toIntOrNull() ?: throw IllegalArgumentException("Invalid user ID format")
            val user = userService.getUser(id)
            "Success: ${user.name}"

        } catch (e: UserNotFoundException) {
            // TODO: Верните понятный ответ пользователю
            "Error 404: User not found"

        } catch (e: IllegalArgumentException) {
            "Error 400: ${e.message}"

        } catch (e: Exception) {
            "Error 500: Internal server error"
        }
    }
}

fun main() {
    println("=== Тест 1: Успешное получение пользователя ===")
    try {
        val user = getUserData(123)
        println("Пользователь: $user")
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
        println("Причина: ${e.cause?.message}")
    }

    println("\n=== Тест 2: Несуществующий пользователь ===")
    try {
        getUserData(999)
    } catch (e: UserNotFoundException) {
        println("Ошибка: ${e.message}")
        println("SQL ошибка: ${e.cause?.message}")
    }

    println("\n=== Тест 3: Safe версия ===")
    getUserDataSafe(123)
        .onSuccess { println("Успех: $it") }
        .onFailure { println("Ошибка: ${it.message}, причина: ${it.cause?.message}") }

    println("\n=== Тест 4: Многоуровневая обработка ===")
    val controller = UserController()
    println(controller.handleGetUser("123"))
    println(controller.handleGetUser("invalid"))
    println(controller.handleGetUser("999"))
}
