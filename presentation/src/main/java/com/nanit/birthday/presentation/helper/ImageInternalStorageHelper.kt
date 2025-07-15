package com.nanit.birthday.presentation.helper

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageInternalStorageHelper(
    private val context: Context
) {
    suspend fun copyImageToInternalStorage(tempUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val timestamp = System.currentTimeMillis()
                val filename = "baby_photo_$timestamp.jpg"
                val destinationFile = File(context.filesDir, filename)

                context.contentResolver.openInputStream(tempUri)?.use { input ->
                    FileOutputStream(destinationFile).use { output ->
                        input.copyTo(output)
                    }
                }

                Uri.fromFile(destinationFile).toString()
            } catch (_: Exception) {
                null
            }
        }
    }

    suspend fun cleanupOldTempFiles() {
        return withContext(Dispatchers.IO) {
            context.cacheDir.listFiles()?.forEach { file ->
                if (file.name.startsWith("temp_baby_photo_")) {
                    file.delete()
                }
            }
        }
    }
}