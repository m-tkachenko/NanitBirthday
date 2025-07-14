package com.nanit.birthday.domain.repository

import com.nanit.birthday.domain.model.Baby
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * Repository interface for baby data operations.
 *
 * This interface defines the contract for baby data access from the domain layer
 * perspective. It abstracts away all data source details.
 *
 * Key principles:
 * - Domain-focused operations (no database-specific details)
 * - Business-friendly method names and parameters
 * - Result types that make sense for business logic
 * - Clear documentation of expected behavior
 */
interface BabyRepository {
    /**
     * Observes the baby profile reactively.
     *
     * @return Flow emitting Baby updates or null if no baby exists
     */
    fun observeBaby(): Flow<Baby?>

    /**
     * Gets the current baby profile.
     *
     * @return Current Baby or null if no profile exists
     */
    suspend fun getBaby(): Baby?

    /**
     * Saves a complete baby profile.
     * Creates new profile or updates existing one.
     *
     * @param baby Complete baby data to save
     * @return Result indicating success or failure with error details
     */
    suspend fun saveBaby(baby: Baby): Result<Unit>

    /**
     * Updates baby's name only.
     *
     * @param updatedName New baby name
     * @return Result indicating success or failure
     */
    suspend fun updateBabyByName(updatedName: String): Result<Unit>

    /**
     * Updates baby's birthday only.
     *
     * @param birthday New birthday date
     * @return Result indicating success or failure
     */
    suspend fun updateBabyBirthday(updatedBirthday: LocalDate): Result<Unit>

    /**
     * Updates baby's picture only.
     *
     * @param pictureUri New picture URI or null to remove picture
     * @return Result indicating success or failure
     */
    suspend fun updateBabyPicture(updatedPictureUri: String?): Result<Unit>

    /**
     * Checks if baby profile exists.
     *
     * @return true if baby profile exists, false otherwise
     */
    suspend fun babyExists(): Boolean

    /**
     * Deletes the baby profile.
     *
     * @return Result indicating success or failure
     */
    suspend fun deleteBaby(): Result<Unit>
}