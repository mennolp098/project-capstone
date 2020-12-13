package com.example.capstoneproject.room

import android.content.Context
import androidx.lifecycle.LiveData
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


    fun getAll(): LiveData<List<GameSession>> = gameSessionDao.getAll()

    fun create(gameSession: GameSession): List<Long> = gameSessionDao.create(gameSession)

    fun getById(id: Int): LiveData<GameSession> = gameSessionDao.getById(id)

    fun delete(gameSession: GameSession) = gameSessionDao.delete(gameSession)

    fun update(gameSession: GameSession) = gameSessionDao.update(gameSession)

    suspend fun deleteAll() = gameSessionDao.deleteAll()

    fun getSessionWithPlayerResultsAndGameById(gameSessionUid: Int) = gameSessionDao.getSessionWithPlayerResultsAndGameById(gameSessionUid)

    fun getSessionsWithPlayerResultsAndGame() = gameSessionDao.getSessionsWithPlayerResultsAndGame()

    fun getLastCreatedGameSession() = gameSessionDao.getLastCreatedGameSession()
}