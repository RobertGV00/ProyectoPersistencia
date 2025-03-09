package com.example.proyectopersistencia.viewmodel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [CiudadEntity::class], version = 1, exportSchema = false)
abstract class AppBBDD : RoomDatabase() {
    abstract fun ciudadDao(): CiudadDao

    companion object {
        @Volatile
        private var INSTANCE: AppBBDD? = null

        fun getDatabase(context: Context): AppBBDD {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppBBDD::class.java,
                    "app_database"
                )
                    .addCallback(roomCallback)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    preCargarCiudades(INSTANCE!!.ciudadDao())
                }
            }
        }

        private suspend fun preCargarCiudades(dao: CiudadDao) {
            val ciudadesPorDefecto = listOf(
                CiudadEntity(1, "Madrid", 15.0, "https://openweathermap.org/img/wn/01d@2x.png"),
                CiudadEntity(2, "Green Bay", -2.0, "https://openweathermap.org/img/wn/02d@2x.png"),
                CiudadEntity(3, "Bucharest", 10.0, "https://openweathermap.org/img/wn/03d@2x.png"),
                CiudadEntity(4, "Londres", 12.0, "https://openweathermap.org/img/wn/04d@2x.png"),
                CiudadEntity(5, "Cuenca", 8.0, "https://openweathermap.org/img/wn/09d@2x.png")
            )
            dao.insertarCiudades(ciudadesPorDefecto)
        }
    }
}
