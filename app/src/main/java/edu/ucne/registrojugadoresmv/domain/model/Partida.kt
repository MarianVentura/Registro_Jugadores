package edu.ucne.registrojugadoresmv.domain.model

data class Partida(
    val partidaId: Int = 0,
    val fecha: String,
    val jugador1Id: Int,
    val jugador2Id: Int,
    val ganadorId: Int? = null,
    val esFinalizada: Boolean = false
)

data class PartidaConNombres(
    val partida: Partida,
    val jugador1Nombre: String,
    val jugador2Nombre: String,
    val ganadorNombre: String?
)