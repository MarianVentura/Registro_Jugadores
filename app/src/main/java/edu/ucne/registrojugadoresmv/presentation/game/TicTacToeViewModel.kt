package edu.ucne.registrojugadoresmv.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Movimiento
import edu.ucne.registrojugadoresmv.domain.usecase.movimientos.GetMovimientosUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.movimientos.SaveMovimientoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val getMovimientosUseCase: GetMovimientosUseCase,
    private val saveMovimientoUseCase: SaveMovimientoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicTacToeUiState())
    val uiState: StateFlow<TicTacToeUiState> = _uiState.asStateFlow()

    fun loadMovimientos(partidaId: Int) {
        viewModelScope.launch {
            getMovimientosUseCase(partidaId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            movimientos = resource.data ?: emptyList(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    fun saveMovimiento(partidaId: Int, jugador: String, fila: Int, columna: Int) {
        viewModelScope.launch {
            val movimiento = Movimiento(
                partidaId = partidaId,
                jugador = jugador,
                posicionFila = fila,
                posicionColumna = columna
            )

            when (val result = saveMovimientoUseCase(movimiento)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Movimiento guardado",
                        errorMessage = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message
                    )
                }
                else -> {}
            }
        }
    }
}

data class TicTacToeUiState(
    val movimientos: List<Movimiento> = emptyList(),
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
