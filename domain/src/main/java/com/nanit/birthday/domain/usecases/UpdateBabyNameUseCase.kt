package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow

/**
 * Use case for updating baby's name or creating new incomplete baby.
 *
 * Business rules enforced by BabyValidator:
 * - Name must not be blank after trimming
 * - Name must be between 1-50 characters
 *
 * Behavior:
 * - If baby exists: updates only the name field
 * - If baby doesn't exist: creates new baby with only name field set
 */
class UpdateBabyNameUseCase(
    private val babyRepository: BabyRepository,
    private val babyValidator: BabyValidator
) {
    operator fun invoke(newName: String) = flow<Resource<Unit>> {
        emit(Resource.Loading)

        val trimmedName = newName.trim()

        // Step 1: Validate new name using validator
        val validationResult = babyValidator.validateName(trimmedName)
        if (validationResult.isFailure) {
            emit(Resource.Error(validationResult.errorMessage!!))
            return@flow
        }

        // Step 2: Check if baby exists
        val babyExistsResult = babyRepository.babyExists()
        if (babyExistsResult.isFailure) {
            emit(Resource.Error(
                message = babyExistsResult.exceptionOrNull()?.toUserMessage()
                    ?: "Failed to check baby profile"
            ))
            return@flow
        }

        val babyExists = babyExistsResult.getOrDefault(false)

        // Step 3: Either update existing baby or create new partial baby
        val result = if (babyExists) {
            // Update existing baby's name only
            babyRepository.updateBabyName(trimmedName)
        } else {
            // Create new baby with only name field set
            val partialBaby = Baby.withName(trimmedName)
            babyRepository.saveBaby(partialBaby)
        }

        emit(
            result.fold(
                onSuccess = { Resource.Success(Unit) },
                onFailure = { throwable ->
                    Resource.Error(
                        message = throwable.toUserMessage(),
                        throwable = throwable
                    )
                }
            )
        )
    }
}