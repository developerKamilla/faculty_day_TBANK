package backend.fd.seminar4.homework.basics.exercises.task1

/**
 * Задача 1: Иерархия исключений
 *
 * Создайте собственную иерархию исключений для системы интернет-магазина.
 */

/**
 * TODO: Создайте иерархию исключений
 *
 * Требования:
 * - Базовое исключение ShopException
 * - 3-4 специфичных исключения (см. примеры ниже)
 * - Каждое исключение должно иметь осмысленное сообщение
 */

// Базовое исключение
sealed class ShopException(message: String) : Exception(message)

// TODO: Добавьте специфичные исключения:
// - ProductNotFoundException: товар не найден
// - InsufficientStockException: недостаточно товара на складе
// - InvalidPriceException: некорректная цена
// - PaymentFailedException: ошибка оплаты

// Пример:
class ProductNotFoundException(
    val productId: String
) : ShopException("Product not found: $productId")

// TODO: Добавьте остальные исключения


// Классы для примера
data class Product(val id: String, val name: String, val price: Double, val stock: Int)

class Shop {
    private val products = mapOf(
        "P001" to Product("P001", "Laptop", 50000.0, 5),
        "P002" to Product("P002", "Mouse", 1000.0, 0)
    )

    fun findProduct(id: String): Product {
        // TODO: Выбросьте ProductNotFoundException если товар не найден
        return products[id] ?: throw ProductNotFoundException(id)
    }

    fun checkStock(product: Product, quantity: Int) {
        // TODO: Выбросьте InsufficientStockException если недостаточно товара
    }

    fun validatePrice(price: Double) {
        // TODO: Выбросьте InvalidPriceException если цена <= 0
    }
}

fun main() {
    val shop = Shop()

    println("=== Тест 1: Товар найден ===")
    try {
        val product = shop.findProduct("P001")
        println("Найден: ${product.name}")
    } catch (e: ShopException) {
        println("Ошибка: ${e.message}")
    }

    println("\n=== Тест 2: Товар не найден ===")
    try {
        shop.findProduct("P999")
    } catch (e: ShopException) {
        println("Ошибка: ${e.message}")
    }

    // TODO: Добавьте тесты для других исключений
}
