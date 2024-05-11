package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReservaEventos(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val users_id: Int,
    val espacio_id: Int,
    val check_in: String,
    val check_out: String,
    val cantidad_personas: Int,
    val precio_total: String,
    val pagado: Int,
    val created_at: String?,
    val updated_at: String?
) {
    override fun toString(): String {
        return "ReservaEventos $id: $users_id - $precio_total por evento"
    }
}
