package edu.ucne.registrojugadoresmv.presentation.logros.logroViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadoresmv.domain.model.Logro
import edu.ucne.registrojugadoresmv.domain.usecase.logrosUseCases.*
import edu.ucne.registrojugadoresmv.presentation.logro.logroEvent.LogroEvent
import edu.ucne.registrojugadoresmv.presentation.logro.logroUiState.LogroUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogroViewModel @Inject constructor(
    private val getLogrosUseCase: GetLogrosUseCase,
    private val upsertLogroUseCase: UpsertLogroUseCase,
    private val deleteLogroUseCase: DeleteLogroUseCase,
    private val getLogroByIdUseCase: GetLogroByIdUseCase,
    private val validateLogroUseCase: ValidateLogroUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogroUiState())
    val uiState: StateFlow<LogroUiState> = _uiState.asStateFlow()

    init {
        loadLogros()
    }

    fun onEvent(event: LogroEvent) {
        when (event) {
            is LogroEvent.NombreChanged -> {
                _uiState.update { it.copy(
                    nombre = event.nombre,
                    nombreError = null,
                    errorMessage = null,
                    successMessage = null
                )}
            }
            is LogroEvent.DescripcionChanged -> {
                _uiState.update { it.copy(
                    descripcion = event.descripcion,
                    descripcionError = null,
                    errorMessage = null,
                    successMessage = null
                )}
            }
            LogroEvent.SaveLogro -> saveLogro()
            LogroEvent.ClearForm -> clearForm()
            is LogroEvent.EditLogro -> editLogro(event.logro)
            is LogroEvent.DeleteLogro -> deleteLogro(event.logroId)
            is LogroEvent.SelectLogro -> selectLogro(event.logroId)
            is LogroEvent.ConfirmDeleteLogro -> confirmDeleteLogro(event.logro)
        }
    }

    private fun loadLogros() {
        viewModelScope.launch {
            getLogrosUseCase()
                .catch { e ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar logros: ${e.message}"
                    )}
                }
                .collect { logros ->
                    _uiState.update { it.copy(
                        logros = logros,
                        isLoading = false,
                        errorMessage = null
                    )}
                }
        }
    }

    private fun saveLogro() {
        viewModelScope.launch {
            val current = _uiState.value

            val nombreError = validateLogroUseCase.validateNombre(current.nombre)
            val descripcionError = validateLogroUseCase.validateDescripcion(current.descripcion)

            if (nombreError != null || descripcionError != null) {
                _uiState.update { it.copy(
                    nombreError = nombreError,
                    descripcionError = descripcionError,
                    isLoading = false
                )}
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            val logro = Logro(
                logroId = current.logroId,
                nombre = current.nombre.trim(),
                descripcion = current.descripcion.trim()
            )

            upsertLogroUseCase(logro)
                .onSuccess {
                    val message = if (current.logroId == 0) "Logro guardado exitosamente"
                    else "Logro actualizado exitosamente"

                    _uiState.update { it.copy(
                        logros = it.logros,
                        successMessage = message,
                        isLoading = false
                    )}
                }
                .onFailure { e ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )}
                }
        }
    }

    private fun editLogro(logro: Logro) {
        _uiState.update { it.copy(
            logroId = logro.logroId,
            nombre = logro.nombre,
            descripcion = logro.descripcion,
            nombreError = null,
            descripcionError = null,
            errorMessage = null,
            successMessage = null,
            isEditing = true
        )}
    }

    private fun selectLogro(logroId: Int) {
        viewModelScope.launch {
            val logro = getLogroByIdUseCase(logroId)
            logro?.let { editLogro(it) }
        }
    }

    private fun deleteLogro(logroId: Int) {
        viewModelScope.launch {
            try {
                deleteLogroUseCase(logroId)
                _uiState.update { it.copy(
                    successMessage = "Logro eliminado exitosamente",
                    errorMessage = null
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    successMessage = null,
                    errorMessage = "Error al eliminar logro: ${e.message}"
                )}
            }
        }
    }

    private fun confirmDeleteLogro(logro: Logro) {
        deleteLogro(logro.logroId)
    }

    private fun clearForm() {
        _uiState.update { it.copy(
            logroId = 0,
            nombre = "",
            descripcion = "",
            nombreError = null,
            descripcionError = null,
            errorMessage = null,
            successMessage = null,
            isEditing = false
        )}
    }
}
