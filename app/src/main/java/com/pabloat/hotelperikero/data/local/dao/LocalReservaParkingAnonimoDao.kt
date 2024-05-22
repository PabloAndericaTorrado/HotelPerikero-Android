package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.ReservaParkingAnonimo
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalReservaParkingAnonimoDao {

    @Query("SELECT * FROM ReservaParkingAnonimo")
    fun getAll(): Flow<List<ReservaParkingAnonimo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservaParkingAnonimo: ReservaParkingAnonimo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reservaParkingAnonimo: List<ReservaParkingAnonimo>)
}