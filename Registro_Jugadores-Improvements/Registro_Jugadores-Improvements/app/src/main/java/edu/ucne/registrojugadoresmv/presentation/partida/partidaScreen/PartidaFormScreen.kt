package edu.ucne.registrojugadoresmv.presentation.partida.partidaScreen

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import edu.ucne.registrojugadoresmv.presentation.partida.partidaViewModel.PartidaViewModel
import edu.ucne.registrojugadoresmv.presentation.partida.partidaEvent.PartidaEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaFormScreen(
    viewModel: PartidaViewModel = hiltViewModel(),
    onNavigateToGame: (jugador1Id: Int, jugador2Id: Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear Partida",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Selecciona los Jugadores",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF6200EE)
                    )

                    Text(
                        text = "Elige los dos jugadores que competirán en esta partida de Tic-Tac-Toe",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666)
                    )

                    HorizontalDivider()

                    // Dropdown Jugador 1
                    var expandedJugador1 by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedJugador1,
                        onExpandedChange = { expandedJugador1 = !expandedJugador1 }
                    ) {
                        OutlinedTextField(
                            value = state.jugadores.find { it.jugadorId == state.jugador1Id }?.nombres
                                ?: "Selecciona un jugador",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jugador 1") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF6200EE)
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJugador1)
                            },
                            modifier = Modifier.menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            ),
                            shape = RoundedCornerShape(12.dp),
                            isError = state.jugador1Error != null,
                            supportingText = {
                                state.jugador1Error?.let {
                                    Text(
                                        text = it,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedJugador1,
                            onDismissRequest = { expandedJugador1 = false }
                        ) {
                            if (state.jugadores.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay jugadores registrados") },
                                    onClick = {},
                                    enabled = false
                                )
                            } else {
                                state.jugadores.forEach { jugador ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(jugador.nombres)
                                                Text(
                                                    text = "Partidas: ${jugador.partidas}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.Gray
                                                )
                                            }
                                        },
                                        onClick = {
                                            viewModel.onEvent(PartidaEvent.Jugador1Changed(jugador.jugadorId))
                                            expandedJugador1 = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Dropdown Jugador 2
                    var expandedJugador2 by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedJugador2,
                        onExpandedChange = { expandedJugador2 = !expandedJugador2 }
                    ) {
                        OutlinedTextField(
                            value = state.jugadores.find { it.jugadorId == state.jugador2Id }?.nombres
                                ?: "Selecciona un jugador",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jugador 2") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF6200EE)
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJugador2)
                            },
                            modifier = Modifier.menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            ),
                            shape = RoundedCornerShape(12.dp),
                            isError = state.jugador2Error != null,
                            supportingText = {
                                state.jugador2Error?.let {
                                    Text(
                                        text = it,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedJugador2,
                            onDismissRequest = { expandedJugador2 = false }
                        ) {
                            if (state.jugadores.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay jugadores registrados") },
                                    onClick = {},
                                    enabled = false
                                )
                            } else {
                                state.jugadores.forEach { jugador ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(jugador.nombres)
                                                Text(
                                                    text = "Partidas: ${jugador.partidas}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.Gray
                                                )
                                            }
                                        },
                                        onClick = {
                                            viewModel.onEvent(PartidaEvent.Jugador2Changed(jugador.jugadorId))
                                            expandedJugador2 = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Botón Iniciar Partida
            Button(
                onClick = {
                    if (state.jugador1Id != null && state.jugador2Id != null) {
                        if (state.jugador1Id == state.jugador2Id) {
                            // Mostrar error
                        } else {
                            onNavigateToGame(state.jugador1Id!!, state.jugador2Id!!)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.jugador1Id != null && state.jugador2Id != null &&
                        state.jugador1Id != state.jugador2Id,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Iniciar Partida",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Info Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF6200EE).copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF6200EE)
                    )
                    Text(
                        text = "Ambos jugadores deben estar registrados antes de iniciar la partida",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2C2C2C)
                    )
                }
            }
        }
    }
}

// ==================== PREVIEW ====================

@Preview(showSystemUi = true)
@Composable
fun PartidaFormScreenPreview() {
    MaterialTheme {
        PartidaFormScreen(
            onNavigateToGame = { _, _ -> },
            onNavigateBack = {}
        )
    }
}