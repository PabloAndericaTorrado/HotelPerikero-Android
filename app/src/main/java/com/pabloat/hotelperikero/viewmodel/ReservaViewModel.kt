package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.local.dao.LocalReservaDao
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservaViewModel(private val reservaDao: LocalReservaDao) : ViewModel() {

    fun crearReserva(reserva: Reserva) {
        viewModelScope.launch {
            reservaDao.insert(reserva)  // Guardar en Room

            val call = RetrofitClient.instance.createReserva(reserva)
            call.enqueue(object : Callback<Reserva> {
                override fun onResponse(call: Call<Reserva>, response: Response<Reserva>) {
                    if (response.isSuccessful) {
                        Log.d("ReservaViewModel", "Reserva creada: ${response.body()}")
                    } else {
                        Log.e("ReservaViewModel", "Error al crear la reserva: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Reserva>, t: Throwable) {
                    Log.e("ReservaViewModel", "Error de red u otro error: ${t.message}")
                }
            })
        }
    }
}
