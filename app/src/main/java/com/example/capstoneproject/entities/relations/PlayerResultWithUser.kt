package com.example.capstoneproject.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.capstoneproject.entities.PlayerResult
import com.example.capstoneproject.entities.User

data class PlayerResultWithUser(
    @Embedded val playerResult: PlayerResult,
    @Relation(
        parentColumn = "userUid",
        entityColumn = "userUid"
    )
    val user: User
)