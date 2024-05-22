package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.ReservaParking
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalReservaParkingDao {

    @Query("SELECT * FROM ReservaParking")
    fun getAll(): Flow<List<ReservaParking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservaParking: ReservaParking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reservaParking: List<ReservaParking>)
}