package edu.ucne.registrojugadoresmv.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.model.Movimiento
import edu.ucne.registrojugadoresmv.domain.usecase.movimientosUseCase.GetMovimientosUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.movimientosUseCase.SaveMovimientoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val getMovimientosUseCase: GetMovimientosUseCase,
    private val saveMovimientoUseCase: SaveMovimientoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicTacToeUiState())
    val uiState: StateFlow<TicTacToeUiState> = _uiState.asStateFlow()

    fun onEvent(event: TicTacToeEvent) {
        when (event) {
            is TicTacToeEvent.PartidaIdChanged -> {
                _uiState.update { it.copy(partidaIdText = event.partidaId) }
            }
            is TicTacToeEvent.LoadMovimientos -> {
                val id = _uiState.value.partidaIdText.toIntOrNull()
                if (id != null && id > 0) {
                    _uiState.update { it.copy(currentPartidaId = id) }
                    loadMovimientos(id)
                }
            }
            is TicTacToeEvent.MakeMove -> {
                makeMove(event.index)
            }
            is TicTacToeEvent.ResetGame -> {
                resetGame()
            }
            is TicTacToeEvent.ClearMessages -> {
                _uiState.update {
                    it.copy(successMessage = null, errorMessage = null)
                }
            }
        }
    }

    private fun checkWinner(board: List<String>): String? {
        val winPatterns = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for (pattern in winPatterns) {
            val (a, b, c) = pattern
            if (board[a].isNotEmpty() && board[a] == board[b] && board[b] == board[c]) {
                return board[a]
            }
        }

        if (board.all { it.isNotEmpty() }) {
            return "DRAW"
        }

        return null
    }

    private fun loadMovimientos(partidaId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getMovimientosUseCase(partidaId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        val movimientos = resource.data ?: emptyList()

                        val newBoard = MutableList(9) { "" }
                        var lastPlayer = "O"

                        movimientos.forEach { movimiento ->
                            val index = movimiento.posicionFila * 3 + movimiento.posicionColumna
                            if (index in 0..8) {
                                newBoard[index] = movimiento.jugador
                                lastPlayer = movimiento.jugador
                            }
                        }

                        val nextPlayer = if (lastPlayer == "X") "O" else "X"
                        val winner = checkWinner(newBoard)
                        val isGameOver = winner != null

                        _uiState.update {
                            it.copy(
                                movimientos = movimientos,
                                board = newBoard,
                                currentPlayer = nextPlayer,
                                winner = winner,
                                gameOver = isGameOver,
                                isLoading = false,
                                errorMessage = null,
                                successMessage = if (movimientos.isEmpty())
                                    "No hay movimientos para esta partida"
                                else
                                    "Movimientos cargados: ${movimientos.size}"
                            )
                        }

                        kotlinx.coroutines.delay(3000)
                        _uiState.update { it.copy(successMessage = null) }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message ?: "Error al cargar movimientos"
                            )
                        }

                        kotlinx.coroutines.delay(5000)
                        _uiState.update { it.copy(errorMessage = null) }
                    }
                }
            }
        }
    }

    private fun makeMove(index: Int) {
        val state = _uiState.value

        if (state.board[index].isEmpty() &&
            !state.gameOver &&
            state.currentPartidaId != null) {

            val fila = index / 3
            val columna = index % 3

            val newBoard = state.board.toMutableList().apply {
                this[index] = state.currentPlayer
            }

            _uiState.update { it.copy(board = newBoard) }

            saveMovimiento(
                partidaId = state.currentPartidaId,
                jugador = state.currentPlayer,
                fila = fila,
                columna = columna
            )

            val winner = checkWinner(newBoard)
            if (winner != null) {
                _uiState.update {
                    it.copy(
                        winner = winner,
                        gameOver = true
                    )
                }
            } else {
                val nextPlayer = if (state.currentPlayer == "X") "O" else "X"
                _uiState.update { it.copy(currentPlayer = nextPlayer) }
            }
        }
    }

    private fun saveMovimiento(partidaId: Int, jugador: String, fila: Int, columna: Int) {
        viewModelScope.launch {
            val movimiento = Movimiento(
                partidaId = partidaId,
                jugador = jugador,
                posicionFila = fila,
                posicionColumna = columna
            )

            when (val result = saveMovimientoUseCase(movimiento)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            successMessage = "Movimiento guardado en el servidor",
                            errorMessage = null
                        )
                    }

                    kotlinx.coroutines.delay(2000)
                    _uiState.update { it.copy(successMessage = null) }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error al guardar: ${result.message}",
                            successMessage = null
                        )
                    }

                    kotlinx.coroutines.delay(5000)
                    _uiState.update { it.copy(errorMessage = null) }
                }
                else -> {}
            }
        }
    }

    private fun resetGame() {
        val currentPartidaId = _uiState.value.currentPartidaId

        _uiState.update {
            it.copy(
                board = List(9) { "" },
                currentPlayer = "X",
                winner = null,
                gameOver = false
            )
        }

        currentPartidaId?.let { id ->
            loadMovimientos(id)
        }
    }
}