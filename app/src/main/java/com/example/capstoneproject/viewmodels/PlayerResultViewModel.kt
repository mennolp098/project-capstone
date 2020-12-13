package com.example.capstoneproject.viewmodels

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.capstoneproject.entities.GameSession
import com.example.capstoneproject.entities.PlayerResult
import com.example.capstoneproject.room.PlayerResultRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayerResultViewModel(application: Application) : AndroidViewModel(application) {
    internal lateinit var listener: PlayerResultViewModelListener
    private val playerResultRepository = PlayerResultRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    val createdPlayerResults = MutableLiveData<Int>(0)
    //val playerResultList = playerResultRepository.getAll()

    // Interface for event listening.
    interface PlayerResultViewModelListener {
        fun onPlayerResultsCreated()
    }

    fun setParentFragment(fragment: Fragment)
    {
        try {
            listener = fragment as PlayerResultViewModelListener
        } catch (e: ClassCastException) {
            // The parent fragment doesn't implement the interface, throw exception
            throw ClassCastException((fragment.toString() +
                    " must implement PlayerResultViewModelListener"))
        }
    }

    fun createPlayerResults(playerIds: ArrayList<Int>, gameSessionUid: Int) {
        var players = ArrayList<PlayerResult>()
        playerIds.forEach { playerId ->
            var newPlayerResult = PlayerResult(
                playerResultUid = null,
                userUid = playerId,
                gameSessionUid = gameSessionUid
            )
            players.add(newPlayerResult)
        }


        mainScope.launch {
            withContext(Dispatchers.IO) {
                playerResultRepository.createAll(players)
                listener.onPlayerResultsCreated()
            }
            success.value = true
        }
    }

    fun update(playerResult: PlayerResult) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                playerResultRepository.update(playerResult)
            }
            success.value = true
        }
    }

    fun delete(playerResult: PlayerResult) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                playerResultRepository.delete(playerResult)
            }
            success.value = true
        }
    }

    fun deleteAll() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                playerResultRepository.deleteAll()
            }
            success.value = true
        }
    }
}