package backend.fd.seminar4.homework.result.solutions.task3

import java.io.File

/**
 * РЕШЕНИЕ Задачи 3: Преобразование ошибок
 */

fun downloadFile(url: String): Result<ByteArray> = runCatching {
    // Симуляция загрузки
    println("Downloading from $url...")
    ByteArray(15_000_000) { it.toByte() }  // 15MB для теста
}

fun downloadAndSave(url: String, path: String): Result<File> {
    return downloadFile(url)
        .mapCatching { bytes ->
            println("Downloaded ${bytes.size} bytes")

            if (bytes.size > 10_000_000) {
                throw IllegalStateException("File too large: ${bytes.size} bytes (max 10MB)")
            }

            val file = File(path)
            file.writeBytes(bytes)
            file
        }
        .recover { error ->
            println("✗ Recovery: ${error.message}")
            val emptyFile = File("$path.error")
            emptyFile.writeText("Error: ${error.message}")
            emptyFile
        }
}

fun main() {
    println("=== Test downloadAndSave ===")

    downloadAndSave("http://example.com/small", "/tmp/small.dat")
        .fold(
            onSuccess = { println("✓ Saved: ${it.path}, size=${it.length()}") },
            onFailure = { println("✗ Failed: ${it.message}") }
        )

    println("\n=== Large file (with recovery) ===")
    downloadAndSave("http://example.com/large", "/tmp/large.dat")
        .fold(
            onSuccess = { println("✓ Recovered to: ${it.path}") },
            onFailure = { println("✗ Failed: ${it.message}") }
        )
}
