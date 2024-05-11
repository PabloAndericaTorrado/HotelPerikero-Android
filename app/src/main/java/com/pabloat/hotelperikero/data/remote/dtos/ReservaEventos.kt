package com.pabloat.hotelperikero.data.remote.dtos

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos

data class ReservaEventosDTO(
    @SerializedName("data")
    val data: List<ReservaEventoDTO>
)

data class ReservaEventoDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("users_id") val users_id: Int,
    @SerializedName("espacio_id") val espacio_id: Int,
    @SerializedName("check_in") val check_in: String,
    @SerializedName("check_out") val check_out: String,
    @SerializedName("cantidad_personas") val cantidad_personas: Int,
    @SerializedName("precio_total") val precio_total: String,
    @SerializedName("pagado") val pagado: Int,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?
) {
    fun toReservaEventos(): ReservaEventos {
        return ReservaEventos(
            id = id,
            users_id = users_id,
            espacio_id = espacio_id,
            check_in = check_in,
            check_out = check_out,
            cantidad_personas = cantidad_personas,
            precio_total = precio_total,
            pagado = pagado,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}