package ru.tbank.education.school.lesson8.homework.payments

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentProcessorTest {
    
    private lateinit var processor: PaymentProcessor
    private lateinit var account1: Account
    private lateinit var account2: Account
    
    @BeforeEach
    fun setUp() {
        processor = PaymentProcessor()
        account1 = Account("111111", "Джеймс Бонд", 101010.0)
        account2 = Account("222222", "Майк Вазовски", 202.0)
        
        processor.addAccount(account1)
        processor.addAccount(account2)
    }
    
    @Test
    @DisplayName("Успешный перевод между счетами")
    fun successfulTransfer() {
        val result = processor.transfer("111111", "222222", 300.0)
        
        assertTrue(result)
        assertEquals(100710.0, account1.balance)  // 101010 - 300 = 100710
        assertEquals(502.0, account2.balance)     // 202 + 300 = 502
    }
    
    @Test
    @DisplayName("Перевод с недостаточным балансом")
    fun transferWithInsufficientFunds() {
        val exception = assertThrows<InsufficientFundsException> {
            processor.transfer("111111", "222222", 200000.0)  // Больше чем 101010
        }
        
        assertEquals("Недостаточно средств на счете 111111", exception.message)
        assertEquals(101010.0, account1.balance)
        assertEquals(202.0, account2.balance)
    }
    
    @Test
    @DisplayName("Перевод с несуществующего счета")
    fun transferFromNonExistentAccount() {
        val exception = assertThrows<AccountNotFoundException> {
            processor.transfer("999999", "222222", 100.0)
        }
        
        assertEquals("Счет 999999 не найден", exception.message)
    }

    @Test
    @DisplayName("Пополнение несуществующего счета")
    fun depositToNonExistentAccount() {
        val exception = assertThrows<AccountNotFoundException> {
            processor.deposit("999999", 500.0)
        }
        
        assertEquals("Счет 999999 не найден", exception.message)
    }
    
    @Test
    @DisplayName("Перевод на несуществующий счет")
    fun transferToNonExistentAccount() {
        val exception = assertThrows<AccountNotFoundException> {
            processor.transfer("111111", "999999", 100.0)
        }
        
        assertEquals("Счет 999999 не найден", exception.message)
    }

    @Test
    @DisplayName("Снятие с несуществующего счета")
    fun withdrawFromNonExistentAccount() {
        val exception = assertThrows<AccountNotFoundException> {
            processor.withdraw("999999", 100.0)
        }
        assertEquals("Счет 999999 не найден", exception.message)
    }
    

    @Test
    @DisplayName("Пополнение счета на отрицательную сумму")
    fun depositNegativeAmount() {
        val exception = assertThrows<IllegalArgumentException> {
            processor.deposit("111111", -100.0)
        }
        
        assertEquals("Сумма пополнения должна быть положительной", exception.message)
    }
    
    @Test
    @DisplayName("Перевод отрицательной суммы")
    fun transferNegativeAmount() {
        val exception = assertThrows<IllegalArgumentException> {
            processor.transfer("111111", "222222", -100.0)
        }
        
        assertEquals("Сумма перевода должна быть положительной", exception.message)
    }

    @Test
    @DisplayName("Снятие отрицательной суммы")
    fun withdrawNegativeAmount() {
        val exception = assertThrows<IllegalArgumentException> {
            processor.withdraw("111111", -100.0)
        }
        assertEquals("Сумма снятия должна быть положительной", exception.message)
    }
    
    
    @Test
    @DisplayName("Перевод нулевой суммы")
    fun transferZeroAmount() {
        val exception = assertThrows<IllegalArgumentException> {
            processor.transfer("111111", "222222", 0.0)
        }
        
        assertEquals("Сумма перевода должна быть положительной", exception.message)
    }
    
    @Test
    @DisplayName("Перевод самому себе")
    fun transferToSameAccount() {
        val exception = assertThrows<IllegalArgumentException> {
            processor.transfer("111111", "111111", 100.0)
        }
        
        assertEquals("Нельзя переводить средства на тот же счет", exception.message)
    }
    
    @Test
    @DisplayName("Пополнение счета")
    fun depositToAccount() {
        processor.deposit("111111", 500.0)
        
        assertEquals(101510.0, account1.balance)  // 101010 + 500 = 101510
    }
    
    @Test
    @DisplayName("Пополнение счета на ноль")
    fun depositZeroAmount() {
        val exception = assertThrows<IllegalArgumentException> {
            processor.deposit("111111", 0.0)
        }
        
        assertEquals("Сумма пополнения должна быть положительной", exception.message)
    }
    
    @Test
    @DisplayName("Снятие со счета")
    fun withdrawFromAccount() {
        val result = processor.withdraw("111111", 300.0)
        assertTrue(result)
        assertEquals(100710.0, account1.balance)  // 101010 - 300 = 100710
    }
    
    @Test
    @DisplayName("Снятие со счета с недостаточным балансом")
    fun withdrawWithInsufficientFunds() {
        val exception = assertThrows<InsufficientFundsException> {
            processor.withdraw("111111", 200000.0)  // Больше чем 101010
        }
        assertEquals("Недостаточно средств на счете 111111", exception.message)
        assertEquals(101010.0, account1.balance)
    }
    
    @Test
    @DisplayName("Снятие нулевой суммы")
    fun withdrawZeroAmount() {
        val exception = assertThrows<IllegalArgumentException> {
            processor.withdraw("111111", 0.0)
        }
        assertEquals("Сумма снятия должна быть положительной", exception.message)
    }
    
    @Test
    @DisplayName("Получение баланса")
    fun getBalance() {
        val balance = processor.getBalance("111111")
        assertEquals(101010.0, balance)
    }
    
    @Test
    @DisplayName("Получение баланса несуществующего счета")
    fun getBalanceOfNonExistentAccount() {
        val exception = assertThrows<AccountNotFoundException> {
            processor.getBalance("999999")
        }
        assertEquals("Счет 999999 не найден", exception.message)
    }
    
    @Test
    @DisplayName("История транзакций после перевода")
    fun transactionHistoryAfterTransfer() {
        processor.transfer("111111", "222222", 300.0)
        val history1 = processor.getTransactionHistory("111111")
        val history2 = processor.getTransactionHistory("222222")
        assertEquals(1, history1.size)
        assertEquals(1, history2.size)
        assertEquals(TransactionType.TRANSFER_OUT, history1[0].type)
        assertEquals(TransactionType.TRANSFER_IN, history2[0].type)
        assertEquals(300.0, history1[0].amount)
        assertEquals(300.0, history2[0].amount)
    }
    
    @Test
    @DisplayName("Работа с дробными числами")
    fun testPrecisionWithDecimalNumbers() {
        processor.deposit("111111", 0.1)
        processor.transfer("111111", "222222", 0.2)
        processor.withdraw("222222", 0.1)
        assertEquals(101009.9, account1.balance, 0.0001)
        assertEquals(202.1, account2.balance, 0.0001)
    }
    
    @Nested
    @DisplayName("Тесты для нескольких операций")
    inner class MultipleOperationsTest {
        
        @Test
        @DisplayName("Пополнение, перевод и снятие")
        fun depositTransferWithdraw() {
            processor.deposit("111111", 500.0)
            processor.transfer("111111", "222222", 700.0)
            processor.withdraw("222222", 200.0)
            assertEquals(100810.0, account1.balance)
            assertEquals(702.0, account2.balance)
            val history1 = processor.getTransactionHistory("111111")
            val history2 = processor.getTransactionHistory("222222")
            assertEquals(2, history1.size) // deposit + transfer_out
            assertEquals(2, history2.size) // transfer_in + withdraw
        }
        
        @Test
        @DisplayName("Перевод после неудачного снятия")
        fun transferAfterFailedWithdraw() {
            assertThrows<InsufficientFundsException> {
                processor.withdraw("111111", 200000.0)  // Слишком много
            }
            assertEquals(101010.0, account1.balance) // Баланс не изменился
            processor.transfer("111111", "222222", 500.0)
            assertEquals(100510.0, account1.balance)  // 101010 - 500 = 100510
            assertEquals(702.0, account2.balance)     // 202 + 500 = 702
        }
        
        @Test
        @DisplayName("Множественные переводы между счетами")
        fun multipleTransfers() {
            processor.transfer("111111", "222222", 100.0)  // 101010-100=100910, 202+100=302
            processor.transfer("222222", "111111", 50.0)   // 100910+50=100960, 302-50=252
            processor.transfer("111111", "222222", 200.0)  // 100960-200=100760, 252+200=452
            
            assertEquals(100760.0, account1.balance)
            assertEquals(452.0, account2.balance)
            
            assertEquals(3, processor.getTransactionHistory("111111").size)
            assertEquals(3, processor.getTransactionHistory("222222").size)
        }
    }
}
