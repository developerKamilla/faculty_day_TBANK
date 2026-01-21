package ru.tbank.education.school.lesson8.practise

import java.io.*
import java.util.zip.*

fun createZipSimple(sourceDir: String, zipFile: String) {
    val dir = File(sourceDir)
    println("Checking if correct path: '${dir.absolutePath}'")

    if (!dir.exists()) {
        println("No folder found at: '${dir.absolutePath}'")
        return
    }
    val files = mutableListOf<File>()
    fun searchFiles(currentDir: File) {
        currentDir.listFiles()?.forEach { item ->
            if (item.isFile && (item.name.endsWith(".txt") || item.name.endsWith(".log"))) {
                files.add(item)
            } else if (item.isDirectory) {
                searchFiles(item)
            }
        }
    }
    searchFiles(dir)

    if (files.isEmpty()) {
        println("No txt or log files in the folder")
        return
    }

    println("Starting archiving")
    FileOutputStream(zipFile).use { stream ->
        ZipOutputStream(stream).use { res ->
            for (file in files) {
                val entry = ZipEntry(file.name)
                res.putNextEntry(entry)
                FileInputStream(file).use { book ->
                    book.copyTo(res)
                }
                res.closeEntry()
                println("File added: ${file.name}")
            } }
    }
    println("Created: $zipFile")
}

fun main() {
    createZipSimple(
        "C:\\Users\\LG\\faculty_day_TBANK\\lesson8\\src\\main\\kotlin\\ru\\tbank\\education\\school\\lesson8\\practise\\les9",
        "C:\\Users\\LG\\faculty_day_TBANK\\lesson8\\src\\main\\kotlin\\ru\\tbank\\education\\school\\lesson8\\practise\\archive.zip"
    )}