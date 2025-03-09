package com.example.proyectopersistencia.view.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "pantallaClima") {

        composable("pantallaClima") {
            PantallaClima(navController)
        }
        composable("listaCiudades") {
            ListaCiudadesScreen(navController)
        }
        composable("pantallaClima/{ciudad}") { backStackEntry ->
            val ciudad = backStackEntry.arguments?.getString("ciudad") ?: "Madrid"
            PantallaClima(navController, ciudad)
        }
    }
}
