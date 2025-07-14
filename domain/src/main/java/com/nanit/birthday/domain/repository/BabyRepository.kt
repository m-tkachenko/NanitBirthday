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
     * @return Flow emitting Result with Baby or null
     */
    fun observeBaby(): Flow<Result<Baby?>>

    /**
     * Gets the current baby profile.
     *
     * @return Result with Baby or null
     */
    suspend fun getBaby(): Result<Baby?>

    /**
     * Saves or updates baby profile.
     *
     * @param baby Baby to save
     * @return Result indicating success or failure
     */
    suspend fun saveBaby(baby: Baby): Result<Unit>

    /**
     * Updates baby's name.
     *
     * @param updatedName New name (will be trimmed)
     * @return Result indicating success or failure
     */
    suspend fun updateBabyName(updatedName: String): Result<Unit>

    /**
     * Updates baby's birthday.
     *
     * @param updatedBirthday New birthday
     * @return Result indicating success or failure
     */
    suspend fun updateBabyBirthday(updatedBirthday: LocalDate): Result<Unit>

    /**
     * Updates baby's picture.
     *
     * @param updatedPictureUri New picture URI or null to remove
     * @return Result indicating success or failure
     */
    suspend fun updateBabyPicture(updatedPictureUri: String?): Result<Unit>

    /**
     * Checks if baby profile exists.
     *
     * @return Result with boolean
     */
    suspend fun babyExists(): Result<Boolean>

    /**
     * Deletes the baby profile.
     *
     * @return Result indicating success or failure
     */
    suspend fun deleteBaby(): Result<Unit>
}