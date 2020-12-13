package com.example.capstoneproject.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.capstoneproject.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE userUid = 1 LIMIT 1")
    fun getOwner(): LiveData<User>

    @Query("SELECT * FROM user WHERE fullName LIKE :name LIMIT 1")
    fun findByName(name: String): LiveData<User>

    @Query("SELECT * FROM user WHERE userUid IN (:ids)")
    fun getUsersByIds(ids: ArrayList<Int>): LiveData<List<User>>

    @Update
    fun update(vararg user: User)

    @Insert
    fun create(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
