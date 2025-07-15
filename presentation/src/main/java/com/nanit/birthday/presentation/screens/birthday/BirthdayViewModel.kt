package com.nanit.birthday.presentation.screens.birthday

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.usecases.UpdateBabyPictureUseCase
import com.nanit.birthday.presentation.helper.ImageInternalStorageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageStorageHelper: ImageInternalStorageHelper,
    private val updateBabyPictureUseCase: UpdateBabyPictureUseCase
): ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isCapturingForShare = MutableStateFlow(false)
    val isCapturingForShare: StateFlow<Boolean> = _isCapturingForShare.asStateFlow()

    fun updatePicture(pictureUri: Uri?) {
        clearError()

        if (pictureUri == null) {
            savePictureToRepository(null)
        } else {
            viewModelScope.launch {
                val permanentUri = imageStorageHelper.copyImageToInternalStorage(pictureUri)

                if (permanentUri != null) {
                    savePictureToRepository(permanentUri)
                } else {
                    _errorMessage.value = "Failed to save image"
                }
            }
        }
    }

    fun shareBirthday(onCaptureContent: suspend () -> Bitmap?) {
        viewModelScope.launch {
            try {
                // Step 1: Hide UI controls for clean capture
                _isCapturingForShare.value = true

                // Small delay to ensure UI updates
                delay(100)

                // Step 2: Capture the content
                val bitmap = onCaptureContent()

                if (bitmap != null) {
                    // Step 3: Save bitmap and create share intent
                    val shareUri = saveBitmapToCache(bitmap)
                    if (shareUri != null)
                        openShareDialog(shareUri)
                    else
                        _errorMessage.value = "Failed to save image for sharing"
                } else {
                    _errorMessage.value = "Failed to capture screen"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Share failed: ${e.message}"
            } finally {
                _isCapturingForShare.value = false
            }
        }
    }

    private suspend fun saveBitmapToCache(bitmap: Bitmap): Uri? =
        withContext(Dispatchers.IO) {
            try {
                val cacheDir = File(context.cacheDir, "shared_images")
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }

                val imageFile = File(cacheDir, "birthday_${System.currentTimeMillis()}.jpg")

                FileOutputStream(imageFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }

                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    imageFile
                )
            } catch (e: IOException) {
                null
            }
        }

    private fun openShareDialog(imageUri: Uri) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                putExtra(Intent.EXTRA_TEXT, "Check out this birthday celebration! 🎉")
                putExtra(Intent.EXTRA_SUBJECT, "Birthday Celebration")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share Birthday")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            _errorMessage.value = "Failed to open share dialog"
        }
    }

    private fun savePictureToRepository(pictureUri: String?) {
        viewModelScope.launch {
            updateBabyPictureUseCase(pictureUri)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {}
                        is Resource.Error -> {
                            _errorMessage.value = resource.message
                        }
                    }
                }
        }
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _errorMessage.value = null
    }

    fun setError(message: String) {
        _errorMessage.value = message
    }
}