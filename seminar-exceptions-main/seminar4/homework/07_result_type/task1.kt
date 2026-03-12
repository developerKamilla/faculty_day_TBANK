package backend.fd.seminar4.homework.result.exercises.task1

data class User(val id: Int, val name: String)

fun parseJson(json: String): User {
    // TODO: Используйте runCatching для безопасного парсинга
    // Упрощенная симуляция
    if (!json.contains("id")) throw IllegalArgumentException("Missing id")
    return User(1, "Test")
}

// TODO: Реализуйте parseUser возвращающую Result<User>
fun parseUser(json: String): Result<User> {
    throw NotImplementedError()
}

// TODO: Используйте onSuccess и onFailure
fun displayUser(json: String) {
    throw NotImplementedError()
}

fun main() {
    displayUser("{\"id\": 1, \"name\": \"Alice\"}")
    displayUser("invalid json")
}
