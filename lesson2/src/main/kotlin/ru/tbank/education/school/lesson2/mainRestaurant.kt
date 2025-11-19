package ru.tbank.education.school.lesson2

fun main() {
    val dishMenu = listOf(
        Dish("Ratatui", 400.0),
        Dish("Pasta", 250.0),
        Dish("Lasagna by Garfield", 300.0)
    )

    val drinkMenu = listOf(
        Drink("Lavender raff", 230.0, 330),
        Drink("Matcha Latte", 130.0, 250),
        Drink("Kumis", 500.0, 200)
    )

    val order = Order("Order#001")
    val dishOrder = Dish("Pasta", 250.0)
    println(dishOrder.toOrder())
    val unknownDish = Dish("Pelmeni", 20.0)
    println(unknownDish.toOrder())
    order.addItem(dishOrder, 2)
    val dish2 = Dish("Lasagna by Garfield", 300.0)
    order.addItem(dish2)
    val drinkOrder = Drink("Kumis", 500.0, 200)
    println(drinkOrder.toOrder())
    order.addItem(drinkOrder, 3)
    val fakeDrink = Drink("Lavender raff", 999.0, 330)
    println(fakeDrink.toOrder())
    println()
    order.printOrder()
}