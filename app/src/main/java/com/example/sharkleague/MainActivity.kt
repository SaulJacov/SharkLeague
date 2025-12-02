package com.example.sharkleague

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sharkleague.ui.theme.SharkLeagueTheme
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.Groups


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharkLeagueTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(2) } // 2 = Home por default

    Scaffold(
        topBar = { TopLogoBar() },
        bottomBar = {
            BottomNavigationBar(
                selected = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        HomeContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun TopLogoBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(android.R.drawable.ic_menu_gallery),
            contentDescription = "Logo temporal",
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun BottomNavigationBar(selected: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == 0,
            onClick = { onTabSelected(0) },
            label = { Text("General") },
            icon = { Icon(Icons.Outlined.Info, contentDescription = "General") }
        )
        NavigationBarItem(
            selected = selected == 1,
            onClick = { onTabSelected(1) },
            label = { Text("Partidos") },
            icon = { Icon(Icons.Outlined.SportsSoccer, contentDescription = "Partidos") }
        )
        NavigationBarItem(
            selected = selected == 2,
            onClick = { onTabSelected(2) },
            label = { Text("Home") },
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = selected == 3,
            onClick = { onTabSelected(3) },
            label = { Text("Equipos") },
            icon = { Icon(Icons.Outlined.Groups, contentDescription = "Equipos") }
        )
        NavigationBarItem(
            selected = selected == 4,
            onClick = { onTabSelected(4) },
            label = { Text("Perfil") },
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") }
        )
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val proximos = listOf("Shark FC vs Tigres 🔵🟡 – 18 Feb, 4:00 PM")
    val recientes = listOf("Shark FC 3 - 1 Dragones 🦈🔥 – 10 Feb")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Partidos próximos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(proximos) { partido ->
            Text(
                text = partido,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Text(
                text = "Partidos recientes",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(recientes) { partido ->
            Text(
                text = partido,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    SharkLeagueTheme {
        MainScreen()
    }
}
