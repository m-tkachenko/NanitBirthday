package com.nanit.birthday.presentation.screens.birthday

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.presentation.screens.birthday.sections.AgeSection
import com.nanit.birthday.presentation.screens.birthday.sections.ImageSection

@Composable
fun BirthdayScreen(
    birthdayData: BirthdayDisplayData,
    onNavigateBack: () -> Unit
) {
    val imageLoader = rememberSvgImageLoader()

    BirthdayContentScreen(
        birthdayData = birthdayData,
        imageLoader = imageLoader,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun BirthdayContentScreen(
    birthdayData: BirthdayDisplayData,
    imageLoader: ImageLoader,
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
                .padding(top = 20.dp, bottom = 15.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AgeSection(
                birthdayData = birthdayData,
                imageLoader = imageLoader
            )

            Spacer(modifier = Modifier.height(15.dp))

            ImageSection(
                birthdayData = birthdayData,
                imageLoader = imageLoader
            )
        }

        AsyncImage(
            model = birthdayData.theme.toDecorationResource(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillHeight
        )
    }
}

@Composable
private fun rememberSvgImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
}