package com.nanit.birthday.data.local.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
/**
 * Type converters for Room database to handle custom data types.
 *
 * These converters allow Room to store and retrieve [LocalDate]
 * objects by converting them to/from string format (YYYY-MM-DD).
 *
 * Example: 2024-12-25 ↔ LocalDate(2024, 12, 25)
 */
class DateConverters {

    /**
     * Converts LocalDate to String for database storage.
     *
     * @param date The LocalDate to convert, nullable
     * @return date string (YYYY-MM-DD) or null
     */
    @TypeConverter
    fun fromLocalDate(date: LocalDate?) = date?.toString()

    /**
     * Converts String from database back to [LocalDate].
     *
     * @param dateString date string (YYYY-MM-DD), nullable
     * @return [LocalDate] object or null
     * @throws IllegalArgumentException if the string format is invalid
     */
    @TypeConverter
    fun toLocalDate(dateString: String?) =
        dateString?.let {
            try {
                LocalDate.parse(it)
            } catch (_: Exception) {
                null
            }
        }
}