package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalServicioEventoDao {

    @Query("SELECT * FROM ServicioEvento")
    fun getAll(): Flow<List<ServicioEvento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(serviciosEventos: List<ServicioEvento>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(serviciosEventos: List<ServicioEvento>)
}