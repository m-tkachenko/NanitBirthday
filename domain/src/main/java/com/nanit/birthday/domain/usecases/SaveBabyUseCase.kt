package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate

/**
 * Use case for saving/creating a baby profile (partial or complete).
 *
 * Business rules:
 * - Name must be valid if provided (not blank, 1-50 characters)
 * - Birthday cannot be in the future if provided
 * - Picture URI must be valid format if provided
 * - At least one field must be provided (not all null)
 */
class SaveBabyUseCase(
    private val babyRepository: BabyRepository,
    private val babyValidator: BabyValidator
) {
    operator fun invoke(
        name: String? = null,
        birthday: LocalDate? = null,
        pictureUri: String? = null
    ) = flow<Resource<Unit>> {
        emit(Resource.Loading)

        // Step 1: Clean and prepare input data
        val cleanName = name?.trim()?.takeIf { it.isNotBlank() }
        val cleanPictureUri = pictureUri?.trim()?.takeIf { it.isNotBlank() }

        // Step 2: Validate that at least one field is provided
        if (cleanName == null && birthday == null && cleanPictureUri == null) {
            emit(Resource.Error("At least one field must be provided"))
            return@flow
        }

        // Step 3: Validate provided fields
        val validationResult = babyValidator.validateBabyData(
            name = cleanName,
            birthday = birthday,
            pictureUri = cleanPictureUri
        )

        if (validationResult.isFailure) {
            emit(Resource.Error(validationResult.errorMessage!!))
            return@flow
        }

        // Step 4: Create baby with provided data (other fields remain null)
        val baby = Baby.create(
            name = cleanName,
            birthday = birthday,
            pictureUri = cleanPictureUri
        )

        // Step 5: Save to repository
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
                )
        )
    }
}