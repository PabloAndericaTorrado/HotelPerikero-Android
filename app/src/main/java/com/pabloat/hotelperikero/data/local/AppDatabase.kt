package com.pabloat.hotelperikero.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pabloat.hotelperikero.data.local.dao.LocalEspacioDao
import com.pabloat.hotelperikero.data.local.dao.LocalHabitacionDao
import com.pabloat.hotelperikero.data.local.dao.LocalReseniaDao
import com.pabloat.hotelperikero.data.local.dao.LocalReservaDao
import com.pabloat.hotelperikero.data.local.dao.LocalReservaEventosDao
import com.pabloat.hotelperikero.data.local.dao.LocalReservaServicioDao
import com.pabloat.hotelperikero.data.local.dao.LocalServicioDao
import com.pabloat.hotelperikero.data.local.dao.LocalServicioEventoDao
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento

/**
 * AppDataBase es una clase que se encarga de gestionar la base de datos local.
 */

@Database(
    entities = [Habitacion::class,
        Reserva::class, Servicio::class, Resenia::class,
        ReservaEventos::class, ServicioEvento::class, Espacio::class,
        ReservaServicio::class],
    version = 8
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun habitacionDao(): LocalHabitacionDao
    abstract fun reservaDao(): LocalReservaDao
    abstract fun servicioDao(): LocalServicioDao
    abstract fun reseniaDao(): LocalReseniaDao

    abstract fun reservaEventosDao(): LocalReservaEventosDao

    abstract fun servicioEventoDao(): LocalServicioEventoDao

    abstract fun espacioDao(): LocalEspacioDao

    abstract fun reservaServicio(): LocalReservaServicioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "hotel.sql"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

