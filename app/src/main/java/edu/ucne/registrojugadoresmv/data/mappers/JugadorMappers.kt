package edu.ucne.registrojugadoresmv.data.mappers

import edu.ucne.registrojugadoresmv.data.local.entities.JugadorEntity
import edu.ucne.registrojugadoresmv.data.remote.jugadores.JugadorRequest
import edu.ucne.registrojugadoresmv.data.remote.jugadores.JugadorResponse
import edu.ucne.registrojugadoresmv.domain.model.Jugador

fun JugadorEntity.toDomain(): Jugador = Jugador(
    id = id,
    remoteId = remoteId,
    nombres = nombres,
    email = email,
    isPendingCreate = isPendingCreate
)

fun Jugador.toEntity(): JugadorEntity = JugadorEntity(
    id = id,
    remoteId = remoteId,
    nombres = nombres,
    email = email,
    isPendingCreate = isPendingCreate
)

fun JugadorResponse.toEntity(): JugadorEntity = JugadorEntity(
    remoteId = jugadorId,
    nombres = nombres,
    email = email,
    isPendingCreate = false
)

fun Jugador.toRequest(): JugadorRequest = JugadorRequest(
    nombres = nombres,
    email = email
)

fun JugadorResponse.toDomain(): Jugador = Jugador(
    remoteId = jugadorId,
    nombres = nombres,
    email = email,
    isPendingCreate = false
)