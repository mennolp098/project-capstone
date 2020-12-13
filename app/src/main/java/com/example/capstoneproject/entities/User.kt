package com.example.capstoneproject.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "userUid", index = true) var userUid: Int?,
    @ColumnInfo(name = "fullName") var fullName: String,
    @ColumnInfo(name = "isAppOwner") var isAppOwner: Boolean,
    @ColumnInfo(name = "isSelected") var isSelected: Boolean,
    @ColumnInfo(name = "color") var color: Int
)
