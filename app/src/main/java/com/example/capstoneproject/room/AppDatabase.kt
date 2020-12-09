package com.example.capstoneproject.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.capstoneproject.models.Game
import com.example.capstoneproject.models.User

@Database(entities = arrayOf(User::class, Game::class), version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "APP_DATABASE"

        @Volatile
        private var appDatabaseInstance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (appDatabaseInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (appDatabaseInstance == null) {
                        appDatabaseInstance =
                            Room.databaseBuilder(context.applicationContext,
                                AppDatabase::class.java,
                                DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return appDatabaseInstance
        }
    }
}
