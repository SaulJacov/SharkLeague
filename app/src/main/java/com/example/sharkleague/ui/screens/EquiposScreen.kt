package com.example.sharkleague.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharkleague.SharkLeagueApplication
import com.example.sharkleague.data.model.Equipo
import com.example.sharkleague.ui.viewmodel.EquiposViewModel
import com.example.sharkleague.ui.viewmodel.EquiposViewModelFactory

@Composable
fun EquiposContent(
    modifier: Modifier = Modifier,
    equiposViewModel: EquiposViewModel = viewModel(
        factory = EquiposViewModelFactory((LocalContext.current.applicationContext as SharkLeagueApplication).equipoRepository)
    )
) {
    val equiposList by equiposViewModel.allEquipos.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var equipoToEdit by remember { mutableStateOf<Equipo?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                equipoToEdit = null
                showDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Equipo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Equipos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            if (equiposList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay equipos. ¡Añade uno!")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(equiposList) { equipo ->
                        EquipoItem(
                            equipo = equipo,
                            onEdit = {
                                equipoToEdit = it
                                showDialog = true
                            },
                            onDelete = {
                                equiposViewModel.delete(it)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddEditEquipoDialog(
            equipo = equipoToEdit,
            onDismiss = { showDialog = false },
            onConfirm = { equipoName ->
                if (equipoToEdit == null) {
                    equiposViewModel.insert(Equipo(name = equipoName))
                } else {
                    equiposViewModel.update(equipoToEdit!!.copy(name = equipoName))
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun EquipoItem(
    equipo: Equipo,
    onEdit: (Equipo) -> Unit,
    onDelete: (Equipo) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = equipo.name, style = MaterialTheme.typography.bodyLarge)
        Row {
            IconButton(onClick = { onEdit(equipo) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = { onDelete(equipo) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEquipoDialog(
    equipo: Equipo?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(equipo?.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (equipo == null) "Añadir Equipo" else "Editar Equipo") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Nombre del equipo") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onConfirm(text)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
