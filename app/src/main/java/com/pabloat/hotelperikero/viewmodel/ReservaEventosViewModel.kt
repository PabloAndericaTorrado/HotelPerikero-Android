package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.local.dao.LocalReservaEventosDao
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservaEventosViewModel(private val reservaEventosDao: LocalReservaEventosDao) : ViewModel() {
    private val _reservaEventosCreada = MutableStateFlow<ReservaEventos?>(null)
    val reservaEventosCreada: StateFlow<ReservaEventos?> get() = _reservaEventosCreada

    fun crearReservaEventos(reservaEventos: ReservaEventos) {
        viewModelScope.launch {
            reservaEventosDao.insert(reservaEventos)

            val call = RetrofitClient.instance.createReservaEventos(reservaEventos)
            call.enqueue(object : Callback<ReservaEventos> {
                override fun onResponse(call: Call<ReservaEventos>, response: Response<ReservaEventos>) {
                    if (response.isSuccessful) {
                        Log.d("ReservaViewModel", "Reserva de evento creada: ${response.body()}")
                        _reservaEventosCreada.value = response.body()
                    } else {
                        Log.e("ReservaViewModel", "Error al crear la reserva de evento: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ReservaEventos>, t: Throwable) {
                    Log.e("ReservaViewModel", "Error de red u otro error: ${t.message}")
                }
            })
        }
    }

    fun resetReservaEventosCreada() {
        _reservaEventosCreada.value = null
    }
}
