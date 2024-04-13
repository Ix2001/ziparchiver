package com.demo.ziparchiver.controller

import com.demo.ziparchiver.dto.UnZipRequestDto
import com.demo.ziparchiver.service.ZipArchiveSerice
import com.demo.ziparchiver.dto.ZipRequestDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.zip.ZipFile

@RestController
@RequestMapping("/archive")
@Tag(name = "zipArchiveController", description = "контроллер для архивирования и разорхивирование файлов")
class ZipArchiveController(
    val zipArchiveService: ZipArchiveSerice
) {
    @PostMapping("/zip")
    @Operation(summary = "этот эндпоинт принимает в качестве тело запроса обьект ZipRequestDto в котором нужно указать путь к файлам и название для архива" +
            "и пожалйста не заьывайте что в JSON формате нужно путь указывать в формате D:\\Folder\\Folder\\fileName.file")
    fun zip(
        @RequestBody zipRequestDto: ZipRequestDto
    ): ZipFile {
        return zipArchiveService.createZip(zipRequestDto)
    }
    @PostMapping("/unzip")
    @Operation(summary ="этот эндпоинт принимает в качестве тело запроса лист обьектов UnZipRequestDto где нужно указать " +
            "путь к архиву и название папки в которую будет разорхивирован наш архив")
    fun unZip(
        @RequestBody unZipRequestDto: List<UnZipRequestDto>
    ): File {
        return zipArchiveService.extractZip(unZipRequestDto)
    }
}