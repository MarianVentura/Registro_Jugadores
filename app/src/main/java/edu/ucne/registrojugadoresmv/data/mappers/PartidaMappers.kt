package edu.ucne.registrojugadoresmv.data.mappers

import edu.ucne.registrojugadoresmv.data.local.entities.PartidaEntity
import edu.ucne.registrojugadoresmv.domain.model.Partida

fun PartidaEntity.toDomain(): Partida = Partida(
    partidaId = partidaId,
    fecha = fecha,
    jugador1Id = jugador1Id,
    jugador2Id = jugador2Id,
    ganadorId = ganadorId,
    esFinalizada = esFinalizada
)

fun Partida.toEntity(): PartidaEntity = PartidaEntity(
    partidaId = partidaId,
    fecha = fecha,
    jugador1Id = jugador1Id,
    jugador2Id = jugador2Id,
    ganadorId = ganadorId,
    esFinalizada = esFinalizada
)