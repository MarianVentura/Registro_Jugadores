package edu.ucne.registrojugadoresmv.presentation.jugador.jugadorViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.usecase.GetJugadoresUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.InsertJugadorUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.ValidateJugadorUseCase
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorEvent.JugadorEvent
import edu.ucne.registrojugadoresmv.presentation.jugador.jugadorUiState.JugadorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadorViewModel @Inject constructor(
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
                updateUiState { currentState ->
                    currentState.copy(
                        nombres = event.nombres,
                        nombresError = null,
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }

            is JugadorEvent.PartidasChanged -> {
                updateUiState { currentState ->
                    currentState.copy(
                        partidas = event.partidas,
                        partidasError = null,
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }

            is JugadorEvent.SaveJugador -> {
                saveJugador()
            }

            is JugadorEvent.ClearForm -> {
                updateUiState { currentState ->
                    JugadorUiState(jugadores = currentState.jugadores)
                }
            }

            is JugadorEvent.DeleteJugador -> {
                deleteJugador(event.jugadorId)
            }

            is JugadorEvent.SelectJugador -> {
                selectJugador(event.jugadorId)
            }

            is JugadorEvent.EditJugador -> {
                editJugador(event.jugador)
            }

            is JugadorEvent.ConfirmDeleteJugador -> {
                confirmDeleteJugador(event.jugador)
            }
        }
    }

    private fun updateUiState(update: (JugadorUiState) -> JugadorUiState) {
        _uiState.update(update)
    }

    private fun saveJugador() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val nombresError = validateJugadorUseCase.validateNombre(currentState.nombres)
            val partidasError = validatePartidasImproved(currentState.partidas)

            if (nombresError != null || partidasError != null) {
                updateUiState { state ->
                    state.copy(
                        nombresError = nombresError,
                        partidasError = partidasError,
                        isLoading = false
                    )
                }
                return@launch
            }

            updateUiState { state -> state.copy(isLoading = true) }

            val jugador = Jugador(
                jugadorId = currentState.jugadorId,
                nombres = currentState.nombres.trim(),
                partidas = currentState.partidas.toInt()
            )

            insertJugadorUseCase(jugador)
                .onSuccess {
                    updateUiState { state ->
                        JugadorUiState(
                            jugadores = state.jugadores,
                            successMessage = "Jugador guardado exitosamente"
                        )
                    }
                }
                .onFailure { exception ->
                    updateUiState { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Error desconocido"
                        )
                    }
                }
        }
    }

    private fun validatePartidasImproved(partidas: String): String? {
        return when {
            partidas.isBlank() -> "Las partidas son obligatorias"
            partidas.toIntOrNull() == null -> "Las partidas deben ser un número válido"
            partidas.toInt() < 0 -> "Las partidas no pueden ser negativas"
            partidas.toInt() > 10000 -> "El número de partidas es demasiado alto (máximo 10,000)"
            else -> null
        }
    }

    private fun getJugadores() {
        getJugadoresUseCase()
            .onEach { jugadores ->
                updateUiState { state ->
                    state.copy(
                        jugadores = jugadores,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
            .catch { exception ->
                updateUiState { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar jugadores: ${exception.message}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun deleteJugador(jugadorId: Int) {
        viewModelScope.launch {
            try {
                updateUiState { state -> state.copy(isLoading = true) }

                val jugador = _uiState.value.jugadores.find { it.jugadorId == jugadorId }
                if (jugador != null) {
                    updateUiState { state ->
                        state.copy(
                            jugadores = state.jugadores.filter { it.jugadorId != jugadorId },
                            isLoading = false,
                            successMessage = "Jugador eliminado exitosamente"
                        )
                    }
                } else {
                    updateUiState { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "Jugador no encontrado"
                        )
                    }
                }
            } catch (exception: Exception) {
                updateUiState { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Error al eliminar jugador: ${exception.message}"
                    )
                }
            }
        }
    }

    private fun selectJugador(jugadorId: Int) {
        val jugador = _uiState.value.jugadores.find { it.jugadorId == jugadorId }
        if (jugador != null) {
            updateUiState { state ->
                state.copy(
                    jugadorId = jugador.jugadorId,
                    nombres = jugador.nombres,
                    partidas = jugador.partidas.toString(),
                    nombresError = null,
                    partidasError = null,
                    errorMessage = null,
                    successMessage = null
                )
            }
        }
    }

    private fun editJugador(jugador: Jugador) {
        updateUiState { state ->
            state.copy(
                jugadorId = jugador.jugadorId,
                nombres = jugador.nombres,
                partidas = jugador.partidas.toString(),
                nombresError = null,
                partidasError = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun confirmDeleteJugador(jugador: Jugador) {
        deleteJugador(jugador.jugadorId)
    }
}