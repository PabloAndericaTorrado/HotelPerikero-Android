package com.pabloat.GameHubConnect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Videojuego es una clase que representa el objeto Videojuego que se almacena en la base de datos local.
 * @param id es el identificador del videojuego.
 * @param title es el título del videojuego.
 * @param thumbnail es la URL de la imagen del videojuego.
 * @param developer es el desarrollador del videojuego.
 * @param shortDescription es la descripción corta del videojuego.
 * @param genre es el género del videojuego.
 * @param platform es la plataforma en la que se puede jugar el videojuego.
 * @param date es la fecha de lanzamiento del videojuego.
 * @param valoracion es la valoración del videojuego.
 */
@Entity
data class Videojuego(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val thumbnail: String,
    val developer: String,
    val shortDescription: String,
    val genre: String,
    val platform: String,
    val date: String,
    var valoracion: Float // nuevo atributo
){
    override fun toString(): String {
        return super.toString()
    }
}
