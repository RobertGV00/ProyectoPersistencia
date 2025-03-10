package com.example.proyectopersistencia.viewmodel


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopersistencia.model.RespuestaClima
import com.example.proyectopersistencia.repository.ClimaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClimaViewModel @Inject constructor(
    private val ciudadDao: CiudadDao,
    private val repositorioClima: ClimaRepository
) : ViewModel() {


    private val _climaActual = MutableLiveData<RespuestaClima?>()
    val climaActual: LiveData<RespuestaClima?> = _climaActual

    private val _ciudadesGuardadas = MutableLiveData<List<CiudadEntity>>(emptyList())
    val ciudadesGuardadas: LiveData<List<CiudadEntity>> = _ciudadesGuardadas

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> = _mensajeError

    private val _mensajeConfirmacion = MutableLiveData<String?>()
    val mensajeConfirmacion: LiveData<String?> = _mensajeConfirmacion


    fun obtenerClima(ciudad: String) {
        _cargando.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiKey = "f9ce626e93bef28713fb264cb662814a"
                val clima = repositorioClima.obtenerClimaActual(ciudad, apiKey)

                _climaActual.postValue(clima)
                _cargando.postValue(false)
            } catch (e: Exception) {
                _mensajeError.postValue(" Error: ${e.message}")
                _cargando.postValue(false)
            }
        }
    }

    fun guardarCiudad(ciudad: RespuestaClima) {
        viewModelScope.launch(Dispatchers.IO) {
            val ciudadExiste = ciudadDao.existeCiudad(ciudad.name) > 0
            if (ciudadExiste) {
                _mensajeError.postValue(" La ciudad ${ciudad.name} ya está guardada.")
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

            _mensajeConfirmacion.postValue(" Ciudad ${ciudad.name} añadida correctamente.")
        }
    }

    fun actualizarCiudad(ciudad: CiudadEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ciudadDao.actualizarCiudad(ciudad.nombre, ciudad.temperatura, ciudad.icono)

                cargarCiudades()

                _mensajeConfirmacion.postValue(" Ciudad ${ciudad.nombre} actualizada correctamente.")
            } catch (e: Exception) {
                _mensajeError.postValue(" Error al actualizar: ${e.message}")
            }
        }
    }

    fun eliminarCiudad(ciudadNombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            ciudadDao.eliminarCiudad(ciudadNombre)

            cargarCiudades()

            _mensajeConfirmacion.postValue(" Ciudad $ciudadNombre eliminada correctamente.")
        }
    }

    fun actualizarClimaCiudad(ciudadNombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiKey = "f9ce626e93bef28713fb264cb662814a"
                val climaActualizado = repositorioClima.obtenerClimaActual(ciudadNombre, apiKey)

                ciudadDao.actualizarCiudad(
                    ciudadNombre,
                    climaActualizado.main.temp,
                    "https://openweathermap.org/img/wn/${climaActualizado.weather[0].icon}@2x.png"
                )
                cargarCiudades()
                _mensajeConfirmacion.postValue(" Clima de $ciudadNombre actualizado correctamente.")
            } catch (e: Exception) {
                _mensajeError.postValue("Error al actualizar el clima de $ciudadNombre: ${e.message}")
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun limpiarMensajes() {
        _mensajeError.postValue(null)
        _mensajeConfirmacion.postValue(null)
    }

    fun cargarCiudades() {
        viewModelScope.launch(Dispatchers.IO) {
            val ciudades = ciudadDao.obtenerCiudades()
            _ciudadesGuardadas.postValue(ciudades)
        }
    }

    init {
        cargarCiudades()
    }
}