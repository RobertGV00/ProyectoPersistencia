package com.example.proyectopersistencia.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectopersistencia.viewmodel.AppBBDD
import com.example.proyectopersistencia.viewmodel.CiudadDao
import com.example.proyectopersistencia.viewmodel.CiudadEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Module
@InstallIn(SingletonComponent::class)
class ModuloBaseDatos {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppBBDD {
        return Room.databaseBuilder(
            context,
            AppBBDD::class.java,
            "app_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = provideDatabase(context).ciudadDao()
                        preCargarCiudades(dao)
                    }
                }
            })
            .build()
    }

    @Provides
    fun provideCiudadDao(db: AppBBDD): CiudadDao {
        return db.ciudadDao()
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
