package edu.ucne.registrojugadoresmv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registrojugadoresmv.data.local.database.AppDatabase
import edu.ucne.registrojugadoresmv.data.repository.JugadorRepositoryImpl
import edu.ucne.registrojugadoresmv.domain.usecase.GetJugadoresUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.InsertJugadorUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.ValidateJugadorUseCase
import edu.ucne.registrojugadoresmv.presentation.home.HomeScreen
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorScreen.JugadorScreen
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorViewModel.JugadorViewModel
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorViewModel.JugadorViewModelFactory
import edu.ucne.registrojugadoresmv.presentation.partida.partidaScreen.PartidaScreen

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
                }
            )
        }

        composable<Screen.JugadorList> {
            val context = LocalContext.current
            val database = remember { AppDatabase.getDatabase(context) }
            val jugadorRepository = remember { JugadorRepositoryImpl(database.jugadorDao()) }
            val viewModel: JugadorViewModel = viewModel(
                factory = JugadorViewModelFactory(
                    GetJugadoresUseCase(jugadorRepository),
                    InsertJugadorUseCase(jugadorRepository),
                    ValidateJugadorUseCase(jugadorRepository)
                )
            )
            JugadorScreen(viewModel = viewModel)
        }

        composable<Screen.PartidaList> {
            PartidaScreen()
        }
    }
}