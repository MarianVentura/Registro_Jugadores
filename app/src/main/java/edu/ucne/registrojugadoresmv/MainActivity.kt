package edu.ucne.registrojugadoresmv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.ucne.registrojugadoresmv.presentation.jugador.JugadorScreen
import edu.ucne.registrojugadoresmv.ui.theme.Registro_JugadoresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Registro_JugadoresTheme {
                JugadorScreen()
            }
        }
    }
}