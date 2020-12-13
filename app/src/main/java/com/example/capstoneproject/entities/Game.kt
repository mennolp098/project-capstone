package com.example.capstoneproject.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "gameUid", index = true) var gameUid: Int?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "trackKind") var trackKind: String?,
    @ColumnInfo(name = "trackAmount") var trackAmount: String?,
    @ColumnInfo(name = "trackEnd") var trackEnd: String?,
    @ColumnInfo(name = "isSelected") var isSelected: Boolean?
)
