package com.example.proyectopersistencia.view.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectoapis.R
import com.example.proyectopersistencia.viewmodel.ClimaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaClima(navController: NavController, ciudad: String, viewModel: ClimaViewModel = hiltViewModel()) {

    var ciudad by remember { mutableStateOf(TextFieldValue(ciudad)) }

    val clima = viewModel.climaActual.observeAsState().value
    val cargando = viewModel.cargando.observeAsState(false).value
    val mensajeError = viewModel.mensajeError.observeAsState().value
    val mensajeConfirmacion = viewModel.mensajeConfirmacion.observeAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.titulo_clima),
                        color= Color.White,
                        fontSize = 30.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF3700B3),
                content = {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { navController.navigate("listaCiudades") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Icon(Icons.Outlined.Place, contentDescription = null, tint = Color(0xFF6200EE))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.ciudades_guardadas), color = Color(0xFF6200EE))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = ciudad.text,
                onValueChange = { ciudad = TextFieldValue(it) },
                label = { Text(stringResource(R.string.introducir_ciudad)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (ciudad.text.isNotEmpty()) {
                        viewModel.obtenerClima(ciudad.text)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text(text = stringResource(R.string.boton_obtener_clima), color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (cargando) {
                CircularProgressIndicator()
            }

            clima?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "${stringResource(R.string.ciudad)} ${it.name}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "${stringResource(R.string.temperatura)} ${it.main.temp} ${stringResource(R.string.grados)}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "${stringResource(R.string.temperatura_min)} ${it.main.temp_min}")
                        Text(text = "${stringResource(R.string.temperatura_max)} ${it.main.temp_max}")
                        Text(text = "${stringResource(R.string.humedad)} ${it.main.humidity}")
                        Text(text = "${stringResource(R.string.Sensacion_termica)} ${it.main.feels_like}")
                        Text(text = "${stringResource(R.string.descripcion_clima)} ${it.weather[0].description}", style = MaterialTheme.typography.bodyMedium)

                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png",
                            contentDescription = "Icono del clima",
                            modifier = Modifier.size(112.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        clima?.let { ciudad ->
                            viewModel.guardarCiudad(ciudad)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text(text = stringResource(R.string.guardar_ciudad), color = Color.White)
                }
            }

            mensajeError?.let {
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }

            mensajeConfirmacion?.let {
                Text(
                    text = it,
                    color = Color.Green,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            LaunchedEffect(mensajeConfirmacion) {
                if (mensajeConfirmacion != null) {
                    kotlinx.coroutines.delay(2000)
                    viewModel.limpiarMensajes()
                }
            }

            LaunchedEffect(mensajeError) {
                if (mensajeError != null) {
                    kotlinx.coroutines.delay(2000)
                    viewModel.limpiarMensajes()
                }
            }


        }
    }
}
