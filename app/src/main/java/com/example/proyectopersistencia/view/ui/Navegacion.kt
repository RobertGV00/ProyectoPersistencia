package com.example.proyectopersistencia.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectopersistencia.viewmodel.ClimaViewModel

@Composable
fun Navegacion(navController: NavHostController, viewModel: ClimaViewModel) {
    NavHost(navController = navController, startDestination = "pantallaClima") {

        composable(route = "pantallaClima") {
            PantallaClima(navController, "Madrid")
        }

        composable(route = "pantallaClima/{ciudad}") { backStackEntry ->
            val ciudad = backStackEntry.arguments?.getString("ciudad") ?: "Madrid"
            PantallaClima(navController, ciudad)
        }

        composable(route = "listaCiudades") {
            ListaCiudadesScreen(navController, viewModel)
        }

        composable(route = "editarCiudad/{nombre}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val ciudades by viewModel.ciudadesGuardadas.observeAsState(emptyList())
            val ciudad = ciudades.find { it.nombre == nombre }

            if (ciudad != null) {
                FormularioEditarCiudad(ciudad) { ciudadActualizada ->
                    viewModel.actualizarCiudad(ciudadActualizada)
                    navController.popBackStack()
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}

