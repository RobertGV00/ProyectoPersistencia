package com.example.proyectopersistencia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectopersistencia.model.RespuestaClima
import com.example.proyectopersistencia.repository.ClimaRepository
import kotlinx.coroutines.launch


class ClimaViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorioClima = ClimaRepository()

    // Instancia de la base de datos y el DAO

    private val _climaActual = MutableLiveData<RespuestaClima?>()
    val climaActual: LiveData<RespuestaClima?> = _climaActual

    private val _ciudadesGuardadas = MutableLiveData<List<CiudadEntity>>()
    val ciudadesGuardadas: LiveData<List<CiudadEntity>> = _ciudadesGuardadas

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> = _mensajeError


    fun obtenerClima(ciudad: String) {
        _cargando.value = true
        viewModelScope.launch {
            try {
                val apiKey = "f9ce626e93bef28713fb264cb662814a"
                val urlCompleta = "https://api.openweathermap.org/data/2.5/weather?q=$ciudad&appid=$apiKey&units=metric"
                println("URL Completa: $urlCompleta")

                val clima = repositorioClima.obtenerClimaActual(ciudad, apiKey)
                _climaActual.value = clima
                _cargando.value = false
                println("clima obtenido: $clima")
            } catch (e: Exception) {
                _mensajeError.value = "Error: ${e.message}"
                _cargando.value = false
            }
        }
    }

    fun guardarCiudad(ciudad: RespuestaClima) {
        viewModelScope.launch {
            val ciudadExiste = ciudadDao.existeCiudad(ciudad.name) > 0
            if (ciudadExiste) {
                _mensajeError.postValue("La ciudad ${ciudad.name} ya está guardada.")
                return@launch
            }

            val nuevaCiudad = CiudadEntity(
                id = 0,
                nombre = ciudad.name,
                temperatura = ciudad.main.temp,
                icono = "https://openweathermap.org/img/wn/${ciudad.weather[0].icon}@2x.png"
            )
            ciudadDao.insertarCiudad(nuevaCiudad)
            cargarCiudades()
        }
    }

    private fun cargarCiudadesPorDefecto() {
        if (_ciudadesGuardadas.value.isNullOrEmpty()) {
            _ciudadesGuardadas.value = listOf(
                CiudadEntity(1, "Madrid", 15.0, "https://openweathermap.org/img/wn/01d@2x.png"),
                CiudadEntity(2, "Green Bay", -2.0, "https://openweathermap.org/img/wn/02d@2x.png"),
                CiudadEntity(3, "Bucharest", 10.0, "https://openweathermap.org/img/wn/03d@2x.png"),
                CiudadEntity(4, "Londres", 12.0, "https://openweathermap.org/img/wn/04d@2x.png"),
                CiudadEntity(5, "Cuenca", 8.0, "https://openweathermap.org/img/wn/09d@2x.png")
            )
        }
    }


    fun agregarCiudad(ciudad: CiudadEntity) {
        if (!_ciudadesGuardadas.value!!.any { it.nombre == ciudad.nombre }) {
            _ciudadesGuardadas.value = _ciudadesGuardadas.value!! + ciudad
        }
    }

    fun cargarCiudades() {
        viewModelScope.launch {
            _ciudadesGuardadas.postValue(ciudadDao.obtenerCiudades())
        }
    }

    fun eliminarCiudad(ciudadNombre: String) {
        viewModelScope.launch {
            ciudadDao.eliminarCiudad(ciudadNombre)
            cargarCiudades()
        }
    }

    fun actualizarClimaCiudad(ciudadNombre: String) {
        viewModelScope.launch {
            try {
                val apiKey = "f9ce626e93bef28713fb264cb662814a"
                val climaActualizado = repositorioClima.obtenerClimaActual(ciudadNombre, apiKey)

                ciudadDao.actualizarCiudad(
                    ciudadNombre,
                    climaActualizado.main.temp,
                    "https://openweathermap.org/img/wn/${climaActualizado.weather[0].icon}@2x.png"
                )
                cargarCiudades()
            } catch (e: Exception) {
                println("Error al actualizar el clima de $ciudadNombre: ${e.message}")
            }
        }
    }

    init {
        cargarCiudades()
    }
}