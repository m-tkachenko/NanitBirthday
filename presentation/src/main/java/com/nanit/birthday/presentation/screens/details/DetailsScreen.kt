package com.nanit.birthday.presentation.screens.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    onShowBirthdayScreen: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var birthday by rememberSaveable { mutableStateOf("") }
    var hasPicture by rememberSaveable { mutableStateOf(false) }

    val isButtonEnabled by remember {
        derivedStateOf {
            name.trim().isNotEmpty() && birthday.trim().isNotEmpty()
        }
    }

    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "Baby details form"
            },
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AppTitleSection()
        }

        item {
            NameInputSection(
                name = name,
                onNameChange = { name = it },
                focusRequester = nameFocusRequester
            )
        }

        item {
            BirthdayInputSection(
                birthday = birthday,
                onBirthdayClick = {
                    // TODO: Open date picker
                    birthday = "01/01/2024" // Simulate for now
                }
            )
        }

        item {
            PictureSection(
                hasPicture = hasPicture,
                onPictureChange = { hasPicture = it }
            )
        }

        item {
            ShowBirthdayButton(
                enabled = isButtonEnabled,
                onClick = {
                    focusManager.clearFocus()
                    onShowBirthdayScreen()
                }
            )
        }

        item {
            FormValidationHint(visible = !isButtonEnabled)
        }
    }

    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
    }
}

@Composable
private fun AppTitleSection() {
    Text(
        text = "Happy Birthday!",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    )
}

@Composable
private fun NameInputSection(
    name: String,
    onNameChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Baby's Name") },
        placeholder = { Text("Enter baby's name") },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun BirthdayInputSection(
    birthday: String,
    onBirthdayClick: () -> Unit
) {
    OutlinedTextField(
        value = birthday,
        onValueChange = { },
        label = { Text("Birthday") },
        placeholder = { Text("Select birthday date") },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            IconButton(
                onClick = onBirthdayClick
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun PictureSection(
    hasPicture: Boolean,
    onPictureChange: (Boolean) -> Unit
) {
    val cardShape = RoundedCornerShape(12.dp)
    val backgroundColor = if (hasPicture)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceContainerHigh

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp)
            .clip(cardShape)
            .background(backgroundColor)
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!hasPicture) {
                NoPictureContent(onSelectPicture = { onPictureChange(true) })
            } else {
                PictureSelectedContent(onChangePicture = { onPictureChange(false) })
            }
        }
    }
}

@Composable
private fun NoPictureContent(onSelectPicture: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PhotoCamera,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "No picture selected",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        FilledTonalButton(
            onClick = onSelectPicture
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Select Picture")
        }
    }
}

@Composable
private fun PictureSelectedContent(onChangePicture: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PhotoCamera,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = "Picture Selected ✓",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        TextButton(
            onClick = onChangePicture
        ) {
            Text("Change Picture")
        }
    }
}

@Composable
private fun ShowBirthdayButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Show Birthday Screen",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FormValidationHint(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Text(
            text = "Please enter baby's name and birthday to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
private fun DetailsScreenLightPreview() {
    MaterialTheme {
        DetailsScreen()
    }
}