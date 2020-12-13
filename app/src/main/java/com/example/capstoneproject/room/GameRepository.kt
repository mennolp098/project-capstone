package com.example.capstoneproject.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.capstoneproject.entities.Game

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database =
            AppDatabase.getDatabase(
                context
            )
        gameDao = database!!.gameDao()
    }


    fun getAll(): LiveData<List<Game>?> = gameDao.getAll()

    suspend fun create(game: Game) = gameDao.create(game)

    fun getById(id: Int): LiveData<Game> = gameDao.getById(id)

    suspend fun delete(game: Game) = gameDao.delete(game)

    suspend fun deleteAll() = gameDao.deleteAll()

}