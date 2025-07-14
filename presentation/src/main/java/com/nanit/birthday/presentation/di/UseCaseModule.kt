// presentation/src/main/kotlin/com/nanit/birthday/presentation/di/UseCaseModule.kt
package com.nanit.birthday.presentation.di

import com.nanit.birthday.domain.repository.BabyRepository
import com.nanit.birthday.domain.usecases.BabyExistsUseCase
import com.nanit.birthday.domain.usecases.DeleteBabyUseCase
import com.nanit.birthday.domain.usecases.GetBabyUseCase
import com.nanit.birthday.domain.usecases.GetBirthdayDisplayDataUseCase
import com.nanit.birthday.domain.usecases.ObserveBabyUseCase
import com.nanit.birthday.domain.usecases.SaveBabyUseCase
import com.nanit.birthday.domain.usecases.UpdateBabyBirthdayUseCase
import com.nanit.birthday.domain.usecases.UpdateBabyNameUseCase
import com.nanit.birthday.domain.usecases.UpdateBabyPictureUseCase
import com.nanit.birthday.domain.validator.BabyValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideBabyValidator(): BabyValidator {
        return BabyValidator()
    }

    @Provides
    fun provideGetBabyUseCase(
        babyRepository: BabyRepository
    ): GetBabyUseCase {
        return GetBabyUseCase(babyRepository)
    }

    @Provides
    fun provideGetBirthdayDisplayDataUseCase(
        babyRepository: BabyRepository
    ): GetBirthdayDisplayDataUseCase {
        return GetBirthdayDisplayDataUseCase(babyRepository)
    }

    @Provides
    fun provideObserveBabyUseCase(
        babyRepository: BabyRepository
    ): ObserveBabyUseCase {
        return ObserveBabyUseCase(babyRepository)
    }

    @Provides
    fun provideBabyExistsUseCase(
        babyRepository: BabyRepository
    ): BabyExistsUseCase {
        return BabyExistsUseCase(babyRepository)
    }

    @Provides
    fun provideSaveBabyUseCase(
        babyRepository: BabyRepository,
        babyValidator: BabyValidator
    ): SaveBabyUseCase {
        return SaveBabyUseCase(babyRepository, babyValidator)
    }

    @Provides
    fun provideUpdateBabyBirthdayUseCase(
        babyRepository: BabyRepository,
        babyValidator: BabyValidator
    ): UpdateBabyBirthdayUseCase {
        return UpdateBabyBirthdayUseCase(babyRepository, babyValidator)
    }

    @Provides
    fun provideUpdateNameUseCase(
        babyRepository: BabyRepository,
        babyValidator: BabyValidator
    ): UpdateBabyNameUseCase {
        return UpdateBabyNameUseCase(babyRepository, babyValidator)
    }

    @Provides
    fun provideUpdatePictureUseCase(
        babyRepository: BabyRepository,
        babyValidator: BabyValidator
    ): UpdateBabyPictureUseCase {
        return UpdateBabyPictureUseCase(babyRepository, babyValidator)
    }

    @Provides
    fun provideDeleteBabyUseCase(
        babyRepository: BabyRepository
    ): DeleteBabyUseCase {
        return DeleteBabyUseCase(babyRepository)
    }
}