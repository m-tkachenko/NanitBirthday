package com.nanit.birthday.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nanit.birthday.data.local.converter.DateConverters
import com.nanit.birthday.data.local.dao.BabyDao
import com.nanit.birthday.data.local.entity.BabyEntity

/**
 * Room database for the Nanit Birthday app.
 *
 * This database stores baby information including:
 * - Profile data (name, birthday, picture)
 *
 * Version History:
 * - v1: Initial schema with BabyEntity
 */
@Database(
    entities = [BabyEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverters::class)
abstract class BabyDatabase : RoomDatabase() {
    abstract fun babyDao(): BabyDao

    companion object {
        const val DATABASE_NAME = "nanit_baby_birthday_database"

        @Volatile
        private var INSTANCE: BabyDatabase? = null

        fun getInstance(context: Context): BabyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): BabyDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                BabyDatabase::class.java,
                DATABASE_NAME
            )
                .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                .fallbackToDestructiveMigration(false)
                .build()
    }
}