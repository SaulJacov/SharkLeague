package com.example.sharkleague.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharkleague.SharkLeagueApplication
import com.example.sharkleague.data.model.Partido
import com.example.sharkleague.ui.viewmodel.EquiposViewModel
import com.example.sharkleague.ui.viewmodel.EquiposViewModelFactory
import com.example.sharkleague.ui.viewmodel.PartidosViewModel
import com.example.sharkleague.ui.viewmodel.PartidosViewModelFactory

data class TeamStats(
    val name: String,
    var pj: Int = 0,
    var pg: Int = 0,
    var pe: Int = 0,
    var pp: Int = 0,
    var pts: Int = 0
)

@Composable
fun GeneralContent() {
    val context = LocalContext.current
    val app = context.applicationContext as SharkLeagueApplication
    
    val equiposViewModel: EquiposViewModel = viewModel(factory = EquiposViewModelFactory(app.equipoRepository))
    val partidosViewModel: PartidosViewModel = viewModel(factory = PartidosViewModelFactory(app.partidoRepository))
    
    val equipos by equiposViewModel.allEquipos.collectAsState()
    val partidos by partidosViewModel.allPartidos.collectAsState()

    // Lógica para calcular la clasificación
    val statsMap = equipos.associate { it.name to TeamStats(it.name) }.toMutableMap()

    partidos.forEach { p ->
        if (p.localScore != null && p.visitorScore != null) {
            val local = statsMap[p.localTeamName]
            val visita = statsMap[p.visitorTeamName]

            if (local != null && visita != null) {
                local.pj++
                visita.pj++

                when {
                    p.localScore > p.visitorScore -> {
                        local.pg++; local.pts += 3
                        visita.pp++
                    }
                    p.localScore < p.visitorScore -> {
                        visita.pg++; visita.pts += 3
                        local.pp++
                    }
                    else -> {
                        local.pe++; local.pts += 1
                        visita.pe++; visita.pts += 1
                    }
                }
            }
        }
    }

    val sortedStats = statsMap.values.sortedByDescending { it.pts }

    // Filtrar partidos próximos y recientes
    val proximosPartidos = partidos.filter { it.localScore == null }.take(2)
    val ultimosPartidos = partidos.filter { it.localScore != null }.take(2)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Tabla General", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (equipos.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    Text("Agregue equipos para ver la tabla")
                }
            }
        } else {
            item {
                Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer).padding(8.dp)) {
                    Text("Equipo", Modifier.weight(3f), fontWeight = FontWeight.Bold)
                    Text("PJ", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("G", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("E", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("P", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("Pts", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                }
            }

            items(sortedStats) { stat ->
                Row(Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(stat.name, Modifier.weight(3f))
                    Text(stat.pj.toString(), Modifier.weight(1f))
                    Text(stat.pg.toString(), Modifier.weight(1f))
                    Text(stat.pe.toString(), Modifier.weight(1f))
                    Text(stat.pp.toString(), Modifier.weight(1f))
                    Text(stat.pts.toString(), Modifier.weight(1f), fontWeight = FontWeight.Bold)
                }
                Divider()
            }
        }

        // Sección de Partidos Próximos
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Partidos Próximos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (proximosPartidos.isEmpty()) {
            item { Text("No hay partidos próximos programados", style = MaterialTheme.typography.bodyMedium) }
        } else {
            items(proximosPartidos) { partido ->
                PartidoResumenItem(partido)
            }
        }

        // Sección de Últimos Resultados
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Últimos Resultados", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (ultimosPartidos.isEmpty()) {
            item { Text("No hay resultados recientes", style = MaterialTheme.typography.bodyMedium) }
        } else {
            items(ultimosPartidos) { partido ->
                PartidoResumenItem(partido)
            }
        }
        
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun PartidoResumenItem(partido: Partido) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(partido.localTeamName, fontWeight = FontWeight.Medium)
                if (partido.localScore != null) {
                    Text("${partido.localScore} - ${partido.visitorScore}", fontWeight = FontWeight.Bold)
                } else {
                    Text("vs", color = MaterialTheme.colorScheme.outline)
                }
                Text(partido.visitorTeamName, fontWeight = FontWeight.Medium)
            }
            Text(
                text = "${partido.date} | ${partido.time}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
