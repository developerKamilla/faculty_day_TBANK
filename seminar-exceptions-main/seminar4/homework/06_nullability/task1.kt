package backend.fd.seminar4.homework.nulls.exercises.task1

data class Address(val city: String, val street: String?)
data class Company(val name: String, val address: Address?)
data class User(val name: String, val company: Company?)

// TODO: Реализуйте функции используя safe call (?.) и Elvis operator (?:)

fun getStreet(user: User?): String {
    // TODO: вернуть название улицы или "Unknown"
    throw NotImplementedError()
}

fun getCityUppercase(user: User?): String? {
    // TODO: вернуть город в uppercase или null
    throw NotImplementedError()
}

fun isFromMoscow(user: User?): Boolean {
    // TODO: проверить что у пользователя есть компания в Москве
    throw NotImplementedError()
}

fun main() {
    val user1 = User("Alice", Company("TechCorp", Address("Moscow", "Lenina")))
    val user2 = User("Bob", Company("StartupInc", Address("SPB", null)))
    val user3 = User("Charlie", null)

    println("=== Тест getStreet ===")
    // TODO: протестируйте
}
