package com.pabloat.hotelperikero.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Resenia(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val usuario_id: Int,
    val reserva_id: Int,
    val calificacion: Int,
    val comentario: String,
    val created_at: String,
    val updated_at: String
) {
    override fun toString(): String {
        return "Reseña $id: Usuario $usuario_id, Reserva $reserva_id, Calificación $calificacion - $comentario"
    }
}