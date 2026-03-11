package ru.tbank.education.school.lesson8.homework.library

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Book(
    val title: String,
    val author: String,
    val isbn: String
)

data class BorrowRecord(
    val book: Book,
    val borrower: String,
    val borrowDate: LocalDate,
    var returnDate: LocalDate? = null
)

data class User(
    val name: String,
    var outstandingFine: Int = 0
)

class LibraryService {
    private val books = mutableMapOf<String, Book>()
    private val borrowedBooks = mutableMapOf<String, BorrowRecord>()
    private val users = mutableMapOf<String, User>()
    
    companion object {
        private const val GRACE_PERIOD_DAYS = 7
        private const val FINE_PER_DAY = 50
    }
    
    fun addBook(book: Book) {
        books[book.isbn] = book
    }
    
    fun borrowBook(isbn: String, userName: String) {
        val book = books[isbn] ?: throw IllegalArgumentException("Книга с ISBN $isbn не найдена")
        
        if (borrowedBooks.containsKey(isbn)) {
            throw IllegalArgumentException("Книга с ISBN $isbn уже выдана")
        }
        val user = users.getOrPut(userName) { User(userName) }
        if (user.outstandingFine > 0) {
            throw IllegalArgumentException("Пользователь $userName имеет непогашенный штраф: ${user.outstandingFine}")
        }
        borrowedBooks[isbn] = BorrowRecord(
            book = book,
            borrower = userName,
            borrowDate = LocalDate.now()
        )
    }
    fun returnBook(isbn: String) {
        val record = borrowedBooks[isbn] 
            ?: throw IllegalArgumentException("Книга с ISBN $isbn не была выдана")
        record.returnDate = LocalDate.now()
        val daysBorrowed = ChronoUnit.DAYS.between(record.borrowDate, record.returnDate).toInt()
        val daysOverdue = daysBorrowed - GRACE_PERIOD_DAYS
        if (daysOverdue > 0) {
            val user = users.getOrPut(record.borrower) { User(record.borrower) }
            user.outstandingFine += daysOverdue * FINE_PER_DAY
        }
        borrowedBooks.remove(isbn)
    }
    fun isAvailable(isbn: String): Boolean {
        return books.containsKey(isbn) && !borrowedBooks.containsKey(isbn)
    }
    fun calculateOverdueFine(isbn: String, daysOverdue: Int): Int {
        if (!borrowedBooks.containsKey(isbn)) {
            throw IllegalArgumentException("Книга с ISBN $isbn не выдана")
        }
        
        var fine: Int
        if (daysOverdue > GRACE_PERIOD_DAYS) {
            fine = (daysOverdue - GRACE_PERIOD_DAYS) * FINE_PER_DAY
        } else {
            fine = 0
        }
        return fine
    }
    
    fun getUserFine(userName: String): Int {
        return users[userName]?.outstandingFine ?: 0
    }
    
    fun payFine(userName: String, amount: Int) {
        val user = users[userName] 
            ?: throw IllegalArgumentException("Пользователь $userName не найден")
        
        if (amount > user.outstandingFine) {
            throw IllegalArgumentException("Сумма платежа превышает размер штрафа")
        }
        
        if (amount <= 0) {
            throw IllegalArgumentException("Сумма платежа должна быть положительной")
        }
        
        user.outstandingFine -= amount
    }
}
