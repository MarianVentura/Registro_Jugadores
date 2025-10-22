package edu.ucne.registrojugadoresmv.data.mappers

import edu.ucne.registrojugadoresmv.data.remote.dto.MovimientoDto
import edu.ucne.registrojugadoresmv.domain.model.Movimiento

fun MovimientoDto.toDomain(): Movimiento {
    return Movimiento(
        partidaId = partidaId,
        jugador = jugador,
        posicionFila = posicionFila,
        posicionColumna = posicionColumna
    )
}

fun Movimiento.toDto(): MovimientoDto {
    return MovimientoDto(
        partidaId = partidaId,
        jugador = jugador,
        posicionFila = posicionFila,
        posicionColumna = posicionColumna
    )
}