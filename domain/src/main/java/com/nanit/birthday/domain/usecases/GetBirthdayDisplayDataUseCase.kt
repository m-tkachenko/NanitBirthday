package com.nanit.birthday.domain.usecases

import com.nanit.birthday.core.Resource
import com.nanit.birthday.domain.exception.toUserMessage
import com.nanit.birthday.domain.model.AgeUnit
import com.nanit.birthday.domain.model.BirthdayDisplayData
import com.nanit.birthday.domain.model.BirthdayTheme
import com.nanit.birthday.domain.repository.BabyRepository
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Use case for generating birthday display data with random theme selection.
 *
 * Business rules:
 * - Age displayed in months until 12 months, then in years
 * - Random theme selection each time
 * - Requires complete baby profile (name + birthday)
 */
@OptIn(ExperimentalTime::class)
class GetBirthdayDisplayDataUseCase(
    private val babyRepository: BabyRepository
) {
    operator fun invoke() = flow<Resource<BirthdayDisplayData>> {
        emit(Resource.Loading)

        // Get baby data
        val babyResult = babyRepository.getBaby()
        if (babyResult.isFailure) {
            emit(Resource.Error(
                message = babyResult.exceptionOrNull()?.toUserMessage()
                    ?: "Failed to load baby data"
            ))
            return@flow
        }

        val baby = babyResult.getOrNull()
        if (baby == null) {
            emit(Resource.Error("No baby profile found"))
            return@flow
        }

        // Validate required data
        if (baby.name.isNullOrBlank() || baby.birthday == null) {
            emit(Resource.Error("Baby profile is incomplete. Name and birthday are required."))
            return@flow
        }

        // Calculate age
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val age = calculateAge(baby.birthday, today)

        // Create display data with random theme
        val displayData = BirthdayDisplayData(
            babyName = baby.name,
            ageNumber = age.number,
            ageUnit = age.unit,
            pictureUri = baby.pictureUri,
            theme = BirthdayTheme.random()
        )

        emit(Resource.Success(displayData))
    }

    /**
     * Calculates baby's age following the business rule:
     * - Show months for babies under 1 year
     * - Show years for babies 1 year and older
     */
    private fun calculateAge(birthday: LocalDate, today: LocalDate): AgeData {
        val period = birthday.periodUntil(today)

        return if (period.years == 0) {
            // Under 1 year - show months
            AgeData(
                number = period.months,
                unit = AgeUnit.MONTHS
            )
        } else {
            // 1 year or older - show years
            AgeData(
                number = period.years,
                unit = AgeUnit.YEARS
            )
        }
    }

    private data class AgeData(
        val number: Int,
        val unit: AgeUnit
    )
}