package com.example.proyectopersistencia.repository

import com.example.proyectopersistencia.api.RetrofitCliente
import com.example.proyectopersistencia.model.RespuestaClima

class ClimaRepository {

    //funcion para obtener el clima actual de una ciudad:

    suspend fun obtenerClimaActual(ciudad: String, apiKey: String): RespuestaClima {
        return RetrofitCliente.servicioApi.obtenerClimaActual(ciudad, apiKey)
    }
}