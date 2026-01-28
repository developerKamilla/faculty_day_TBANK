import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

fun main() {
    task4()
    println()
    task5()
    println()
    task6()
    println()
    task7()
    println()
    testNormalize()
    testing()
}
fun task4() {
    val strings = listOf("A-123", "B-7", "AA-12", "C-001", "D-99x")
    val regex = Regex("""^([A-Z])-(\\d{1,3})$""")
    val result = strings.filter { regex.matches(it) }
    println("Задание 4: $result")
}


fun task5() {
    val strings = listOf("  Hello   world  ", "A   B    C", "   one")
    val result = strings.map { it.trim().replace(Regex("""\\s+"""), " ") }
    println("Задание 5: $result")
}

fun task6() {
    val datePairs = listOf(
        Pair("2026-01-01", "2026-01-10"),
        Pair("2025-12-31", "2026-01-01"),
        Pair("2026-02-01", "2026-01-22")
    )

    val result = datePairs.map { (first, second) ->
        ChronoUnit.DAYS.between(LocalDate.parse(first), LocalDate.parse(second))
    }
    println("Задание 6: $result")
}

fun task7() {
    val strings = listOf("math:Ivan", "bio:Olga", "math:Max", "bio:Ivan", "cs:Olga")
    val result = mutableMapOf<String, MutableList<String>>()

    strings.forEach {
        val (subject, student) = it.split(":")
        result.getOrPut(subject) { mutableListOf() }.add(student)
    }

    println("Задание 7: $result")
}

fun normalize(line: String): Triple<String, Int, String>? {
    val trimmedLine = line.trim()
    val patternA = Regex("""(\d{4})-(\d{2})-(\d{2})\s+(\d{2}:\d{2}).*?ID:\s*(\d+).*?STATUS:\s*(\w+)""", RegexOption.IGNORE_CASE)
    val patternB = Regex("""TS=(\d{2})/(\d{2})/(\d{4})-(\d{2}:\d{2}).*?status=(\w+).*?#(\d+)""", RegexOption.IGNORE_CASE)
    val patternC = Regex("""\[(\d{2})\.(\d{2})\.(\d{4})\s+(\d{2}:\d{2})\]\s+(\w+).*?id:\s*(\d+)""", RegexOption.IGNORE_CASE)
    patternA.find(trimmedLine)?.let { match ->
        val (year, month, day, time, idStr, status) = match.destructured
        val statusLower = status.lowercase()
        if (statusLower in listOf("sent", "delivered")) {
            val dt = "$year-$month-$day $time"
            return Triple(dt, idStr.toInt(), statusLower)
        }
    }
    patternB.find(trimmedLine)?.let { match ->
        val (day, month, year, time, status, idStr) = match.destructured
        val statusLower = status.lowercase()
        if (statusLower in listOf("sent", "delivered")) {
            val dt = "$year-$month-$day $time"
            return Triple(dt, idStr.toInt(), statusLower)
        }
    }
    patternC.find(trimmedLine)?.let { match ->
        val (day, month, year, time, status, idStr) = match.destructured
        val statusLower = status.lowercase()
        if (statusLower in listOf("sent", "delivered")) {
            val dt = "$year-$month-$day $time"
            return Triple(dt, idStr.toInt(), statusLower)
        }
    }
    return null
}

fun testNormalize() {
    val logs = listOf(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "[22.01.2026 09:40] delivered (id:044)",
        "Битая строка"
    )
    println("Тестирование normalize:")
    logs.forEach { log ->
        val result = normalize(log)
        if (result != null) {
            val (dt, id, status) = result
            println("  Успех: $dt, ID: $id, Статус: $status")
        } else {
            println("  Битая строка: $log")
        }
    }
}

data class DeliveryResult(val id: Int, val duration: Int)

fun calculateDeliveries(normalizedLines: List<Triple<String, Int, String>>): Map<String, Any> {
    val grouped = normalizedLines.groupBy { it.second }

    val complete = mutableListOf<DeliveryResult>()
    val incomplete = mutableListOf<Int>()
    val timeError = mutableListOf<Int>()

    grouped.forEach { (id, events) ->
        val sentEvent = events.find { it.third == "sent" }
        val delivered = events.find { it.third == "delivered" }

        when {
            sentEvent == null || delivered == null -> {
                incomplete.add(id)
            }
            else -> {
                // Функция для преобразования даты в общее количество минут
                fun toMinutes(dt: String): Long {
                    val (date, time) = dt.split(" ")
                    val (year, month, day) = date.split("-").map { it.toInt() }
                    val (hour, minute) = time.split(":").map { it.toInt() }

                    // Простое преобразование, достаточно для сравнения и разницы
                    return (year * 365L * 24 * 60) +
                            (month * 30L * 24 * 60) +  // Приблизительно
                            (day * 24L * 60) +
                            (hour * 60L) +
                            minute
                }

                val sentMin = toMinutes(sentEvent.first)
                val delMin = toMinutes(delivered.first)

                if (delMin < sentMin) {
                    timeError.add(id)
                } else {
                    val duration = (delMin - sentMin).toInt()
                    complete.add(DeliveryResult(id, duration))
                }
            }
        }
    }
    return mapOf(
        "complete" to complete,
        "incomplete" to incomplete,
        "timeError" to timeError,
    )
}

fun generateReport(complet: List<DeliveryResult>) {
    if (complet.isEmpty()) {
        println("No finished delivery")
        return
    }

    val sorted = complet.sortedByDescending { it.duration }
    sorted.forEach {println("ID: ${it.id}, Duration: ${it.duration}")}
    println("Longest order:")
    val longest = sorted.first()
    println("ID: ${longest.id}, Duration: ${longest.duration}")

    println("(>20 минут):")
    val noo = sorted.filter { it.duration > 20 }
    if (noo.isEmpty()) {
        println("No rule-breakers")
    } else {
        noo.forEach {println("ID: ${it.id}, Duration: ${it.duration}")}
    }
}

fun testing() {
    val logs = listOf(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "[22.01.2026 09:40] delivered (id:044)",
        "TS=22/01/2026-09:05; status=delivered; #047",
        "Битая строка",
        "TS=22/01/2026-09:15; status=sent; #044",
        "TS=22/01/2026-08:00; status=delivered; #045",
        "2026-01-22 10:00 | ID:046 | STATUS:sent",
        "2026-01-22 09:00 | ID:047 | STATUS:sent",
    )
    val normalized = mutableListOf<Triple<String, Int, String>>()
    val broken = mutableListOf<String>()

    logs.forEach { log ->
        normalize(log)?.let {
            normalized.add(it)
        } ?: run {
            broken.add(log)
        }
    }
    println("Битых строк: ${broken.size}")
    if (broken.isNotEmpty()) {
        println("Битые строки: ${broken.joinToString(", ")}")
    }
    // расчет доставок
    val results = calculateDeliveries(normalized)
    val complete = results["complete"] as? List<DeliveryResult> ?: emptyList()
    val incomplete = results["incomplete"] as? List<Int> ?: emptyList()
    val timeError = results["timeError"] as? List<Int> ?: emptyList()
    println("Корректных доставок: ${complete.size}")
    println("Not full ID: ${if (incomplete.isEmpty()) "no" else incomplete.joinToString(", ")}")
    println("TimeError: ${if (timeError.isEmpty()) "no" else timeError.joinToString(", ")}")
    println("ОТЧЕТ")
    generateReport(complete)
}