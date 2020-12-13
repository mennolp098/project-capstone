package com.example.capstoneproject.room

import android.content.Context
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


    suspend fun getAll(): List<Game> = gameDao.getAll()

    suspend fun create(game: Game) = gameDao.create(game)

    suspend fun getById(id: Int): Game = gameDao.getById(id)

    suspend fun delete(game: Game) = gameDao.delete(game)

    suspend fun deleteAll() = gameDao.deleteAll()

}