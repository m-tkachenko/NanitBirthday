package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow

/**
 * Use case for updating baby's picture or creating new incomplete baby.
 *
 * Business rules enforced by BabyValidator:
 * - Picture URI must be valid format if provided
 * - Null or blank URI is valid (removes picture)
 *
 * Behavior:
 * - If baby exists: updates only the picture field
 * - If baby doesn't exist and pictureUri is not null: creates new baby with only picture field set
 * - If baby doesn't exist and pictureUri is null: no operation (nothing to save)
 */
class UpdateBabyPictureUseCase(
    private val babyRepository: BabyRepository,
    private val babyValidator: BabyValidator
) {
    operator fun invoke(newPictureUri: String?) = flow<Resource<Unit>> {
        emit(Resource.Loading)

        // Step 1: Validate new picture URI using validator
        val validationResult = babyValidator.validatePictureUri(newPictureUri)
        if (validationResult.isFailure) {
            emit(Resource.Error(validationResult.errorMessage!!))
            return@flow
        }

        val cleanPictureUri = newPictureUri?.trim()?.takeIf { it.isNotBlank() }

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
            // Update existing baby's picture only
            babyRepository.updateBabyPicture(cleanPictureUri)
        } else {
            // Create new baby with only picture field set (if URI is not null)
            if (cleanPictureUri != null) {
                val partialBaby = Baby.withPicture(cleanPictureUri)
                babyRepository.saveBaby(partialBaby)
            } else {
                // Don't create baby for null picture - nothing to save
                Result.success(Unit)
            }
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