package com.nanit.birthday.data.local.converter

import androidx.room.TypeConverter
import com.nanit.birthday.data.local.converter.exception.DateConversionException
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeParseException

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
    fun toLocalDate(dateString: String?): LocalDate? {
        if (dateString.isNullOrBlank()) return null

        return try {
            LocalDate.parse(dateString.trim())
        } catch (exception: DateTimeParseException) {
            throw DateTimeParseException(
                "Invalid date format: '$dateString'. Expected format: $DATE_FORMAT",
                exception.toString(),
                exception.errorIndex
            )
        } catch (exception: IllegalArgumentException) {
            throw DateConversionException(
                "Invalid date format: '$dateString'. Expected format: $DATE_FORMAT",
                exception
            )
        }
    }

    companion object {
        // ISO-8601 format for consistent date storage
        private const val DATE_FORMAT = "yyyy-MM-dd"

    }
}