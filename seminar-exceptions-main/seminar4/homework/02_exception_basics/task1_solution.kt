package backend.fd.seminar4.homework.basics.solutions.task1

/**
 * РЕШЕНИЕ Задачи 1: Иерархия исключений
 */

// Базовое исключение
sealed class ShopException(message: String) : Exception(message)

// Специфичные исключения
class ProductNotFoundException(val productId: String) :
    ShopException("Product not found: $productId")

class InsufficientStockException(val productId: String, val requested: Int, val available: Int) :
    ShopException("Insufficient stock for product $productId: requested $requested, available $available")

class InvalidPriceException(val price: Double) :
    ShopException("Invalid price: $price (must be positive)")

class PaymentFailedException(val amount: Double, val reason: String) :
    ShopException("Payment failed: cannot process $amount, reason: $reason")

// Классы для примера
data class Product(val id: String, val name: String, val price: Double, val stock: Int)

class Shop {
    private val products = mapOf(
        "P001" to Product("P001", "Laptop", 50000.0, 5),
        "P002" to Product("P002", "Mouse", 1000.0, 0),
        "P003" to Product("P003", "Keyboard", 2000.0, 10)
    )

    fun findProduct(id: String): Product {
        return products[id] ?: throw ProductNotFoundException(id)
    }

    fun checkStock(product: Product, quantity: Int) {
        if (product.stock < quantity) {
            throw InsufficientStockException(product.id, quantity, product.stock)
        }
    }

    fun validatePrice(price: Double) {
        if (price <= 0) {
            throw InvalidPriceException(price)
        }
    }

    fun processPayment(amount: Double, cardNumber: String) {
        // Симуляция проверки
        when {
            cardNumber.isBlank() -> throw PaymentFailedException(amount, "Card number is empty")
            amount <= 0 -> throw PaymentFailedException(amount, "Invalid amount")
            amount > 100000 -> throw PaymentFailedException(amount, "Amount exceeds limit")
            else -> println("✓ Payment of $amount processed successfully")
        }
    }

    // Комплексная операция покупки
    fun purchaseProduct(productId: String, quantity: Int, cardNumber: String): Result<String> {
        return try {
            // Находим продукт
            val product = findProduct(productId)
            println("Found product: ${product.name}")

            // Проверяем наличие
            checkStock(product, quantity)
            println("Stock check passed: $quantity available")

            // Рассчитываем сумму
            val totalAmount = product.price * quantity
            validatePrice(totalAmount)
            println("Total amount: $totalAmount")

            // Обрабатываем платеж
            processPayment(totalAmount, cardNumber)

            Result.success("Purchase successful: $quantity x ${product.name} for $totalAmount")

        } catch (e: ShopException) {
            println("✗ ${e.message}")
            Result.failure(e)
        }
    }
}

fun main() {
    val shop = Shop()

    println("=== Тест 1: Товар найден ===")
    try {
        val product = shop.findProduct("P001")
        println("✓ Найден: ${product.name}, цена: ${product.price}")
    } catch (e: ShopException) {
        println("✗ Ошибка: ${e.message}")
    }

    println("\n=== Тест 2: Товар не найден ===")
    try {
        shop.findProduct("P999")
    } catch (e: ProductNotFoundException) {
        println("✗ Ошибка: ${e.message}")
        println("  Product ID: ${e.productId}")
    }

    println("\n=== Тест 3: Проверка наличия - достаточно товара ===")
    try {
        val product = shop.findProduct("P001")
        shop.checkStock(product, 3)
        println("✓ Товара достаточно")
    } catch (e: ShopException) {
        println("✗ Ошибка: ${e.message}")
    }

    println("\n=== Тест 4: Недостаточно товара ===")
    try {
        val product = shop.findProduct("P002")
        shop.checkStock(product, 5)
    } catch (e: InsufficientStockException) {
        println("✗ Ошибка: ${e.message}")
        println("  Запрошено: ${e.requested}, доступно: ${e.available}")
    }

    println("\n=== Тест 5: Валидация цены ===")
    try {
        shop.validatePrice(-100.0)
    } catch (e: InvalidPriceException) {
        println("✗ Ошибка: ${e.message}")
        println("  Invalid price: ${e.price}")
    }

    println("\n=== Тест 6: Успешная покупка ===")
    shop.purchaseProduct("P001", 2, "1234-5678-9012-3456")
        .onSuccess { println("✓ $it") }
        .onFailure { println("✗ Ошибка: ${it.message}") }

    println("\n=== Тест 7: Покупка несуществующего товара ===")
    shop.purchaseProduct("P999", 1, "1234-5678")
        .onFailure { println("Тип ошибки: ${it::class.simpleName}") }

    println("\n=== Тест 8: Покупка товара которого нет в наличии ===")
    shop.purchaseProduct("P002", 5, "1234-5678")
        .onFailure { println("Тип ошибки: ${it::class.simpleName}") }

    println("\n=== Тест 9: Ошибка оплаты ===")
    shop.purchaseProduct("P003", 1, "")
        .onFailure { println("Тип ошибки: ${it::class.simpleName}") }

    println("\n=== Демонстрация иерархии ===")
    println("Все исключения являются ShopException:")
    try {
        throw ProductNotFoundException("TEST")
    } catch (e: ShopException) {
        println("✓ Поймано как ShopException: ${e.message}")
    }
}
