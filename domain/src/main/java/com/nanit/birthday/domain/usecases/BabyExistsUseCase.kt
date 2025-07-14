package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.repository.BabyRepository
import kotlinx.coroutines.flow.flow

/**
 * Use case for checking if baby profile exists.
 *
 * This is a simple read operation with no validation required.
 * Returns boolean indicating whether baby profile exists in the system.
 */
class BabyExistsUseCase(
    private val babyRepository: BabyRepository
) {
    operator fun invoke() = flow<Resource<Boolean>> {
        emit(Resource.Loading)
        emit(
            babyRepository
                .babyExists()
                .fold(
                    onSuccess = { exists ->
                        Resource.Success(exists)
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