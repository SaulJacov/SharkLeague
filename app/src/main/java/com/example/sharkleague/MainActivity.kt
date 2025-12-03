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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sharkleague.ui.theme.SharkLeagueTheme

// Sealed class for navigation routes
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object General : Screen("general", "General", Icons.Outlined.Info)
    object Partidos : Screen("partidos", "Partidos", Icons.Outlined.SportsSoccer)
    object Home : Screen("home", "Home", Icons.Outlined.Home)
    object Equipos : Screen("equipos", "Equipos", Icons.Outlined.Groups)
    object Perfil : Screen("perfil", "Perfil", Icons.Outlined.Person)
}

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
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopLogoBar() },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.General.route) { GeneralContent() }
            composable(Screen.Partidos.route) { PartidosContent() }
            composable(Screen.Home.route) { HomeContent() }
            composable(Screen.Equipos.route) { EquiposContent() }
            composable(Screen.Perfil.route) { PerfilContent() }
        }
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
            painter = painterResource(R.drawable.logo_sharkleague),
            contentDescription = "Logo temporal",
            modifier = Modifier.size(300.dp)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.General,
        Screen.Partidos,
        Screen.Home,
        Screen.Equipos,
        Screen.Perfil,
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val proximos = listOf("Chivas vs Tigres – 18 Feb, 4:00 PM")
    val recientes = listOf("Toluca 3 - 1 Dragones – 10 Feb")

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

// Placeholder Composables for other screens
@Composable
fun GeneralContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla General")
    }
}

@Composable
fun PartidosContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de Partidos")
    }
}

@Composable
fun EquiposContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de Equipos")
    }
}

@Composable
fun PerfilContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de Perfil")
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    SharkLeagueTheme {
        MainScreen()
    }
}