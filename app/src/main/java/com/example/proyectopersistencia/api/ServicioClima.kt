package com.example.proyectopersistencia.api

import com.example.proyectopersistencia.model.RespuestaClima
import retrofit2.http.GET
import retrofit2.http.Query

interface ServicioClima {
    //obtener el clima actual de una ciudad:

    @GET("weather")
    suspend fun obtenerClimaActual( //recuerda que lo de suspend es que la solicitud es asíncrona y tirará de corutinas para llamarla desde viewmodel
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") unidades: String = "metric",
        @Query("lang") idioma: String = "es"
    ): RespuestaClima
}