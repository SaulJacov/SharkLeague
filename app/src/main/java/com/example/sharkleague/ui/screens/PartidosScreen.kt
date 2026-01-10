package com.example.sharkleague.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharkleague.SharkLeagueApplication
import com.example.sharkleague.data.model.Equipo
import com.example.sharkleague.data.model.Partido
import com.example.sharkleague.ui.viewmodel.EquiposViewModel
import com.example.sharkleague.ui.viewmodel.EquiposViewModelFactory
import com.example.sharkleague.ui.viewmodel.PartidosViewModel
import com.example.sharkleague.ui.viewmodel.PartidosViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PartidosContent(
    modifier: Modifier = Modifier,
    partidosViewModel: PartidosViewModel = viewModel(
        factory = PartidosViewModelFactory((LocalContext.current.applicationContext as SharkLeagueApplication).partidoRepository)
    ),
    equiposViewModel: EquiposViewModel = viewModel(
        factory = EquiposViewModelFactory((LocalContext.current.applicationContext as SharkLeagueApplication).equipoRepository)
    )
) {
    val partidosList by partidosViewModel.allPartidos.collectAsState()
    val equiposList by equiposViewModel.allEquipos.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var showScoreDialog by remember { mutableStateOf(false) }
    var partidoToProcess by remember { mutableStateOf<Partido?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                partidoToProcess = null
                showAddEditDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Partido")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Partidos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            if (partidosList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay partidos. ¡Añade uno!")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(partidosList) { partido ->
                        PartidoItem(
                            partido = partido,
                            onEdit = {
                                partidoToProcess = it
                                showAddEditDialog = true
                            },
                            onRegisterScore = {
                                partidoToProcess = it
                                showScoreDialog = true
                            },
                            onDelete = {
                                partidosViewModel.delete(it)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddEditPartidoDialog(
            partido = partidoToProcess,
            equiposDisponibles = equiposList,
            onDismiss = { showAddEditDialog = false },
            onConfirm = { local, visitor, date, time ->
                if (partidoToProcess == null) {
                    partidosViewModel.insert(Partido(localTeamName = local, visitorTeamName = visitor, date = date, time = time))
                } else {
                    partidosViewModel.update(partidoToProcess!!.copy(localTeamName = local, visitorTeamName = visitor, date = date, time = time))
                }
                showAddEditDialog = false
            }
        )
    }

    if (showScoreDialog) {
        RegisterScoreDialog(
            partido = partidoToProcess,
            onDismiss = { showScoreDialog = false },
            onConfirm = { localScore, visitorScore ->
                partidoToProcess?.let {
                    partidosViewModel.update(it.copy(localScore = localScore, visitorScore = visitorScore))
                }
                showScoreDialog = false
            }
        )
    }
}

@Composable
fun PartidoItem(
    partido: Partido,
    onEdit: (Partido) -> Unit,
    onRegisterScore: (Partido) -> Unit,
    onDelete: (Partido) -> Unit
) {
    Card(modifier = Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = partido.localTeamName, style = MaterialTheme.typography.titleLarge)
                if (partido.localScore != null && partido.visitorScore != null) {
                    Text(text = " ${partido.localScore} - ${partido.visitorScore} ", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
                } else {
                    Text(text = " vs ", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                }
                Text(text = partido.visitorTeamName, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fecha: ${partido.date} | Hora: ${partido.time}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { onEdit(partido) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { onRegisterScore(partido) }) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = "Registrar Marcador")
                }
                IconButton(onClick = { onDelete(partido) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPartidoDialog(
    partido: Partido?,
    equiposDisponibles: List<Equipo>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var localTeam by remember { mutableStateOf(partido?.localTeamName ?: "") }
    var visitorTeam by remember { mutableStateOf(partido?.visitorTeamName ?: "") }
    var date by remember { mutableStateOf(partido?.date ?: "") }
    var time by remember { mutableStateOf(partido?.time ?: "") }

    val context = LocalContext.current

    val initialDateCalendar = Calendar.getInstance()
    if (date.isNotBlank()) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        try {
            initialDateCalendar.time = sdf.parse(date) ?: Date()
        } catch (_: Exception) {
        }
    }

    val initialTimeCalendar = Calendar.getInstance()
    if (time.isNotBlank()) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        try {
            initialTimeCalendar.time = sdf.parse(time) ?: Date()
        } catch (_: Exception) {
        }
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val newDate = Calendar.getInstance()
            newDate.set(year, month, dayOfMonth)
            date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
        },
        initialDateCalendar.get(Calendar.YEAR),
        initialDateCalendar.get(Calendar.MONTH),
        initialDateCalendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val newTime = Calendar.getInstance()
            newTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            newTime.set(Calendar.MINUTE, minute)
            time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(newTime.time)
        },
        initialTimeCalendar.get(Calendar.HOUR_OF_DAY),
        initialTimeCalendar.get(Calendar.MINUTE),
        false
    )

    var expandedLocal by remember { mutableStateOf(false) }
    var expandedVisitor by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (partido == null) "Añadir Partido" else "Editar Partido") },
        text = {
            Column {
                ExposedDropdownMenuBox(
                    expanded = expandedLocal,
                    onExpandedChange = { expandedLocal = !expandedLocal }
                ) {
                    OutlinedTextField(
                        value = localTeam,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Equipo Local") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocal) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedLocal,
                        onDismissRequest = { expandedLocal = false }
                    ) {
                        equiposDisponibles.forEach { equipo ->
                            DropdownMenuItem(
                                text = { Text(equipo.name) },
                                onClick = {
                                    localTeam = equipo.name
                                    expandedLocal = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedVisitor,
                    onExpandedChange = { expandedVisitor = !expandedVisitor }
                ) {
                    OutlinedTextField(
                        value = visitorTeam,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Equipo Visitante") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVisitor) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedVisitor,
                        onDismissRequest = { expandedVisitor = false }
                    ) {
                        equiposDisponibles.forEach { equipo ->
                            DropdownMenuItem(
                                text = { Text(equipo.name) },
                                onClick = {
                                    visitorTeam = equipo.name
                                    expandedVisitor = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = { Text("Fecha") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = {},
                    label = { Text("Hora") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { timePickerDialog.show() }) {
                            Icon(Icons.Filled.AccessTime, contentDescription = "Seleccionar Hora")
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(localTeam, visitorTeam, date, time) },
                enabled = localTeam.isNotBlank() && visitorTeam.isNotBlank() && date.isNotBlank() && time.isNotBlank() && localTeam != visitorTeam
            ) { Text("Guardar") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun RegisterScoreDialog(
    partido: Partido?,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var localScore by remember { mutableStateOf(partido?.localScore?.toString() ?: "") }
    var visitorScore by remember { mutableStateOf(partido?.visitorScore?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Marcador") },
        text = {
            Column {
                Text("${partido?.localTeamName} vs ${partido?.visitorTeamName}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedTextField(
                        value = localScore,
                        onValueChange = { localScore = it },
                        label = { Text("Local") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        value = visitorScore,
                        onValueChange = { visitorScore = it },
                        label = { Text("Visitante") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(localScore.toIntOrNull() ?: 0, visitorScore.toIntOrNull() ?: 0) },
                enabled = localScore.isNotBlank() && visitorScore.isNotBlank() && localScore.toIntOrNull() != null && visitorScore.toIntOrNull() != null
            ) { Text("Guardar") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
