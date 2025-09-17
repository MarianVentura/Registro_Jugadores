package edu.ucne.registrojugadoresmv.presentation.partida

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PartidaViewModel @Inject constructor(
    private val getPartidasUseCase: GetPartidasUseCase,
    private val insertPartidaUseCase: InsertPartidaUseCase,
    private val updatePartidaUseCase: UpdatePartidaUseCase,
    private val deletePartidaUseCase: DeletePartidaUseCase,
    private val getPartidaByIdUseCase: GetPartidaByIdUseCase,
    private val validatePartidaUseCase: ValidatePartidaUseCase,
    private val getJugadoresUseCase: GetJugadoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PartidaUiState())
    val uiState: StateFlow<PartidaUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: PartidaEvent) {
        when (event) {
            is PartidaEvent.FechaChanged -> {
                updateUiState { currentState ->
                    currentState.copy(
                        fecha = event.fecha,
                        fechaError = null,
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }

            is PartidaEvent.Jugador1Changed -> {
                updateUiState { currentState ->
                    currentState.copy(
                        jugador1Id = event.jugador1Id,
                        jugadoresError = null,
                        ganadorId = if (currentState.ganadorId == event.jugador1Id) null else currentState.ganadorId,
                        errorMessage = null,
                        successMessage = null,
                        showJugador1Dropdown = false
                    )
                }
            }

            is PartidaEvent.Jugador2Changed -> {
                updateUiState { currentState ->
                    currentState.copy(
                        jugador2Id = event.jugador2Id,
                        jugadoresError = null,
                        ganadorId = if (currentState.ganadorId == event.jugador2Id) null else currentState.ganadorId,
                        errorMessage = null,
                        successMessage = null,
                        showJugador2Dropdown = false
                    )
                }
            }

            is PartidaEvent.GanadorChanged -> {
                updateUiState { currentState ->
                    currentState.copy(
                        ganadorId = event.ganadorId,
                        ganadorError = null,
                        errorMessage = null,
                        successMessage = null,
                        showGanadorDropdown = false
                    )
                }
            }

            is PartidaEvent.EsFinalizadaChanged -> {
                updateUiState { currentState ->
                    currentState.copy(
                        esFinalizada = event.esFinalizada,
                        ganadorId = if (!event.esFinalizada) null else currentState.ganadorId,
                        ganadorError = null,
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }

            is PartidaEvent.SavePartida -> {
                savePartida()
            }

            is PartidaEvent.ClearForm -> {
                clearForm()
            }

            is PartidaEvent.SelectPartida -> {
                selectPartida(event.partidaId)
            }

            is PartidaEvent.EditPartida -> {
                editPartida(event.partida)
            }

            is PartidaEvent.DeletePartida -> {
                updateUiState { it.copy(showDeleteDialog = true) }
            }

            is PartidaEvent.ConfirmDeletePartida -> {
                deletePartida(event.partida)
            }

            is PartidaEvent.DismissDeleteDialog -> {
                updateUiState { it.copy(showDeleteDialog = false) }
            }

            is PartidaEvent.LoadJugadores -> {
                loadJugadores()
            }

            else -> {}
        }
    }

    private fun updateUiState(update: (PartidaUiState) -> PartidaUiState) {
        _uiState.update(update)
    }

    private fun loadData() {
        loadPartidas()
        loadJugadores()
        // Establecer fecha actual por defecto
        val fechaActual = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        updateUiState { it.copy(fecha = fechaActual) }
    }

    private fun loadPartidas() {
        getPartidasUseCase()
            .onEach { partidas ->
                updateUiState { state ->
                    state.copy(
                        partidas = partidas,
                        isLoading = false
                    )
                }
            }
            .catch { exception ->
                updateUiState { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar partidas: ${exception.message}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadJugadores() {
        getJugadoresUseCase()
            .onEach { jugadores ->
                updateUiState { state ->
                    state.copy(jugadores = jugadores)
                }
            }
            .catch { exception ->
                updateUiState { state ->
                    state.copy(
                        errorMessage = "Error al cargar jugadores: ${exception.message}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun savePartida() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // Validaciones
            val fechaError = validatePartidaUseCase.validateFecha(currentState.fecha)
            val jugadoresError = validatePartidaUseCase.validateJugadores(
                currentState.jugador1Id,
                currentState.jugador2Id
            )
            val ganadorError = validatePartidaUseCase.validateGanador(
                currentState.ganadorId,
                currentState.esFinalizada,
                currentState.jugador1Id,
                currentState.jugador2Id
            )

            if (fechaError != null || jugadoresError != null || ganadorError != null) {
                updateUiState { state ->
                    state.copy(
                        fechaError = fechaError,
                        jugadoresError = jugadoresError,
                        ganadorError = ganadorError,
                        isLoading = false
                    )
                }
                return@launch
            }

            updateUiState { state -> state.copy(isLoading = true) }

            val partida = Partida(
                partidaId = currentState.partidaId,
                fecha = currentState.fecha,
                jugador1Id = currentState.jugador1Id,
                jugador2Id = currentState.jugador2Id,
                ganadorId = currentState.ganadorId,
                esFinalizada = currentState.esFinalizada
            )

            val result = if (currentState.isEditing) {
                updatePartidaUseCase(partida)
            } else {
                insertPartidaUseCase(partida)
            }

            result
                .onSuccess {
                    clearForm()
                    updateUiState { state ->
                        state.copy(
                            successMessage = if (currentState.isEditing)
                                "Partida actualizada exitosamente"
                            else
                                "Partida guardada exitosamente",
                            isLoading = false
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

    private fun selectPartida(partidaId: Int) {
        viewModelScope.launch {
            val partida = getPartidaByIdUseCase(partidaId)
            if (partida != null) {
                updateUiState { state ->
                    state.copy(
                        partidaId = partida.partidaId,
                        fecha = partida.fecha,
                        jugador1Id = partida.jugador1Id,
                        jugador2Id = partida.jugador2Id,
                        ganadorId = partida.ganadorId,
                        esFinalizada = partida.esFinalizada,
                        isEditing = true,
                        fechaError = null,
                        jugadoresError = null,
                        ganadorError = null,
                        errorMessage = null,
                        successMessage = null
                    )
                }
            }
        }
    }

    private fun editPartida(partida: Partida) {
        updateUiState { state ->
            state.copy(
                partidaId = partida.partidaId,
                fecha = partida.fecha,
                jugador1Id = partida.jugador1Id,
                jugador2Id = partida.jugador2Id,
                ganadorId = partida.ganadorId,
                esFinalizada = partida.esFinalizada,
                isEditing = true,
                fechaError = null,
                jugadoresError = null,
                ganadorError = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun deletePartida(partida: Partida) {
        viewModelScope.launch {
            updateUiState { state -> state.copy(isLoading = true, showDeleteDialog = false) }

            deletePartidaUseCase(partida)
                .onSuccess {
                    updateUiState { state ->
                        state.copy(
                            isLoading = false,
                            successMessage = "Partida eliminada exitosamente"
                        )
                    }
                }
                .onFailure { exception ->
                    updateUiState { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "Error al eliminar partida: ${exception.message}"
                        )
                    }
                }
        }
    }

    private fun clearForm() {
        val fechaActual = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        updateUiState { state ->
            PartidaUiState(
                partidas = state.partidas,
                jugadores = state.jugadores,
                fecha = fechaActual
            )
        }
    }
}