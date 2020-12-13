package com.example.capstoneproject.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.capstoneproject.entities.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): LiveData<List<Game>?>

    @Query("SELECT * FROM game WHERE gameUid = :id LIMIT 1")
    fun getById(id:Int): LiveData<Game>

    @Insert
    fun create(vararg game: Game)

    @Delete
    fun delete(game: Game)

    @Query("DELETE FROM game")
    suspend fun deleteAll()
}
