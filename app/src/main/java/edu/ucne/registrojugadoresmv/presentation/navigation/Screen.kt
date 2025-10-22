package edu.ucne.registrojugadoresmv.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object JugadorList : Screen()

    @Serializable
    data class JugadorForm(val jugadorId: Int = 0) : Screen()

    @Serializable
    data object PartidaList : Screen()

    @Serializable
    data object PartidaForm : Screen()

    @Serializable
    data class TicTacToeGame(
        val jugador1Id: Int,
        val jugador2Id: Int,
        val jugador1Nombre: String = "",
        val jugador2Nombre: String = ""
    ) : Screen()

    @Serializable
    data object LogroList : Screen()

    @Serializable
    data class LogroForm(val logroId: Int = 0) : Screen()
}