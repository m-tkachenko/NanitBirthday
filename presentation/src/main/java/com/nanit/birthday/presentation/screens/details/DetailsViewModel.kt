package com.nanit.birthday.presentation.screens.details

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.domain.usecases.GetBabyUseCase
import com.nanit.birthday.domain.usecases.GetBirthdayDisplayDataUseCase
import com.nanit.birthday.domain.usecases.ObserveBabyUseCase
import com.nanit.birthday.domain.usecases.UpdateBabyBirthdayUseCase
import com.nanit.birthday.domain.usecases.UpdateBabyNameUseCase
import com.nanit.birthday.domain.usecases.UpdateBabyPictureUseCase
import com.nanit.birthday.presentation.helper.ImageInternalStorageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import javax.inject.Inject
import kotlin.time.ExperimentalTime

/**
 * ViewModel for the Details screen.
 *
 * Responsibilities:
 * - Manage UI state for baby details form
 * - Handle name input with automatic saving (debounced)
 * - Handle birthday updates with immediate saving
 * - Load existing baby data on initialization
 * - Provide reactive updates when baby data changes
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getBabyBirthdayDisplayDataUseCase: GetBirthdayDisplayDataUseCase,
    private val updateBabyBirthdayUseCase: UpdateBabyBirthdayUseCase,
    private val updateBabyPictureUseCase: UpdateBabyPictureUseCase,
    private val imageStorageHelper: ImageInternalStorageHelper,
    private val updateBabyNameUseCase: UpdateBabyNameUseCase,
    private val observeBabyUseCase: ObserveBabyUseCase,
    private val getBabyUseCase: GetBabyUseCase
) : ViewModel() {

    // UI State for the name field
    private val _nameState = MutableStateFlow("")
    val nameState: StateFlow<String> = _nameState.asStateFlow()

    // UI State for the birthday field (as milliseconds for date picker)
    private val _birthdayState = MutableStateFlow<Long?>(null)
    val birthdayState: StateFlow<Long?> = _birthdayState.asStateFlow()

    // Loading state for operations
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Baby data state
    private val _babyState = MutableStateFlow<Baby?>(null)
    val babyState: StateFlow<Baby?> = _babyState.asStateFlow()

    private val _birthdayDisplayData = MutableStateFlow<BirthdayDisplayData?>(null)
    val birthdayDisplayData: StateFlow<BirthdayDisplayData?> = _birthdayDisplayData.asStateFlow()

    private val _isBirthdayLoading = MutableStateFlow(false)
    val isBirthdayLoading: StateFlow<Boolean> = _isBirthdayLoading.asStateFlow()

    init {
        observeBabyData()
        setupNameAutoSave()
        loadInitialBabyData()

        viewModelScope.launch {
            imageStorageHelper.cleanupOldTempFiles()
        }
    }

    /**
     * Updates the name in the UI state.
     * The actual saving is handled automatically by the debounced flow.
     */
    fun updateName(newName: String) {
        _nameState.value = newName
        clearError()
    }

    /**
     * Updates the birthday immediately when user selects a date.
     */
    fun updateBirthday(birthdayMillis: Long?) {
        birthdayMillis?.let { millis ->
            _birthdayState.value = millis
            clearError()
            saveBirthdayToRepository(millis)
        }
    }

    fun updatePicture(pictureUri: Uri?) {
        clearError()

        if (pictureUri == null) {
            savePictureToRepository(null)
        } else {
            viewModelScope.launch {
                _isLoading.value = true
                val permanentUri = imageStorageHelper.copyImageToInternalStorage(pictureUri)
                _isLoading.value = false

                if (permanentUri != null) {
                    savePictureToRepository(permanentUri)
                } else {
                    _errorMessage.value = "Failed to save image"
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

    /**
     * Observes baby data changes from the repository.
     * This ensures the UI stays in sync with the database.
     */
    @OptIn(ExperimentalTime::class)
    private fun observeBabyData() {
        observeBabyUseCase()
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _babyState.value = resource.data
                        resource.data?.let { baby ->
                            // Update name state if different
                            if (_nameState.value != baby.name)
                                _nameState.value = baby.name ?: ""

                            // Update birthday state if different
                            val babyBirthdayMillis = baby.birthday.toMillis()
                            if (_birthdayState.value != babyBirthdayMillis) {
                                _birthdayState.value = babyBirthdayMillis
                            }
                        }
                    }
                    is Resource.Error -> {
                        _errorMessage.value = resource.message
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Sets up automatic name saving with debounce to avoid excessive database calls.
     * Only saves when:
     * 1. Name is not empty after trimming
     * 2. Name has actually changed
     * 3. User has stopped typing for 500ms
     */
    private fun setupNameAutoSave() {
        _nameState
            .debounce(500)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .onEach { trimmedName ->
                val currentBaby = _babyState.value
                if (currentBaby == null || currentBaby.name != trimmedName) {
                    saveNameToRepository(trimmedName)
                }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalTime::class)
    private fun loadInitialBabyData() {
        viewModelScope.launch {
            getBabyUseCase()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                        is Resource.Success -> {
                            _isLoading.value = false
                            _babyState.value = resource.data
                            // Set initial states if baby exists
                            resource.data?.let { baby ->
                                _nameState.value = baby.name ?: ""
                                _birthdayState.value = baby.birthday.toMillis()
                            }
                        }
                        is Resource.Error -> {
                            _isLoading.value = false
                            _errorMessage.value = resource.message
                        }
                    }
                }
        }
    }

    fun loadBirthdayScreenData(onNavigateToBirthday: (BirthdayDisplayData) -> Unit) {
        viewModelScope.launch {
            _isBirthdayLoading.value = true
            clearError()

            getBabyBirthdayDisplayDataUseCase()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            _isBirthdayLoading.value = false
                            val displayData = resource.data
                            _birthdayDisplayData.value = displayData
                            onNavigateToBirthday(displayData)
                        }
                        is Resource.Error -> {
                            _isBirthdayLoading.value = false
                            _errorMessage.value = resource.message
                        }
                    }
                }
        }
    }

    private fun saveNameToRepository(name: String) {
        viewModelScope.launch {
            updateBabyNameUseCase(name)
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

    @OptIn(ExperimentalTime::class)
    private fun saveBirthdayToRepository(birthdayMillis: Long) {
        viewModelScope.launch {
            updateBabyBirthdayUseCase(birthdayMillis)
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

    @OptIn(ExperimentalTime::class)
    private fun LocalDate?.toMillis() =
        this?.atStartOfDayIn(TimeZone.currentSystemDefault())?.toEpochMilliseconds()

}