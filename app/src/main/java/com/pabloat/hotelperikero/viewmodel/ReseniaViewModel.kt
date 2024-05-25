package com.pabloat.hotelperikero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pabloat.hotelperikero.data.local.dao.LocalReseniaDao
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReseniaViewModel(private val reseniaDao: LocalReseniaDao) : ViewModel() {
    private val _reseniaCreada = MutableStateFlow<Resenia?>(null)
    val reseniaCreada: StateFlow<Resenia?> get() = _reseniaCreada

    fun crearResenia(resenia: Resenia) {
        viewModelScope.launch {
            reseniaDao.insertSingle(resenia)

            val call = RetrofitClient.instance.createResenia(resenia)
            call.enqueue(object : Callback<Resenia> {
                override fun onResponse(call: Call<Resenia>, response: Response<Resenia>) {
                    if (response.isSuccessful) {
                        Log.d("ReseniaViewModel", "Reseña creada: ${response.body()}")
                        _reseniaCreada.value = response.body()
                    } else {
                        Log.e("ReseñaViewModel", "Error al crear la reseña: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Resenia>, t: Throwable) {
                    Log.e("ReseniaViewModel", "Error de red u otro error: ${t.message}")
                }
            })
        }
    }

    fun resetReseniaCreada() {
        _reseniaCreada.value = null
    }
}