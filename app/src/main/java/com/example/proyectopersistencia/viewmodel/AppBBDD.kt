package com.example.proyectopersistencia.viewmodel


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CiudadEntity::class], version = 1, exportSchema = false)
abstract class AppBBDD : RoomDatabase() {
    abstract fun ciudadDao(): CiudadDao

    }

