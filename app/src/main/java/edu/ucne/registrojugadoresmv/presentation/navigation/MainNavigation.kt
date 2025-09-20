package edu.ucne.registrojugadoresmv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registrojugadoresmv.presentation.home.HomeScreen
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorScreen.JugadorScreen
import edu.ucne.registrojugadoresmv.presentation.partida.partidaScreen.PartidaScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Jugadores : Screen("jugadores")
    object Partidas : Screen("partidas")
}

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToJugadores = {
                    navController.navigate(Screen.Jugadores.route)
                },
                onNavigateToPartidas = {
                    navController.navigate(Screen.Partidas.route)
                }
            )
        }

        composable(Screen.Jugadores.route) {
            JugadorScreen()
        }

        composable(Screen.Partidas.route) {
            PartidaScreen()
        }
    }
}