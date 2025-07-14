package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.validator.BabyValidator
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate

/**
 * Use case for updating baby's birthday or creating new incomplete baby.
 *
 * Business rules enforced by BabyValidator:
 * - Birthday cannot be in the future
 *
 * Behavior:
 * - If baby exists: updates only the birthday field
 * - If baby doesn't exist: creates new baby with only birthday field set
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
            // Update existing baby's birthday only
            babyRepository.updateBabyBirthday(newBirthday)
        } else {
            // Create new baby with only birthday field set
            val partialBaby = Baby.withBirthday(newBirthday)
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