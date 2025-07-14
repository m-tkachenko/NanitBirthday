package com.nanit.birthday.domain.validator

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Error
    val errorMessage: String? get() = if (this is Error) message else null
}