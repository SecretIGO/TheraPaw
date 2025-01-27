package com.experiments.therapaw.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

fun saveImageToInternalStorage(
    context: Context,
    imgData: ByteArray,
    fileName: String
): String? {
    return try {
        val file = File(context.filesDir, fileName)
        val bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.size)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun loadImageFromPath(filePath: String): Bitmap? {
    return try {
        BitmapFactory.decodeFile(filePath)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}