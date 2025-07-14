package com.nanit.birthday.domain.model

import kotlinx.datetime.LocalDate

/**
 * Domain model representing a baby in the business layer.
 *
 * This is the pure business representation of a baby, independent of any
 * data storage or UI concerns. Used throughout the domain and presentation layers.
 */
data class Baby(
    val id: Long,
    val name: String? = null,
    val birthday: LocalDate? = null,
    val pictureUri: String? = null
) {
    companion object {
        const val SINGLE_BABY_ID = 1L

        /**
         * Creates a new baby with only the specified fields.
         * Other fields remain null until explicitly set.
         */
        fun create(
            name: String? = null,
            birthday: LocalDate? = null,
            pictureUri: String? = null
        ) = Baby(
            id = SINGLE_BABY_ID,
            name = name,
            birthday = birthday,
            pictureUri = pictureUri
        )

        /**
         * Creates a baby with only name set.
         */
        fun withName(name: String) = create(name = name)

        /**
         * Creates a baby with only birthday set.
         */
        fun withBirthday(birthday: LocalDate) = create(birthday = birthday)

        /**
         * Creates a baby with only picture set.
         */
        fun withPicture(pictureUri: String) = create(pictureUri = pictureUri)
    }

    /**
     * Returns true if this baby has a complete profile (all required fields set).
     * For this app, we consider name and birthday as required.
     */
    fun isComplete(): Boolean = name != null && birthday != null

    /**
     * Returns true if this baby profile is empty (no fields set).
     */
    fun isEmpty(): Boolean = name == null && birthday == null && pictureUri == null
}