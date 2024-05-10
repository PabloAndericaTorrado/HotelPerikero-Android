package com.pabloat.hotelperikero.data.remote

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.Resenia

data class ReseniasDTO(
    @SerializedName("data")
    val data: List<ReseniaDTO>
)

data class ReseniaDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("usuario_id") val usuario_id: Int,
    @SerializedName("reserva_id") val reserva_id: Int,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) {
    fun toResenia(): Resenia {
        return Resenia(
            id = id,
            usuario_id = usuario_id,
            reserva_id = reserva_id,
            calificacion = calificacion,
            comentario = comentario,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}