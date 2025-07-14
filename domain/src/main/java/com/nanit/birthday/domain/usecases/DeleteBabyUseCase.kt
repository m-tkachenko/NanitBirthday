package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.repository.BabyRepository
import kotlinx.coroutines.flow.flow

/**
 * Use case for deleting baby profile.
 *
 * This is a destructive operation with no validation required.
 * Removes the baby profile completely from the system.
 */
class DeleteBabyUseCase(
    private val babyRepository: BabyRepository
) {
    operator fun invoke() = flow<Resource<Unit>> {
        emit(Resource.Loading)
        emit(
            babyRepository
                .deleteBaby()
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