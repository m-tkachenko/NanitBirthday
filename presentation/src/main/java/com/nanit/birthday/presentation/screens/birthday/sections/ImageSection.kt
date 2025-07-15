package com.nanit.birthday.presentation.screens.birthday.sections

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import com.nanit.birthday.domain.model.AgeUnit
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.domain.model.BirthdayTheme
import com.nanit.birthday.presentation.R
import com.nanit.birthday.presentation.screens.birthday.theme.BirthdayConst
import com.nanit.birthday.presentation.screens.birthday.toBabyPlaceholderBorderColor
import com.nanit.birthday.presentation.screens.birthday.toBabyPlaceholderResource

@Composable
fun ImageSection(
    birthdayData: BirthdayDisplayData,
    imageLoader: ImageLoader?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = BirthdayConst.Dimens.imageSectionHorizontalPadding)
    ) {
        BabyImage(
            pictureUri = birthdayData.pictureUri,
            theme = birthdayData.theme,
            imageLoader = imageLoader
        )

        Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenSections))

        NanitLogo()
    }
}

@Composable
private fun BabyImage(
    pictureUri: String?,
    theme: BirthdayTheme,
    imageLoader: ImageLoader?,
    modifier: Modifier = Modifier
) {
    val borderColor = theme.toBabyPlaceholderBorderColor()

    val imageModifier = modifier
        .size(BirthdayConst.Dimens.babyImageSize)
        .border(
            width = BirthdayConst.Dimens.babyImageBorderWidth,
            color = borderColor,
            shape = CircleShape
        )
        .clip(CircleShape)

    if (pictureUri != null && imageLoader != null) {
        AsyncImage(
            model = pictureUri,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = theme.toBabyPlaceholderResource()),
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun NanitLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.nanit_logo),
        contentDescription = "Nanit",
        modifier = modifier.size(BirthdayConst.Dimens.nanitLogoSize)
    )
}

@Preview(showBackground = true)
@Composable
private fun ImageSectionPreviewGreen() {
    ImageSection(
        birthdayData = BirthdayDisplayData(
            babyName = "Emma",
            ageNumber = 2,
            ageUnit = AgeUnit.YEARS,
            pictureUri = null,
            theme = BirthdayTheme.GREEN
        ),
        imageLoader = null
    )
}

@Preview(showBackground = true)
@Composable
private fun ImageSectionPreviewYellow() {
    ImageSection(
        birthdayData = BirthdayDisplayData(
            babyName = "Oliver",
            ageNumber = 6,
            ageUnit = AgeUnit.MONTHS,
            pictureUri = null,
            theme = BirthdayTheme.YELLOW
        ),
        imageLoader = null
    )
}
