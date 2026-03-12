package backend.fd.seminar4.homework.checked.exercises.task2

// TODO: Создайте иерархию исключений для API библиотеки
sealed class ApiException(message: String) : Exception(message)

// TODO: Определите какие исключения checked, какие unchecked (в концептуальном смысле)
class NetworkTimeoutException : ApiException("Network timeout")
// ... добавьте остальные

fun main() {
    println("=== Задача: Определить тип каждого исключения ===")
    // 1. NetworkTimeout при запросе - checked или unchecked?
    // 2. Некорректный API ключ - checked или unchecked?
    // 3. Null вместо параметра - checked или unchecked?
    // 4. Сервер вернул 500 - checked или unchecked?
    // 5. JSON неожиданной структуры - checked или unchecked?
}
