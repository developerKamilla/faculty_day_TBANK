package backend.fd.seminar4.homework.practices.solutions.task3

/**
 * РЕШЕНИЕ Задачи 3: Railway Oriented Programming
 */

data class RegistrationForm(val username: String, val email: String, val password: String, val age: String)
data class ValidatedUser(val username: String, val email: String, val password: String, val age: Int)

fun validateUsername(username: String): Result<String> = runCatching {
    require(username.length >= 3) { "Username too short" }
    require(username.length <= 20) { "Username too long" }
    username
}

fun validateEmail(email: String): Result<String> = runCatching {
    require(email.contains("@")) { "Email must contain @" }
    email
}

fun validatePassword(password: String): Result<String> = runCatching {
    require(password.length >= 8) { "Password too short (min 8)" }
    password
}

fun validateAge(age: String): Result<Int> = runCatching {
    val ageInt = age.toInt()
    require(ageInt in 18..120) { "Age must be 18-120" }
    ageInt
}

fun validateForm(form: RegistrationForm): Result<ValidatedUser> {
    return validateUsername(form.username)
        .mapCatching { username ->
            val email = validateEmail(form.email).getOrThrow()
            val password = validatePassword(form.password).getOrThrow()
            val age = validateAge(form.age).getOrThrow()
            ValidatedUser(username, email, password, age)
        }
}

fun main() {
    println("=== Valid form ===")
    validateForm(RegistrationForm("alice", "alice@example.com", "password123", "25"))
        .onSuccess { println("✓ Validated: ${it.username}") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== Invalid username ===")
    validateForm(RegistrationForm("ab", "alice@example.com", "password123", "25"))
        .onFailure { println("✗ ${it.message}") }

    println("\n=== Invalid age ===")
    validateForm(RegistrationForm("alice", "alice@example.com", "password123", "15"))
        .onFailure { println("✗ ${it.message}") }
}
