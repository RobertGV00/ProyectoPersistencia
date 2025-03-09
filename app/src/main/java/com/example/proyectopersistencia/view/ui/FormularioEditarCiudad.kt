package com.example.proyectopersistencia.view.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectopersistencia.viewmodel.CiudadEntity

@Composable
fun FormularioEditarCiudad(
    ciudad: CiudadEntity,
    onActualizar: (CiudadEntity) -> Unit
) {
    var nombre by remember { mutableStateOf(ciudad.nombre) }
    var temperatura by remember { mutableStateOf(ciudad.temperatura.toString()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Editar Ciudad", fontSize = MaterialTheme.typography.titleLarge.fontSize)
        Spacer(modifier = Modifier.height(10.dp))

        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = temperatura, onValueChange = { temperatura = it }, label = { Text("Temperatura") })

        Button(onClick = {
            onActualizar(CiudadEntity(ciudad.id, nombre, temperatura.toDouble(), ciudad.icono))
        }) {
            Text("Guardar Cambios")
        }
    }
}