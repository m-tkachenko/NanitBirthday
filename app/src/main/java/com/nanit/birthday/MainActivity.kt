package com.nanit.birthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nanit.birthday.presentation.screens.details.DetailsScreen
import com.nanit.birthday.presentation.theme.NanitBirthdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NanitBirthdayTheme {
                DetailsScreen()
            }
        }
    }
}