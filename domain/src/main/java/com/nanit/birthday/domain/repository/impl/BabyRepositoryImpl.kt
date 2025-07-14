package com.nanit.birthday.domain.repository.impl

import com.nanit.birthday.domain.model.Baby
import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.repository.exception.BabyDataException
import com.nanit.birthday.domain.repository.exception.BabyNotFoundException
import com.nanit.birthday.domain.source.LocalBabyDataSource
import kotlinx.datetime.LocalDate

class BabyRepositoryImpl(
    private val localDataSource: LocalBabyDataSource
): BabyRepository {
    override fun observeBaby() =
        localDataSource.observeBaby()

    override suspend fun getBaby() =
        localDataSource.getBaby()

    override suspend fun saveBaby(baby: Baby): Result<Unit> {
        return try {
            localDataSource.saveBaby(baby)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                exception = BabyDataException("Failed to save baby data", e)
            )
        }
    }

    override suspend fun updateBabyByName(updatedName: String): Result<Unit> {
        return try {
            val success = localDataSource.updateBabyPartial(name = updatedName)

            if (success)
                Result.success(Unit)
            else
                Result.failure(
                    exception = BabyNotFoundException("No baby profile exists to update")
                )
        } catch (e: Exception) {
            Result.failure(
                exception = BabyDataException("Failed to update baby name", e)
            )
        }
    }

    override suspend fun updateBabyBirthday(updatedBirthday: LocalDate): Result<Unit> {
        return try {
            val success = localDataSource.updateBabyPartial(birthday = updatedBirthday)

            if (success)
                Result.success(Unit)
            else
                Result.failure(
                    exception = BabyNotFoundException("No baby profile exists to update")
                )
        } catch (e: Exception) {
            Result.failure(
                exception = BabyDataException("Failed to update baby birthday", e)
            )
        }
    }

    override suspend fun updateBabyPicture(updatedPictureUri: String?): Result<Unit> {
        return try {
            val success = localDataSource.updateBabyPartial(pictureUri = updatedPictureUri)

            if (success)
                Result.success(Unit)
            else
                Result.failure(
                    exception = BabyNotFoundException("No baby profile exists to update")
                )
        } catch (e: Exception) {
            Result.failure(
                exception = BabyDataException("Failed to update baby picture", e)
            )
        }
    }

    override suspend fun babyExists() =
        localDataSource.babyExists()

    override suspend fun deleteBaby(): Result<Unit> {
        return try {
            val success = localDataSource.deleteBaby()

            if (success)
                Result.success(Unit)
            else
                Result.failure(
                    exception = BabyNotFoundException("No baby profile exists to delete")
                )
        } catch (e: Exception) {
            Result.failure(
                exception = BabyDataException("Failed to delete baby data", e)
            )
        }
    }
}