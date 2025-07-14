package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching baby profile once.
 *
 * Business rules:
 * - Returns Success with Baby when profile exists
 * - Returns Success with null when no baby profile exists (user needs to create one)
 * - Returns Error with user-friendly message when operation fails
 */
class GetBabyUseCase(
    private val babyRepository: BabyRepository
) {
    operator fun invoke() = flow<Resource<Baby?>> {
        emit(Resource.Loading)
        emit(babyRepository.getBaby().fold(
            onSuccess = { baby ->
                Resource.Success(baby) // baby can be null if no profile exists
            },
            onFailure = { throwable ->
                Resource.Error(
                    message = throwable.toUserMessage()
                )
            }
        ))
    }
}