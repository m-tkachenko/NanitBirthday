package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate

/**
 * Use case for updating baby's birthday.
 *
 * Business rules enforced by BabyValidator:
 * - Birthday cannot be in the future
 */
class UpdateBabyBirthdayUseCase(
    private val babyRepository: BabyRepository,
    private val babyValidator: BabyValidator
) {
    operator fun invoke(newBirthday: LocalDate) = flow<Resource<Unit>> {
        emit(Resource.Loading)

        // Step 1: Validate new birthday using validator
        val validationResult = babyValidator.validateBirthday(newBirthday)
        if (validationResult.isFailure) {
            emit(Resource.Error(validationResult.errorMessage!!))
            return@flow
        }

        // Step 2: Update baby birthday in repository
        emit(
            babyRepository
                .updateBabyBirthday(newBirthday)
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