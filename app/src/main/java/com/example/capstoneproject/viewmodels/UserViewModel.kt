package com.example.capstoneproject.viewmodels

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.room.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    internal lateinit var listener: UserViewModelListener
    private val userRepository = UserRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    val userList = userRepository.getAll()
    val owner = userRepository.getOwner()

    // Interface for event listening.
    interface UserViewModelListener {
        fun onOwnerRetrieved(owner: LiveData<User>)
    }

    fun setParentFragment(fragment: Fragment)
    {
        try {
            listener = fragment as UserViewModelListener
        } catch (e: ClassCastException) {
            // The parent fragment doesn't implement the interface, throw exception
            throw ClassCastException((fragment.toString() +
                    " must implement UserViewModelListener"))
        }
    }

    fun retrieveOwner() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                val owner = userRepository.getOwner()
                listener.onOwnerRetrieved(owner)
            }
            success.value = true
        }
    }

    fun createUser(fullName: String, isAppOwner: Boolean, color: Int) {
        val newUser = User(
            userUid = null,
            fullName = fullName,
            isAppOwner = isAppOwner,
            isSelected = false,
            color = color

        )

        if(isUserValid(newUser)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    userRepository.create(newUser)
                }
                success.value = true
            }
        }
    }

    fun update(user: User) {
        if(isUserValid(user)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    userRepository.update(user)
                }
                success.value = true
            }
        }
    }

    fun delete(user: User) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.delete(user)
            }
            success.value = true
        }
    }

    fun deleteAll() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.deleteAll()
            }
            success.value = true
        }
    }

    private fun isUserValid(user: User): Boolean {
        return when {
            user.fullName.isBlank() -> {
                error.value = "Full name must not be empty"
                false
            }
            else -> true
        }
    }
}