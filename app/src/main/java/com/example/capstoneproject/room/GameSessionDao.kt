package com.example.capstoneproject.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.GameSession
import com.example.capstoneproject.entities.PlayerResult
import com.example.capstoneproject.entities.relations.GameSessionWithPlayerResultsAndGame

@Dao
interface GameSessionDao {
    @Query("SELECT * FROM GameSession")
    fun getAll(): LiveData<List<GameSession>>

    @Query("SELECT * FROM GameSession WHERE gameSessionUid = :id LIMIT 1")
    fun getById(id:Int): LiveData<GameSession>

    @Insert
    fun create(vararg gameSessions: GameSession): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(vararg game: Game)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayerResult(vararg playerResult: PlayerResult)

    @Update
    fun update(gameSession: GameSession)

    @Delete
    fun delete(gameSession: GameSession)

    @Query("DELETE FROM GameSession")
    suspend fun deleteAll()

    @Query("SELECT * FROM GameSession ORDER BY gameSessionUid DESC LIMIT 1")
    fun getLastCreatedGameSession(): LiveData<GameSession>

    @Transaction
    @Query("SELECT * FROM GameSession WHERE gameSessionUid = :gameSessionUid LIMIT 1")
    fun getSessionWithPlayerResultsAndGameById(gameSessionUid: Int): LiveData<GameSessionWithPlayerResultsAndGame>

    @Transaction
    @Query("SELECT * FROM GameSession")
    fun getSessionsWithPlayerResultsAndGame(): LiveData<List<GameSessionWithPlayerResultsAndGame>>

    /*
    @Transaction
    @Query("SELECT * FROM PlayerResult WHERE gameSessionUid = :gameSessionUid")
    fun getPlayerResultsFromGameSessionId(gameSessionUid: Int): List<GameSessionWithPlayerResultsAndGame>

    @Transaction
    @Query("SELECT * FROM Game INNER JOIN GameSession ON game.gameUid = gamesession.gameUid WHERE gamesession.gameSessionUid LIKE :gameSessionUid LIMIT 1"
    )
    fun getGameFromGameSessionId(gameSessionUid: Int): Game */
}
