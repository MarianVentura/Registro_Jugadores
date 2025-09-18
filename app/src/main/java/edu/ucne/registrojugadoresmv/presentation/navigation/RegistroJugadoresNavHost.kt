package edu.ucne.registrojugadoresmv.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrojugadoresmv.presentation.jugador.JugadorScreen
import edu.ucne.registrojugadoresmv.presentation.partida.PartidaScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroJugadoresNavHost(
    navController: NavHostController
) {
    // Observa la entrada actual del back stack para determinar el item seleccionado
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route

    // Helper: compara la ruta actual con la qualifiedName de la clase de Screen.
    // En la API type-safe la "route" suele contener el qualifiedName del tipo.
    fun isCurrent(screenQualifiedName: String?): Boolean {
        return currentRoute?.startsWith(screenQualifiedName ?: "") == true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                // Jugadores
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Jugadores") },
                    label = { Text("Jugadores") },
                    selected = isCurrent(Screen.JugadorList::class.qualifiedName),
                    onClick = {
                        // Navegación type-safe: pasar la instancia del Screen
                        navController.navigate(Screen.JugadorList) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                // Partidas
                NavigationBarItem(
                    icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Partidas") },
                    label = { Text("Partidas") },
                    selected = isCurrent(Screen.PartidaList::class.qualifiedName),
                    onClick = {
                        navController.navigate(Screen.PartidaList) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.JugadorList,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pantalla lista de jugadores (sin args)
            composable<Screen.JugadorList> {
                JugadorScreen()
            }

            // Pantalla lista de partidas (sin args)
            composable<Screen.PartidaList> {
                PartidaScreen()
            }

            // Formulario Jugador con argumento (type-safe)
            composable<Screen.JugadorForm> { backStackEntry: NavBackStackEntry ->
                // Reconstruye el objeto Screen.JugadorForm y lee el jugadorId
                // Requiere navigation-compose >= 2.8.0
                val route = backStackEntry.toRoute<Screen.JugadorForm>()
                val jugadorId = route.jugadorId
                // Usa jugadorId dentro de tu pantalla si tu JugadorScreen lo soporta.
                // Si no acepta argumentos, simplemente llama a JugadorScreen()
                // Ejemplo (si JugadorScreen acepta un jugadorId opcional):
                // JugadorScreen(jugadorId = jugadorId)
                JugadorScreen()
            }

            // Formulario Partida con argumento (type-safe)
            composable<Screen.PartidaForm> { backStackEntry: NavBackStackEntry ->
                val route = backStackEntry.toRoute<Screen.PartidaForm>()
                val partidaId = route.partidaId
                // PartidaScreen(partidaId = partidaId)  // si tu pantalla acepta arg
                PartidaScreen()
            }
        }
    }
}
