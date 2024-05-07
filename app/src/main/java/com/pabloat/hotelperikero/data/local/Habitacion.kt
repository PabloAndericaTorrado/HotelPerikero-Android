package com.pabloat.hotelperikero.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habitacion (
    @PrimaryKey(autoGenerate = true) val id:Int?,
    val numero_habitacion:String,
    val tipo:String,
    val precio:Int,
    val descripcion: String,
    val capacidad: Int,
    val caracteristicas: String,
    val disponibilidad: Int,
    val created_at: String,
    val updated_at: String,

    ){
    override fun toString(): String {
        return super.toString()
    }
}
