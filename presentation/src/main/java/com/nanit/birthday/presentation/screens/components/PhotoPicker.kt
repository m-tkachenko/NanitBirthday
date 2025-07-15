package com.nanit.birthday.presentation.components

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.nanit.birthday.presentation.screens.components.ImageSourceDialog
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoPicker(
    isVisible: Boolean,
    onPhotoSelected: (Uri?) -> Unit?,
    onDismiss: () -> Unit,
    onError: ((String) -> Unit)? = null
) {
    val context = LocalContext.current

    var showImageSourceDialog by rememberSaveable { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    val tempImageFile = remember {
        createTempImageFile(context)
    }

    val tempImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onPhotoSelected(uri)
        }
        onDismiss()
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            onPhotoSelected(tempImageUri)
        } else {
            onError?.invoke("Failed to capture photo")
        }
        onDismiss()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            try {
                cameraLauncher.launch(tempImageUri)
            } catch (e: Exception) {
                onError?.invoke("Camera not available: ${e.message}")
                onDismiss()
            }
        } else {
            onError?.invoke("Camera permission is required to take photos")
            onDismiss()
        }
    }

    val handleCameraSelection = {
        showImageSourceDialog = false

        when {
            cameraPermissionState.status.isGranted -> {
                try {
                    cameraLauncher.launch(tempImageUri)
                } catch (e: Exception) {
                    onError?.invoke("Camera not available: ${e.message}")
                    onDismiss()
                }
            }
            cameraPermissionState.status.shouldShowRationale -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    val handleGallerySelection = {
        showImageSourceDialog = false
        try {
            galleryLauncher.launch("image/*")
        } catch (e: Exception) {
            onError?.invoke("Gallery not available: ${e.message}")
            onDismiss()
        }
    }

    DisposableEffect(isVisible) {
        if (isVisible) {
            showImageSourceDialog = true
        }
        onDispose { }
    }

    if (showImageSourceDialog) {
        ImageSourceDialog(
            onDismiss = {
                showImageSourceDialog = false
                onDismiss()
            },
            onGallerySelected = handleGallerySelection,
            onCameraSelected = handleCameraSelection
        )
    }
}

private fun createTempImageFile(context: Context): File {
    val timestamp = System.currentTimeMillis()
    return File(context.cacheDir, "temp_baby_photo_$timestamp.jpg").apply {
        parentFile?.mkdirs()

        parentFile?.listFiles()?.forEach { file ->
            if (file.name.startsWith("temp_baby_photo_") &&
                file.lastModified() < timestamp - 24 * 60 * 60 * 1000) {
                file.delete()
            }
        }
    }
}