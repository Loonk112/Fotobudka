package com.example.fotobudka.room

import androidx.room.*

@Dao
interface SettingsDao {
    @Transaction
    @Query("""
        Select * From settings_table WHERE settings_table.id = :id
    """)
    fun getSettingsById(id: Int): Settings

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settings: Settings)
}