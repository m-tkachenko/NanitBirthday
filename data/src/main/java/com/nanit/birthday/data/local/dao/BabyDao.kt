package com.nanit.birthday.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nanit.birthday.data.local.entity.BabyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface BabyDao {
    /**
     * Observes the baby profile reactively.
     * Emits new values whenever the baby data changes.
     *
     * @param id Baby ID to observe
     * @return Flow of BabyEntity or null if no baby exists
     */
    @Query("SELECT * FROM baby WHERE id = :id LIMIT 1")
    fun observeBaby(id: Long): Flow<BabyEntity>

    /**
     * Gets the baby profile.
     *
     * @return BabyEntity or null if no baby exists
     */
    @Query("SELECT * FROM baby WHERE id = :id LIMIT 1")
    suspend fun getBaby(id: Long): BabyEntity?

    /**
     * Inserts a new baby profile.
     *
     * @param baby The baby entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaby(baby: BabyEntity)

    /**
     * Updates existing baby profile.
     *
     * @param baby The baby entity with updated data
     * @return Number of rows updated (should be 1 if successful, 0 if not found)
     */
    @Update
    suspend fun updateBaby(baby: BabyEntity): Int

    /**
     * Performs a partial update of baby fields.
     * Only non-null parameters will be updated.
     *
     * @param id Baby ID to update
     * @param name New name (null to keep existing)
     * @param birthday New birthday (null to keep existing)
     * @param pictureUri New picture URI (null to keep existing)
     * @return Number of rows updated
     */
    @Transaction
    suspend fun updateBabyPartial(
        id: Long,
        name: String? = null,
        birthday: LocalDate? = null,
        pictureUri: String? = null
    ): Int {
        val existingBaby = getBaby(id) ?: return 0

        val updatedBaby = existingBaby.copy(
            name = name ?: existingBaby.name,
            birthday = birthday ?: existingBaby.birthday,
            pictureUri = pictureUri ?: existingBaby.pictureUri
        )

        return updateBaby(updatedBaby)
    }

    /**
     * Checks if a baby profile exists.
     *
     * @param id Baby ID to check
     * @return true if baby exists, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM baby WHERE id = :id)")
    suspend fun existsBaby(id: Long): Boolean

    /**
     * Deletes the baby profile.
     *
     * @param id Baby ID to delete (defaults to single baby ID)
     * @return Number of rows deleted (1 if successful, 0 if not found)
     */
    @Query("DELETE FROM baby WHERE id = :id")
    suspend fun deleteBaby(id: Long): Int

    /**
     * Deletes a baby by entity.
     *
     * @param baby Baby entity to delete
     * @return Number of rows deleted
     */
    @Delete
    suspend fun deleteBaby(baby: BabyEntity): Int

    /**
     * Clears all baby data.
     */
    @Query("DELETE FROM baby")
    suspend fun deleteAllBabies()
}