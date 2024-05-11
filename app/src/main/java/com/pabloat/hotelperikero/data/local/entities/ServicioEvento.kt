package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ServicioEvento(
    @PrimaryKey val id: Int,
    val nombre: String,
    val precio: String,
    val descripcion: String,
    val created_at: String?,
    val updated_at: String?
) {
    override fun toString(): String {
        return "ServicioEvento $id: $nombre - $precio"
    }
}
