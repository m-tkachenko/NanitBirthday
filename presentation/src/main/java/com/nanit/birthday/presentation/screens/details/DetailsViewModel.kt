package com.nanit.birthday.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.usecases.GetBabyUseCase
import com.nanit.birthday.domain.usecases.ObserveBabyUseCase
import com.nanit.birthday.domain.usecases.UpdateBabyNameUseCase
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
import javax.inject.Inject

/**
 * ViewModel for the Details screen.
 *
 * Responsibilities:
 * - Manage UI state for baby details form
 * - Handle name input with automatic saving (debounced)
 * - Load existing baby data on initialization
 * - Provide reactive updates when baby data changes
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getBabyUseCase: GetBabyUseCase,
    private val observeBabyUseCase: ObserveBabyUseCase,
    private val updateBabyNameUseCase: UpdateBabyNameUseCase
) : ViewModel() {

    // UI State for the name field
    private val _nameState = MutableStateFlow("")
    val nameState: StateFlow<String> = _nameState.asStateFlow()

    // Loading state for operations
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Baby data state
    private val _babyState = MutableStateFlow<Baby?>(null)
    val babyState: StateFlow<Baby?> = _babyState.asStateFlow()

    init {
        // Start observing baby data changes
        observeBabyData()

        // Setup automatic name saving with debounce
        setupNameAutoSave()

        // Load initial baby data
        loadInitialBabyData()
    }

    /**
     * Updates the name in the UI state.
     * The actual saving is handled automatically by the debounced flow.
     */
    fun updateName(newName: String) {
        _nameState.value = newName
        // Clear any previous error when user starts typing
        _errorMessage.value = null
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Observes baby data changes from the repository.
     * This ensures the UI stays in sync with the database.
     */
    private fun observeBabyData() {
        observeBabyUseCase()
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _babyState.value = resource.data
                        resource.data?.let { baby ->
                            if (_nameState.value != baby.name)
                                _nameState.value = baby.name.toString()
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
                if (currentBaby == null || currentBaby.name != trimmedName)
                    saveNameToRepository(trimmedName)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Loads initial baby data when ViewModel is created.
     */
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
                            // Set initial name if baby exists
                            resource.data?.let { baby ->
                                _nameState.value = baby.name.toString()
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

    /**
     * Saves the name to the repository using the UpdateBabyNameUseCase.
     * Handles the case where no baby exists yet by creating a minimal baby profile.
     */
    private fun saveNameToRepository(name: String) {
        viewModelScope.launch {
            updateBabyNameUseCase(name)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Don't show loading for auto-save operations
                        }
                        is Resource.Success -> {
                            // Successfully saved - the reactive observer will update UI
                        }
                        is Resource.Error -> {
                            _errorMessage.value = resource.message
                        }
                    }
                }
        }
    }
}