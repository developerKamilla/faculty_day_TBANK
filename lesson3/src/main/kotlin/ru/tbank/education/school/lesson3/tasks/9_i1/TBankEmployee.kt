package ru.tbank.education.school.lesson2

interface TBankEmployee{
    fun writeCode()
    fun deployToProduction()
    fun answerClientCall()
    fun processLoanRequest()
}

// каждый из этих классов должен реализовать ВСЕ методы интерфейса
class Developer(name: String) : TBankEmployee
class SupportOperator(name: String) : TBankEmployee
class LoanManager(name: String) : TBankEmployee

fun main() {
    val dev = Developer("Алексей")
    val support = SupportOperator("Мария")
    val loanManager = LoanManager("Игорь")
    // В реальности:
    // - разработчик не должен рассматривать кредиты
    // - оператор поддержки не должен деплоить в прод
    // - кредитный менеджер не должен писать код, и т.д.
    dev.writeCode()
    dev.processLoanRequest()
    support.answerClientCall()
    support.deployToProduction()
    loanManager.processLoanRequest()
    loanManager.writeCode()
}


package ru.tbank.education.school.lesson2
interface TBankEmployee {
    fun answerClientCall()
}
interface CodeWritingEmployee {
    fun writeCode()
}
interface DeploymentEmployee {
    fun deployToProduction()
}
interface LoanEmployee {
    fun processLoanRequest()
}
class Developer(private val name: String) : TBankEmployee, CodeWritingEmployee {
    override fun writeCode()
    override fun answerClientCall()
    fun deployToProduction()
}
class SupportOperator(private val name: String) : TBankEmployee, LoanEmployee {
    override fun answerClientCall()
    override fun processLoanRequest()
}
class LoanManager(private val name: String) : TBankEmployee, LoanEmployee , CodeWritingEmployee {
    override fun answerClientCall()
    override fun processLoanRequest()
    fun deployToProduction()

}
fun main() {
    val dev = Developer("Алексей")
    val support = SupportOperator("Мария")
    val loanManager = LoanManager("Игорь")
    dev.writeCode()
    support.answerClientCall()
    loanManager.processLoanRequest()
}

