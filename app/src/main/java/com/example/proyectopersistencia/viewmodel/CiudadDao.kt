package com.example.proyectopersistencia.viewmodel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CiudadDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarCiudad(ciudad: CiudadEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarCiudades(ciudades: List<CiudadEntity>)

    @Query("SELECT * FROM ciudades")
     fun obtenerCiudades(): LiveData<List<CiudadEntity>>

    @Query("DELETE FROM ciudades WHERE nombre = :ciudadNombre")
    suspend fun eliminarCiudad(ciudadNombre: String)

    @Query("SELECT COUNT(*) FROM ciudades WHERE nombre = :ciudadNombre")
    suspend fun existeCiudad(ciudadNombre: String): Int

    @Query("UPDATE ciudades SET temperatura = :temp, icono = :icono WHERE nombre = :ciudadNombre")
    suspend fun actualizarCiudad(ciudadNombre: String, temp: Double, icono: String)


}



