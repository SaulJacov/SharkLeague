package com.example.sharkleague.data.repository

import com.example.sharkleague.data.local.PartidoDao
import com.example.sharkleague.data.model.Partido
import kotlinx.coroutines.flow.Flow

class PartidoRepository(private val partidoDao: PartidoDao) {

    val allPartidos: Flow<List<Partido>> = partidoDao.getAllPartidos()

    suspend fun insert(partido: Partido) {
        partidoDao.insertPartido(partido)
    }

    suspend fun update(partido: Partido) {
        partidoDao.updatePartido(partido)
    }

    suspend fun delete(partido: Partido) {
        partidoDao.deletePartido(partido)
    }
}
