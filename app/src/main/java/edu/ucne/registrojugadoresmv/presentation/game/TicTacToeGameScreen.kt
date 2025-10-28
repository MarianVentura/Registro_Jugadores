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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrojugadoresmv.presentation.partida.partidaViewModel.PartidaViewModel
import edu.ucne.registrojugadoresmv.presentation.partida.partidaEvent.PartidaEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeGameScreen(
    jugador1Id: Int,
    jugador2Id: Int,
    jugador1Nombre: String = "Jugador 1",
    jugador2Nombre: String = "Jugador 2",
    onGameFinished: (ganadorId: Int?) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PartidaViewModel = hiltViewModel(),
    ticTacToeViewModel: TicTacToeViewModel = hiltViewModel()
) {
    val state by ticTacToeViewModel.uiState.collectAsState()
    var partidaGuardada by remember { mutableStateOf(false) }

    val jugador1Symbol = "X"
    val jugador2Symbol = "O"

    LaunchedEffect(Unit) {
        viewModel.onEvent(PartidaEvent.Jugador1Changed(jugador1Id))
        viewModel.onEvent(PartidaEvent.Jugador2Changed(jugador2Id))
    }

    LaunchedEffect(state.gameOver) {
        if (state.gameOver && !partidaGuardada) {
            val ganadorId = when (state.winner) {
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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "ID de Partida",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = state.partidaIdText,
                            onValueChange = {
                                ticTacToeViewModel.onEvent(TicTacToeEvent.PartidaIdChanged(it))
                            },
                            modifier = Modifier.weight(1f),
                            label = { Text("Ej: 1") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        IconButton(
                            onClick = {
                                ticTacToeViewModel.onEvent(TicTacToeEvent.LoadMovimientos)
                            },
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF6200EE),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Recargar",
                                tint = Color.White
                            )
                        }
                    }

                    if (state.currentPartidaId != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Partida activa: #${state.currentPartidaId}",
                                fontSize = 14.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            if (state.isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF2196F3).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Cargando movimientos...",
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (state.successMessage != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = state.successMessage!!,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (state.errorMessage != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFF44336).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = state.errorMessage!!,
                        color = Color(0xFFC62828),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerInfoSimple(
                    nombre = jugador1Nombre,
                    simbolo = jugador1Symbol,
                    esTurno = state.currentPlayer == jugador1Symbol && !state.gameOver
                )

                Text(
                    text = "VS",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF6200EE)
                )

                PlayerInfoSimple(
                    nombre = jugador2Nombre,
                    simbolo = jugador2Symbol,
                    esTurno = state.currentPlayer == jugador2Symbol && !state.gameOver
                )
            }

            if (!state.gameOver && state.currentPartidaId != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF6200EE).copy(alpha = 0.1f),
                                    Color(0xFF03DAC6).copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Turno de ${if (state.currentPlayer == jugador1Symbol) jugador1Nombre else jugador2Nombre}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (row in 0..2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            GameCell(
                                value = state.board[index],
                                onClick = {
                                    ticTacToeViewModel.onEvent(TicTacToeEvent.MakeMove(index))
                                },
                                enabled = !state.gameOver && state.currentPartidaId != null
                            )
                        }
                    }
                }
            }

            if (state.gameOver) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (state.winner) {
                            "DRAW" -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (state.winner) {
                                "DRAW" -> Icons.Default.Handshake
                                else -> Icons.Default.EmojiEvents
                            },
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = when (state.winner) {
                                "DRAW" -> "¡Empate!"
                                jugador1Symbol -> "¡Ganó ${jugador1Nombre}!"
                                jugador2Symbol -> "¡Ganó ${jugador2Nombre}!"
                                else -> ""
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (state.gameOver) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            partidaGuardada = false
                            ticTacToeViewModel.onEvent(TicTacToeEvent.ResetGame)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reiniciar", fontWeight = FontWeight.Bold)
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
                        Text("Finalizar", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = {
                        ticTacToeViewModel.onEvent(TicTacToeEvent.ResetGame)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    ),
                    enabled = state.currentPartidaId != null
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reiniciar Juego", fontWeight = FontWeight.Bold)
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
                width = 3.dp,
                color = Color(0xFF6200EE).copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled && value.isEmpty()) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 52.sp,
            fontWeight = FontWeight.Black,
            color = when (value) {
                "X" -> Color(0xFF6200EE)
                "O" -> Color(0xFF03DAC6)
                else -> Color.Transparent
            }
        )
    }
}

@Composable
fun PlayerInfoSimple(
    nombre: String,
    simbolo: String,
    esTurno: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = simbolo,
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = if (simbolo == "X") Color(0xFF6200EE) else Color(0xFF03DAC6)
        )

        Text(
            text = nombre,
            fontSize = 16.sp,
            fontWeight = if (esTurno) FontWeight.Bold else FontWeight.Normal,
            color = if (esTurno) Color(0xFF6200EE) else Color.Gray,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        if (esTurno) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = Color(0xFF6200EE),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}