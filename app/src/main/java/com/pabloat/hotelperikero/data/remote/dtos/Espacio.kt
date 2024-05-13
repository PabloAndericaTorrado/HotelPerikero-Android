package com.pabloat.hotelperikero.data.remote.dtos

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.entities.Espacio

data class EspaciosDTO(
    @SerializedName("data")
    val data: List<EspacioDTO>
)

data class EspacioDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("capacidad_maxima") val capacidad_maxima: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: String,
    @SerializedName("disponible") val disponible: Int,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?
) {
    fun toEspacio(): Espacio {
        return Espacio(
            id = id,
            nombre = nombre,
            capacidad_maxima = capacidad_maxima,
            descripcion = descripcion,
            precio = precio,
            disponible = disponible,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}
