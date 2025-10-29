package edu.ucne.registrojugadoresmv.data.remote.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.registrojugadoresmv.data.remote.Resource
import edu.ucne.registrojugadoresmv.domain.usecase.SyncJugadoresUseCase

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val syncJugadoresUseCase: SyncJugadoresUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "🔄 SyncWorker iniciado - Intento ${runAttemptCount + 1}")

        return try {
            Log.d(TAG, "📡 Llamando a syncJugadoresUseCase...")

            when (val result = syncJugadoresUseCase()) {
                is Resource.Success -> {
                    Log.d(TAG, "✅ Sincronización exitosa")
                    Result.success()
                }
                is Resource.Error -> {
                    Log.e(TAG, "❌ Error en sincronización: ${result.message}")
                    if (runAttemptCount < 3) {
                        Log.d(TAG, "🔁 Programando reintento...")
                        Result.retry()
                    } else {
                        Log.e(TAG, "💀 Máximo de reintentos alcanzado")
                        Result.failure()
                    }
                }
                is Resource.Loading -> {
                    Log.d(TAG, "⏳ Sincronización en progreso, reintentando...")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Excepción en SyncWorker: ${e.message}", e)
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        const val WORK_NAME = "SyncJugadoresWork"
        private const val TAG = "SyncWorker"
    }
}