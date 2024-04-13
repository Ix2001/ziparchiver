package com.demo.ziparchiver.service

import com.demo.ziparchiver.dto.UnZipRequestDto
import com.demo.ziparchiver.dto.ZipRequestDto
import java.io.File
import java.util.zip.ZipFile

interface ZipArchiveSerice {

    fun createZip(zipRequestDto: ZipRequestDto): ZipFile

    fun extractZip(unZipRequestDtoList: List<UnZipRequestDto>): File
}