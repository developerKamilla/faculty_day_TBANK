package backend.fd.seminar4.homework.practices.solutions.task1

/**
 * РЕШЕНИЕ Задачи 1: Информативные сообщения
 */

data class Account(val id: String, var balance: Double)

class InsufficientFundsException(
    val accountId: String,
    val requested: Double,
    val available: Double
) : Exception("Account $accountId has insufficient funds: requested $requested, available $available")

class InvalidAmountException(val amount: Double) :
    Exception("Invalid amount: $amount (must be positive)")

fun transferMoney(from: Account, to: Account, amount: Double) {
    require(amount > 0) { throw InvalidAmountException(amount) }
    require(from.balance >= amount) {
        throw InsufficientFundsException(from.id, amount, from.balance)
    }

    from.balance -= amount
    to.balance += amount
    println("✓ Transferred $amount from ${from.id} to ${to.id}")
}

fun main() {
    val acc1 = Account("ACC1", 1000.0)
    val acc2 = Account("ACC2", 500.0)

    println("=== Valid transfer ===")
    transferMoney(acc1, acc2, 200.0)

    println("\n=== Invalid amount ===")
    try {
        transferMoney(acc1, acc2, -100.0)
    } catch (e: InvalidAmountException) {
        println("✗ ${e.message}")
    }

    println("\n=== Insufficient funds ===")
    try {
        transferMoney(acc2, acc1, 10000.0)
    } catch (e: InsufficientFundsException) {
        println("✗ ${e.message}")
        println("  Details: requested=${e.requested}, available=${e.available}")
    }
}
