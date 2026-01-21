interface DeploymentEmployee {
    fun deployToProduction()
}

class Developer(private val name: String) : TBankEmployee, CodeWritingEmployee, DeploymentEmployee {
    override fun writeCode() {
        println("$name пишет код")
    }

    override fun answerClientCall() {
        // Разработчик не отвечает на звонки клиентов
    }

    override fun processLoanRequest() {
        // Разработчик не обрабатывает заявки на кредиты
    }

    override fun deployToProduction() {
        println("$name деплоит код в продакшен")
    }
}
