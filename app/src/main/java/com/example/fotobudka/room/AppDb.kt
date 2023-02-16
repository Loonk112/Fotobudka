package com.example.fotobudka.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Settings :: class], version = 2)
abstract class AppDb : RoomDatabase() {

    abstract fun SettingsDao() : SettingsDao
    companion object{
        @Volatile
        private var INSTANCE : AppDb? = null

        fun getDatabase(context: Context):AppDb{
            val tmpInstance = INSTANCE
            if(tmpInstance != null){
                return tmpInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }

}
