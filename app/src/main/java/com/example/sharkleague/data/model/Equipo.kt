package com.example.sharkleague.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipos")
data class Equipo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
