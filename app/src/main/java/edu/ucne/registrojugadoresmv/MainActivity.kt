package edu.ucne.registrojugadoresmv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrojugadoresmv.presentation.navigation.MainNavigation
import edu.ucne.registrojugadoresmv.ui.theme.Registro_JugadoresTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Registro_JugadoresTheme {
                val navController = rememberNavController()
                MainNavigation(navController = navController)
            }
        }
    }
}
