@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyectopersistencia.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyectoapis.R
import com.example.proyectopersistencia.viewmodel.CiudadEntity
import com.example.proyectopersistencia.viewmodel.ClimaViewModel


// Modelo de datos para Ciudad
data class Ciudad(val nombre: String, val imageUrl: String)

@Composable
fun ListaCiudadesScreen(navController: NavController, viewModel: ClimaViewModel = hiltViewModel()) {
    val ciudadesGuardadas by viewModel.ciudadesGuardadas.observeAsState(emptyList())
    val mensajeConfirmacion by viewModel.mensajeConfirmacion.observeAsState()
    val mensajeError by viewModel.mensajeError.observeAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.ciudades_guardadas), color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF512DA8)),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.volver), tint = Color.White)
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
            mensajeConfirmacion?.let {
                Text(
                    text = it,
                    color = Color.Green,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            mensajeError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(ciudadesGuardadas) { ciudad ->
                    CiudadItem(ciudad, navController, viewModel)
                }
            }
        }
    }

    LaunchedEffect(mensajeConfirmacion, mensajeError) {
        kotlinx.coroutines.delay(2000)
        viewModel.limpiarMensajes()
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
    val imagenGenerica = "https://img.freepik.com/vector-gratis/silueta-horizonte-ilustracion_53876-78786.jpg?t=st=1741557486~exp=1741561086~hmac=65b975bb0cb950ab4037ea97234e19c0580bfca971b7d6722d159b200a4ec4fe&w=996"
    return imagenesCiudades[ciudad] ?: imagenGenerica
}


@Composable
fun CiudadItem(ciudad: CiudadEntity, navController: NavController, viewModel: ClimaViewModel) {
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
            // 🔹 Imagen de la ciudad con Coil
            Image(
                painter = rememberAsyncImagePainter(obtenerImagenCiudad(ciudad.nombre)), // 🔹 Ahora se usa la función
                contentDescription = "Imagen de ${ciudad.nombre}",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = ciudad.nombre, fontWeight = FontWeight.Bold)
                Text(text = stringResource(R.string.temperatura) + ": ${ciudad.temperatura}°C")
            }

            IconButton(onClick = { viewModel.actualizarClimaCiudad(ciudad.nombre) }) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.actualizar_clima)
                )
            }

            IconButton(onClick = { viewModel.eliminarCiudad(ciudad.nombre) }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.eliminar_ciudad)
                )
            }

            IconButton(onClick = { navController.navigate("editarCiudad/${ciudad.nombre}") }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(R.string.editar_ciudad)
                )
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

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.editar_ciudad),
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(stringResource(R.string.nombre)) })
            OutlinedTextField(
                value = temperatura,
                onValueChange = { temperatura = it },
                label = { Text(stringResource(R.string.temperatura)) })

            Button(onClick = {
                onActualizar(CiudadEntity(ciudad.id, nombre, temperatura.toDouble(), ciudad.icono))
            }) {
                Text(stringResource(R.string.guardar_cambios))
            }
        }
    }
}




