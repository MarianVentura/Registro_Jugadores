package edu.ucne.registrojugadoresmv.domain.usecase.partidasUseCases

data class ValidacionesPartidas(
    val isValid: Boolean,
    val error: String? = null
)

fun validateJugador1(value: String, jugador2: String): ValidacionesPartidas {
    if (value.isBlank()) {
        return ValidacionesPartidas(false, "Debe de haber un jugador 1.")
    }
    val jugador1 = value.toIntOrNull()
    if (jugador1 == null || jugador1 <= 0) {
        return ValidacionesPartidas(false, "El jugador 1 debe de ser válido.")
    }
    return ValidacionesPartidas(true)
}

fun validateJugador2(value: String, jugador1: String): ValidacionesPartidas {
    if (value.isBlank()) {
        return ValidacionesPartidas(false, "Debe de haber un jugador 2.")
    }
    val jugador2 = value.toIntOrNull()
    if (jugador2 == null || jugador2 <= 0) {
        return ValidacionesPartidas(false, "El jugador 2 debe de ser válido.")
    }
    return ValidacionesPartidas(true)
}