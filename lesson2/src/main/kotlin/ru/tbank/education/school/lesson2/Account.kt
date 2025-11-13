package ru.tbank.education.school.lesson2

open class Account(
    val id: String,
    var balance: Double,
    var customerId: String,
) {
    fun deposit(amount: Double) {
        balance += amount
    }

    open fun withdraw(amount: Double): Boolean {
        if (balance >= amount) {
            balance -= amount
            return true
        }
        return false
    }
}

class CreditAccount(
    id: String,
    balance: Double,
    customerId: String,
    creditlimit: Double,
) : Account(
    id,
    balance,
    customerId
) {
    var creditlimit = creditlimit

    override fun withdraw(amount: Double): Boolean {
        if (balance + creditlimit >= amount) {
            balance -= amount
            return true
        }
        return false
    }
}