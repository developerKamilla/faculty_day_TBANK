package ru.tbank.education.school.lesson2

fun main() {
    val account = Account(id = "A001", balance = 1000.0, customerId = "C001")
    println("Balance Account: ${account.balance}")
    account.deposit(200.0)
    println("Deposit 200: ${account.balance}")
    val successWithdraw1 = account.withdraw(500.0)
    println("Minus 500 try: Success = $successWithdraw1, Money left: ${account.balance}")
    val successWithdraw2 = account.withdraw(1000.0)
    println("Minus 1000 try: Success = $successWithdraw2, Money left: ${account.balance}")
    val creditAccount = CreditAccount(id = "C001", balance = 500.0, customerId = "C002", creditlimit = 300.0)
    println("\nBalance CreditAccount: ${creditAccount.balance}")
    val successCreditWithdraw1 = creditAccount.withdraw(700.0)
    println("Minus 700 try: Success = $successCreditWithdraw1, Money left: ${creditAccount.balance}")
    val successCreditWithdraw2 = creditAccount.withdraw(200.0)
    println("Minus 200 try: Success = $successCreditWithdraw2, Money left: ${creditAccount.balance}")
}