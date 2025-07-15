package com.nanit.birthday.presentation.screens.birthday.sections

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.presentation.R
import com.nanit.birthday.presentation.screens.birthday.toBabyPlaceholderBorderColor
import com.nanit.birthday.presentation.screens.birthday.toBabyPlaceholderResource

@Composable
fun ImageSection(
    birthdayData: BirthdayDisplayData,
    imageLoader: ImageLoader
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 50.dp)
    ) {
        AsyncImage(
            model = birthdayData.pictureUri ?: birthdayData.theme.toBabyPlaceholderResource(),
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier
                .size(224.dp)
                .border(
                    width = 6.dp,
                    color = birthdayData.theme.toBabyPlaceholderBorderColor(),
                    shape = CircleShape
                )
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(15.dp))

        AsyncImage(
            model = R.drawable.nanit_logo,
            imageLoader = imageLoader,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
    }
}
