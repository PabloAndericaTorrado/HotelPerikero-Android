package com.pabloat.GameHubConnect.data.remote

import com.google.gson.annotations.SerializedName
import com.pabloat.GameHubConnect.data.local.Videojuego

/**
 * VideoJuegosDTO es una clase que representa el objeto JSON que se recibe de la API.
 * @param data es la lista de videojuegos que se recibe de la API.
 */

data class VideoJuegosDTO(
    @SerializedName("games")
    val data: List<VideoJuegoDTO>
)

data class VideoJuegoDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("short_description")
    val short_description   : String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("platform")
    val platform: String,
    @SerializedName("developer")
    val developer: String,
    @SerializedName("release_date")
    val date: String
) {
    fun toVideojuego(): Videojuego {
        return Videojuego(
            id = id,
            title = title,
            thumbnail = thumbnail,
            developer = developer,
            shortDescription = short_description,
            genre = genre,
            platform = platform,
            date = date,
            valoracion = 0.0f
        )
    }
}

fun VideoJuegosDTO.toLocalList(): List<Videojuego> {
    val tempList = mutableListOf<Videojuego>()
    for (i in 0 .. 5 - 1){
        val nuevo = Videojuego(
            id = data.get(i).id,
            title = data.get(i).title,
            thumbnail = data.get(i).thumbnail,
            developer = data.get(i).developer,
            shortDescription = data.get(i).short_description,
            genre = data.get(i).genre,
            platform = data.get(i).platform,
            date = data.get(i).date,
            valoracion = 0.0f

        )
        tempList.add(nuevo)
    }
    return tempList
}



