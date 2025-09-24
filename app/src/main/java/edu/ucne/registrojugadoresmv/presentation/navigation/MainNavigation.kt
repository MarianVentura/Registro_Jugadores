package edu.ucne.registrojugadoresmv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registrojugadoresmv.presentation.home.HomeScreen
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorScreen.JugadorScreen
import edu.ucne.registrojugadoresmv.presentation.partida.partidaScreen.PartidaScreen
import edu.ucne.registrojugadoresmv.presentation.logros.logroScreen.LogroScreen
import edu.ucne.registrojugadoresmv.presentation.logros.logroViewModel.LogroViewModel

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToJugadores = {
                    navController.navigate(Screen.JugadorList)
                },
                onNavigateToPartidas = {
                    navController.navigate(Screen.PartidaList)
                },
                onNavigateToLogros = {
                    navController.navigate(Screen.LogroList)
                }
            )
        }

        composable<Screen.JugadorList> {
            val viewModel = hiltViewModel<edu.ucne.registrojugadoresmv.presentation.jugador.jugadorViewModel.JugadorViewModel>()
            JugadorScreen(viewModel = viewModel)
        }

        composable<Screen.PartidaList> {
            PartidaScreen()
        }

        composable<Screen.LogroList> {  // ← NUEVA RUTA AGREGADA
            val viewModel = hiltViewModel<LogroViewModel>()
            LogroScreen(viewModel = viewModel)
        }
    }
}