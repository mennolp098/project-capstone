package com.example.capstoneproject.room

import android.content.Context
import com.example.capstoneproject.entities.PlayerResult

class PlayerResultRepository(context: Context) {

    private val playerResultDao: PlayerResultDao

    init {
        val database =
            AppDatabase.getDatabase(
                context
            )
        playerResultDao = database!!.playerResultDao()
    }


    suspend fun getAll(): List<PlayerResult> = playerResultDao.getAll()

    suspend fun create(playerResult: PlayerResult) = playerResultDao.create(playerResult)

    suspend fun getById(id: Int): PlayerResult = playerResultDao.getById(id)

    suspend fun delete(playerResult: PlayerResult) = playerResultDao.delete(playerResult)

    suspend fun deleteAll() = playerResultDao.deleteAll()

    suspend fun update(playerResult: PlayerResult) = playerResultDao.update(playerResult)

}