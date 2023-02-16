package com.example.fotobudka.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
class Settings (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "s1") val s1: String?
    )