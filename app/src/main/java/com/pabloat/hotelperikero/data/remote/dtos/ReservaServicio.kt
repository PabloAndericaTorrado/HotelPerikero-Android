package com.pabloat.hotelperikero.data.remote.dtos

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio


data class ReservaServiciosDTO(
    @SerializedName("data")
    val data: List<ReservaServicioDTO>
)

data class ReservaServicioDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("reserva_id") val reserva_id: Int,
    @SerializedName("servicio_id") val servicio_id: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?
) {
    fun toReservaServicio(): ReservaServicio {
        return ReservaServicio(
            id = id,
            reserva_id = reserva_id,
            servicio_id = servicio_id,
            cantidad = cantidad,
            created_at = created_at ?: "",
            updated_at = updated_at ?: ""
        )
    }
}