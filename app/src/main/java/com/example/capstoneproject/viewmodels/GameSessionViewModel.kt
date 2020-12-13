package com.example.capstoneproject.viewmodels

import android.app.Application
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstoneproject.dialogs.ConfirmDialog
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.GameSession
import com.example.capstoneproject.entities.User
import com.example.capstoneproject.entities.relations.GameSessionWithPlayerResultsAndGame
import com.example.capstoneproject.room.GameSessionRepository
import com.example.capstoneproject.room.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameSessionViewModel(application: Application) : AndroidViewModel(application) {
    internal lateinit var listener: GameSessionViewModelListener
    private val gameSessionRepository = GameSessionRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    val gameSessionList = gameSessionRepository.getAll()
    val gameSessionWithPlayerResultsAndGameList = gameSessionRepository.getSessionsWithPlayerResultsAndGame()

    // Interface for event listening.
    interface GameSessionViewModelListener {
        fun onGameSessionCreated(gameSession: GameSession)
    }

    fun setParentFragment(fragment: Fragment)
    {
        try {
            listener = fragment as GameSessionViewModelListener
        } catch (e: ClassCastException) {
            // The parent fragment doesn't implement the interface, throw exception
            throw ClassCastException((fragment.toString() +
                    " must implement GameSessionViewModelListener"))
        }
    }

    fun getGameSessionWithPlayerResultsAndGameById(id: Int): LiveData<GameSessionWithPlayerResultsAndGame> {
        return gameSessionRepository.getSessionWithPlayerResultsAndGameById(id)
    }

    fun createGameSession(gameUid: Int) {
        val newGameSession = GameSession(
            gameSessionUid = null,
            gameUid = gameUid
        )

        mainScope.launch {
            withContext(Dispatchers.IO) {
                newGameSession.gameSessionUid = gameSessionRepository.create(newGameSession)[0].toInt()
                listener.onGameSessionCreated(newGameSession)
            }
            success.value = true
        }

    }

    fun getLastCreatedGameSession(): LiveData<GameSession> {
        return gameSessionRepository.getLastCreatedGameSession()
    }

    fun update(gameSession: GameSession) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameSessionRepository.update(gameSession)
            }
            success.value = true
        }
    }

    fun delete(gameSession: GameSession) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameSessionRepository.delete(gameSession)
            }
            success.value = true
        }
    }

    fun deleteAll() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameSessionRepository.deleteAll()
            }
            success.value = true
        }
    }
}