package com.pabloat.GameHubConnect.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * LocalVideojuegoDao es una interfaz que se encarga de definir las operaciones que se pueden realizar en la base de datos local.
 */
@Dao
interface LocalVideojuegoDao {
    @Query("SELECT * FROM Videojuego")
    fun getAll(): Flow<List<Videojuego>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(videojuegos: List<Videojuego>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertone(videojuego: Videojuego)

    @Update
    suspend fun update(videojuego: Videojuego)

    @Delete
    suspend fun delete(videojuego: Videojuego)

    @Query("SELECT DISTINCT genre FROM Videojuego")
    fun getAllGenres(): Flow<List<String>>

    @Query("SELECT * FROM Videojuego WHERE genre = :genre")
    fun getVideojuegosByGenre(genre: String): Flow<List<Videojuego>>

    @Query("SELECT * FROM Videojuego WHERE title LIKE :title LIMIT 1")
    fun getVideojuegoByTitle(title: String): Videojuego?

    @Query("DELETE FROM Videojuego WHERE id = :id")
    suspend fun deleteVideojuego(id: Int)

    @Query("SELECT * FROM Videojuego WHERE id = :videojuegoId")
    suspend fun getVideojuegoById(videojuegoId: Int?): Videojuego?

}