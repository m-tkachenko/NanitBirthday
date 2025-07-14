package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow

/**
 * Use case for updating baby's picture.
 *
 * Business rules enforced by BabyValidator:
 * - Picture URI must be valid format if provided
 * - Null or blank URI is valid (removes picture)
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

        // Step 2: Update baby picture in repository
        emit(
            babyRepository
                .updateBabyPicture(newPictureUri?.trim()?.takeIf { it.isNotBlank() })
                .fold(
                    onSuccess = {
                        Resource.Success(Unit)
                    },
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