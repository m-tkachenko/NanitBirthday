package com.nanit.birthday.domain.model

data class BirthdayDisplayData(
    val babyName: String,
    val ageNumber: Int,
    val ageUnit: AgeUnit,
    val pictureUri: String?,
    val theme: BirthdayTheme
)
