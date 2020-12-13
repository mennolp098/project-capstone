package com.example.capstoneproject.room

import android.content.Context
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.GameSession

class GameSessionRepository(context: Context) {

    private val gameSessionDao: GameSessionDao

    init {
        val database =
            AppDatabase.getDatabase(
                context
            )
        gameSessionDao = database!!.gameSessionDao()
    }


    suspend fun getAll(): List<GameSession> = gameSessionDao.getAll()

    suspend fun create(gameSession: GameSession): List<Long> = gameSessionDao.create(gameSession)

    suspend fun getById(id: Int): GameSession = gameSessionDao.getById(id)

    suspend fun delete(gameSession: GameSession) = gameSessionDao.delete(gameSession)

    suspend fun update(gameSession: GameSession) = gameSessionDao.update(gameSession)

    suspend fun deleteAll() = gameSessionDao.deleteAll()

    suspend fun getSessionWithPlayerResultsAndGameById(gameSessionUid: Int) = gameSessionDao.getSessionWithPlayerResultsAndGameById(gameSessionUid)

    suspend fun getSessionsWithPlayerResultsAndGame() = gameSessionDao.getSessionsWithPlayerResultsAndGame()

}