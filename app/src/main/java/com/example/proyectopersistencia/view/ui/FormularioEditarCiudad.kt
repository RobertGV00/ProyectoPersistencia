package com.example.proyectopersistencia.view.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.proyectoapis.R
import com.example.proyectopersistencia.viewmodel.CiudadEntity

@Composable
fun FormularioEditarCiudad(
    ciudad: CiudadEntity,
    onActualizar: (CiudadEntity) -> Unit
) {
    var nombre by remember { mutableStateOf(ciudad.nombre) }
    var temperatura by remember { mutableStateOf(ciudad.temperatura.toString()) }
    var errorMensaje by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = stringResource(R.string.edit_city), fontSize = MaterialTheme.typography.titleLarge.fontSize)
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.city_name)) }
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = temperatura,
            onValueChange = { temperatura = it },
            label = { Text(stringResource(R.string.temperature)) }
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (errorMensaje.isNotEmpty()) {
            Text(text = errorMensaje, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(onClick = {
            val temp = temperatura.toDoubleOrNull()
            if (nombre.isBlank() || temp == null) {
                errorMensaje = "Ingresa un nombre y una temperatura válida"
            } else {
                onActualizar(CiudadEntity(ciudad.id, nombre, temp, ciudad.icono))
            }
        }) {
            Text(stringResource(R.string.save_changes))
        }
    }
}
