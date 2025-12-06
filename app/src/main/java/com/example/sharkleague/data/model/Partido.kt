package com.example.sharkleague.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidos")
data class Partido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val localTeamName: String,
    val visitorTeamName: String,
    val date: String,
    val time: String,
    val localScore: Int? = null,
    val visitorScore: Int? = null
)
