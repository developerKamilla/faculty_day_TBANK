package ru.tbank.education.school.lesson2

class Bank {
    var accountSeq = 1
    var clientSeq = 1
    private val clients: MutableList<Client> = mutableListOf()
    private val accounts: MutableList<Account> = mutableListOf()

    fun addClient(clientFullName: String) {
        val newClient = Client(
            id = "C-${clientSeq}",
            fullname = clientFullName
        )
        clientSeq++
        clients.add(newClient)
    }
    fun addAccount(clientId: String) {
        val newAccount = Account(
            id = "A-${accountSeq}",
            balance = 0.0,
            customerId = clientId
        )
        accountSeq++
        accounts.add(newAccount)
    }

    fun transfer(fromAccount: Account, toAccount: Account, amount: Double) {
        val fromAccount = accounts.find {it.id == fromAccount.id}!! // тк может быть null и тогда исключение
        val toAccount = accounts.find {it.id == toAccount.id}!! // тк может быть null и тогда исключение
        val ok = fromAccount.withdraw(amount)
        if (ok) {
            toAccount.deposit(amount)
        }
    }
}