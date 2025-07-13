package com.nanit.birthday.data.local.converter.exception

/**
 * Custom exception for date conversion errors.
 */
class DateConversionException(
    message: String,
    cause: Throwable
): RuntimeException(message, cause)