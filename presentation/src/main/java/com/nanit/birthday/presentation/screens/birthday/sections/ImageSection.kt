package com.nanit.birthday.presentation.screens.birthday.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.nanit.birthday.domain.model.AgeUnit
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.domain.model.BirthdayTheme
import com.nanit.birthday.presentation.R
import com.nanit.birthday.presentation.screens.birthday.constants.BirthdayConst
import com.nanit.birthday.presentation.screens.birthday.extensions.toBabyCameraButtonResource
import com.nanit.birthday.presentation.screens.birthday.extensions.toBabyPlaceholderBorderColor
import com.nanit.birthday.presentation.screens.birthday.extensions.toBabyPlaceholderResource

@Composable
fun ImageSection(
    birthdayTheme: BirthdayTheme,
    babyPictureUri: String?,
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
    imageLoader: ImageLoader?,
    showCameraIcon: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = BirthdayConst.Dimens.imageSectionHorizontalPadding)
    ) {
        EditableBabyImage(
            pictureUri = babyPictureUri,
            onCameraClick = onCameraClick,
            theme = birthdayTheme,
            imageLoader = imageLoader,
            showCameraIcon = showCameraIcon
        )

        Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenSections))

        NanitLogo()
    }
}

@Composable
private fun EditableBabyImage(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader?,
    onCameraClick: () -> Unit,
    theme: BirthdayTheme,
    pictureUri: String?,
    showCameraIcon: Boolean = true
) {
    Box(
        modifier = modifier.size(BirthdayConst.Dimens.babyImageSize),
        contentAlignment = Alignment.Center
    ) {
        BabyImage(
            pictureUri = pictureUri,
            theme = theme,
            imageLoader = imageLoader,
            modifier = Modifier.matchParentSize()
        )

        if (showCameraIcon) {
            val imageRadius = BirthdayConst.Dimens.babyImageSize / 2
            val offSet = imageRadius.value * 0.707f // cos45 degrees

            CameraIconButton(
                theme = theme,
                onClick = onCameraClick,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = offSet.dp,
                        y = -offSet.dp
                    )
            )
        }
    }
}

@Composable
private fun CameraIconButton(
    theme: BirthdayTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(BirthdayConst.Dimens.cameraButtonSize),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        )
    ) {
        Icon(
            painter = painterResource(id = theme.toBabyCameraButtonResource()),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = Color.Unspecified
        )
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
            contentScale = ContentScale.Crop,
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
        birthdayTheme = BirthdayTheme.GREEN,
        babyPictureUri = null,
        imageLoader = null,
        onCameraClick = {}
    )
}