package edu.ucne.registrojugadoresmv.data.mappers

import edu.ucne.registrojugadoresmv.data.local.entities.LogroEntity
import edu.ucne.registrojugadoresmv.domain.model.Logro

fun LogroEntity.toDomain(): Logro {
    return Logro(
        logroId = logroId,
        nombre = nombre,
        descripcion = descripcion
    )
}

fun Logro.toEntity(): LogroEntity {
    return LogroEntity(
        logroId = logroId,
        nombre = nombre,
        descripcion = descripcion
    )
}