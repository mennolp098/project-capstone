package com.example.capstoneproject.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.capstoneproject.entities.User

class UserRepository(context: Context) {

    private val userDao: UserDao

    init {
        val database =
            AppDatabase.getDatabase(
                context
            )
        userDao = database!!.userDao()
    }

    fun getByName(name: String): LiveData<User> = userDao.findByName(name)

    fun getAll(): LiveData<List<User>> = userDao.getAll()

    fun getOwner(): LiveData<User> = userDao.getOwner()

    fun getUsersByIds(ids: ArrayList<Int>): LiveData<List<User>> = userDao.getUsersByIds(ids)

    fun create(user: User) = userDao.create(user)

    fun update(user: User) = userDao.update(user)

    fun delete(user: User) = userDao.delete(user)

    suspend fun deleteAll() = userDao.deleteAll()

}