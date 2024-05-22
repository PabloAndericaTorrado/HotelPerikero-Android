package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReservaParkingAnonimo(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val parking_id: Int,
    val matricula: String,
    val fecha_hora_entrada: String,
    val salida_registrada: Int,
    val created_at: String?,
    val updated_at: String?
) {
    override fun toString(): String {
        return "ReservaParkingAnonimo $id: Parking $parking_id - Matrícula $matricula"
    }
}