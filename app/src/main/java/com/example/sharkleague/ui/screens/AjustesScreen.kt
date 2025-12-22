package com.example.sharkleague.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AjustesContent(modifier: Modifier = Modifier) {
    var selectedTheme by remember { mutableStateOf("Claro") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ajustes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Sección de Tema
        Text(
            text = "Tema de la aplicación",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            RadioButton(
                selected = selectedTheme == "Claro",
                onClick = { selectedTheme = "Claro" }
            )
            Text("Tema Claro", modifier = Modifier.padding(start = 8.dp))
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            RadioButton(
                selected = selectedTheme == "Oscuro",
                onClick = { selectedTheme = "Oscuro" }
            )
            Text("Tema Oscuro", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
        Divider()
        Spacer(modifier = Modifier.height(32.dp))

        // Sección Acerca de la App
        Text(
            text = "Acerca de la App",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Shark League",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Versión 1.0.0",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aplicación oficial para la gestión de torneos y equipos de la Shark League. Administra tus equipos, registra marcadores y sigue la tabla de posiciones en tiempo real.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "© 2024 Shark League Team",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
