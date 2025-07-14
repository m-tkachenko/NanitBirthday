package com.nanit.birthday.presentation.screens.birthday

import androidx.compose.runtime.Composable
import com.nanit.birthday.domain.model.BirthdayDisplayData

@Composable
fun BirthdayScreen(
    birthdayData: BirthdayDisplayData,
    onNavigateBack: () -> Unit
) {
    BirthdayContentScreen(
        birthdayData = birthdayData,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun BirthdayContentScreen(
    birthdayData: BirthdayDisplayData,
    onNavigateBack: () -> Unit
) {

}