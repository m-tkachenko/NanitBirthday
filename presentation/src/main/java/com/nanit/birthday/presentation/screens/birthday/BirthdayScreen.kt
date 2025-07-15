package com.nanit.birthday.presentation.screens.birthday

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.nanit.birthday.domain.model.AgeUnit
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.domain.model.BirthdayTheme
import com.nanit.birthday.presentation.screens.birthday.constants.BirthdayConst
import com.nanit.birthday.presentation.screens.birthday.extensions.toBackgroundColor
import com.nanit.birthday.presentation.screens.birthday.extensions.toDecorationResource
import com.nanit.birthday.presentation.screens.birthday.sections.AgeSection
import com.nanit.birthday.presentation.screens.birthday.sections.ImageSection
import com.nanit.birthday.presentation.theme.BirthdayDarkBlue

/**
 * Main Birthday celebration screen displaying baby's age and photo.
 * Supports 3 different themes that are randomly selected.
 *
 * @param birthdayData Display data containing baby info and theme
 * @param onNavigateBack Callback when user wants to go back
 */
@Composable
fun BirthdayScreen(
    birthdayData: BirthdayDisplayData,
    onNavigateBack: () -> Unit
) {
    val imageLoader =
        if (birthdayData.pictureUri != null) {
            rememberImageLoader()
        } else
            null

    BirthdayContent(
        onNavigateBack = onNavigateBack,
        birthdayData = birthdayData,
        imageLoader = imageLoader
    )
}

@Composable
private fun BirthdayContent(
    birthdayData: BirthdayDisplayData,
    imageLoader: ImageLoader?,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(birthdayData.theme.toBackgroundColor())
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = BirthdayConst.Dimens.screenPaddingTop,
                    bottom = BirthdayConst.Dimens.screenPaddingBottom
                )
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AgeSection(birthdayData = birthdayData)

            Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenSections))

            ImageSection(
                onCameraClick = { },
                birthdayData = birthdayData,
                imageLoader = imageLoader
            )
        }

        Image(
            painter = painterResource(id = birthdayData.theme.toDecorationResource()),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillHeight
        )

        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 10.dp,
                    start = 12.dp
                )
                .statusBarsPadding()
                .size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = BirthdayDarkBlue
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close birthday screen",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }
}

@Preview(name = "Green Theme")
@Composable
private fun BirthdayScreenPreviewGreen() {
    BirthdayScreen(
        birthdayData = BirthdayDisplayData(
            babyName = "Emma",
            ageNumber = 2,
            ageUnit = AgeUnit.YEARS,
            pictureUri = null,
            theme = BirthdayTheme.GREEN
        ),
        onNavigateBack = {}
    )
}

@Preview(name = "Long Name")
@Composable
private fun BirthdayScreenPreviewLongName() {
    BirthdayScreen(
        birthdayData = BirthdayDisplayData(
            babyName = "Alexander Benjamin",
            ageNumber = 6,
            ageUnit = AgeUnit.MONTHS,
            pictureUri = null,
            theme = BirthdayTheme.YELLOW
        ),
        onNavigateBack = {}
    )
}