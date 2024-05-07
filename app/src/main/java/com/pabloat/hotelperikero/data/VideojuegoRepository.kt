package com.pabloat.GameHubConnect.data

import android.util.Log
import com.pabloat.GameHubConnect.data.local.Videojuego
import com.pabloat.GameHubConnect.data.local.VideojuegoDatasource
import com.pabloat.GameHubConnect.data.remote.RemoteVideojuegoDataSource
import kotlinx.coroutines.flow.Flow

/**
 * VideojuegoRepository es una clase que se encarga de gestionar la obtención de datos de la base de datos local y remota.
 * @param localds es el origen de datos local.
 * @param remoteds es el origen de datos remoto.
 */
class VideojuegoRepository(
    private val localds: VideojuegoDatasource,
    private val remoteds: RemoteVideojuegoDataSource
) {
    suspend fun getRemoteVideojuego(

    ): List<Videojuego> {
        val videojuegos = remoteds.getVideojuegos()
        val videojuegosLocales = ArrayList<Videojuego>()

        for (videojuego in videojuegos) {
            videojuegosLocales.add(videojuego.toVideojuego())
        }
        localds.insertVideojuego(videojuegosLocales)
        return videojuegosLocales
        //val listaVideojuegos = remoteds.getVideojuegos().toLocalList()
        //return listaVideojuegos
    }

    suspend fun getLocalVideojuego(): Flow<List<Videojuego>>{
        return localds.getAllVideojuego()
    }

    suspend fun insertone(videojuego: Videojuego) {
        Log.d("VM", "Repositorio... Añadimos el videojuego: $videojuego")
        localds.insertoneVideojuego(videojuego)
    }

    suspend fun searchVideojuegoByTitle(title: String): Videojuego? {
        return localds.searchVideojuegoByTitle(title)
    }

    suspend fun deleteVideojuego(id: Int) {
        localds.deleteVideojuego(id)
    }

    suspend fun updateVideojuego(videojuego: Videojuego) {
        localds.updateVideojuego(videojuego)
    }

    fun getAllGenres(): Flow<List<String>> {
        return localds.getAllGenres()
    }

    fun getVideojuegosByGenre(genre: String): Flow<List<Videojuego>> {
        return localds.getVideojuegosByGenre(genre)
    }

    suspend fun getVideojuegoById(videojuegoId: Int?): Videojuego? {
        return localds.getVideojuegoById(videojuegoId)
    }

}
