package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalReservaDao {

    @Query("SELECT * FROM Reserva")
    fun getAll(): Flow<List<Reserva>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservas: List<Reserva>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reservas: List<Reserva>)

}
