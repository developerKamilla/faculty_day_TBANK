package backend.fd.seminar4.homework.result.exercises.task3

import java.io.File

fun downloadFile(url: String): Result<ByteArray> = runCatching {
    // Симуляция
    ByteArray(1024) { it.toByte() }
}

// TODO: downloadAndSave с проверкой размера
fun downloadAndSave(url: String, path: String): Result<File> {
    throw NotImplementedError()
}

fun main() {
    downloadAndSave("http://example.com/file", "/tmp/file.dat")
        .onSuccess { println("✓ Saved: ${it.path}") }
        .onFailure { println("✗ Error: ${it.message}") }
}
