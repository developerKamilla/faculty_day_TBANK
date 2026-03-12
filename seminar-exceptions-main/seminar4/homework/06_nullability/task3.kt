package backend.fd.seminar4.homework.nulls.exercises.task3

data class User(val name: String, val age: Int, val email: String)

fun findUser(id: String): User? = if (id == "1") User("Alice", 25, "alice@example.com") else null

// ПЛОХОЙ КОД с !!
fun processUser(userId: String) {
    val user = findUser(userId)
    val name = user!!.name
    val age = user!!.age
    val email = user!!.email
    println("User: $name, $age, $email")
}

/**
 * TODO: Ответьте на вопросы:
 * 1. Что не так с этим кодом?
 * 2. Когда использование !! оправдано?
 * 3. Перепишите без !!
 */

fun processUserSafe(userId: String) {
    // TODO: реализуйте
}

fun main() {
    println("=== Плохой код ===")
    processUser("1")
    // processUser("999")  // NPE!

    println("\n=== Исправленный код ===")
    // TODO: тест
}
