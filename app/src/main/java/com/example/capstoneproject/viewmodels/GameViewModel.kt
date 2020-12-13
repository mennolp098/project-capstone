package com.example.capstoneproject.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.room.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameRepository = GameRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()
    val gamesList = gameRepository.getAll()

    fun getGameById(id: Int): LiveData<Game> {
        return gameRepository.getById(id)
    }

    fun createGame(name: String, trackEnd: String, trackAmount: String, trackKind: String) {
        val newGame = Game(
            gameUid = null,
            name = name,
            trackEnd = trackEnd,
            trackAmount = trackAmount,
            trackKind = trackKind

        )

        if(isGameValid(newGame)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.create(newGame)
                }
                success.value = true
            }
        }
    }

    fun delete(game: Game) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.delete(game)
            }
            success.value = true
        }
    }

    fun deleteAllGames() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAll()
            }
            success.value = true
        }
    }

    private fun isGameValid(game: Game): Boolean {
        return when {
            game.name?.isBlank()!! -> {
                error.value = "Title must not be empty"
                false
            }
            else -> true
        }
    }

}