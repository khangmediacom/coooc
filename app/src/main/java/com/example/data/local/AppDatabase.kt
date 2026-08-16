package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.LessonProgressDao
import com.example.data.local.dao.MatchHistoryDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.local.entity.MatchHistoryEntity
import com.example.data.local.entity.UserProfileEntity

@Database(
    entities = [MatchHistoryEntity::class, UserProfileEntity::class, LessonProgressEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchHistoryDao(): MatchHistoryDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun lessonProgressDao(): LessonProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "khmer_chess_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
