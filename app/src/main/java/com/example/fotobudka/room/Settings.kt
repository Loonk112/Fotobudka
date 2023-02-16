package com.example.fotobudka.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings_table")
data class Settings (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "delay") val  delay: Int,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "bannerName") val bannerName: String,
    @ColumnInfo(name = "bannerBgColor") val bannerBGColor: String,
    @ColumnInfo(name = "bannerFontColor") val bannerFontColor: String
    )