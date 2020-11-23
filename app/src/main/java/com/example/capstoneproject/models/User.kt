package com.example.capstoneproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "full_name") val fullName: String?,
    @ColumnInfo(name = "is_app_owner") val isAppOwner: Boolean?
)
