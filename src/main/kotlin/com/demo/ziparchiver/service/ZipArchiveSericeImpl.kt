package com.demo.ziparchiver.service

import com.demo.ziparchiver.dto.UnZipRequestDto
import com.demo.ziparchiver.dto.ZipRequestDto
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
* Реализация сервиса для работы с архивами.
*/
@Service
class ZipArchiveServiceImpl : ZipArchiveSerice {

    /**
     * Создает архив на основе переданных файлов и сохраняет его по указанному пути.
     *
     * @param zipRequestDto объект, содержащий список путей к файлам и путь для сохранения архива
     * @return созданный архив
     */
    override fun createZip(zipRequestDto: ZipRequestDto): ZipFile {
        var zipFileName = zipRequestDto.archivePath
        if (!zipFileName.endsWith(".zip")) {
            zipFileName += ".zip"
        }

        val zipFile = File(zipFileName)
        ZipOutputStream(FileOutputStream(zipFile)).use { zipOutputStream ->
            zipRequestDto.filePath.forEach { filePath ->
                val file = File(filePath)
                if (file.exists()) {
                    if (file.isFile) {
                        addFileToZip(zipOutputStream, file)
                    } else {
                        println("Путь $filePath указывает на директорию, а не на файл и будет пропущен.")
                    }
                } else {
                    println("Файл $filePath не найден и будет пропущен.")
                }
            }
        }
        println("Архив $zipFileName успешно создан.")
        return ZipFile(zipFile)
    }

    /**
     * Добавляет файл в архив.
     *
     * @param zipOutputStream поток вывода для архива
     * @param file файл для добавления в архив
     */
    private fun addFileToZip(zipOutputStream: ZipOutputStream, file: File) {
        zipOutputStream.putNextEntry(ZipEntry(file.name))
        file.inputStream().use { inputStream ->
            inputStream.copyTo(zipOutputStream)
        }
        zipOutputStream.closeEntry()
    }

    /**
     * Разархивирует архивы и сохраняет содержимое в указанную папку.
     *
     * @param unZipRequestDtoList список объектов, содержащих путь к архиву и путь к папке для разархивирования
     * @return папка с разархивированными файлами
     */
    override fun extractZip(unZipRequestDtoList: List<UnZipRequestDto>): File {
        // Создание выходной директории
        val outputDirectory = File("extracted_files")
        outputDirectory.mkdirs()

        // Разархивирование каждого архива из списка
        unZipRequestDtoList.forEach { unZipRequestDto ->
            val zipFileName = unZipRequestDto.filePath
            val outputFolder = File(unZipRequestDto.folderPath)
            ZipInputStream(FileInputStream(zipFileName)).use { zipInputStream ->
                var entry = zipInputStream.nextEntry
                while (entry != null) {
                    val entryFile = File(outputFolder, entry.name)
                    if (entry.isDirectory) {
                        entryFile.mkdirs()
                    } else {
                        Files.createDirectories(entryFile.parentFile.toPath())
                        FileOutputStream(entryFile).use { outputStream ->
                            zipInputStream.copyTo(outputStream)
                        }
                    }
                    zipInputStream.closeEntry()
                    entry = zipInputStream.nextEntry
                }
            }
            println("Архив $zipFileName успешно разархивирован в ${outputFolder.absolutePath}.")
        }
        return outputDirectory
    }
}