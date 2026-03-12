package backend.fd.seminar4.homework.motivation.exercises.task3

/**
 * Задача 3: Fail Fast vs Defensive Programming
 *
 * Сравните два подхода к обработке ошибок.
 */

// Подход A: Fail Fast
fun divideFailFast(a: Int, b: Int): Int {
    if (b == 0) throw IllegalArgumentException("Division by zero")
    return a / b
}

// Подход B: Defensive Programming
fun divideDefensive(a: Int, b: Int): Int {
    if (b == 0) return 0
    return a / b
}

/**
 * TODO: Ваши задачи:
 *
 * 1. Какой подход лучше и почему? (напишите в комментарии)
 * Ответ: ...
 *
 * 2. В каких ситуациях можно использовать подход B?
 * Ответ: ...
 *
 * 3. Приведите примеры реальных функций для каждого подхода
 */

// Пример 1: Функция, где уместен Fail Fast подход
fun exampleFailFast(/* параметры */) {
    // TODO: Реализуйте пример
    // Подсказка: валидация данных, критичные операции
}

// Пример 2: Функция, где уместен Defensive подход
fun exampleDefensive(/* параметры */) {
    // TODO: Реализуйте пример
    // Подсказка: UI, значения по умолчанию, опциональные операции
}

/**
 * Дополнительное задание:
 * Реализуйте функцию деления с использованием Result
 */
fun divideSafe(a: Int, b: Int): Result<Int> {
    // TODO: Реализуйте безопасное деление
    return Result.success(0)
}

fun main() {
    println("=== Тест Fail Fast ===")
    try {
        println("10 / 2 = ${divideFailFast(10, 2)}")
        println("10 / 0 = ${divideFailFast(10, 0)}")
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }

    println("\n=== Тест Defensive ===")
    println("10 / 2 = ${divideDefensive(10, 2)}")
    println("10 / 0 = ${divideDefensive(10, 0)}")  // Вернет 0

    println("\n=== Тест Safe ===")
    divideSafe(10, 2).onSuccess { println("10 / 2 = $it") }
    divideSafe(10, 0).onFailure { println("Ошибка: ${it.message}") }
}
