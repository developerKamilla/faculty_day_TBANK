package backend.fd.seminar4.homework.practices.exercises.task3

data class RegistrationForm(
    val username: String,
    val email: String,
    val password: String,
    val age: String
)

data class ValidatedUser(
    val username: String,
    val email: String,
    val password: String,
    val age: Int
)

// TODO: Создайте цепочку валидации через Result
fun validateUsername(username: String): Result<String> = runCatching {
    // TODO
    username
}

fun validateEmail(email: String): Result<String> = runCatching {
    // TODO
    email
}

fun validatePassword(password: String): Result<String> = runCatching {
    // TODO
    password
}

fun validateAge(age: String): Result<Int> = runCatching {
    // TODO
    age.toInt()
}

// TODO: Объедините все валидации
fun validateForm(form: RegistrationForm): Result<ValidatedUser> {
    throw NotImplementedError()
}

fun main() {
    val form = RegistrationForm("alice", "alice@example.com", "pass123", "25")
    // TODO: тест
}
