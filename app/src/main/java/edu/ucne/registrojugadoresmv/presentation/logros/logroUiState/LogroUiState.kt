package edu.ucne.registrojugadoresmv.presentation.logro.logroUiState

import edu.ucne.registrojugadoresmv.domain.model.Logro

data class LogroUiState(
    val logroId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val nombreError: String? = null,
    val descripcionError: String? = null,
    val logros: List<Logro> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val selectedLogro: Logro? = null,
    val showDeleteDialog: Boolean = false,
    val isEditing: Boolean = false
)