package com.example.capstoneproject.room

import android.content.Context
import com.example.capstoneproject.models.User

class UserRepository(context: Context) {

    private val userDao: UserDao

    init {
        val database =
            AppDatabase.getDatabase(
                context
            )
        userDao = database!!.userDao()
    }

    suspend fun getByName(name: String): User = userDao.findByName(name)

    suspend fun getAll(): List<User> = userDao.getAll()

    suspend fun getOwner(): User = userDao.getOwner()

    suspend fun getUsersByIds(ids: ArrayList<Int>): List<User> = userDao.getUsersByIds(ids)

    suspend fun create(user: User) = userDao.create(user)

    suspend fun update(user: User) = userDao.update(user)

    suspend fun delete(user: User) = userDao.delete(user)

    suspend fun deleteAll() = userDao.deleteAll()

}