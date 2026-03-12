package backend.fd.seminar4.homework.practices.solutions.task2

import java.sql.SQLException

/**
 * РЕШЕНИЕ Задачи 2: Уровни обработки ошибок
 */

data class User(val id: Int, val name: String)
data class Response(val status: Int, val body: String)

// Исключения для каждого уровня
class DataAccessException(message: String, cause: Throwable) : Exception(message, cause)
class UserNotFoundException(val userId: Int) : Exception("User $userId not found")
class ServiceUnavailableException(message: String) : Exception(message)

// Data Layer - только SQLException
class UserRepository {
    fun findUser(id: Int): User {
        if (id == 999) throw SQLException("Connection failed")
        if (id == 1) return User(1, "Alice")
        throw SQLException("User not found")
    }
}

// Business Layer - преобразует в бизнес-исключения
class UserService(private val repo: UserRepository) {
    fun getUser(id: Int): User {
        try {
            return repo.findUser(id)
        } catch (e: SQLException) {
            if (e.message?.contains("not found") == true) {
                throw UserNotFoundException(id)
            } else {
                throw DataAccessException("Database error", e)
            }
        }
    }
}

// Presentation Layer - преобразует в HTTP ответы
class UserController(private val service: UserService) {
    fun handleGetUser(id: String): Response {
        return try {
            val userId = id.toIntOrNull() ?: return Response(400, "Invalid ID format")
            val user = service.getUser(userId)
            Response(200, "User: ${user.name}")
        } catch (e: UserNotFoundException) {
            Response(404, "User not found")
        } catch (e: DataAccessException) {
            Response(503, "Service unavailable")
        } catch (e: Exception) {
            Response(500, "Internal error")
        }
    }
}

fun main() {
    val repo = UserRepository()
    val service = UserService(repo)
    val controller = UserController(service)

    println("=== Success ===")
    println(controller.handleGetUser("1"))

    println("\n=== Not found ===")
    println(controller.handleGetUser("2"))

    println("\n=== DB error ===")
    println(controller.handleGetUser("999"))

    println("\n=== Invalid input ===")
    println(controller.handleGetUser("invalid"))
}
