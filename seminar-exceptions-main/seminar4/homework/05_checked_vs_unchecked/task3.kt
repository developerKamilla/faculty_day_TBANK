package backend.fd.seminar4.homework.checked.exercises.task3

// Java интерфейс (для справки)
// interface DataRepository {
//     Data getData(String id) throws DataNotFoundException, DatabaseException;
//     void saveData(Data data) throws ValidationException, DatabaseException;
// }

data class Data(val id: String, val content: String)

// TODO: Перепишите интерфейс на Kotlin
interface DataRepository {
    // TODO: добавьте методы
}

// TODO: Реализуйте класс
class DataRepositoryImpl : DataRepository {
    // TODO: реализация
}

// TODO: Используйте репозиторий и обработайте ошибки
fun main() {
    println("=== TODO: Реализуйте работу с репозиторием ===")
}
