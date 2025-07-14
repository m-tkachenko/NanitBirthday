package com.nanit.birthday.data.di

import com.nanit.birthday.data.repository.BabyRepositoryImpl
import com.nanit.birthday.domain.repository.BabyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindBabyRepository(
        babyRepositoryImpl: BabyRepositoryImpl
    ): BabyRepository
}