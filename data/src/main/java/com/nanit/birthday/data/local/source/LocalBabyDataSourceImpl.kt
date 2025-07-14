package com.nanit.birthday.data.local.source

import com.nanit.birthday.domain.source.LocalBabyDataSource
import com.nanit.birthday.data.local.dao.BabyDao
import com.nanit.birthday.data.mapper.toDomain
import com.nanit.birthday.data.mapper.toEntity
import com.nanit.birthday.domain.model.Baby
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Implementation of LocalBabyDataSource using Room database.
 *
 * This class bridges the gap between the repository (domain layer) and
 * the Room database (data layer), handling all data transformations
 * and database operations.
 *
 * Key responsibilities:
 * - Database operation execution
 * - Entity ↔ Domain model mapping
 * - Flow transformations
 */
class LocalBabyDataSourceImpl @Inject constructor(
    private val babyDao: BabyDao
): LocalBabyDataSource {
    override fun observeBaby(): Flow<Baby?> {
        return babyDao.observeBaby(Baby.SINGLE_BABY_ID)
            .map { entity -> entity.toDomain() as Baby? }
            .catch {
                emit(null)
            }
    }

    override suspend fun getBaby() =
        babyDao.getBaby(Baby.SINGLE_BABY_ID)?.toDomain()

    override suspend fun saveBaby(baby: Baby) {
        babyDao.insertBaby(baby.toEntity())
    }

    override suspend fun updateBabyPartial(
        name: String?,
        birthday: LocalDate?,
        pictureUri: String?
    ): Boolean {
        val rowsUpdated = babyDao.updateBabyPartial(
            id = Baby.SINGLE_BABY_ID,
            name = name,
            birthday = birthday,
            pictureUri = pictureUri
        )

        return rowsUpdated > 0
    }

    override suspend fun babyExists() =
        babyDao.existsBaby(Baby.SINGLE_BABY_ID)

    override suspend fun deleteBaby(): Boolean {
        val rowsDeleted = babyDao.deleteBaby(Baby.SINGLE_BABY_ID)
        return rowsDeleted > 0
    }

    override suspend fun clearAllData() {
        babyDao.deleteAllBabies()
    }
}