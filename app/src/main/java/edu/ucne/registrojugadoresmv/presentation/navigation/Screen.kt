package edu.ucne.registrojugadoresmv.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object JugadorList : Screen()

    @Serializable
    data class JugadorForm(val jugadorId: Int = 0) : Screen()

    @Serializable
    data object PartidaList : Screen()

    @Serializable
    data class PartidaForm(val partidaId: Int = 0) : Screen()

    @Serializable
    data object LogroList : Screen()  // ← NUEVA PANTALLA AGREGADA

    @Serializable
    data class LogroForm(val logroId: Int = 0) : Screen()  // ← NUEVA PANTALLA AGREGADA

    @Serializable
    data object Home : Screen()
}