package com.example.capstoneproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") var uid: Int?,
    @ColumnInfo(name = "full_name") var fullName: String,
    @ColumnInfo(name = "is_app_owner") var isAppOwner: Boolean,
    @ColumnInfo(name = "isSelected") var isSelected: Boolean,
    @ColumnInfo(name = "color") var color: Int
)
