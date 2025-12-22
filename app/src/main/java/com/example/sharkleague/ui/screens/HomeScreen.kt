package com.example.sharkleague.ui.screens

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
import com.example.sharkleague.ui.viewmodel.PartidosViewModel
import com.example.sharkleague.ui.viewmodel.PartidosViewModelFactory

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as SharkLeagueApplication
    val partidosViewModel: PartidosViewModel = viewModel(factory = PartidosViewModelFactory(app.partidoRepository))
    
    val partidos by partidosViewModel.allPartidos.collectAsState()

    // Filtrar: 2 próximos (sin marcador) y 2 recientes (con marcador)
    val proximos = partidos.filter { it.localScore == null }.take(2)
    val recientes = partidos.filter { it.localScore != null }.take(2)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Partidos próximos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (proximos.isEmpty()) {
            item { Text("No hay partidos programados", modifier = Modifier.padding(bottom = 16.dp)) }
        } else {
            items(proximos) { partido ->
                HomePartidoItem(partido)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Resultados recientes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (recientes.isEmpty()) {
            item { Text("Aún no hay resultados registrados", modifier = Modifier.padding(bottom = 16.dp)) }
        } else {
            items(recientes) { partido ->
                HomePartidoItem(partido)
            }
        }
    }
}

@Composable
fun HomePartidoItem(partido: Partido) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(partido.localTeamName, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                
                if (partido.localScore != null) {
                    Text(
                        text = "${partido.localScore} - ${partido.visitorScore}",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                } else {
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                
                Text(
                    partido.visitorTeamName,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
            Text(
                text = "${partido.date} | ${partido.time}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
