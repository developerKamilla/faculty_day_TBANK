package backend.fd.seminar4.homework.comparison.exercises.task1

data class User(val id: Int, val name: String)

// TODO: Реализуйте 4 способа поиска пользователя

// 1. Исключения
fun findUserException(id: Int): User {
    throw NotImplementedError()
}

// 2. Nullable
fun findUserNullable(id: Int): User? {
    throw NotImplementedError()
}

// 3. Result
fun findUserResult(id: Int): Result<User> {
    throw NotImplementedError()
}

// 4. Sealed class
sealed class UserResult

fun findUserSealed(id: Int): UserResult {
    throw NotImplementedError()
}

// TODO: Сравните удобство использования
fun main() {
    println("=== Сравнение подходов ===")
}
