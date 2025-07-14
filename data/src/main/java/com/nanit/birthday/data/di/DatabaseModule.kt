package com.nanit.birthday.data.di

import android.content.Context
import com.nanit.birthday.data.local.dao.BabyDao
import com.nanit.birthday.data.local.database.BabyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBabyDatabase(
        @ApplicationContext context: Context
    ): BabyDatabase = BabyDatabase.getInstance(context)

    @Provides
    fun provideBabyDao(database: BabyDatabase): BabyDao = database.babyDao()
}