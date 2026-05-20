import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

data class DownloadStats(
    val totalTime: Long,
    val successful: Int,
    val failed: Int,
    val total: Int
) {
    override fun toString(): String {
        return """
            |
            |Статистика загрузки:
            |Общее время: ${totalTime} мс
            |Успешных загрузок: $successful/$total
            |Неуспешных загрузок: $failed/$total
        """.trimMargin()
    }
}

object ImageDownloader {
    fun run(urls: List<String>, outputDir: String): DownloadStats {
        // Создаём папку для загрузок
        val outputDirectory = File(outputDir)
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs()
        }

        val successfulCount = AtomicInteger(0)
        val failedCount = AtomicInteger(0)
        val completedCount = AtomicInteger(0)

        val startTime = System.currentTimeMillis()

        val executor = Executors.newFixedThreadPool(5)

        val futures = urls.mapIndexed { index, url ->
            executor.submit {
                try {
                    val fileName = "image_${index + 1}_${System.currentTimeMillis()}.jpg"
                    val outputFile = File(outputDirectory, fileName)

                    URL(url).openStream().use { inputStream ->
                        FileOutputStream(outputFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    successfulCount.incrementAndGet()
                    val completed = completedCount.incrementAndGet()
                    println("Downloaded $completed/${urls.size}")

                } catch (e: Exception) {
                    println("Ошибка при загрузке $url: ${e.message}")

                    // Обновляем счётчики
                    failedCount.incrementAndGet()
                    val completed = completedCount.incrementAndGet()
                    println("Downloaded $completed/${urls.size}")
                }
            }
        }

        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        val totalTime = System.currentTimeMillis() - startTime

        return DownloadStats(totalTime, successfulCount.get(), failedCount.get(), urls.size)
    }
}

fun generateImageUrls(count: Int = 10): List<String> {
    return List(count) { index ->
        "https://picsum.photos/200/300?random=${System.currentTimeMillis()}_$index"
    }
}

fun main() {
    val urls = generateImageUrls(10)
    val outputDir = "downloads"

    println("Начинаю загрузку ${urls.size} изображений...")
    val stats = ImageDownloader.run(urls, outputDir)
    println(stats)
    val downloadedFiles = File(outputDir).listFiles()?.size ?: 0
    println("\nСохранено файлов в папке '$outputDir': $downloadedFiles")
}
