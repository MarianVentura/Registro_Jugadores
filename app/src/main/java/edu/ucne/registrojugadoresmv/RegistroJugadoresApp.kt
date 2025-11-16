package edu.ucne.registrojugadoresmv

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import edu.ucne.registrojugadoresmv.data.remote.worker.MyWorkerFactory
import javax.inject.Inject

@HiltAndroidApp
class RegistroJugadoresApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: MyWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
