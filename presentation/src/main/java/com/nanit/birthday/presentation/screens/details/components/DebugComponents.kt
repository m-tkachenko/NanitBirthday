package com.nanit.birthday.presentation.screens.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nanit.birthday.domain.model.Baby

/**
 * Debug information component for development purposes.
 *
 * ⚠️ IMPORTANT: Remove this component in production builds!
 *
 * Displays current baby state for debugging during development.
 * Useful for verifying data persistence and state updates.
 */
@Composable
fun DebugBabyInfo(
    baby: Baby,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = "Debug Info:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        DebugInfoRow(
            label = "ID",
            value = baby.id.toString()
        )

        DebugInfoRow(
            label = "Name",
            value = baby.name ?: "Not set"
        )

        DebugInfoRow(
            label = "Birthday",
            value = baby.birthday?.toString() ?: "Not set"
        )

        DebugInfoRow(
            label = "Picture",
            value = baby.pictureUri ?: "None"
        )

        DebugInfoRow(
            label = "Complete",
            value = if (baby.isComplete()) "✅ Yes" else "❌ No"
        )

        DebugInfoRow(
            label = "Empty",
            value = if (baby.isEmpty()) "❌ Yes" else "✅ No"
        )
    }
}

/**
 * Helper component for displaying debug key-value pairs.
 */
@Composable
private fun DebugInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}