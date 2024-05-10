package com.pabloat.hotelperikero.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * AppDataBase es una clase que se encarga de gestionar la base de datos local.
 */

@Database(
    entities = [Habitacion::class, Reserva::class, Servicio::class, Resenia::class],
    version = 4
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun habitacionDao(): LocalHabitacionDao
    abstract fun reservaDao(): LocalReservaDao
    abstract fun servicioDao():LocalServicioDao

    abstract fun reseniaDao(): LocalReseniaDao

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

