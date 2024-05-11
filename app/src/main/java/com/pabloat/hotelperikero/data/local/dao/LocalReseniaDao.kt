package com.pabloat.hotelperikero.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pabloat.hotelperikero.data.local.entities.Resenia
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalReseniaDao {
    @Query("SELECT * FROM Resenia")
    fun getAll(): Flow<List<Resenia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resenias: List<Resenia>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(resenias: List<Resenia>)


}