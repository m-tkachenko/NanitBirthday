package com.nanit.birthday.core

/**
 * Resource wrapper for UI state management.
 */
sealed class Resource<out T> {

    /**
     * Loading state - operation is in progress
     */
    object Loading : Resource<Nothing>()

    /**
     * Success state - operation completed successfully
     * @param data The successful result data
     */
    data class Success<T>(val data: T) : Resource<T>()

    /**
     * Error state - operation failed
     * @param message User-friendly error message
     * @param throwable Optional throwable for debugging (not shown to user)
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : Resource<Nothing>()

    /**
     * Utility properties for easy state checking
     */
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    /**
     * Get data if in success state, null otherwise
     */
    val dataOrNull: T? get() = if (this is Success) data else null

    /**
     * Get error message if in error state, null otherwise
     */
    val errorMessage: String? get() = if (this is Error) message else null
}

/**
 * Extension functions for easier Resource handling
 */
inline fun <T> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

inline fun <T> Resource<T>.onError(action: (String) -> Unit): Resource<T> {
    if (this is Resource.Error) action(message)
    return this
}

inline fun <T> Resource<T>.onLoading(action: () -> Unit): Resource<T> {
    if (this is Resource.Loading) action()
    return this
}