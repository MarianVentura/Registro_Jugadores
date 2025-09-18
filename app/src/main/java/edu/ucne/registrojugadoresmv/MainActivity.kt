package edu.ucne.registrojugadoresmv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.ucne.registrojugadoresmv.data.local.database.AppDatabase
import edu.ucne.registrojugadoresmv.data.repository.JugadorRepositoryImpl
import edu.ucne.registrojugadoresmv.domain.usecase.GetJugadoresUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.InsertJugadorUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.ValidateJugadorUseCase
import edu.ucne.registrojugadoresmv.presentation.jugador.JugadorScreen
import edu.ucne.registrojugadoresmv.presentation.jugador.JugadorViewModel.JugadorViewModel
import edu.ucne.registrojugadoresmv.ui.theme.Registro_JugadoresTheme

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var repository: JugadorRepositoryImpl
    private lateinit var jugadorViewModel: JugadorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        database = AppDatabase.getDatabase(applicationContext)


        repository = JugadorRepositoryImpl(database.jugadorDao())


        val getJugadoresUseCase = GetJugadoresUseCase(repository)
        val insertJugadorUseCase = InsertJugadorUseCase(repository)
        val validateJugadorUseCase = ValidateJugadorUseCase(repository)


        jugadorViewModel = JugadorViewModel(
            getJugadoresUseCase,
            insertJugadorUseCase,
            validateJugadorUseCase
        )

        setContent {
            Registro_JugadoresTheme {
                JugadorScreen(viewModel = jugadorViewModel)
            }
        }
    }
}
