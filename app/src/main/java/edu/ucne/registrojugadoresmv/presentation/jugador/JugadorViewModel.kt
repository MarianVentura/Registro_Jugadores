package edu.ucne.registrojugadoresmv.presentation.jugador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.usecase.*
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
    private val updateJugadorUseCase: UpdateJugadorUseCase,
    private val deleteJugadorUseCase: DeleteJugadorUseCase,
    private val validateJugadorUseCase: ValidateJugadorUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase
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

            is JugadorEvent.EmailChanged -> {
                updateUiState { currentState ->
                    currentState.copy(
                        email = event.email,
                        emailError = null,
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

            is JugadorEvent.SyncJugadores -> {
                syncJugadores()
            }
        }
    }

    private fun updateUiState(update: (JugadorUiState) -> JugadorUiState) {
        _uiState.update(update)
    }

    private fun saveJugador() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val nombresError = validateJugadorUseCase.validateNombre(
                currentState.nombres,
                if (currentState.isEditing) currentState.selectedJugador?.id else null
            )
            val emailError = validateJugadorUseCase.validateEmail(currentState.email)

            if (nombresError != null || emailError != null) {
                updateUiState { state ->
                    state.copy(
                        nombresError = nombresError,
                        emailError = emailError,
                        isLoading = false
                    )
                }
                return@launch
            }

            updateUiState { state -> state.copy(isLoading = true) }

            val jugador = if (currentState.isEditing && currentState.selectedJugador != null) {
                currentState.selectedJugador.copy(
                    nombres = currentState.nombres.trim(),
                    email = currentState.email.trim(),
                    partidas = currentState.partidas.toIntOrNull() ?: 0
                )
            } else {
                Jugador(
                    nombres = currentState.nombres.trim(),
                    email = currentState.email.trim(),
                    partidas = currentState.partidas.toIntOrNull() ?: 0,
                    isPendingCreate = true
                )
            }

            val result = if (currentState.isEditing) {
                updateJugadorUseCase(jugador)
            } else {
                insertJugadorUseCase(jugador)
            }

            when (result) {
                is Resource.Success -> {
                    updateUiState { state ->
                        JugadorUiState(
                            jugadores = state.jugadores,
                            successMessage = if (currentState.isEditing)
                                "Jugador actualizado. Se sincronizará con el servidor."
                            else
                                "Jugador guardado. Se sincronizará con el servidor."
                        )
                    }

                    triggerSyncUseCase()

                    kotlinx.coroutines.delay(3000)
                    updateUiState { state -> state.copy(successMessage = null) }
                }
                is Resource.Error -> {
                    updateUiState { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Error desconocido"
                        )
                    }
                }
                is Resource.Loading -> {}
            }
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

    private fun deleteJugador(jugadorId: String) {
        viewModelScope.launch {
            try {
                updateUiState { state -> state.copy(isLoading = true) }

                val jugador = _uiState.value.jugadores.find { it.id == jugadorId }
                if (jugador != null) {
                    when (val result = deleteJugadorUseCase(jugador)) {
                        is Resource.Success -> {
                            updateUiState { state ->
                                state.copy(
                                    isLoading = false,
                                    successMessage = "Jugador eliminado exitosamente"
                                )
                            }

                            kotlinx.coroutines.delay(3000)
                            updateUiState { state -> state.copy(successMessage = null) }
                        }
                        is Resource.Error -> {
                            updateUiState { state ->
                                state.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: "Error al eliminar"
                                )
                            }
                        }
                        is Resource.Loading -> {}
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

    private fun selectJugador(jugadorId: String) {
        val jugador = _uiState.value.jugadores.find { it.id == jugadorId }
        if (jugador != null) {
            editJugador(jugador)
        }
    }

    private fun editJugador(jugador: Jugador) {
        updateUiState { state ->
            state.copy(
                selectedJugador = jugador,
                nombres = jugador.nombres,
                email = jugador.email,
                partidas = jugador.partidas.toString(),
                isEditing = true,
                nombresError = null,
                emailError = null,
                partidasError = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun confirmDeleteJugador(jugador: Jugador) {
        deleteJugador(jugador.id)
    }

    private fun syncJugadores() {
        triggerSyncUseCase()
        updateUiState { state ->
            state.copy(successMessage = "Sincronización iniciada")
        }

        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            updateUiState { state -> state.copy(successMessage = null) }
        }
    }
}