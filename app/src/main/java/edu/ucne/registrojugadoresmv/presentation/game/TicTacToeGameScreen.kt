package edu.ucne.registrojugadoresmv.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrojugadoresmv.presentation.partida.partidaViewModel.PartidaViewModel
import edu.ucne.registrojugadoresmv.presentation.partida.partidaEvent.PartidaEvent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeGameScreen(
    jugador1Id: Int,
    jugador2Id: Int,
    jugador1Nombre: String = "Jugador 1",
    jugador2Nombre: String = "Jugador 2",
    onGameFinished: (ganadorId: Int?) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PartidaViewModel = hiltViewModel()
) {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var gameOver by remember { mutableStateOf(false) }
    var partidaGuardada by remember { mutableStateOf(false) }

    val jugador1Symbol = "X"
    val jugador2Symbol = "O"

    LaunchedEffect(Unit) {
        viewModel.onEvent(PartidaEvent.Jugador1Changed(jugador1Id))
        viewModel.onEvent(PartidaEvent.Jugador2Changed(jugador2Id))
    }

    fun checkWinner(): String? {
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

    fun makeMove(index: Int) {
        if (board[index].isEmpty() && !gameOver) {
            board = board.toMutableList().apply {
                this[index] = currentPlayer
            }

            val result = checkWinner()
            if (result != null) {
                winner = result
                gameOver = true

                val ganadorId = when (result) {
                    jugador1Symbol -> jugador1Id
                    jugador2Symbol -> jugador2Id
                    else -> 0
                }
                onGameFinished(ganadorId)
            } else {
                currentPlayer = if (currentPlayer == "X") "O" else "X"
            }
        }
    }

    fun resetGame() {
        board = List(9) { "" }
        currentPlayer = "X"
        winner = null
        gameOver = false
        partidaGuardada = false
    }

    LaunchedEffect(gameOver) {
        if (gameOver && !partidaGuardada) {
            val ganadorId = when (winner) {
                jugador1Symbol -> jugador1Id
                jugador2Symbol -> jugador2Id
                "DRAW" -> 0
                else -> null
            }

            viewModel.onEvent(PartidaEvent.GanadorChanged(ganadorId))
            viewModel.onEvent(PartidaEvent.EsFinalizadaChanged(true))
            viewModel.onEvent(PartidaEvent.SavePartida)

            partidaGuardada = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tic-Tac-Toe",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PlayerInfo(
                        nombre = jugador1Nombre,
                        simbolo = jugador1Symbol,
                        esTurno = currentPlayer == jugador1Symbol && !gameOver
                    )

                    Text(
                        text = "VS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE)
                    )

                    PlayerInfo(
                        nombre = jugador2Nombre,
                        simbolo = jugador2Symbol,
                        esTurno = currentPlayer == jugador2Symbol && !gameOver
                    )
                }
            }

            if (!gameOver) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6200EE).copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Turno de: ${if (currentPlayer == jugador1Symbol) jugador1Nombre else jugador2Nombre} ($currentPlayer)",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6200EE)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0..2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            GameCell(
                                value = board[index],
                                onClick = { makeMove(index) },
                                enabled = !gameOver
                            )
                        }
                    }
                }
            }

            if (gameOver) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (winner) {
                            "DRAW" -> Color(0xFFFF9800).copy(alpha = 0.2f)
                            else -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        }
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = when (winner) {
                                "DRAW" -> Icons.Default.Handshake
                                else -> Icons.Default.EmojiEvents
                            },
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = when (winner) {
                                "DRAW" -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = when (winner) {
                                "DRAW" -> "¡Empate!"
                                jugador1Symbol -> "¡${jugador1Nombre} Ganó!"
                                jugador2Symbol -> "¡${jugador2Nombre} Ganó!"
                                else -> ""
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (winner) {
                                "DRAW" -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (gameOver) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { resetGame() },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Jugar de Nuevo")
                    }

                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Finalizar")
                    }
                }
            } else {
                Button(
                    onClick = { resetGame() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reiniciar Juego")
                }
            }
        }
    }
}

@Composable
fun GameCell(
    value: String,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFF6200EE).copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled && value.isEmpty()) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = when (value) {
                "X" -> Color(0xFF6200EE)
                "O" -> Color(0xFF03DAC6)
                else -> Color.Transparent
            }
        )
    }
}

@Composable
fun PlayerInfo(
    nombre: String,
    simbolo: String,
    esTurno: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = simbolo,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (simbolo == "X") Color(0xFF6200EE) else Color(0xFF03DAC6)
        )

        Text(
            text = nombre,
            fontSize = 14.sp,
            fontWeight = if (esTurno) FontWeight.Bold else FontWeight.Normal,
            color = if (esTurno) Color(0xFF6200EE) else Color.Gray,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        if (esTurno) {
            Text(
                text = "• En juego •",
                fontSize = 12.sp,
                color = Color(0xFF6200EE)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TicTacToeGameScreenPreview() {
    MaterialTheme {
        TicTacToeGameScreen(
            jugador1Id = 1,
            jugador2Id = 2,
            jugador1Nombre = "Sabaku no Gaara",
            jugador2Nombre = "Uzumaki Naruto",
            onGameFinished = {},
            onNavigateBack = {}
        )
    }
}