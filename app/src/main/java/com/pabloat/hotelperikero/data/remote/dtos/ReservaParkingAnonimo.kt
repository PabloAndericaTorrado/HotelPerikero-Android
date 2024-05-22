package com.pabloat.hotelperikero.data.remote.dtos

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.entities.ReservaParkingAnonimo

data class ReservasParkingAnonimosDTO(
    @SerializedName("data")
    val data: List<ReservaParkingAnonimoDTO>
)

data class ReservaParkingAnonimoDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("parking_id") val parking_id: Int,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("fecha_hora_entrada") val fecha_hora_entrada: String,
    @SerializedName("salida_registrada") val salida_registrada: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) {
    fun toReservaParkingAnonimo(): ReservaParkingAnonimo {
        return ReservaParkingAnonimo(
            id = id,
            parking_id = parking_id,
            matricula = matricula,
            fecha_hora_entrada = fecha_hora_entrada,
            salida_registrada = salida_registrada,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}