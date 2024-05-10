package com.pabloat.hotelperikero.data.remote

class RemoteHotelDataSource(private val apiService: ApiService) {
    suspend fun getHabitaciones(): List<HabitacionDTO> {
        // Suponiendo que HabitacionesDTO envuelve la respuesta de la API que contiene la lista de habitaciones
        val response = apiService.getHabitaciones()
        return response.data  // Asegúrate de que 'data' es la lista de HabitacionDTO
    }
    suspend fun getReservas(): List<ReservaDTO>{
        val response = apiService.getReservas()
        return response.data
    }

    suspend fun getServicios(): List<ServicioDTO>{
        val response = apiService.getServicios()
        return response.data
    }

    suspend fun getResenias(): List<ReseniaDTO> {
        val response = apiService.getResenias()
        return response.data
    }
}
