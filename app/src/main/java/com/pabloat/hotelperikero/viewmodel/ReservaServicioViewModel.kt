package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.local.dao.LocalReservaServicioDao
import com.pabloat.hotelperikero.data.local.entities.ReservaServicio
import com.pabloat.hotelperikero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservaServicioViewModel(private val reservaServicioDao: LocalReservaServicioDao) :
    ViewModel() {

    private val _reservaServicioCreada = MutableStateFlow<ReservaServicio?>(null)
    val reservaServicioCreada: StateFlow<ReservaServicio?> get() = _reservaServicioCreada

    fun crearReserva(reserva: ReservaServicio) {
        viewModelScope.launch {
            reservaServicioDao.insert(reserva)

            val call = RetrofitClient.instance.createReservaServicios(reserva)
            call.enqueue(object : Callback<ReservaServicio> {
                override fun onResponse(
                    call: Call<ReservaServicio>,
                    response: Response<ReservaServicio>
                ) {
                    if (response.isSuccessful) {
                        Log.d("ReservaViewModel", "Reserva creada: ${response.body()}")
                        _reservaServicioCreada.value = response.body()
                    } else {
                        Log.e(
                            "ReservaViewModel",
                            "Error al crear la reserva: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ReservaServicio>, t: Throwable) {
                    Log.e("ReservaViewModel", "Error de red u otro error: ${t.message}")
                }
            })

        }
    }

    fun resetReservaCreada() {
        _reservaServicioCreada.value = null
    }
}