package com.pabloat.hotelperikero.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalServicioDao {
    @Query("SELECT * FROM Servicio")
    fun getAll(): Flow<List<Servicio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(servicios: List<Servicio>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(servicios: List<Servicio>)
}