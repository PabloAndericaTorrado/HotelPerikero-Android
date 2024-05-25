package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.local.dao.LocalReservaParkingDao
import com.pabloat.hotelperikero.data.local.entities.ReservaParking
import com.pabloat.hotelperikero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParkingViewModel(private val reservaParkingDao: LocalReservaParkingDao) : ViewModel() {
    private val _reservaParkingCreada = MutableStateFlow<ReservaParking?>(null)

    val reservaParkingCreada: StateFlow<ReservaParking?> get() = _reservaParkingCreada

    fun crearReservaParking(reservaParking: ReservaParking) {
        viewModelScope.launch {
            reservaParkingDao.insert(reservaParking)

            val call = RetrofitClient.instance.createReservaParking(reservaParking)
            call.enqueue(object : Callback<ReservaParking> {
                override fun onResponse(
                    call: Call<ReservaParking>,
                    response: Response<ReservaParking>
                ) {
                    if (response.isSuccessful) {
                        Log.d("ReservaViewModel", "Reserva de parking creada: ${response.body()}")
                        _reservaParkingCreada.value = response.body()
                    } else {
                        Log.e(
                            "ReservaViewModel",
                            "Error al crear la reserva de parking: ${
                                response.errorBody()?.string()
                            }"
                        )
                    }
                }

                override fun onFailure(call: Call<ReservaParking>, t: Throwable) {
                    Log.e("ReservaViewModel", "Error de red u otro error: ${t.message}")
                }
            })


        }
    }

    fun resetReservaParkingCreada() {
        _reservaParkingCreada.value = null
    }
}