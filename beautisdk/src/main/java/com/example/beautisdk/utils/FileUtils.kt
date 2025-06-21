package com.example.beautisdk.utils

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun copyUriToCacheFile(context: Context, uri: Uri): Result<File> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return Result.failure(IllegalArgumentException("Can't open input stream from URI"))

            val fileName = "photo_${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)

            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }

            Result.success(tempFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}