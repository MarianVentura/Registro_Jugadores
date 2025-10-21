package edu.ucne.registrojugadoresmv.presentation.logro.logroEvent

import edu.ucne.registrojugadoresmv.domain.model.Logro

sealed class LogroEvent {
    data class NombreChanged(val nombre: String) : LogroEvent()
    data class DescripcionChanged(val descripcion: String) : LogroEvent()
    object SaveLogro : LogroEvent()
    object ClearForm : LogroEvent()
    data class DeleteLogro(val logroId: Int) : LogroEvent()
    data class SelectLogro(val logroId: Int) : LogroEvent()
    data class EditLogro(val logro: Logro) : LogroEvent()
    data class ConfirmDeleteLogro(val logro: Logro) : LogroEvent()
}