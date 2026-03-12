package backend.fd.seminar4.homework.practices.exercises.task1

data class Account(val id: String, var balance: Double)

// ПЛОХОЙ КОД
fun transferMoney(from: Account, to: Account, amount: Double) {
    if (amount <= 0) throw Exception("Bad amount")
    if (from.balance < amount) throw Exception("Error")
    // TODO: Перепишите с хорошими сообщениями и require/check
}

// TODO: Создайте специфичные исключения
class InsufficientFundsException : Exception()

fun main() {
    val acc1 = Account("ACC1", 1000.0)
    val acc2 = Account("ACC2", 500.0)
    // TODO: тест
}
