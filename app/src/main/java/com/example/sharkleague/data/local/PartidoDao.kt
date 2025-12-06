package com.example.sharkleague.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sharkleague.data.model.Partido
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartido(partido: Partido)

    @Query("SELECT * FROM partidos ORDER BY date DESC, time DESC")
    fun getAllPartidos(): Flow<List<Partido>>

    @Update
    suspend fun updatePartido(partido: Partido)

    @Delete
    suspend fun deletePartido(partido: Partido)
}
