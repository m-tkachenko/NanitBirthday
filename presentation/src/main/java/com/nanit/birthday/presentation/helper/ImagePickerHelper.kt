package com.nanit.birthday.presentation.helper

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File

/**
 * Helper class for handling image selection from camera or gallery.
 * Encapsulates permission handling, camera capture, and gallery selection.
 */
class ImagePickerHelper(
    private val context: Context,
    private val onImageSelected: (Uri?) -> Unit,
    private val onError: (String) -> Unit
) {
    private var tempImageUri: Uri? = null

    private lateinit var galleryLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    private lateinit var cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>

    fun initializeLaunchers(
        galleryLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
        cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>
    ) {
        this.galleryLauncher = galleryLauncher
        this.cameraLauncher = cameraLauncher
    }

    /**
     * Launches gallery picker for image selection.
     */
    fun launchGallery() {
        try {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } catch (e: Exception) {
            onError("Failed to open gallery")
        }
    }

    /**
     * Launches camera for photo capture.
     * Creates temporary file and uses FileProvider for camera access.
     */
    fun launchCamera() {
        try {
            val imageFile = createTempImageFile()
            tempImageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            tempImageUri?.let { uri ->
                cameraLauncher.launch(uri)
            } ?: run {
                onError("Failed to create camera file")
            }
        } catch (e: Exception) {
            onError("Failed to open camera")
        }
    }

    /**
     * Handles gallery result.
     */
    fun handleGalleryResult(uri: Uri?) {
        onImageSelected(uri)
    }

    /**
     * Handles camera result.
     */
    fun handleCameraResult(success: Boolean) {
        if (success && tempImageUri != null) {
            onImageSelected(tempImageUri)
        } else {
            onError("Failed to capture photo")
        }
        tempImageUri = null
    }

    /**
     * Creates a temporary file for camera capture.
     */
    private fun createTempImageFile(): File {
        val timeStamp = System.currentTimeMillis()
        val fileName = "baby_photo_$timeStamp.jpg"
        return File(context.cacheDir, fileName)
    }
}

/**
 * Composable function that creates and remembers an ImagePickerHelper with all necessary launchers.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberImagePickerHelper(
    onImageSelected: (Uri?) -> Unit,
    onError: (String) -> Unit
): ImagePickerHelper {
    val context = LocalContext.current

    // Create the helper
    val helper = remember {
        ImagePickerHelper(
            context = context,
            onImageSelected = onImageSelected,
            onError = onError
        )
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        helper.handleGalleryResult(uri)
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        helper.handleCameraResult(success)
    }

    // Initialize launchers in the helper
    helper.initializeLaunchers(galleryLauncher, cameraLauncher)

    return helper
}