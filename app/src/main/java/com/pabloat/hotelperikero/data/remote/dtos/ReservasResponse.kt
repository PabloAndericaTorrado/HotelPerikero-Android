package com.pabloat.hotelperikero.data.remote.dtos

import com.pabloat.hotelperikero.data.local.entities.Reserva


data class ReservasResponse(val message: String, val data: List<Reserva>)