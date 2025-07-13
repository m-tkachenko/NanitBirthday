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
    val name: String,
    val birthday: LocalDate,
    val pictureUri: String? = null
) {
    companion object {
        const val SINGLE_BABY_ID = 1L

        fun create(
            name: String,
            birthday: LocalDate,
            pictureUri: String? = null
        ) = Baby(
            id = SINGLE_BABY_ID,
            name = name,
            birthday = birthday,
            pictureUri = pictureUri
        )
    }
}