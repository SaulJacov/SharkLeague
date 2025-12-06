package com.example.sharkleague

import android.app.Application
import com.example.sharkleague.data.local.SharkLeagueDatabase
import com.example.sharkleague.data.repository.EquipoRepository
import com.example.sharkleague.data.repository.PartidoRepository

class SharkLeagueApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { SharkLeagueDatabase.getDatabase(this) }
    val equipoRepository by lazy { EquipoRepository(database.equipoDao()) }
    val partidoRepository by lazy { PartidoRepository(database.partidoDao()) }
}
