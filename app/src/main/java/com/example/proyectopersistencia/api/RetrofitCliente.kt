package com.example.proyectopersistencia.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCliente {
    //url base:
    private const val URL_BASE = "https://api.openweathermap.org/data/2.5/" //esta es la url base

    val servicioApi: ServicioClima by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create()) // usa Gson para convertir el JSON a objetos kotlin
            .build()
            .create(ServicioClima::class.java)
    }
}