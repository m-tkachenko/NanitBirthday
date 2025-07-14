package com.nanit.birthday.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

/**
 * Room entity representing a baby's information in the local database.
 *
 * This entity stores the core baby data needed for the birthday app:
 * - Basic info (name, birthday)
 * - Picture reference (file URI)
 */
@Entity(tableName = "baby")
data class BabyEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = SINGLE_BABY_ID,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "birthday")
    val birthday: LocalDate? = null,

    @ColumnInfo(name = "picture_uri")
    val pictureUri: String? = null,
) {
    companion object {
        private const val SINGLE_BABY_ID = 1L
    }
}