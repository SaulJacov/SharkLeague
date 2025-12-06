package com.example.sharkleague.data.repository

import com.example.sharkleague.data.local.EquipoDao
import com.example.sharkleague.data.model.Equipo
import kotlinx.coroutines.flow.Flow

class EquipoRepository(private val equipoDao: EquipoDao) {

    val allEquipos: Flow<List<Equipo>> = equipoDao.getAllEquipos()

    suspend fun insert(equipo: Equipo) {
        equipoDao.insertEquipo(equipo)
    }

    suspend fun update(equipo: Equipo) {
        equipoDao.updateEquipo(equipo)
    }

    suspend fun delete(equipo: Equipo) {
        equipoDao.deleteEquipo(equipo)
    }
}
