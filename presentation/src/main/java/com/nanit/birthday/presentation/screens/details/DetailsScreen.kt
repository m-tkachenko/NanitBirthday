package com.nanit.birthday.presentation.screens.details

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.nanit.birthday.presentation.screens.details.components.DebugBabyInfo
import com.nanit.birthday.presentation.screens.details.components.ImageSourceDialog
import com.nanit.birthday.presentation.screens.details.components.PictureSection
import com.nanit.birthday.presentation.screens.details.sections.AppTitleSection
import com.nanit.birthday.presentation.screens.details.sections.BirthdayInputSection
import com.nanit.birthday.presentation.screens.details.sections.FormValidationHint
import com.nanit.birthday.presentation.screens.details.sections.NameInputSection
import com.nanit.birthday.presentation.screens.details.sections.ShowBirthdayButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Main Details Screen - Orchestrates the baby profile input flow.
 *
 * This screen handles:
 * - State management and business logic coordination
 * - Camera/Gallery integration with permissions
 * - Navigation to birthday screen
 * - Error handling and user feedback
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    onShowBirthdayScreen: () -> Unit = {}
) {
    // ViewModel state collection
    val nameState by viewModel.nameState.collectAsState()
    val birthdayState by viewModel.birthdayState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val babyState by viewModel.babyState.collectAsState()

    // Local UI state for camera/gallery functionality
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }

    // Camera permission handling
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    // Camera capture setup
    val tempImageFile = remember {
        File(context.cacheDir, "temp_baby_photo_${System.currentTimeMillis()}.jpg")
    }
    val tempImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }

    // Activity result launchers
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            viewModel.updatePicture(uri)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            selectedImageUri = tempImageUri
            viewModel.updatePicture(tempImageUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(tempImageUri)
        }
    }

    // Date formatting for display
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val birthdayDisplayText = remember(birthdayState) {
        birthdayState?.let { millis ->
            dateFormatter.format(millis)
        } ?: ""
    }

    // Form validation state
    val isButtonEnabled by remember {
        derivedStateOf {
            nameState.trim().isNotEmpty() && birthdayState != null
        }
    }

    // Camera action handlers
    val onSelectPicture = {
        focusManager.clearFocus()
        showImageSourceDialog = true
    }

    val onGallerySelected = {
        showImageSourceDialog = false
        galleryLauncher.launch("image/*")
    }

    val onCameraSelected = {
        showImageSourceDialog = false
        when {
            cameraPermissionState.status.isGranted -> {
                cameraLauncher.launch(tempImageUri)
            }
            cameraPermissionState.status.shouldShowRationale -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Side effects
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
        babyState?.pictureUri?.let { storedUri ->
            if (selectedImageUri == null) {
                selectedImageUri = storedUri.toUri()
            }
        }
    }

    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
    }

    // Main UI Layout
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    contentDescription = "Baby details form"
                },
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AppTitleSection()
            }

            item {
                NameInputSection(
                    name = nameState,
                    onNameChange = viewModel::updateName,
                    focusRequester = nameFocusRequester,
                    isLoading = isLoading
                )
            }

            item {
                BirthdayInputSection(
                    birthday = birthdayDisplayText,
                    birthdayState = birthdayState,
                    onBirthdaySelected = viewModel::updateBirthday,
                    onFocusCleared = { focusManager.clearFocus() }
                )
            }

            item {
                PictureSection(
                    selectedImageUri = selectedImageUri ?: babyState?.pictureUri?.toUri(),
                    onSelectPicture = onSelectPicture
                )
            }

            item {
                ShowBirthdayButton(
                    enabled = isButtonEnabled,
                    onClick = {
                        focusManager.clearFocus()
                        onShowBirthdayScreen()
                    }
                )
            }

            item {
                FormValidationHint(visible = !isButtonEnabled)
            }

            // Debug info (remove in production)
            item {
                babyState?.let { baby ->
                    DebugBabyInfo(baby = baby)
                }
            }
        }

        // Snackbar for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Dialogs
    if (showImageSourceDialog) {
        ImageSourceDialog(
            onDismiss = { showImageSourceDialog = false },
            onGallerySelected = onGallerySelected,
            onCameraSelected = onCameraSelected
        )
    }
}