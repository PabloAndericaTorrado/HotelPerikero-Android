package com.pabloat.hotelperikero.data.remote

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.Habitacion

data class HabitacionesDTO(
    @SerializedName("habitaciones")
    val data: List<HabitacionDTO>
)

data class HabitacionDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("numero_habitacion")
    val numero_habitacion:String,
    @SerializedName("tipo")
    val tipo:String,
    @SerializedName("precio")
    val precio:Int,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("capacidad")
    val capacidad: Int,
    @SerializedName("caracteristicas")
    val caracteristicas: String,
    @SerializedName("disponibilidad")
    val disponibilidad: Int,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("updated_at")
    val updated_at: String,
    ){
    fun toHabitacion(): Habitacion {
        return Habitacion(
            id=id,
            numero_habitacion=numero_habitacion,
            tipo=tipo,
            precio=precio,
            descripcion=descripcion,
            capacidad=capacidad,
            caracteristicas=caracteristicas,
            disponibilidad=disponibilidad,
            created_at=created_at,
            updated_at=updated_at

        )
    }
}
