package com.pabloat.hotelperikero.data.remote

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.Servicio

data class ServiciosDTO(
    @SerializedName("data")
    val data: List<ServicioDTO>
)

data class ServicioDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) {
    fun toServicio(): Servicio {
        return Servicio(
            id = id,
            nombre = nombre,
            precio = precio,
            descripcion = descripcion,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}