package com.example.proyectopersistencia.model

//para futuras modificaciones de lo que se quiere mostrar leer el txt que está desgranado el json que devuelve y solo es ir metiendo val con cada campo.

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int
)


data class Weather(
    val description: String,
    val icon: String
)

data class RespuestaClima(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

