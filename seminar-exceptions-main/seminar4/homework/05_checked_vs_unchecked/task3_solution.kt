package backend.fd.seminar4.homework.checked.solutions.task3

/**
 * РЕШЕНИЕ Задачи 3: Миграция с Java
 */

data class Data(val id: String, val content: String)

// Исключения
class DataNotFoundException(val id: String) : Exception("Data not found: $id")
class DatabaseException(message: String, cause: Throwable? = null) : Exception(message, cause)
class ValidationException(message: String) : Exception(message)

// Kotlin интерфейс (без throws!)
interface DataRepository {
    fun getData(id: String): Data  // Может выбросить исключения, но не объявляем
    fun saveData(data: Data)        // Аналогично
}

// Реализация
class DataRepositoryImpl : DataRepository {
    private val storage = mutableMapOf(
        "1" to Data("1", "Content 1"),
        "2" to Data("2", "Content 2")
    )

    override fun getData(id: String): Data {
        return storage[id] ?: throw DataNotFoundException(id)
    }

    override fun saveData(data: Data) {
        if (data.content.isBlank()) {
            throw ValidationException("Content cannot be empty")
        }
        storage[data.id] = data
    }
}

// Версия с Result (рекомендуется)
interface DataRepositorySafe {
    fun getData(id: String): Result<Data>
    fun saveData(data: Data): Result<Unit>
}

class DataRepositorySafeImpl : DataRepositorySafe {
    private val storage = mutableMapOf(
        "1" to Data("1", "Content 1"),
        "2" to Data("2", "Content 2")
    )

    override fun getData(id: String): Result<Data> = runCatching {
        storage[id] ?: throw DataNotFoundException(id)
    }

    override fun saveData(data: Data): Result<Unit> = runCatching {
        if (data.content.isBlank()) throw ValidationException("Content cannot be empty")
        storage[data.id] = data
    }
}

fun main() {
    val repo = DataRepositoryImpl()

    println("=== Kotlin версия (без обязательной обработки) ===")
    try {
        val data = repo.getData("1")
        println("✓ Получено: ${data.content}")

        repo.getData("999")  // Упадет
    } catch (e: DataNotFoundException) {
        println("✗ ${e.message}")
    }

    println("\n=== Safe версия с Result ===")
    val safeRepo = DataRepositorySafeImpl()

    safeRepo.getData("1")
        .onSuccess { println("✓ Получено: ${it.content}") }
        .onFailure { println("✗ ${it.message}") }

    safeRepo.getData("999")
        .onSuccess { println("Получено: ${it.content}") }
        .onFailure { println("✗ ${it.message}") }

    safeRepo.saveData(Data("3", ""))
        .onSuccess { println("✓ Сохранено") }
        .onFailure { println("✗ ${it.message}") }

    println("\n=== Сравнение Java vs Kotlin ===")
    println("Java:")
    println("  - Нужно объявлять throws")
    println("  - Компилятор требует обработки")
    println("  - Много boilerplate кода")
    println("  - ~30 строк для try-catch")

    println("\nKotlin:")
    println("  - Не нужно объявлять throws")
    println("  - Обработка опциональна")
    println("  - Меньше кода")
    println("  - Result для явной обработки")
}
