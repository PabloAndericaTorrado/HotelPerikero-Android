package com.pabloat.hotelperikero.data.remote.dtos

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.entities.ReservaParking

data class ReservasParkingDTO(
    @SerializedName("data")
    val data: List<ReservaParkingDTO>
)

data class ReservaParkingDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("reserva_habitacion_id") val reserva_habitacion_id: Int,
    @SerializedName("parking_id") val parking_id: Int,
    @SerializedName("fecha_inicio") val fecha_inicio: String,
    @SerializedName("fecha_fin") val fecha_fin: String,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("salida_registrada") val salida_registrada: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) {
    fun toReservaParking(): ReservaParking {
        return ReservaParking(
            id = id,
            reserva_habitacion_id = reserva_habitacion_id,
            parking_id = parking_id,
            fecha_inicio = fecha_inicio,
            fecha_fin = fecha_fin,
            matricula = matricula,
            salida_registrada = salida_registrada,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}