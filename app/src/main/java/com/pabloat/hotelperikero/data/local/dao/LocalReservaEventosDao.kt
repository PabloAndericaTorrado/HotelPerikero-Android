package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalReservaEventosDao {

    @Query("SELECT * FROM ReservaEventos")
    fun getAll(): Flow<List<ReservaEventos>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservaEventos: List<ReservaEventos>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reservaEventos: List<ReservaEventos>)
}