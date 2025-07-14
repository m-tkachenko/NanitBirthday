package com.nanit.birthday.data.repository

import com.nanit.birthday.data.local.dao.BabyDao
import com.nanit.birthday.data.mapper.toDomain
import com.nanit.birthday.data.mapper.toEntity
import com.nanit.birthday.domain.exception.BabyException
import com.nanit.birthday.domain.exception.DataOperation
import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation for baby data operations.
 *
 * This repository:
 * - Does NOT perform validation (that's for use cases)
 * - Handles database errors gracefully
 * - Maps between data and domain layers
 * - Ensures operations run on IO dispatcher
 */
@Singleton
class BabyRepositoryImpl @Inject constructor(
    private val babyDao: BabyDao,
    private val ioDispatcher: CoroutineDispatcher
) : BabyRepository {

    override fun observeBaby(): Flow<Result<Baby?>> =
        babyDao.observeBaby(Baby.SINGLE_BABY_ID)
            .map { entity ->
                Result.success(entity?.toDomain())
            }
            .catch { throwable ->
                emit(
                    Result.failure(
                        exception = BabyException.DatabaseException(
                            throwable = throwable,
                            operation = DataOperation.RETRIEVE
                        )
                    )
                )
            }
            .flowOn(ioDispatcher)

    override suspend fun getBaby(): Result<Baby?> =
        withContext(ioDispatcher) {
            runCatching {
                babyDao.getBaby(Baby.SINGLE_BABY_ID)?.toDomain()
            }.mapError { error ->
                BabyException.DatabaseException(
                    throwable = error, operation = DataOperation.RETRIEVE
                )
            }
    }

    override suspend fun saveBaby(baby: Baby): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                babyDao.insertBaby(baby.toEntity())
            }.mapError { error ->
                BabyException.DatabaseException(
                    throwable = error,
                    operation = DataOperation.UPDATE
                )
            }
        }

    override suspend fun updateBabyName(updatedName: String): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                val rowsUpdated = babyDao.updateBabyPartial(
                    id = Baby.SINGLE_BABY_ID,
                    name = updatedName.trim()
                )

                if (rowsUpdated == 0) {
                    throw BabyException.NotFound
                }
            }.mapError { error ->
                when (error) {
                    is BabyException -> error
                    else ->
                        BabyException.DatabaseException(
                            throwable = error,
                            operation = DataOperation.UPDATE
                        )
                }
            }
        }

    override suspend fun updateBabyBirthday(updatedBirthday: LocalDate): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                val rowsUpdated = babyDao.updateBabyPartial(
                    id = Baby.SINGLE_BABY_ID,
                    birthday = updatedBirthday
                )

                if (rowsUpdated == 0) {
                    throw BabyException.NotFound
                }
            }.mapError { error ->
                when (error) {
                    is BabyException -> error
                    else ->
                        BabyException.DatabaseException(
                            throwable = error,
                            operation = DataOperation.UPDATE
                        )
                }
            }
        }

    override suspend fun updateBabyPicture(updatedPictureUri: String?): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                val rowsUpdated = babyDao.updateBabyPartial(
                    id = Baby.SINGLE_BABY_ID,
                    pictureUri = updatedPictureUri?.trim()
                )

                if (rowsUpdated == 0)
                    throw BabyException.NotFound
            }.mapError { error ->
                when (error) {
                    is BabyException -> error
                    else ->
                        BabyException.DatabaseException(
                            throwable = error,
                            operation = DataOperation.UPDATE
                        )
                }
            }
        }

    override suspend fun babyExists(): Result<Boolean> =
        withContext(ioDispatcher) {
            runCatching {
                babyDao.existsBaby(Baby.SINGLE_BABY_ID)
            }.mapError { error ->
                BabyException.DatabaseException(
                    throwable = error,
                    operation = DataOperation.RETRIEVE
                )
            }
        }

    override suspend fun deleteBaby(): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                val rowsDeleted = babyDao.deleteBaby(Baby.SINGLE_BABY_ID)

                if (rowsDeleted == 0)
                    throw BabyException.NotFound
            }.mapError { error ->
                when (error) {
                    is BabyException -> error
                    else ->
                        BabyException.DatabaseException(
                            throwable = error,
                            operation = DataOperation.DELETE
                        )
                }
            }
        }
}

/**
 * Extension function to map Result errors.
 */
private inline fun <T, E : Throwable> Result<T>.mapError(
    transform: (exception: Throwable) -> E
): Result<T> {
    return when {
        isSuccess -> this
        else -> Result.failure(transform(exceptionOrNull()!!))
    }
}