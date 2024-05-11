package com.pabloat.hotelperikero.data.remote.dtos

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento

data class ServicioEventosDTO(
    @SerializedName("data")
    val data: List<ServicioEventoDTO>
)

data class ServicioEventoDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) {
    fun toServicioEvento(): ServicioEvento {
        return ServicioEvento(
            id = id,
            nombre = nombre,
            precio = precio,
            descripcion = descripcion,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}
