package com.example.capstoneproject.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.capstoneproject.entities.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): List<Game>

    @Query("SELECT * FROM game WHERE gameUid = :id LIMIT 1")
    fun getById(id:Int): Game

    @Insert
    fun create(vararg game: Game)

    @Delete
    fun delete(game: Game)

    @Query("DELETE FROM game")
    suspend fun deleteAll()
}
