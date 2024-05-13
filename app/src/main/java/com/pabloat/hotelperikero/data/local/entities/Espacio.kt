package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Espacio(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val nombre: String,
    val capacidad_maxima: Int,
    val descripcion: String,
    val precio: String,
    val disponible: Int,
    val created_at: String?,
    val updated_at: String?
) {
    override fun toString(): String {
        return "Espacio $nombre: $descripcion - Precio: $precio"
    }
}