package com.example.capstoneproject.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(foreignKeys =
    arrayOf(
        ForeignKey(
        onDelete = CASCADE, entity = User::class,
                parentColumns = arrayOf("userUid"), childColumns = arrayOf("userUid")
        )
    )
)
data class PlayerResult(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playerResultUid", index = true) var playerResultUid: Int?,
    @ColumnInfo(name = "gameSessionUid", index = true) var gameSessionUid: Int,
    @ColumnInfo(name = "userUid", index = true) var userUid: Int,
    @ColumnInfo(name = "amount") var amount: Int = 0,
    @ColumnInfo(name = "isWinner") var isWinner: Boolean = false
)
