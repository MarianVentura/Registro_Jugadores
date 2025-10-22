package edu.ucne.registrojugadoresmv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorViewModel.JugadorViewModel


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
            val viewModel: JugadorViewModel = hiltViewModel()
            JugadorScreen(viewModel = viewModel)
        }

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

        composable<Screen.PartidaForm> {
            PartidaFormScreen(
                onNavigateToGame = { jugador1Id, jugador2Id, jugador1Nombre, jugador2Nombre ->
                    navController.navigate(
                        Screen.TicTacToeGame(
                            jugador1Id = jugador1Id,
                            jugador2Id = jugador2Id,
                            jugador1Nombre = jugador1Nombre,
                            jugador2Nombre = jugador2Nombre
                        )
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.TicTacToeGame> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TicTacToeGame>()

            TicTacToeGameScreen(
                jugador1Id = args.jugador1Id,
                jugador2Id = args.jugador2Id,
                jugador1Nombre = args.jugador1Nombre,
                jugador2Nombre = args.jugador2Nombre,
                onGameFinished = { },
                onNavigateBack = {
                    navController.popBackStack(Screen.PartidaList, inclusive = false)
                }
            )
        }

        composable<Screen.LogroList> {
            val viewModel = hiltViewModel<LogroViewModel>()
            LogroScreen(viewModel = viewModel)
        }
    }
}