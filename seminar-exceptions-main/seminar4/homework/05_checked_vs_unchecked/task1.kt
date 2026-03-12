package backend.fd.seminar4.homework.checked.exercises.task1

/**
 * Задача 1: Анализ Java кода
 * Изучите разницу между checked и unchecked исключениями при миграции с Java на Kotlin
 */

// Java код (для справки, не компилируется как есть):
// public String readFile(String path) throws IOException {
//     FileReader reader = new FileReader(path);
//     BufferedReader br = new BufferedReader(reader);
//     return br.readLine();
// }

/**
 * TODO: Перепишите Java код на Kotlin
 */
fun readFile(path: String): String? {
    // TODO: Реализуйте чтение файла
    // В Kotlin не нужно указывать throws!
    throw NotImplementedError("Реализуйте readFile")
}

/**
 * TODO: Ответьте на вопросы в комментариях:
 *
 * 1. Почему в Kotlin не нужно указывать throws?
 * Ответ: ...
 *
 * 2. Нужно ли обрабатывать IOException при вызове этой функции в Kotlin?
 * Ответ: ...
 *
 * 3. Какие плюсы у такого подхода?
 * Ответ: ...
 *
 * 4. Какие минусы у такого подхода?
 * Ответ: ...
 */

// TODO: Напишите функцию, которая ВЫЗЫВАЕТ readFile
fun processFile(path: String) {
    // TODO: Нужно ли обрабатывать исключения?
}

fun main() {
    println("=== Тест Kotlin версии ===")
    // TODO: Вызовите readFile и обработайте возможные ошибки
}
