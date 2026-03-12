package backend.fd.seminar4.homework.motivation.solutions.task2

/**
 * РЕШЕНИЕ Задачи 2: Цена ошибки
 */

data class BankAccount(
    val id: String,
    var balance: Double
)

class BankingSystem {
    private val accounts = mutableMapOf(
        "ACC001" to BankAccount("ACC001", 1000.0),
        "ACC002" to BankAccount("ACC002", 500.0)
    )

    fun getAccount(accountId: String): BankAccount? = accounts[accountId]

    fun updateBalance(accountId: String, newBalance: Double) {
        accounts[accountId]?.balance = newBalance
    }

    fun logTransaction(from: String, to: String, amount: Double) {
        println("Transaction: $from -> $to, amount: $amount")
    }
}

/**
 * Критичные места:
 *
 * 1. Место: Проверка существования счета отправителя
 *    Последствия: Деньги могут быть списаны с несуществующего счета (фантомная операция)
 *
 * 2. Место: Проверка достаточности средств
 *    Последствия: Овердрафт, отрицательный баланс, финансовые потери
 *
 * 3. Место: Атомарность операции (снятие и зачисление)
 *    Последствия: Деньги могут исчезнуть если операция упадет между снятием и зачислением
 *
 * 4. Место: Проверка существования счета получателя
 *    Последствия: Деньги списаны, но не зачислены
 *
 * 5. Место: Логирование транзакции
 *    Последствия: Невозможно отследить операцию для аудита
 */

// Базовая реализация БЕЗ обработки ошибок (ПЛОХО!)
fun transferMoney(
    system: BankingSystem,
    fromAccountId: String,
    toAccountId: String,
    amount: Double
) {
    val fromAccount = system.getAccount(fromAccountId)!!  // Может упасть!
    val toAccount = system.getAccount(toAccountId)!!      // Может упасть!

    fromAccount.balance -= amount  // Может стать отрицательным!
    // Если здесь произойдет ошибка, деньги исчезнут!
    toAccount.balance += amount

    system.logTransaction(fromAccountId, toAccountId, amount)
}

// РЕШЕНИЕ: Безопасная версия с обработкой ошибок
sealed class TransferError : Exception() {
    data class AccountNotFound(val accountId: String) :
        TransferError() {
        override val message = "Account not found: $accountId"
    }

    data class InsufficientFunds(val accountId: String, val required: Double, val available: Double) :
        TransferError() {
        override val message = "Insufficient funds in $accountId: required $required, available $available"
    }

    data class InvalidAmount(val amount: Double) :
        TransferError() {
        override val message = "Invalid amount: $amount (must be positive)"
    }
}

fun transferMoneyWithErrorHandling(
    system: BankingSystem,
    fromAccountId: String,
    toAccountId: String,
    amount: Double
): Result<Unit> = runCatching {
    // Валидация суммы
    require(amount > 0) {
        throw TransferError.InvalidAmount(amount)
    }

    // Проверка существования счетов
    val fromAccount = system.getAccount(fromAccountId)
        ?: throw TransferError.AccountNotFound(fromAccountId)

    val toAccount = system.getAccount(toAccountId)
        ?: throw TransferError.AccountNotFound(toAccountId)

    // Проверка достаточности средств
    if (fromAccount.balance < amount) {
        throw TransferError.InsufficientFunds(
            fromAccountId,
            amount,
            fromAccount.balance
        )
    }

    // Атомарная операция (в реальности нужна транзакция БД)
    val originalFromBalance = fromAccount.balance
    val originalToBalance = toAccount.balance

    try {
        fromAccount.balance -= amount
        toAccount.balance += amount
        system.logTransaction(fromAccountId, toAccountId, amount)

        println("✓ Transfer successful: $fromAccountId -> $toAccountId, amount: $amount")
    } catch (e: Exception) {
        // Откат изменений в случае ошибки
        fromAccount.balance = originalFromBalance
        toAccount.balance = originalToBalance
        throw e
    }
}

fun main() {
    val system = BankingSystem()

    println("=== Начальное состояние ===")
    println("ACC001: ${system.getAccount("ACC001")?.balance}")
    println("ACC002: ${system.getAccount("ACC002")?.balance}")

    println("\n=== Тест 1: Успешный перевод ===")
    transferMoneyWithErrorHandling(system, "ACC001", "ACC002", 200.0)
        .onSuccess {
            println("ACC001: ${system.getAccount("ACC001")?.balance}")
            println("ACC002: ${system.getAccount("ACC002")?.balance}")
        }
        .onFailure { println("Ошибка: ${it.message}") }

    println("\n=== Тест 2: Недостаточно средств ===")
    transferMoneyWithErrorHandling(system, "ACC002", "ACC001", 10000.0)
        .onFailure { println("✗ Ошибка: ${it.message}") }

    println("\n=== Тест 3: Несуществующий счет ===")
    transferMoneyWithErrorHandling(system, "ACC001", "ACC999", 100.0)
        .onFailure { println("✗ Ошибка: ${it.message}") }

    println("\n=== Тест 4: Отрицательная сумма ===")
    transferMoneyWithErrorHandling(system, "ACC001", "ACC002", -50.0)
        .onFailure { println("✗ Ошибка: ${it.message}") }

    println("\n=== Финальное состояние ===")
    println("ACC001: ${system.getAccount("ACC001")?.balance}")
    println("ACC002: ${system.getAccount("ACC002")?.balance}")
}
