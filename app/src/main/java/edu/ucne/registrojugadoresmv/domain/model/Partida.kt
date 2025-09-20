package edu.ucne.registrojugadoresmv.domain.model

import java.util.Date

data class Partida(
    val partidaId: Int? = null,
    val fecha: Date = Date(),
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false
)