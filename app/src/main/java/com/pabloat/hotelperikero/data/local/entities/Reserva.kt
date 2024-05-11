package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reserva (
    @PrimaryKey(autoGenerate = true) val id:Int?,
    val users_id: Int,
    val habitacion_id: Int,
    val check_in: String,
    val check_out: String,
    val precio_total: String,
    val pagado: Int,
    val confirmado: Int,
    val dni: String?,
    val numero_personas: Int,
    val created_at: String,
    val updated_at: String
    )
    {
        override fun toString(): String {
            return "Reserva $habitacion_id: $users_id - $precio_total por noche"
        }
}