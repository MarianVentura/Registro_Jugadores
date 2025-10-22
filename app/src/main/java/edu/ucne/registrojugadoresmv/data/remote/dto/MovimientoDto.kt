package edu.ucne.registrojugadoresmv.data.remote.dto

data class MovimientoDto(
    val partidaId: Int?,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int
)