package backend.fd.seminar4.homework.practices.exercises.task2

// TODO: Спроектируйте обработку ошибок для 3-х уровней

// Data Layer
class UserRepository {
    fun findUser(id: Int): User {
        // Может быть SQLException
        throw NotImplementedError()
    }
}

// Business Layer
class UserService {
    fun getUser(id: Int): User {
        // TODO: используйте repository
        throw NotImplementedError()
    }
}

// Presentation Layer
class UserController {
    fun handleGetUser(id: String): Response {
        // TODO: используйте service
        throw NotImplementedError()
    }
}

data class User(val id: Int, val name: String)
data class Response(val status: Int, val body: String)

fun main() {
    // TODO: демонстрация end-to-end обработки
}
