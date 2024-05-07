package com.pabloat.hotelperikero.data.remote

class RemoteHotelDataSource(private val apiService: ApiService) {
    suspend fun getHabitaciones() =
        apiService.getHabitaciones()
}