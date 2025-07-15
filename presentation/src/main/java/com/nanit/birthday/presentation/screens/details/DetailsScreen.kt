package com.nanit.birthday.presentation.screens.details

import android.net.Uri
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.presentation.components.PhotoPicker
import com.nanit.birthday.presentation.screens.details.components.DebugBabyInfo
import com.nanit.birthday.presentation.screens.details.components.PictureSection
import com.nanit.birthday.presentation.screens.details.sections.AppTitleSection
import com.nanit.birthday.presentation.screens.details.sections.BirthdayInputSection
import com.nanit.birthday.presentation.screens.details.sections.FormValidationHint
import com.nanit.birthday.presentation.screens.details.sections.NameInputSection
import com.nanit.birthday.presentation.screens.details.sections.ShowBirthdayButton
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Main Details Screen - Orchestrates the baby profile input flow.
 *
 * This screen handles:
 * - State management and business logic coordination
 * - Photo selection using the reusable PhotoPicker
 * - Navigation to birthday screen
 * - Error handling and user feedback
 */
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    onShowBirthdayScreen: (BirthdayDisplayData) -> Unit = {}
) {
    // ViewModel state collection
    val nameState by viewModel.nameState.collectAsState()
    val birthdayState by viewModel.birthdayState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isBirthdayLoading by viewModel.isBirthdayLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val babyState by viewModel.babyState.collectAsState()

    // Local UI state for photo picker
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var showPhotoPicker by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }

    // Date formatting for display
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val birthdayDisplayText = remember(birthdayState) {
        birthdayState?.let { millis ->
            dateFormatter.format(millis)
        } ?: ""
    }

    val isButtonEnabled by remember {
        derivedStateOf {
            nameState.trim().isNotEmpty() && birthdayState != null && !isLoading && !isBirthdayLoading
        }
    }

    val onSelectPicture = {
        focusManager.clearFocus()
        showPhotoPicker = true
    }

    val handlePhotoSelected = { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            viewModel.updatePicture(uri)
        }
    }

    val handlePhotoError = { error: String ->
        viewModel.setError(error)
    }

    // Side effects
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    LaunchedEffect(babyState) {
        babyState?.pictureUri?.let { storedUri ->
            if (selectedImageUri == null) {
                selectedImageUri = storedUri.toUri()
            }
        }
    }

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
                    isLoading = isBirthdayLoading,
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.loadBirthdayScreenData(onShowBirthdayScreen)
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

    // Photo Picker Component
    PhotoPicker(
        isVisible = showPhotoPicker,
        onPhotoSelected = handlePhotoSelected,
        onDismiss = { showPhotoPicker = false },
        onError = handlePhotoError
    )
}