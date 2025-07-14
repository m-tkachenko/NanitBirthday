package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate

/**
 * Use case for saving/creating a baby profile.
 *
 * Business rules:
 * - Name must not be blank and should be trimmed
 * - Name must be between 1-50 characters
 * - Birthday cannot be in the future
 * - Picture URI must be valid format if provided
 * - Returns Success when baby is saved successfully
 * - Returns Error with specific validation or system error messages
 */
class SaveBabyUseCase(
    private val babyRepository: BabyRepository,
    private val babyValidator: BabyValidator
) {
    operator fun invoke(
        name: String,
        birthday: LocalDate,
        pictureUri: String? = null
    ) = flow<Resource<Unit>> {
        emit(Resource.Loading)

        // Step 1: Validate input data
        val validationResult = babyValidator.validateBabyData(name, birthday, pictureUri)

        if (validationResult.isFailure) {
            emit(Resource.Error(validationResult.errorMessage!!))
            return@flow
        }

        // Step 2: Create baby with validated data
        val baby = Baby.create(
            name = name.trim(),
            birthday = birthday,
            pictureUri = pictureUri?.trim()?.takeIf { it.isNotBlank() }
        )

        // Step 3: Save to repository
        emit(
            babyRepository
                .saveBaby(baby)
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