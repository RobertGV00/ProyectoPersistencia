package com.example.proyectopersistencia.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectopersistencia.viewmodel.ClimaViewModel

@Composable
fun Navegacion(navController: NavHostController, viewModel: ClimaViewModel) {
    NavHost(navController = navController, startDestination = "pantallaClima/Madrid") {
        composable(route = "pantallaClima/{ciudad}") { backStackEntry ->
            val ciudad = backStackEntry.arguments?.getString("ciudad") ?: "Madrid"
            PantallaClima(navController, ciudad)
        }

        composable(route = "editarCiudad/{nombre}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""

            val ciudades = viewModel.ciudadesGuardadas.observeAsState(emptyList()).value
            val ciudad = ciudades.find { it.nombre == nombre }

            ciudad?.let {
                FormularioEditarCiudad(it) { ciudadActualizada ->
                    viewModel.actualizarCiudad(ciudadActualizada)
                    navController.popBackStack()
                }
            }
        }
    }
}