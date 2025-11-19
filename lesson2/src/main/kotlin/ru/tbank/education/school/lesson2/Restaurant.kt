package ru.tbank.education.school.lesson2

import kotlin.collections.contains

abstract class Menu(private val name:String, protected val price: Double) {
    abstract fun getMenu() : Boolean
    val itemName: String
        get() = name
    abstract fun toOrder() : String
}

class Dish(name: String, price: Double) : Menu(name, price) {
    val dishMenu = mapOf("Dragon Warrior ramen" to 350.0, "Ratatui" to 400.0,
        "Pasta" to 250.0,"Ramen" to 150.0, "Ice cream" to 350.0,
        "Pizza-Cake" to 300.0, "Crabsburger" to 280.0, "Omlet by Calcifer" to 400.0 ,
        "Lasagna by Garfield" to 300.0)

    override fun getMenu(): Boolean {
        println("Our Menu")
        for ((i, j) in dishMenu) {
            println("Dish: $i  Price: $j Bitcoins")
        }
        return true
    }
    override fun toOrder() : String {
        if (itemName in dishMenu && price == dishMenu[itemName] && itemName != "IDK what to order") {
            return "$itemName for $price successfully added"
        }
        return "You did smt wrong :("
    }
}
class Drink(
    name: String,
    price: Double,
    private val ml: Int
) : Menu(name, price) {
    val drinksMenu = mapOf(
        "Bubble Tea" to 80.0, "Nesquik" to 120.0,
        "Matcha Latte" to 130.0, "Kumis" to 500.0, "Indian tea" to 350.0,
        "Lavender raff" to 230.0
    )

    override fun getMenu(): Boolean {
        println("Our drinks menu")
        for ((i, j) in drinksMenu) {
            println("Drink: $i Price: $j Bitcoins")
        }
        return true
    }

    override fun toOrder(): String {
        if (itemName in drinksMenu && price == drinksMenu[itemName] && itemName != "IDK what to order") {
            return "$itemName for $price, Volume: $ml ml successfully added"
        }
        return "Dish does not exist :("
    }
}

data class OrderItem(
    val menuItem: Menu,
    var quantity: Int
)

class Order(
    val orderNumber: String
) {
    private val items: MutableList<OrderItem> = mutableListOf()
    fun addItem(menuItem: Menu, quantity: Int = 1) {
        items.add(OrderItem(menuItem, quantity))
    }
    fun printOrder() {
        println("Order $orderNumber:")
        for (item in items) {
            println("${item.quantity} x ${item.menuItem.toOrder()}")
        }
    }
}

fun main() {
    val dish = Dish("IDK what to order", 0.0)
    dish.getMenu()
    val dish1 = Dish("Ratatui", 400.0)
    val notdish = Dish("Pelmeni", 20.0)
    val dish2 = Dish("Crabsburger", 280.0)
    val drink = Drink("IDK what to order", 0.0, 0)
    drink.getMenu()
    val drink1 = Drink("Lavender raff", 230.0, 330)
    println("Create a name for your order")
    //var order:String = readLine()!!.toString()
    val order = Order("VibeCoder Order :)")
    order.addItem(notdish, 1)
    order.addItem(dish1, 2)
    order.addItem(dish2)
    order.addItem(drink1, 3)
    order.printOrder()
}