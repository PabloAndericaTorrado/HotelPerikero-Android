package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.Espacio
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalEspacioDao {
    @Query("SELECT * FROM Espacio")
    fun getAll(): Flow<List<Espacio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(espacios: List<Espacio>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(espacios: List<Espacio>)

    @Query("SELECT * FROM Espacio WHERE id = :id")
    suspend fun getEspacioById(id: Int?): Espacio?
}