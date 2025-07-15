package com.nanit.birthday.presentation.screens.birthday

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.usecases.UpdateBabyPictureUseCase
import com.nanit.birthday.presentation.helper.ImageInternalStorageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val imageStorageHelper: ImageInternalStorageHelper,
    private val updateBabyPictureUseCase: UpdateBabyPictureUseCase
): ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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