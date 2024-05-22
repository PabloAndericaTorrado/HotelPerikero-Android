package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReservaServicio(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val reserva_id: Int,
    val servicio_id: Int,
    val cantidad: Int,
    val created_at: String,
    val updated_at: String
) {
    override fun toString(): String {
        return "ReservaServicio(id=$id, reserva_id=$reserva_id, servicio_id=$servicio_id, cantidad=$cantidad, created_at='$created_at', updated_at='$updated_at')"
    }
}