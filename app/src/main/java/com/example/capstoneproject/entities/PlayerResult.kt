package com.example.capstoneproject.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity()
data class PlayerResult(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playerResultUid", index = true) var playerResultUid: Int?,
    @ColumnInfo(name = "gameSessionUid", index = true) var gameSessionUid: Int,
    @ColumnInfo(name = "userUid", index = true) var userUid: Int,
    @ColumnInfo(name = "amount") var amount: Int = 0,
    @ColumnInfo(name = "isWinner") var isWinner: Boolean = false
)
