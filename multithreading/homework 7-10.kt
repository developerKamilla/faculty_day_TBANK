import kotlinx.coroutines.*
import java.io.File

// Задание 7. Первая корутина
object CoroutineLaunch {
    fun run(): List<String> = runBlocking {
        val jobs = listOf("Coroutine-A", "Coroutine-B", "Coroutine-C").map { name ->
            launch {
                repeat(5) {
                    delay(500)
                    println(name)
                }
            }
        }
        
        jobs.forEach { it.join() }
        return@runBlocking listOf("Completed")
    }
}

// Задание 7 (с результатами)
object CoroutineLaunchWithResult {
    fun run(): List<String> = runBlocking {
        val results = mutableListOf<String>()
        
        val jobs = listOf("Coroutine-A", "Coroutine-B", "Coroutine-C").map { name ->
            launch {
                repeat(5) {
                    delay(500)
                    val message = "$name: iteration ${it + 1}"
                    println(message)
                    results.add(message)
                }
            }
        }
        
        jobs.forEach { it.join() }
        return@runBlocking results
    }
}

// Задание 8. async/await
object AsyncAwaitEfficient {
    fun run(parts: Int = 4): Long = runBlocking {
        val total = 1_000_000
        val chunkSize = total / parts
        
        (0 until parts).map { part ->
            async {
                val start = part * chunkSize + 1
                val end = if (part == parts - 1) total else (part + 1) * chunkSize
                
                (start..end).fold(0L) { acc, i -> acc + i }
            }
        }.sumOf { it.await() }
    }
}

// Задание 9. Structured concurrency
object StructuredConcurrency {
    fun run(failingCoroutineIndex: Int): Int = runBlocking {
        val results = mutableListOf<Int>()
        coroutineScope {
            val jobs = (0 until 5).map { index ->
                launch {
                    try {
                        delay(100L * (index + 1))
                        if (index == failingCoroutineIndex) {
                            println("Coroutine $index throwing exception")
                            throw RuntimeException("Coroutine $index failed")
                        }
                        val result = index * 10
                        results.add(result)
                        println("Coroutine $index completed with result $result")
                    } catch (e: CancellationException) {
                        println("Coroutine $index was cancelled: ${e.message}")
                        throw e 
                    }
                }
            }
            jobs.forEach { it.join() }
        }
        
        println("Final results: $results")
        results.sum()
    }
}

// Задание 10. withContext
object WithContextIO {
    fun run(filePaths: List<String>): Map<String, String> = runBlocking {
        withContext(Dispatchers.IO) {
            val deferredResults = filePaths.map { filePath ->
                async {
                    try {
                        val content = File(filePath).readText()
                        filePath to content
                    } catch (e: Exception) {
                        println("Error reading file $filePath: ${e.message}")
                        filePath to "Error: ${e.message}"
                    }
                }
            }
            
            deferredResults.awaitAll().toMap()
        }
    }
}

fun main() = runBlocking {
    println("7 задача")
    CoroutineLaunch.run()
    
    println("\n7 (с результатами)")
    val results = CoroutineLaunchWithResult.run()
    println("Collected ${results.size} messages")
    
    println("\n8: async/await")
    val sum = AsyncAwaitEfficient.run(4)
    val expected = 1_000_000L * (1_000_000L + 1) / 2
    println("Sum of 1 to 1,000,000: $sum")
    println("Expected: $expected")
    println("Correct: ${sum == expected}")
    
    println("\n9: Structured concurrency")
    println("--- Case: No failure (index = -1) ---")
    try {
        val result = StructuredConcurrency.run(-1)
        println("Total sum: $result")
    } catch (e: Exception) {
        println("Caught: ${e.message}")
    }
    
    println("\nCoroutine 2 fails")
    try {
        val result = StructuredConcurrency.run(2)
        println("Total sum: $result")
    } catch (e: Exception) {
        println("Caught in main: ${e.message}")
    }
    
    println("\n10: withContext")
    createTestFiles()
    
    val filePaths = listOf("test1.txt", "test2.txt", "test3.txt", "nonexistent.txt")
    val fileContents = WithContextIO.run(filePaths)
    
    fileContents.forEach { (path, content) ->
        println("\n--- $path ---")
        println(content.take(100))
    }
    
    cleanupTestFiles()
}

// Вспомогательные функции для задания 10
fun createTestFiles() {
    val files = mapOf(
        "test1.txt" to "This is content of test file 1.\nLine 2\nLine 3",
        "test2.txt" to "Content of test file 2.\nMore data here.",
        "test3.txt" to "Third test file.\nWith multiple lines.\nAnd some additional content."
    )
    
    files.forEach { (name, content) ->
        File(name).writeText(content)
        println("Created: $name")
    }
}

fun cleanupTestFiles() {
    listOf("test1.txt", "test2.txt", "test3.txt").forEach { name ->
        File(name).delete()
        println("Deleted: $name")
    }
}
