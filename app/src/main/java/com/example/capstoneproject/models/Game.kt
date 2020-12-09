package com.example.capstoneproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") var uid: Int?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "trackKind") var trackKind: String?,
    @ColumnInfo(name = "trackAmount") var trackAmount: String?,
    @ColumnInfo(name = "trackEnd") var trackEnd: String?,
    @ColumnInfo(name = "isSelected") var isSelected: Boolean?
)
