package com.example.capstoneproject.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.capstoneproject.entities.PlayerResult

data class GameSessionWithPlayerResultsAndGame(
    @Embedded val gameSessionWithGame: GameSessionWithGame,
    @Relation(
        parentColumn = "gameSessionUid",
        entityColumn = "gameSessionUid",
        entity = PlayerResult::class
    )
    val playerResults: List<PlayerResultWithUser>
)