package edu.ucne.registrojugadoresmv.presentation.jugador.JugadorViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.usecase.GetJugadoresUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.InsertJugadorUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.ValidateJugadorUseCase
import edu.ucne.registrojugadoresmv.presentation.jugador.JugadorEvent
import edu.ucne.registrojugadoresmv.presentation.jugador.JugadorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class JugadorViewModel(
    private val getJugadoresUseCase: GetJugadoresUseCase,
    private val insertJugadorUseCase: InsertJugadorUseCase,
    private val validateJugadorUseCase: ValidateJugadorUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(JugadorUiState())
    val uiState: StateFlow<JugadorUiState> = _uiState.asStateFlow()

    init {
        getJugadores()
    }

    fun onEvent(event: JugadorEvent) {
        when (event) {
            is JugadorEvent.NombresChanged -> {
                _uiState.value = _uiState.value.copy(
                    nombres = event.nombres,
                    nombresError = null,
                    errorMessage = null,
                    successMessage = null // LIMPIAR MENSAJE DE ÉXITO
                )
            }
            is JugadorEvent.PartidasChanged -> {
                _uiState.value = _uiState.value.copy(
                    partidas = event.partidas,
                    partidasError = null,
                    errorMessage = null,
                    successMessage = null // LIMPIAR MENSAJE DE ÉXITO
                )
            }
            is JugadorEvent.SaveJugador -> {
                saveJugador()
            }
            is JugadorEvent.ClearForm -> {
                _uiState.value = JugadorUiState(jugadores = _uiState.value.jugadores)
            }
            else -> {}
        }
    }

    private fun saveJugador() {
        viewModelScope.launch {
            // Validaciones
            val nombresError =
                validateJugadorUseCase.validateNombre(_uiState.value.nombres)
            val partidasError =
                validateJugadorUseCase.validatePartidas(_uiState.value.partidas)

            if (nombresError != null || partidasError != null) {
                _uiState.value = _uiState.value.copy(
                    nombresError = nombresError,
                    partidasError = partidasError
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true)

            val jugador = Jugador(
                jugadorId = _uiState.value.jugadorId,
                nombres = _uiState.value.nombres.trim(),
                partidas = _uiState.value.partidas.toInt()
            )

            insertJugadorUseCase(jugador)
                .onSuccess {
                    _uiState.value = JugadorUiState(
                        jugadores = _uiState.value.jugadores,
                        successMessage = "Jugador guardado exitosamente",
                        success = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error desconocido"
                    )
                }
        }
    }

    private fun getJugadores() {
        getJugadoresUseCase()
            .onEach { jugadores ->
                _uiState.value = _uiState.value.copy(jugadores = jugadores)
            }
            .launchIn(viewModelScope)
    }
}

// Factory para crear el ViewModel
class JugadorViewModelFactory(
    private val getJugadoresUseCase: GetJugadoresUseCase,
    private val insertJugadorUseCase: InsertJugadorUseCase,
    private val validateJugadorUseCase: ValidateJugadorUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JugadorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JugadorViewModel(
                getJugadoresUseCase,
                insertJugadorUseCase,
                validateJugadorUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}