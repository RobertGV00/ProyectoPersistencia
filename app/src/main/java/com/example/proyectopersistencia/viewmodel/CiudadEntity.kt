package com.example.proyectopersistencia.viewmodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ciudades")
data class CiudadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val temperatura: Double,
    val icono: String
)
