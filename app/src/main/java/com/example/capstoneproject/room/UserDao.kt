package com.example.capstoneproject.room

import com.example.capstoneproject.models.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid = 0 LIMIT 1")
    fun getOwner(): User

    @Query("SELECT * FROM user WHERE full_name LIKE :name LIMIT 1")
    fun findByName(name: String): User

    @Insert
    fun create(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
