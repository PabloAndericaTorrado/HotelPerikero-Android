package com.pabloat.hotelperikero.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalHabitacionDao {
    @Query("SELECT * FROM Habitacion")
    fun getAll(): Flow<List<Habitacion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitaciones: List<Habitacion>)
}