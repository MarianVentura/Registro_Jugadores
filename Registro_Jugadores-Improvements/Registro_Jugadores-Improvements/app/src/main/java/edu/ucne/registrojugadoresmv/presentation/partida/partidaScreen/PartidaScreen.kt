package edu.ucne.registrojugadoresmv.presentation.partida.partidaScreen


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.ucne.registrojugadoresmv.data.local.database.AppDatabase
import edu.ucne.registrojugadoresmv.data.repository.PartidaRepositoryImpl
import edu.ucne.registrojugadoresmv.data.repository.JugadorRepositoryImpl
import edu.ucne.registrojugadoresmv.domain.model.Partida
import edu.ucne.registrojugadoresmv.domain.model.Jugador
import edu.ucne.registrojugadoresmv.domain.usecase.*
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.DeletePartidaUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.ObservePartidaUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.UpsertPartidaUseCase
import edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases.GetPartidaUseCase
import edu.ucne.registrojugadoresmv.presentation.partida.partidaUiState.PartidaUiState
import edu.ucne.registrojugadoresmv.presentation.partida.partidaViewModel.PartidaViewModel
import edu.ucne.registrojugadoresmv.presentation.partida.partidaViewModel.PartidaViewModelFactory
import edu.ucne.registrojugadoresmv.presentation.partida.partidaEvent.PartidaEvent
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.items as items
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaScreen() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val partidaRepository = remember { PartidaRepositoryImpl(database.partidaDao()) }
    val jugadorRepository = remember { JugadorRepositoryImpl(database.jugadorDao()) }
    val viewModel: PartidaViewModel = viewModel(
        factory = PartidaViewModelFactory(
            ObservePartidaUseCase(partidaRepository),
            GetPartidaUseCase(partidaRepository),
            UpsertPartidaUseCase(partidaRepository),
            DeletePartidaUseCase(partidaRepository),
            GetJugadoresUseCase(jugadorRepository)
        )
    )
    val state by viewModel.uiState.collectAsState()

    var showWelcome by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = showWelcome,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        WelcomeScreen(
            onStartRegistration = { showWelcome = false }
        )
    }

    AnimatedVisibility(
        visible = !showWelcome,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
    ) {
        MainPartidaScreen(
            state = state,
            viewModel = viewModel,
            onBackToWelcome = { showWelcome = true }
        )
    }
}

@Composable
fun WelcomeScreen(onStartRegistration: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6200EE),
                        Color(0xFF3700B3),
                        Color(0xFF03DAC6)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Icono grande
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF6200EE).copy(alpha = 0.2f),
                                    Color(0xFF6200EE).copy(alpha = 0.1f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF6200EE)
                    )
                }

                // Título
                Text(
                    text = "Partidas",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color(0xFF2C2C2C)
                )

                Text(
                    text = "Gestión de Partidas Tic-Tac-Toe",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción
                Text(
                    text = "Registra y administra todas las partidas del torneo. Lleva un control completo de los enfrentamientos y resultados.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF444444),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de inicio
                Button(
                    onClick = onStartRegistration,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Text(
                            text = "Gestionar Partidas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPartidaScreen(
    state: PartidaUiState,
    viewModel: PartidaViewModel,
    onBackToWelcome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Partidas",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToWelcome) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Formulario
            item {
                PartidaForm(
                    state = state,
                    viewModel = viewModel
                )
            }

            // Lista de partidas
            item {
                Text(
                    text = "Partidas Registradas (${state.partidas.size})",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (state.partidas.isEmpty()) {
                item {
                    EmptyPartidasCard()
                }
            } else {
                items(state.partidas) { partida ->
                    PartidaItem(
                        partida = partida,
                        jugadores = state.jugadores,
                        onEdit = {
                            viewModel.onEvent(PartidaEvent.EditPartida(partida))
                        },
                        onDelete = {
                            viewModel.onEvent(PartidaEvent.DeletePartida(partida.partidaId ?: 0))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaForm(
    state: PartidaUiState,
    viewModel: PartidaViewModel
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
                text = if (state.selectedPartidaId != null) "Editar Partida" else "Nueva Partida",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF6200EE)
            )

            // Dropdown para Jugador 1
            var expandedJugador1 by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedJugador1,
                onExpandedChange = { expandedJugador1 = !expandedJugador1 }
            ) {
                OutlinedTextField(
                    value = state.jugadores.find { it.jugadorId == state.jugador1Id }?.nombres ?: "",
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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJugador1) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedJugador1,
                    onDismissRequest = { expandedJugador1 = false }
                ) {
                    state.jugadores.forEach { jugador ->
                        DropdownMenuItem(
                            text = { Text(jugador.nombres) },
                            onClick = {
                                viewModel.onEvent(PartidaEvent.Jugador1Changed(jugador.jugadorId ?: 0))
                                expandedJugador1 = false
                            }
                        )
                    }
                }
            }

            // Dropdown para Jugador 2
            var expandedJugador2 by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedJugador2,
                onExpandedChange = { expandedJugador2 = !expandedJugador2 }
            ) {
                OutlinedTextField(
                    value = state.jugadores.find { it.jugadorId == state.jugador2Id }?.nombres ?: "",
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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJugador2) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedJugador2,
                    onDismissRequest = { expandedJugador2 = false }
                ) {
                    state.jugadores.forEach { jugador ->
                        DropdownMenuItem(
                            text = { Text(jugador.nombres) },
                            onClick = {
                                viewModel.onEvent(PartidaEvent.Jugador2Changed(jugador.jugadorId ?: 0))
                                expandedJugador2 = false
                            }
                        )
                    }
                }
            }

            // Dropdown para Ganador (opcional)
            var expandedGanador by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedGanador,
                onExpandedChange = { expandedGanador = !expandedGanador }
            ) {
                OutlinedTextField(
                    value = when (state.ganadorId) {
                        null -> "Sin ganador (en progreso)"
                        0 -> "Empate"
                        else -> state.jugadores.find { it.jugadorId == state.ganadorId }?.nombres ?: ""
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Resultado") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFF6200EE)
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGanador) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedGanador,
                    onDismissRequest = { expandedGanador = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Sin ganador (en progreso)") },
                        onClick = {
                            viewModel.onEvent(PartidaEvent.GanadorChanged(null))
                            expandedGanador = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Empate") },
                        onClick = {
                            viewModel.onEvent(PartidaEvent.GanadorChanged(0))
                            expandedGanador = false
                        }
                    )
                    listOf(state.jugador1Id, state.jugador2Id).forEach { jugadorId ->
                        if (jugadorId != null) {
                            val jugador = state.jugadores.find { it.jugadorId == jugadorId }
                            jugador?.let {
                                DropdownMenuItem(
                                    text = { Text("Ganador: ${it.nombres}") },
                                    onClick = {
                                        viewModel.onEvent(PartidaEvent.GanadorChanged(jugadorId))
                                        expandedGanador = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Switch para partida finalizada
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Partida finalizada",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = state.esFinalizada,
                    onCheckedChange = {
                        viewModel.onEvent(PartidaEvent.EsFinalizadaChanged(it))
                    }
                )
            }

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.onEvent(PartidaEvent.SavePartida) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                            Text(if (state.selectedPartidaId != null) "Actualizar" else "Guardar")
                        }
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.onEvent(PartidaEvent.ClearForm) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                        Text("Limpiar")
                    }
                }
            }

            // Mensajes
            AnimatedVisibility(
                visible = state.successMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                state.successMessage?.let { message ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                            )
                            Text(
                                text = message,
                                color = Color(0xFF2E7D32),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = state.errorMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                state.errorMessage?.let { message ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PartidaItem(
    partida: Partida,
    jugadores: List<Jugador>,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val jugador1 = jugadores.find { it.jugadorId == partida.jugador1Id }
    val jugador2 = jugadores.find { it.jugadorId == partida.jugador2Id }
    val ganador = jugadores.find { it.jugadorId == partida.ganadorId }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val fechaFormateada = dateFormatter.format(partida.fecha)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con fecha y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Partida #${partida.partidaId}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF2C2C2C)
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (partida.esFinalizada)
                            Color(0xFF4CAF50).copy(alpha = 0.2f)
                        else
                            Color(0xFFFF9800).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (partida.esFinalizada) "Finalizada" else "En progreso",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (partida.esFinalizada) Color(0xFF2E7D32) else Color(0xFFE65100)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = fechaFormateada,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Enfrentamiento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = jugador1?.nombres ?: "Jugador 1",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = " VS ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF6200EE)
                )

                Text(
                    text = jugador2?.nombres ?: "Jugador 2",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            // Resultado
            if (partida.esFinalizada) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (partida.ganadorId) {
                        null -> "Sin resultado"
                        0 -> "🤝 Empate"
                        else -> "🏆 Ganador: ${ganador?.nombres ?: "Desconocido"}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .background(
                            color = Color(0xFF2196F3).copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF2196F3)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF44336).copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyPartidasCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF9E9E9E)
            )

            Text(
                text = "No hay partidas registradas",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color(0xFF666666)
            )

            Text(
                text = "Completa el formulario de arriba para agregar tu primera partida",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9E9E9E),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartidaItemPreview() {
    MaterialTheme {
        PartidaItem(
            partida = edu.ucne.registrojugadoresmv.domain.model.Partida(
                partidaId = 1,
                fecha = "2024-01-15 14:30:00",
                jugador1Id = 1,
                jugador2Id = 2,
                ganadorId = 1,
                esFinalizada = true
            ),
            jugadores = listOf(
                edu.ucne.registrojugadoresmv.domain.model.Jugador(1, "Juan Pérez", 10),
                edu.ucne.registrojugadoresmv.domain.model.Jugador(2, "María García", 8)
            ),
            onEdit = {},
            onDelete = {}
        )
    }
}