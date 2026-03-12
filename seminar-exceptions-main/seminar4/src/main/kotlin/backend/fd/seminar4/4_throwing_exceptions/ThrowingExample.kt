package backend.fd.seminar4.throwing

fun main() {
    val te = ThrowingExample()
    te.validateStringForParsingToInt("df")
//    println(te.parseIntWithMessagesThrowException("ab"))

}

// Перевыброс исключений можно испльзовать для объединения нескольких исключений в одно
// Например при обработке файла мы можем - не найти файл, криво его прочитать, неожиданно потерять его (если кто-то удалил)
// Все такие ошибки можно объединить в одну - FileParseException, чтобы не плодить отдельные исключения
class ThrowingExample {

    fun parseIntWithMessagesThrowException(intStr: String): Int {
        return try {
            intStr.toInt()
        } catch (e: NumberFormatException) {
            val msg = "Can't parse \"$intStr\" to integer value"
            throw IllegalArgumentException(msg, e)
        }
    }

    fun validateStringForParsingToInt(intStr: String) {
        if (!intStr.matches(Regex("[0-9]+"))) {
            throw ValidationException("Passed string is not a number")
        }
    }

}
