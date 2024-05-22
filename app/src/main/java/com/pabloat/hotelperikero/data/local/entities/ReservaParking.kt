package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReservaParking(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val reserva_habitacion_id: Int,
    val parking_id: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val matricula: String,
    val salida_registrada: Int,
    val created_at: String?,
    val updated_at: String?
) {
    override fun toString(): String {
        return "ReservaParking $id: Reserva Habitación $reserva_habitacion_id - Parking $parking_id"
    }
}