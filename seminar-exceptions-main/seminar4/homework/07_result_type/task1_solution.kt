package backend.fd.seminar4.homework.result.solutions.task1

/**
 * РЕШЕНИЕ Задачи 1: runCatching
 */

data class User(val id: Int, val name: String)

fun parseUser(json: String): Result<User> = runCatching {
    require(json.contains("id")) { "Missing id field" }
    require(json.contains("name")) { "Missing name field" }

    // Упрощенная симуляция парсинга
    val id = json.substringAfter("\"id\":").trim().substringBefore(",").trim().toInt()
    val name = json.substringAfter("\"name\":").trim().removeSurrounding("\"").substringBefore("\"")

    User(id, name)
}

fun displayUser(json: String) {
    parseUser(json)
        .onSuccess { user ->
            println("✓ User: id=${user.id}, name=${user.name}")
        }
        .onFailure { error ->
            println("✗ Error: ${error.message}")
        }
}

fun main() {
    println("=== Valid JSON ===")
    displayUser("{\"id\": 1, \"name\": \"Alice\"}")

    println("\n=== Invalid JSON ===")
    displayUser("invalid")

    println("\n=== Missing field ===")
    displayUser("{\"id\": 1}")
}
