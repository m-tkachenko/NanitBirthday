package com.nanit.birthday.presentation.screens.birthday.sections

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nanit.birthday.domain.model.AgeUnit
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.domain.model.BirthdayTheme
import com.nanit.birthday.presentation.R
import com.nanit.birthday.presentation.screens.birthday.constants.BirthdayConst
import com.nanit.birthday.presentation.screens.birthday.extensions.toNumberResource

@Composable
fun AgeSection(
    birthdayData: BirthdayDisplayData,
    modifier: Modifier = Modifier
) {
    val ageDescription = "${birthdayData.ageNumber} ${birthdayData.ageUnit.displayText}"

    Text(
        text = "TODAY ${birthdayData.babyName.uppercase()} IS",
        style = BirthdayConst.TextStyles.ageTitle,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BirthdayConst.Dimens.ageTitleHorizontalPadding)
            .semantics {
                contentDescription = "Today ${birthdayData.babyName} is $ageDescription"
            }
    )

    Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenAgeTitle))

    AgeNumberRow(
        ageNumber = birthdayData.ageNumber,
        ageDescription = ageDescription
    )

    Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenAgeNumber))

    Text(
        text = birthdayData.ageUnit.displayText,
        style = BirthdayConst.TextStyles.ageUnit,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun AgeNumberRow(
    ageNumber: Int,
    ageDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.left_swirls),
            contentDescription = null,
            modifier = Modifier.size(BirthdayConst.Dimens.ageDecorationSize)
        )

        Image(
            painter = painterResource(id = ageNumber.toNumberResource()),
            contentDescription = ageDescription,
            modifier = Modifier
                .size(BirthdayConst.Dimens.ageNumberSize)
                .padding(horizontal = BirthdayConst.Dimens.ageNumberPadding)
        )

        Image(
            painter = painterResource(id = R.drawable.right_swirls),
            contentDescription = null,
            modifier = Modifier.size(BirthdayConst.Dimens.ageDecorationSize)
        )
    }
}

// Preview functions
@Preview(showBackground = true)
@Composable
private fun AgeSectionPreview() {
    AgeSection(
        birthdayData = BirthdayDisplayData(
            babyName = "Sophie",
            ageNumber = 3,
            ageUnit = AgeUnit.MONTHS,
            pictureUri = null,
            theme = BirthdayTheme.GREEN
        )
    )
}

@Preview(showBackground = true, name = "Long Name")
@Composable
private fun AgeSectionPreviewLongName() {
    AgeSection(
        birthdayData = BirthdayDisplayData(
            babyName = "Christopher Alexander",
            ageNumber = 11,
            ageUnit = AgeUnit.MONTHS,
            pictureUri = null,
            theme = BirthdayTheme.BLUE
        )
    )
}