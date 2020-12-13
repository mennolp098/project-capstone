package com.example.capstoneproject.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.capstoneproject.entities.Game
import com.example.capstoneproject.entities.GameSession

data class GameSessionWithGame (
    @Embedded val gameSession: GameSession,
    @Relation(
        parentColumn = "gameUid",
        entityColumn = "gameUid"
    )
    val game: Game
)