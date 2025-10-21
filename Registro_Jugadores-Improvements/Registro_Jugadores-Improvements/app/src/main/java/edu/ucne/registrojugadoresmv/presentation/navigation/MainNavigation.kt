package edu.ucne.registrojugadoresmv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrojugadoresmv.presentation.home.HomeScreen
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorScreen.JugadorScreen
import edu.ucne.registrojugadoresmv.presentation.partida.partidaScreen.PartidaListScreen
import edu.ucne.registrojugadoresmv.presentation.partida.partidaScreen.PartidaFormScreen
import edu.ucne.registrojugadoresmv.presentation.game.TicTacToeGameScreen
import edu.ucne.registrojugadoresmv.presentation.logros.logroScreen.LogroScreen
import edu.ucne.registrojugadoresmv.presentation.logros.logroViewModel.LogroViewModel


@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        // Home
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

        // Jugadores
        composable<Screen.JugadorList> {
            val viewModel: edu.ucne.registrojugadoresmv.presentation.jugador.jugadorViewModel.JugadorViewModel = hiltViewModel()
            JugadorScreen(viewModel = viewModel)
        }

        // Lista de Partidas
        composable<Screen.PartidaList> {
            PartidaListScreen(
                onNavigateToCreatePartida = {
                    navController.navigate(Screen.PartidaForm)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Partidas - Crear
        composable<Screen.PartidaForm> {
            PartidaFormScreen(
                onNavigateToGame = { jugador1Id, jugador2Id ->
                    navController.navigate(
                        Screen.TicTacToeGame(
                            jugador1Id = jugador1Id,
                            jugador2Id = jugador2Id
                        )
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Juego TicTacToe
        composable<Screen.TicTacToeGame> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TicTacToeGame>()

            TicTacToeGameScreen(
                jugador1Id = args.jugador1Id,
                jugador2Id = args.jugador2Id,
                onGameFinished = { ganadorId ->
                    // viewModel.savePartida(jugador1Id, jugador2Id, ganadorId)
                },
                onNavigateBack = {
                    navController.popBackStack(Screen.PartidaList, inclusive = false)
                }
            )
        }

        // Logros
        composable<Screen.LogroList> {
            val viewModel = hiltViewModel<LogroViewModel>()
            LogroScreen(viewModel = viewModel)
        }
    }
}