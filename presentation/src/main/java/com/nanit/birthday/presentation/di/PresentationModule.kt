package com.nanit.birthday.presentation.di

import android.content.Context
import com.nanit.birthday.presentation.helper.ImageInternalStorageHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun provideImageInternalStorageHelper(
        @ApplicationContext context: Context
    ): ImageInternalStorageHelper {
        return ImageInternalStorageHelper(context)
    }
}