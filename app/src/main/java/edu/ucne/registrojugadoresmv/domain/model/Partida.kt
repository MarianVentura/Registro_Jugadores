package edu.ucne.registrojugadoresmv.domain.model

data class Partida(
    val partidaId: Int = 0,
    val fecha: String = "",
    val jugador1Id: Int = 0,
    val jugador2Id: Int = 0,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false
)