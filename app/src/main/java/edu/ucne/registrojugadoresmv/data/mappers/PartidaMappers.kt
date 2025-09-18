package edu.ucne.registrojugadoresmv.data.mappers

import edu.ucne.registrojugadoresmv.data.local.dao.PartidaConNombresDto
import edu.ucne.registrojugadoresmv.data.local.entities.Partida as PartidaEntity
import edu.ucne.registrojugadoresmv.domain.model.Partida as PartidaDomain
import edu.ucne.registrojugadoresmv.domain.model.PartidaConNombres

fun PartidaEntity.toDomain(): PartidaDomain {
    return PartidaDomain(
        partidaId = partidaId,
        fecha = fecha,
        jugador1Id = jugador1Id,
        jugador2Id = jugador2Id,
        ganadorId = ganadorId,
        esFinalizada = esFinalizada
    )
}

fun PartidaDomain.toEntity(): PartidaEntity {
    return PartidaEntity(
        partidaId = partidaId,
        fecha = fecha,
        jugador1Id = jugador1Id,
        jugador2Id = jugador2Id,
        ganadorId = ganadorId,
        esFinalizada = esFinalizada
    )
}

fun PartidaConNombresDto.toDomain(): PartidaConNombres {
    return PartidaConNombres(
        partida = PartidaDomain(
            partidaId = partidaId,
            fecha = fecha,
            jugador1Id = jugador1Id,
            jugador2Id = jugador2Id,
            ganadorId = ganadorId,
            esFinalizada = esFinalizada
        ),
        jugador1Nombre = jugador1Nombre,
        jugador2Nombre = jugador2Nombre,
        ganadorNombre = ganadorNombre
    )
}