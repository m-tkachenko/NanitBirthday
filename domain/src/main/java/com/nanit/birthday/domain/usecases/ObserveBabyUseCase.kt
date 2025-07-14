package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.BabyException
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Use case for observing baby profile changes reactively.
 *
 * Business rules:
 * - Emits Loading state when starting observation
 * - Emits Success with Baby when profile exists
 * - Emits Success with null when no baby profile exists (user needs to create one)
 * - Emits Error with user-friendly message when operation fails
 */
class ObserveBabyUseCase(
    private val babyRepository: BabyRepository
) {
    operator fun invoke(): Flow<Resource<Baby?>> {
        return babyRepository.observeBaby()
            .map { result ->
                result.fold(
                    onSuccess = { baby ->
                        Resource.Success(
                            data = baby
                        )
                    },
                    onFailure = { throwable ->
                        Resource.Error(
                            message = throwable.toUserMessage()
                        )
                    }
                )
            }
            .onStart {
                emit(Resource.Loading)
            }
    }
}