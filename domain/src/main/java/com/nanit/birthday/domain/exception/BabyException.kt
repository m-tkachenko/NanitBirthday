package com.nanit.birthday.domain.exception

sealed class BabyException: Exception() {
    object NotFound: BabyException() {
        override val message: String?
            get() = "Baby profile not found"
    }

    data class DatabaseException(
        val throwable: Throwable,
        val operation: DataOperation
    ): BabyException() {
        override val message = "Database ${operation.name} failed. With error: ${throwable.message}"
        override val cause = throwable
    }
}

fun Throwable.toUserMessage(): String =
    when (this) {
        is BabyException.DatabaseException -> {
            "Unable to change baby profile. Please try again."
        }
        is BabyException.NotFound -> {
            "Baby profile not found. Please try creating a new profile."
        }
        else -> {
            "Something went wrong. Please try again later."
        }
    }