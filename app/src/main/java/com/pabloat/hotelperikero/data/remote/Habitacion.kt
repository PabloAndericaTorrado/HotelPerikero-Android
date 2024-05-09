package com.pabloat.hotelperikero.data.remote

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.Habitacion
import com.pabloat.hotelperikero.data.local.Videojuego

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
fun HabitacionesDTO.toLocalList(): List<Habitacion> {
    val tempList = mutableListOf<Habitacion>()
    for (i in 0 .. 5 - 1){
        val nuevo = Habitacion(
            id = data.get(i).id,
            numero_habitacion = data.get(i).numero_habitacion,
            tipo = data.get(i).tipo,
            precio = data.get(i).precio,
            descripcion = data.get(i).descripcion,
            capacidad = data.get(i).capacidad,
            caracteristicas = data.get(i).caracteristicas,
            disponibilidad = data.get(i).disponibilidad,
            created_at = data.get(i).created_at,
            updated_at=data.get(i).updated_at,

        )
        tempList.add(nuevo)
    }
    return tempList
}