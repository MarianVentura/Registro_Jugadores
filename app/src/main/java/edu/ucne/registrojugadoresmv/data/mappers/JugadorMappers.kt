package edu.ucne.registrojugadoresmv.data.mappers

import edu.ucne.registrojugadoresmv.data.local.entities.Jugador as JugadorEntity
import edu.ucne.registrojugadoresmv.domain.model.Jugador as JugadorDomain

fun JugadorEntity.toDomain(): JugadorDomain {
    return JugadorDomain(
        jugadorId = jugadorId,
        nombres = nombres,
        partidas = partidas
    )
}

fun JugadorDomain.toEntity(): JugadorEntity {
    return JugadorEntity(
        jugadorId = jugadorId,
        nombres = nombres,
        partidas = partidas
    )
}