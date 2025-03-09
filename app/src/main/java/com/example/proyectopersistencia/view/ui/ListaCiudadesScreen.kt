@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyectopersistencia.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.proyectoapis.R
import com.example.proyectopersistencia.viewmodel.CiudadEntity
import com.example.proyectopersistencia.viewmodel.ClimaViewModel


// Modelo de datos para Ciudad
data class Ciudad(val nombre: String, val imageUrl: String)

@Composable
fun ListaCiudadesScreen(navController: NavController, viewModel: ClimaViewModel = viewModel()) {
    val ciudadesGuardadas by viewModel.ciudadesGuardadas.observeAsState(emptyList())
    var textoBusqueda by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Ciudades Guardadas", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF512DA8)),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDE7F6))
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar ciudad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            val ciudadesFiltradas = ciudadesGuardadas.filter { it.nombre.contains(textoBusqueda, ignoreCase = true) }

            if (ciudadesFiltradas.isEmpty()) {
                Text(
                    text = "No hay ciudades que coincidan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF311B92),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(ciudadesFiltradas) { ciudad ->
                        CiudadItem(ciudad, navController, viewModel)
                    }
                }
            }
        }
    }
}

fun obtenerImagenCiudad(ciudad: String): String {
    val imagenesCiudades = mapOf(
        "Madrid" to "https://media.istockphoto.com/id/1343071828/es/foto/madrid-espa%C3%B1a-horizonte-de-la-ciudad-al-amanecer-en-el-parque-de-el-retiro-con-temporada-de.jpg?s=2048x2048&w=is&k=20&c=6jEbvKY3pW2XE-V_AFfl0R-0yH5xiOjtxW8XPsTOjX4=",
        "Green Bay" to "https://a.travel-assets.com/findyours-php/viewfinder/images/res70/34000/34849-Green-Bay.jpg",
        "Bucharest" to "https://images.unsplash.com/photo-1574974915729-40753c60260d",
        "Londres" to "https://images.unsplash.com/photo-1543832923-44667a44c804",
        "Cuenca" to "https://plus.unsplash.com/premium_photo-1697729801822-c68999fc5e5c"
    )
    return imagenesCiudades[ciudad] ?: "https://via.placeholder.com/150"
}


@Composable
fun CiudadItem(ciudad: CiudadEntity, navController: NavController, viewModel: ClimaViewModel) {
    val imagenUrl = obtenerImagenCiudad(ciudad.nombre)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la ciudad con Coil
            AsyncImage(
                model = imagenUrl,
                contentDescription = "Imagen de ${ciudad.nombre}",
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = ciudad.nombre, fontWeight = FontWeight.Bold)
                Text(text = "Temperatura: ${ciudad.temperatura}°C")
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { viewModel.actualizarClimaCiudad(ciudad.nombre) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Actualizar clima")
            }

            IconButton(onClick = { viewModel.eliminarCiudad(ciudad.nombre) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar ciudad")
            }
        }
    }

    @Composable
    fun FormularioEditarCiudad(
        ciudad: CiudadEntity,
        onActualizar: (CiudadEntity) -> Unit
    ) {
        var nombre by remember { mutableStateOf(ciudad.nombre) }
        var temperatura by remember { mutableStateOf(ciudad.temperatura.toString()) }

        Column {
            TextField(value = nombre, onValueChange = { nombre = it }, label = { Text(
                stringResource(R.string.ciudad)) })
            TextField(value = temperatura, onValueChange = { temperatura = it }, label = { Text(
                stringResource(R.string.temperatura)) })
            Button(onClick = {
                onActualizar(CiudadEntity(ciudad.id, nombre, temperatura.toDouble(), ciudad.icono))
            }) {
                Text(stringResource(R.string.guardar_cambios))
            }
        }
    }

    @Composable
    fun CiudadItem(ciudad: CiudadEntity, navController: NavController, viewModel: ClimaViewModel) {
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = ciudad.nombre, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = { navController.navigate("editarCiudad/${ciudad.nombre}") }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar ciudad")
                }
            }
        }
    }

}




