package com.nanit.birthday.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nanit.birthday.data.local.entity.BabyEntity
import kotlinx.datetime.LocalDate

@Dao
interface BabyDao {
    /**
     * Gets the baby profile.
     *
     * @return BabyEntity or null if no baby exists
     */
    @Query("SELECT * FROM baby WHERE id = :id LIMIT 1")
    suspend fun getBaby(id: Long = BabyEntity.SINGLE_BABY_ID): BabyEntity?

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
     */
    @Update
    suspend fun updateBaby(baby: BabyEntity)

    /**
     * Updates only the baby's name.
     *
     * @param name New name for the baby
     * @param id Baby ID (defaults to single baby ID)
     */
    @Query("""
        UPDATE baby
        SET name = :name
        WHERE id = :id
    """)
    suspend fun updateBabyName(
        name: String,
        id: Long = BabyEntity.SINGLE_BABY_ID
    )

    /**
     * Updates only the baby's picture URI.
     *
     * @param pictureUri New picture URI
     * @param id Baby ID (defaults to single baby ID)
     */
    @Query("""
        UPDATE baby
        SET picture_uri = :pictureUri
        WHERE id = :id
    """)
    suspend fun updateBabyPicture(
        pictureUri: String,
        id: Long = BabyEntity.SINGLE_BABY_ID
    )

    /**
     * Updates only the baby's birthday date.
     *
     * @param birthday New birthday date
     * @param id Baby ID (defaults to single baby ID)
     */
    @Query("""
        UPDATE baby
        SET birthday = :birthday
        WHERE id = :id
    """)
    suspend fun updateBabyBirthday(
        birthday: LocalDate,
        id: Long = BabyEntity.SINGLE_BABY_ID
    )

    /**
     * Checks if a baby profile exists.
     *
     * @return true if baby exists, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM baby WHERE id = :id)")
    suspend fun isBabyExists(id: Long = BabyEntity.SINGLE_BABY_ID): Boolean

    /**
     * Deletes the baby profile.
     *
     * @param id Baby ID to delete (defaults to single baby ID)
     */
    @Query("DELETE FROM baby WHERE id = :id")
    suspend fun deleteBaby(id: Long = BabyEntity.SINGLE_BABY_ID)

    /**
     * Clears all baby data.
     */
    @Query("DELETE FROM baby")
    suspend fun deleteAllBabies()
}