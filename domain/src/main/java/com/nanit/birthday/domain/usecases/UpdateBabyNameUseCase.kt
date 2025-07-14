package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow

/**
 * Use case for updating baby's name.
 *
 * Business rules enforced by BabyValidator:
 * - Name must not be blank after trimming
 * - Name must be between 1-50 characters
 */
class UpdateBabyNameUseCase (
    private val babyRepository: BabyRepository,
    private val babyValidator: BabyValidator
) {
    operator fun invoke(newName: String) = flow<Resource<Unit>> {
        // Step 1: Validate new name using validator
        val validationResult = babyValidator.validateName(newName)
        if (validationResult.isFailure) {
            emit(Resource.Error(validationResult.errorMessage!!))
            return@flow
        }

        // Step 2: Update baby name in repository
        emit(
            babyRepository
            .updateBabyName(newName.trim())
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
            ))
    }
}