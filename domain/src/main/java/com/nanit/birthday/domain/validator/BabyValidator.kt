package com.nanit.birthday.domain.validator

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Validator for baby data according to business rules.
 *
 * This validator:
 * - Encapsulates all baby-related validation logic
 * - Can be reused across multiple use cases (save, update)
 * - Provides both individual field validation and complete validation
 * - Returns clear, user-friendly error messages
 * - Is easily testable in isolation
 *
 * Business rules:
 * - Name: 1-50 characters, not blank after trimming
 * - Birthday: Cannot be in the future
 * - Picture URI: Must be valid format if provided
 */
class BabyValidator() {
    /**
     * Validates complete baby data for creation/save operations.
     *
     * @param name Baby's name
     * @param birthday Baby's birthday
     * @param pictureUri Optional picture URI
     * @return ValidationResult with success or specific error message
     */
    fun validateBabyData(
        name: String,
        birthday: LocalDate,
        pictureUri: String? = null
    ): ValidationResult {
        // Validate name
        validateName(name).let { result ->
            if (result.isFailure) return result
        }

        // Validate birthday
        validateBirthday(birthday).let { result ->
            if (result.isFailure) return result
        }

        // Validate picture URI if provided
        validatePictureUri(pictureUri).let { result ->
            if (result.isFailure) return result
        }

        return ValidationResult.Success
    }

    /**
     * Validates baby name according to business rules.
     *
     * @param name Baby's name to validate
     * @return ValidationResult with success or error message
     */
    fun validateName(name: String): ValidationResult {
        val trimmedName = name.trim()

        return when {
            trimmedName.isBlank() -> {
                ValidationResult.Error("Baby name cannot be empty")
            }
            trimmedName.length < MIN_NAME_LENGTH -> {
                ValidationResult.Error("Baby name is too short")
            }
            trimmedName.length > MAX_NAME_LENGTH -> {
                ValidationResult.Error("Baby name cannot exceed $MAX_NAME_LENGTH characters")
            }
            else -> ValidationResult.Success
        }
    }

    /**
     * Validates baby birthday according to business rules.
     *
     * @param birthday Baby's birthday to validate
     * @return ValidationResult with success or error message
     */
    @OptIn(ExperimentalTime::class)
    fun validateBirthday(birthday: LocalDate): ValidationResult {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        return if (birthday > today) {
            ValidationResult.Error("Baby's birthday cannot be in the future")
        } else {
            ValidationResult.Success
        }
    }

    /**
     * Validates picture URI format if provided.
     *
     * @param pictureUri Picture URI to validate (can be null or blank)
     * @return ValidationResult with success or error message
     */
    fun validatePictureUri(pictureUri: String?): ValidationResult {
        val trimmedUri = pictureUri?.trim()

        // Null or blank URI is valid (optional field)
        if (trimmedUri.isNullOrBlank()) {
            return ValidationResult.Error("Picture URI is null or blank")
        }

        return if (isValidUriFormat(trimmedUri)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Invalid picture format")
        }
    }

    /**
     * Basic URI format validation.
     * Checks for common URI patterns used in Android apps.
     *
     * @param uri URI string to validate
     * @return true if URI format is valid
     */
    private fun isValidUriFormat(uri: String): Boolean {
        return uri.matches(
            Regex("^(content://|file://|https?://|android\\.resource://).*", RegexOption.IGNORE_CASE)
        )
    }

    companion object {
        private const val MAX_NAME_LENGTH = 50
        private const val MIN_NAME_LENGTH = 1
    }
}