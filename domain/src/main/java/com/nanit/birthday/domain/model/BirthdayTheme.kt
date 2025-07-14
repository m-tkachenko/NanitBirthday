package com.nanit.birthday.domain.model

enum class BirthdayTheme {
    GREEN,
    YELLOW,
    BLUE;

    companion object {
        fun random(): BirthdayTheme {
            return BirthdayTheme.entries.random()
        }
    }
}