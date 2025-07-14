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