package com.nanit.birthday.presentation.navigation

import com.nanit.birthday.presentation.screens.birthday.BirthdayScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.presentation.screens.details.DetailsScreen

/**
 * Main navigation setup for the birthday app.
 * Handles navigation between Details screen and Birthday screen.
 */
@Composable
fun BirthdayNavigation(
    navController: NavHostController = rememberNavController()
) {
    var currentBirthdayData by remember { mutableStateOf<BirthdayDisplayData?>(null) }

    NavHost(
        navController = navController,
        startDestination = BirthdayDestinations.DETAILS_SCREEN
    ) {
        composable(route = BirthdayDestinations.DETAILS_SCREEN) {
            DetailsScreen(
                onShowBirthdayScreen = { birthdayData ->
                    // Store the data and navigate
                    currentBirthdayData = birthdayData
                    navController.navigate(BirthdayDestinations.BIRTHDAY_SCREEN)
                }
            )
        }

        composable(route = BirthdayDestinations.BIRTHDAY_SCREEN) {
            currentBirthdayData?.let { data ->
                BirthdayScreen(
                    birthdayData = data,
                    onNavigateBack = {
                        // Clear data and go back
                        currentBirthdayData = null
                        navController.popBackStack()
                    }
                )
            } ?: run {
                navController.popBackStack()
            }
        }
    }
}