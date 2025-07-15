package com.nanit.birthday.presentation.screens.birthday.extensions

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.nanit.birthday.domain.model.BirthdayTheme
import com.nanit.birthday.presentation.R
import com.nanit.birthday.presentation.theme.BirthdayBlue
import com.nanit.birthday.presentation.theme.BirthdayGreen
import com.nanit.birthday.presentation.theme.BirthdayPlaceholderBorderBlue
import com.nanit.birthday.presentation.theme.BirthdayPlaceholderBorderGreen
import com.nanit.birthday.presentation.theme.BirthdayPlaceholderBorderYellow
import com.nanit.birthday.presentation.theme.BirthdayYellow


/**
 * Extension functions for mapping domain models to presentation resources.
 * These functions ensure type-safe resource mapping without magic values.
 */

/**
 * Maps birthday theme to its corresponding background color.
 */
fun BirthdayTheme.toBackgroundColor(): Color = when (this) {
    BirthdayTheme.GREEN -> BirthdayGreen
    BirthdayTheme.YELLOW -> BirthdayYellow
    BirthdayTheme.BLUE -> BirthdayBlue
}

/**
 * Maps birthday theme to its bottom decoration drawable resource.
 */
@DrawableRes
fun BirthdayTheme.toDecorationResource(): Int = when (this) {
    BirthdayTheme.GREEN -> R.drawable.background_fox
    BirthdayTheme.YELLOW -> R.drawable.background_elephant
    BirthdayTheme.BLUE -> R.drawable.background_pelican
}

/**
 * Maps birthday theme to its baby placeholder drawable resource.
 */
@DrawableRes
fun BirthdayTheme.toBabyPlaceholderResource(): Int = when (this) {
    BirthdayTheme.GREEN -> R.drawable.baby_placeholder_green
    BirthdayTheme.YELLOW -> R.drawable.baby_placeholder_yellow
    BirthdayTheme.BLUE -> R.drawable.baby_placeholder_blue
}

/**
 * Maps birthday theme to the border color for baby image.
 */
fun BirthdayTheme.toBabyPlaceholderBorderColor(): Color = when (this) {
    BirthdayTheme.GREEN -> BirthdayPlaceholderBorderGreen
    BirthdayTheme.YELLOW -> BirthdayPlaceholderBorderYellow
    BirthdayTheme.BLUE -> BirthdayPlaceholderBorderBlue
}

fun BirthdayTheme.toBabyCameraButtonResource(): Int = when(this) {
    BirthdayTheme.GREEN -> R.drawable.add_picture_green
    BirthdayTheme.YELLOW -> R.drawable.add_picture_yellow
    BirthdayTheme.BLUE -> R.drawable.add_picture_blue
}

/**
 * Maps age number to its corresponding drawable resource.
 * For ages > 12, returns a zero drawable
 */
@DrawableRes
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