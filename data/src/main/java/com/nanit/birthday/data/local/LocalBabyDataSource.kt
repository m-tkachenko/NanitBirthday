package com.nanit.birthday.data.local

import com.nanit.birthday.domain.model.Baby
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * Interface for local baby data operations.
 */
interface LocalBabyDataSource {
    /**
     * Observes the baby profile reactively.
     * Emits updates whenever baby data changes.
     *
     * @return Flow of Baby or null if no baby exists
     */
    fun observeBaby(): Flow<Baby?>

    /**
     * Gets the current baby profile.
     *
     * @return Baby or null if no baby profile exists
     */
    suspend fun getBaby(): Baby?

    /**
     * Saves a baby profile (insert or update).
     * Uses upsert strategy - creates if doesn't exist, updates if exists.
     *
     * @param baby Baby to save
     */
    suspend fun saveBaby(baby: Baby)

    /**
     * Updates baby profile with partial data.
     * Only provided fields will be updated, others remain unchanged.
     *
     * @param name New name (null to keep existing)
     * @param birthday New birthday (null to keep existing)
     * @param pictureUri New picture URI (null to keep existing)
     * @return true if update was successful, false if baby doesn't exist
     */
    suspend fun updateBabyPartial(
        name: String? = null,
        birthday: LocalDate? = null,
        pictureUri: String? = null
    ): Boolean

    /**
     * Checks if a baby exists.
     *
     * @return true if baby profile exists, false otherwise
     */
    suspend fun babyExists(): Boolean

    /**
     * Deletes the baby profile.
     *
     * @return true if deletion was successful, false if baby didn't exist
     */
    suspend fun deleteBaby(): Boolean

    /**
     * Clears all baby data.
     */
    suspend fun clearAllData()
}