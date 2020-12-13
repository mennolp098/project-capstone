package com.example.capstoneproject.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.capstoneproject.converters.Converters
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.GameSession
import com.example.capstoneproject.entities.PlayerResult
import com.example.capstoneproject.entities.User

@Database(entities = arrayOf(User::class, Game::class, PlayerResult::class, GameSession::class), version = 20)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gameDao(): GameDao
    abstract fun playerResultDao(): PlayerResultDao
    abstract fun gameSessionDao(): GameSessionDao

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
