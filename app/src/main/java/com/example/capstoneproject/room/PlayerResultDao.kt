package com.example.capstoneproject.room

import androidx.room.*
import com.example.capstoneproject.entities.PlayerResult
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.entities.relations.GameSessionWithPlayerResultsAndGame
import com.example.capstoneproject.entities.relations.PlayerResultWithUser

@Dao
interface PlayerResultDao {
    @Query("SELECT * FROM playerresult")
    fun getAll(): List<PlayerResult>

    @Query("SELECT * FROM playerresult WHERE playerResultUid = :id LIMIT 1")
    fun getById(id:Int): PlayerResult

    @Insert
    suspend fun create(vararg playerResult: PlayerResult)

    @Insert
    suspend fun createAll(playerResult: ArrayList<PlayerResult>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: User)

    @Update
    fun update(vararg playerResult: PlayerResult)

    @Delete
    fun delete(gameRules: PlayerResult)

    @Query("DELETE FROM playerresult")
    suspend fun deleteAll()

    /*
    @Transaction
    @Query("SELECT * FROM User INNER JOIN PlayerResult ON user.userUid = playerResult.userUid")
    fun getUsers(): List<PlayerResultWithUser> */

}

