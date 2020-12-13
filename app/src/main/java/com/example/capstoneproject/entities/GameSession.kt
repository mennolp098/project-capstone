package com.example.capstoneproject.entities

import androidx.room.*
import java.util.*


@Entity()
data class GameSession(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "gameSessionUid", index = true) var gameSessionUid: Int?,
    @ColumnInfo(name = "gameUid", index = true) var gameUid: Int,
    @ColumnInfo(name = "isFinished") var isFinished: Boolean = false,
    @ColumnInfo(name = "createdAt") var createdAt: Date = Date()
)

