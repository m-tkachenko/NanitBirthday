package com.nanit.birthday.presentation.screens.birthday.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nanit.birthday.presentation.theme.BirthdayDarkBlue

/**
 * Centralized theme constants for Birthday screen.
 * This prevents magic numbers and makes the design system maintainable.
 */
object BirthdayConst {
    // Dimensions
    object Dimens {
        val screenPaddingTop = 20.dp
        val screenPaddingBottom = 15.dp

        // Age Section
        val ageTitleHorizontalPadding = 72.dp
        val ageNumberSize = 104.dp
        val ageDecorationSize = 56.dp
        val ageNumberPadding = 16.dp
        val spaceBetweenAgeTitle = 13.dp
        val spaceBetweenAgeNumber = 14.dp

        // Image Section
        val imageSectionHorizontalPadding = 50.dp
        val babyImageSize = 224.dp
        val babyImageBorderWidth = 6.dp
        val nanitLogoSize = 64.dp
        val spaceBetweenSections = 15.dp
    }

    // Text Styles
    object TextStyles {
        val ageTitle = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            lineHeight = 25.sp,
            color = BirthdayDarkBlue
        )

        val ageUnit = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            color = BirthdayDarkBlue
        )
    }
}