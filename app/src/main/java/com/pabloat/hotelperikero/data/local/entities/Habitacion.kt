package com.pabloat.hotelperikero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habitacion(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    val numero_habitacion:String,
    val tipo:String,
    val precio: String,
    val descripcion: String,
    val capacidad: Int,
    val caracteristicas: String,
    val disponibilidad: Int,
    val created_at: String,
    val updated_at: String,

    ){
    override fun toString(): String {
        return "Habitación $numero_habitacion: $tipo - $precio por noche"
    }
}
