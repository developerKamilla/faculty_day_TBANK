package backend.fd.seminar4.homework.throwing.solutions.task2

import java.sql.SQLException

/**
 * РЕШЕНИЕ Задачи 2: Переброс исключений
 */

fun readFromDatabase(query: String): String {
    println("Executing query: $query")
    when {
        query.isEmpty() -> throw SQLException("Empty query")
        query.contains("invalid") -> throw SQLException("Invalid SQL syntax")
        query.contains("timeout") -> throw SQLException("Connection timeout")
        else -> return "Database result for: $query"
    }
}

// Исключения высокого уровня
class UserNotFoundException(userId: Int, cause: Throwable? = null) :
    Exception("User with id $userId not found", cause)

class DatabaseAccessException(message: String, cause: Throwable) :
    Exception("Database access error: $message", cause)

data class UserData(val id: Int, val name: String, val email: String)

// Функция высокого уровня с преобразованием исключений
fun getUserData(userId: Int): UserData {
    try {
        val query = "SELECT * FROM users WHERE id = $userId"
        val result = readFromDatabase(query)
        return UserData(userId, "User$userId", "user$userId@example.com")

    } catch (e: SQLException) {
        if (e.message?.contains("not found") == true) {
            throw UserNotFoundException(userId, e)
        } else {
            throw DatabaseAccessException(e.message ?: "Unknown error", e)
        }
    }
}

// Версия с Result
fun getUserDataSafe(userId: Int): Result<UserData> = runCatching {
    getUserData(userId)
}

// Многоуровневая архитектура
object UserRepository {
    fun findById(id: Int): String {
        return readFromDatabase("SELECT * FROM users WHERE id = $id")
    }
}

class UserService {
    fun getUser(id: Int): UserData {
        try {
            val data = UserRepository.findById(id)
            return UserData(id, "Name", "email@example.com")
        } catch (e: SQLException) {
            throw UserNotFoundException(id, e)
        }
    }
}

class UserController {
    private val userService = UserService()

    fun handleGetUser(userId: String): String {
        return try {
            val id = userId.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid user ID format: $userId")

            val user = userService.getUser(id)
            "✓ Success: ${user.name} (${user.email})"

        } catch (e: UserNotFoundException) {
            "✗ Error 404: User not found"

        } catch (e: IllegalArgumentException) {
            "✗ Error 400: ${e.message}"

        } catch (e: Exception) {
            "✗ Error 500: Internal server error"
        }
    }
}

fun main() {
    println("=== Тест 1: Успешное получение ===")
    try {
        val user = getUserData(123)
        println("✓ Пользователь: $user")
    } catch (e: Exception) {
        println("✗ ${e.message}")
        println("  Причина: ${e.cause?.message}")
    }

    println("\n=== Тест 2: Safe версия ===")
    getUserDataSafe(123)
        .onSuccess { println("✓ $it") }
        .onFailure { println("✗ ${it.message}\n  Причина: ${it.cause?.message}") }

    println("\n=== Тест 3: Многоуровневая обработка ===")
    val controller = UserController()
    println(controller.handleGetUser("123"))
    println(controller.handleGetUser("invalid"))

    println("\n=== Демонстрация цепочки исключений ===")
    try {
        getUserData(999)
    } catch (e: UserNotFoundException) {
        println("✗ Исключение: ${e::class.simpleName}")
        println("  Сообщение: ${e.message}")
        println("  Причина: ${e.cause?.javaClass?.simpleName}")
        println("  Сообщение причины: ${e.cause?.message}")
    }
}
