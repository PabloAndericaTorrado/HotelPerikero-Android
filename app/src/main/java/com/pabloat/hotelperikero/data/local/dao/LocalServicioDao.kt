package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.Servicio
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