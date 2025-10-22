package edu.ucne.registrojugadoresmv.presentation.partida.partidaViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.usecase.*
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.DeletePartidaUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.ObservePartidaUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.GetPartidaUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.UpsertPartidaUseCase
import edu.ucne.registrojugadoresmv.presentation.partida.partidaUiState.PartidaUiState
import edu.ucne.registrojugadoresmv.presentation.partida.partidaEvent.PartidaEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PartidaViewModel @Inject constructor(
    private val observePartidaUseCase: ObservePartidaUseCase,
    private val getPartidaUseCase: GetPartidaUseCase,
    private val upsertPartidaUseCase: UpsertPartidaUseCase,
    private val deletePartidaUseCase: DeletePartidaUseCase,
    private val getJugadoresUseCase: GetJugadoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PartidaUiState())
    val uiState: StateFlow<PartidaUiState> = _uiState.asStateFlow()

    init {
        loadPartidas()
        loadJugadores()
    }

    fun onEvent(event: PartidaEvent) {
        when (event) {
            is PartidaEvent.Jugador1Changed -> {
                _uiState.value = _uiState.value.copy(
                    jugador1Id = event.jugadorId,
                    jugador1Error = null,
                    errorMessage = null,
                    successMessage = null
                )
            }
            is PartidaEvent.Jugador2Changed -> {
                _uiState.value = _uiState.value.copy(
                    jugador2Id = event.jugadorId,
                    jugador2Error = null,
                    errorMessage = null,
                    successMessage = null
                )
            }
            is PartidaEvent.GanadorChanged -> {
                _uiState.value = _uiState.value.copy(
                    ganadorId = event.ganadorId,
                    errorMessage = null,
                    successMessage = null
                )
            }
            is PartidaEvent.EsFinalizadaChanged -> {
                _uiState.value = _uiState.value.copy(
                    esFinalizada = event.esFinalizada,
                    ganadorId = if (!event.esFinalizada) null else _uiState.value.ganadorId,
                    errorMessage = null,
                    successMessage = null
                )
            }
            PartidaEvent.SavePartida -> savePartida()
            PartidaEvent.ClearForm -> clearForm()
            is PartidaEvent.EditPartida -> editPartida(event.partida)
            is PartidaEvent.DeletePartida -> deletePartida(event.partidaId)
            is PartidaEvent.SelectPartida -> selectPartida(event.partidaId)
            is PartidaEvent.ConfirmDeletePartida -> deletePartida(event.partida.partidaId)
        }
    }

    private fun loadPartidas() {
        viewModelScope.launch {
            observePartidaUseCase().collect { partidas ->
                _uiState.value = _uiState.value.copy(partidas = partidas)
            }
        }
    }

    private fun loadJugadores() {
        viewModelScope.launch {
            getJugadoresUseCase().collect { jugadores ->
                _uiState.value = _uiState.value.copy(jugadores = jugadores)
            }
        }
    }

    private fun savePartida() {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.jugador1Id == null || state.jugador1Id <= 0) {
                _uiState.value = state.copy(jugador1Error = "Debe seleccionar el jugador 1")
                return@launch
            }

            if (state.jugador2Id == null || state.jugador2Id <= 0) {
                _uiState.value = state.copy(jugador2Error = "Debe seleccionar el jugador 2")
                return@launch
            }

            if (state.jugador1Id == state.jugador2Id) {
                _uiState.value = state.copy(
                    errorMessage = "Los jugadores deben ser diferentes"
                )
                return@launch
            }

            _uiState.value = state.copy(isLoading = true)

            val partida = Partida(
                partidaId = state.selectedPartidaId ?: 0,
                fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                jugador1Id = state.jugador1Id,
                jugador2Id = state.jugador2Id,
                ganadorId = state.ganadorId,
                esFinalizada = state.esFinalizada
            )

            upsertPartidaUseCase(partida)
                .onSuccess {
                    val message = if (state.selectedPartidaId != null) {
                        "Partida actualizada exitosamente"
                    } else {
                        "Partida guardada exitosamente"
                    }

                    _uiState.value = PartidaUiState(
                        partidas = state.partidas,
                        jugadores = state.jugadores,
                        successMessage = message
                    )
                }
                .onFailure { exception ->
                    _uiState.value = state.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error desconocido"
                    )
                }
        }
    }

    private fun editPartida(partida: Partida) {
        _uiState.value = _uiState.value.copy(
            selectedPartidaId = partida.partidaId,
            jugador1Id = partida.jugador1Id,
            jugador2Id = partida.jugador2Id,
            ganadorId = partida.ganadorId,
            esFinalizada = partida.esFinalizada,
            jugador1Error = null,
            jugador2Error = null,
            errorMessage = null,
            successMessage = null
        )
    }

    private fun selectPartida(partidaId: Int) {
        viewModelScope.launch {
            val partida = getPartidaUseCase(partidaId)
            partida?.let {
                editPartida(it)
            }
        }
    }

    private fun deletePartida(partidaId: Int) {
        viewModelScope.launch {
            try {
                deletePartidaUseCase(partidaId)
                _uiState.value = _uiState.value.copy(
                    successMessage = "Partida eliminada exitosamente",
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar partida: ${e.message}",
                    successMessage = null
                )
            }
        }
    }

    private fun clearForm() {
        _uiState.value = _uiState.value.copy(
            selectedPartidaId = null,
            jugador1Id = null,
            jugador2Id = null,
            ganadorId = null,
            esFinalizada = false,
            jugador1Error = null,
            jugador2Error = null,
            errorMessage = null,
            successMessage = null
        )
    }
}

class PartidaViewModelFactory(
    private val observePartidaUseCase: ObservePartidaUseCase,
    private val getPartidaUseCase: GetPartidaUseCase,
    private val upsertPartidaUseCase: UpsertPartidaUseCase,
    private val deletePartidaUseCase: DeletePartidaUseCase,
    private val getJugadoresUseCase: GetJugadoresUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartidaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PartidaViewModel(
                observePartidaUseCase,
                getPartidaUseCase,
                upsertPartidaUseCase,
                deletePartidaUseCase,
                getJugadoresUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}