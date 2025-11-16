package edu.ucne.registrojugadoresmv.domain.model

import java.util.UUID

data class Jugador(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombres: String,
    val email: String,
    val isPendingCreate: Boolean = false,

    val jugadorId: Int = remoteId ?: 0,
    val partidas: Int = 0
)