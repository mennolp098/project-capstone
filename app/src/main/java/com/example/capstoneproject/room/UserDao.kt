package com.example.capstoneproject.room

import androidx.room.*
import com.example.capstoneproject.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid = 0 LIMIT 1")
    fun getOwner(): User

    @Query("SELECT * FROM user WHERE full_name LIKE :name LIMIT 1")
    fun findByName(name: String): User

    @Query("SELECT * FROM user WHERE uid IN (:ids)")
    fun getUsersByIds(ids: ArrayList<Int>): List<User>

    @Update
    fun update(vararg user: User)

    @Insert
    fun create(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
