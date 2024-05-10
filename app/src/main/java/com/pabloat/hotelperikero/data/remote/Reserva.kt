package com.pabloat.hotelperikero.data.remote

import com.google.gson.annotations.SerializedName
import com.pabloat.hotelperikero.data.local.Reserva

data class ReservasDTO(
    @SerializedName("data")
    val data:List<ReservaDTO>
)

data class ReservaDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("users_id") val users_id: Int,
    @SerializedName("habitacion_id") val habitacion_id: Int,
    @SerializedName("check_in") val check_in: String,
    @SerializedName("check_out") val check_out: String,
    @SerializedName("precio_total") val precio_total: String,
    @SerializedName("pagado") val pagado: Int,
    @SerializedName("confirmado") val confirmado: Int,
    @SerializedName("dni") val dni: String?,
    @SerializedName("numero_personas") val numero_personas: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
){
    fun toReserva(): Reserva {
        return Reserva(
            id = id,
            users_id = users_id,
            habitacion_id = habitacion_id,
            check_in = check_in,
            check_out = check_out,
            precio_total = precio_total,
            pagado = pagado,
            confirmado = confirmado,
            dni = dni,
            numero_personas = numero_personas,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}