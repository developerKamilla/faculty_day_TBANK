package backend.fd.seminar4.stacktrace

import java.io.PrintWriter
import java.io.StringWriter

fun main() {
    val stacktrace = extractExceptionTraceForAnalysis()
    println(stacktrace)

    println("\n=== Kotlin way ===")
    val kotlinStacktrace = extractExceptionTraceKotlinWay()
    println(kotlinStacktrace)
}

private fun extractExceptionTraceForAnalysis(): String {
    return try {
        "ab".toInt()
        "No trace"
    } catch (e: Exception) {
        val stringWriter = StringWriter()
        e.printStackTrace(PrintWriter(stringWriter))
        stringWriter.toString()
    }
}

// Kotlin способ получения stacktrace
private fun extractExceptionTraceKotlinWay(): String {
    return try {
        "ab".toInt()
        "No trace"
    } catch (e: Exception) {
        e.stackTraceToString()
    }
}
