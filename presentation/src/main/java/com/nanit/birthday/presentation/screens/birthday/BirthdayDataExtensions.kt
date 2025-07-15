package com.nanit.birthday.presentation.screens.birthday

import androidx.compose.ui.graphics.Color
import com.nanit.birthday.domain.model.BirthdayTheme
import com.nanit.birthday.presentation.R
import com.nanit.birthday.presentation.theme.BirthdayBlue
import com.nanit.birthday.presentation.theme.BirthdayGreen
import com.nanit.birthday.presentation.theme.BirthdayPlaceholderBorderBlue
import com.nanit.birthday.presentation.theme.BirthdayPlaceholderBorderGreen
import com.nanit.birthday.presentation.theme.BirthdayPlaceholderBorderYellow
import com.nanit.birthday.presentation.theme.BirthdayYellow

fun BirthdayTheme.toBackgroundColor(): Color = when (this) {
    BirthdayTheme.GREEN -> BirthdayGreen
    BirthdayTheme.YELLOW -> BirthdayYellow
    BirthdayTheme.BLUE -> BirthdayBlue
}

fun BirthdayTheme.toDecorationResource(): Int = when (this) {
    BirthdayTheme.GREEN -> R.drawable.background_fox
    BirthdayTheme.YELLOW -> R.drawable.background_elephant
    BirthdayTheme.BLUE -> R.drawable.background_pelican
}

fun BirthdayTheme.toBabyPlaceholderResource(): Int = when (this) {
    BirthdayTheme.GREEN -> R.drawable.baby_placeholder_green
    BirthdayTheme.YELLOW -> R.drawable.baby_placeholder_yellow
    BirthdayTheme.BLUE -> R.drawable.baby_placeholder_blue
}

fun BirthdayTheme.toBabyPlaceholderBorderColor(): Color = when (this) {
    BirthdayTheme.GREEN -> BirthdayPlaceholderBorderGreen
    BirthdayTheme.YELLOW -> BirthdayPlaceholderBorderYellow
    BirthdayTheme.BLUE -> BirthdayPlaceholderBorderBlue
}

fun Int.toNumberResource(): Int = when (this) {
    0 -> R.drawable.number_zero
    1 -> R.drawable.number_one
    2 -> R.drawable.number_two
    3 -> R.drawable.number_three
    4 -> R.drawable.number_four
    5 -> R.drawable.number_five
    6 -> R.drawable.number_six
    7 -> R.drawable.number_seven
    8 -> R.drawable.number_eight
    9 -> R.drawable.number_nine
    10 -> R.drawable.number_ten
    11 -> R.drawable.number_eleven
    12 -> R.drawable.number_twelve
    else -> R.drawable.number_zero // Fallback
}