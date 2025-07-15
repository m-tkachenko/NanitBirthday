package com.nanit.birthday.presentation.screens.birthday.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.presentation.R
import com.nanit.birthday.presentation.screens.birthday.toNumberResource
import com.nanit.birthday.presentation.theme.BirthdayDefaultTextColor

@Composable
fun AgeSection(
    birthdayData: BirthdayDisplayData,
    imageLoader: ImageLoader
) {
    Text(
        text = "TODAY ${birthdayData.babyName.uppercase()} IS",
        fontSize = 21.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
        color = BirthdayDefaultTextColor,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 25.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 72.dp)
    )

    Spacer(modifier = Modifier.height(13.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = R.drawable.left_swirls,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier.size(56.dp)
        )

        AsyncImage(
            model = birthdayData.ageNumber.toNumberResource(),
            contentDescription = null,
            modifier = Modifier
                .size(104.dp)
                .padding(horizontal = 16.dp)
        )

        AsyncImage(
            model = R.drawable.right_swirls,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier.size(56.dp)
        )
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = birthdayData.ageUnit.displayText,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
        color = BirthdayDefaultTextColor,
        modifier = Modifier.fillMaxWidth()
    )
}