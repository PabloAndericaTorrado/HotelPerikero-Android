package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalReservaServicioDao {

    @Query("SELECT * FROM ReservaServicio")
    fun getAll(): Flow<List<ReservaServicio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservaServicio: ReservaServicio)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reservaServicio: List<ReservaServicio>)
}