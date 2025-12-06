package com.example.sharkleague.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sharkleague.data.model.Equipo

@Database(entities = [Equipo::class], version = 1, exportSchema = false)
abstract class SharkLeagueDatabase : RoomDatabase() {

    abstract fun equipoDao(): EquipoDao

    companion object {
        @Volatile
        private var INSTANCE: SharkLeagueDatabase? = null

        fun getDatabase(context: Context): SharkLeagueDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SharkLeagueDatabase::class.java,
                    "shark_league_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
