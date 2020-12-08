package com.example.capstoneproject.room

import com.example.capstoneproject.models.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.capstoneproject.models.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): List<Game>

    @Query("SELECT * FROM game WHERE uid = 0 LIMIT 1")
    fun getOwner(): Game

    @Insert
    fun create(vararg games: Game)

    @Delete
    fun delete(game: Game)

    @Query("DELETE FROM game")
    suspend fun deleteAll()
}
